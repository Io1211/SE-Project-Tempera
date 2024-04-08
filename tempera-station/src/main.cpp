
#include <Arduino.h>
#include <ArduinoBLE.h>

// Libraries for the BME sensor module
#include <Wire.h>
#include <SPI.h>
#include <Adafruit_Sensor.h>
#include "Adafruit_BME680.h"

#include <stdint.h>




// ############### PARAMETERS ############### 

// Toggle additional Informations in serial output:
#define INFO 1
#define ERROR 1

// Define Output Pins for LED
#define LED_R A0
#define LED_G A1
#define LED_B A2

// Define aliases for the button modes
// Order: Deep-Work, Meeting, Out-Of-Office, Present
#define DW D2 
#define MT D3
#define OO D4
#define PT D5
// Set their colors (rgb value):
#define DW_COLOR {0, 0, 255}
#define MT_COLOR {255, 40, 10}
#define OO_COLOR {255, 0, 0}
#define PT_COLOR {0, 64, 0}

// Serial data rate in bits/s
#define SERIAL_DATA_RATE 9600

// Delay in ms after which a new button press will be accepted
#define BUTTON_COOLDOWN 500

// Update interval in ms after which the station transmits the current state
#define UPDATE_INTERVAL 60000

// Device name and custom id
#define DEVICE_NAME "Tempera-Station #1"
#define DEVICE_ID "1234567890"





// ############### FUNCTIONS DECLARATIONS ############### 

pin_size_t whichButtonPressed();
struct color findButtonColor(pin_size_t button);

void printSessionUpdate();
void printLEDUpdate();





// ############### CLASS AND STRUCT DECLARATIONS ############### 
// Attributes are public to skip Getter and Setter.

struct color {
  unsigned red;
  unsigned green;
  unsigned blue;
};

class timedSession {
  public:
    unsigned workMode = 0;
    unsigned long startTime = millis();
    unsigned long lastSessionDuration = 0;
};

class LED {
  public:
    struct color color = {0, 0, 0};

    void turnOn() {
      analogWrite(LED_R, color.red);
      analogWrite(LED_G, color.green);
      analogWrite(LED_B, color.blue);
    }

    void turnOff() {
      setColor({0, 0, 0});
      turnOn();
    }

    void setColor(struct color color) {
      this->color.red = color.red;
      this->color.green = color.green;
      this->color.blue = color.blue;
    }
};





// ############### BLE SETUP ###############

// Setup the device information service
BLEService deviceInformationService("180A");
BLEStringCharacteristic manufacturerNameCharacteristic("2A29", BLERead, 64);
BLEStringCharacteristic serialNumberCharacteristic("2A25", BLERead, 64);

// Setup the elapsed time service for the work time tracking
struct elapsedTimeCharacteristicStructure {
  uint8_t flags = 0;
  uint32_t timeValue; // to-do: update timeValue to its maximum possible size of uint_48
  uint8_t timeSyncSource = 0;
  uint8_t offset = 0;
  uint8_t workMode; // for our purpose the clock status bit is used as the workMode
  uint8_t clockCapabilities = 0; // not used but needed for transmission
};
BLEService elapsedTimeService("183F");
BLECharacteristic currentElapsedTimeCharacteristic("2BF2", BLERead | BLEIndicate, sizeof(elapsedTimeCharacteristicStructure));

// Setup for the climate measurements:
/*
ESS service: 0x181A
Temperature char.: 0x2A6E
Irradiance char.: 0x2A77
Humidity char.: 0x2A6F
NMVOC char.: 0x2BD3
*/

// Functions for connection and data transfer
void blePeripheralConnectHandler(BLEDevice central);
void blePeripheralDisconnectHandler(BLEDevice central);

void readManufacturerName(BLEDevice central, BLECharacteristic characteristic);
void readElapsedTime(BLEDevice central, BLECharacteristic characteristic);
void readSerialNumber(BLEDevice central, BLECharacteristic characteristic);

void writeElapsedTimeCharacteristicStructure(elapsedTimeCharacteristicStructure structure);


// ############### SETUP CODE ###############

// Create objects and variables
unsigned long lastUpdate = millis();
LED led;
timedSession session;
elapsedTimeCharacteristicStructure currentElapsedTime = {0, 0, 0, 0, 1, 0};


void setup() {
  // Setup for the rgb-led pins
  pinMode(LED_R, OUTPUT);
  pinMode(LED_G, OUTPUT);
  pinMode(LED_B, OUTPUT);

  // Setup for the button pins
  pinMode(DW, INPUT_PULLUP);
  pinMode(MT, INPUT_PULLUP);
  pinMode(OO, INPUT_PULLUP);
  pinMode(PT, INPUT_PULLUP);

  // Set serial output data rate in bits/s
  Serial.begin(SERIAL_DATA_RATE);
  if (INFO) Serial.println("Tempera > [INFO] Serial started");

  // Set to Out-Of-Office when device is started
  session.workMode = 4;
  session.startTime = millis();
  session.lastSessionDuration = 0;
  led.setColor(findButtonColor(session.workMode));
  led.turnOn();

  // BLE: Setup for BLE connectivity and device infos
  if (ERROR) {
    if (!BLE.begin()) {
      Serial.println("Tempera > [ERROR] Starting BLE failed!");
      while(1);
    }
    Serial.println("Tempera > [INFO] BLE startet.");
  }
  BLE.setEventHandler(BLEConnected, blePeripheralConnectHandler);
  BLE.setEventHandler(BLEDisconnected, blePeripheralDisconnectHandler);
  BLE.setLocalName(DEVICE_NAME);
  BLE.setDeviceName(DEVICE_NAME);
  BLE.setAdvertisedService(deviceInformationService);

  // BLE: Setup for user description and serial number
  BLEDescriptor deviceInfoDescriptor("2901", "Sensor station with group number");
  manufacturerNameCharacteristic.addDescriptor(deviceInfoDescriptor);
  manufacturerNameCharacteristic.writeValue("G4T1");
  manufacturerNameCharacteristic.setEventHandler(BLERead, readManufacturerName);
  deviceInformationService.addCharacteristic(manufacturerNameCharacteristic);
  // to-do: serial number is not correctly transmitted? 
  // only the first characteristic can be accessed
  BLEDescriptor serialInfoDescriptor("2901", "Unique serial number");
  serialNumberCharacteristic.addDescriptor(serialInfoDescriptor);
  serialNumberCharacteristic.writeValue(DEVICE_ID);
  serialNumberCharacteristic.setEventHandler(BLERead, readSerialNumber);
  deviceInformationService.addCharacteristic(serialNumberCharacteristic);

  BLE.addService(deviceInformationService);

  // BLE: Setup for elapsed time characteristic
  BLEDescriptor elapsedTimeDescriptor("2901", "Elapsed Time service used for time tracking.");
  currentElapsedTimeCharacteristic.addDescriptor(elapsedTimeDescriptor);
  currentElapsedTimeCharacteristic.setEventHandler(BLERead | BLEIndicate, readElapsedTime);
  elapsedTimeService.addCharacteristic(currentElapsedTimeCharacteristic);
  BLE.addService(elapsedTimeService);
  writeElapsedTimeCharacteristicStructure({0, 0, 0, 0, 1, 0});

  // BLE: Advertise services
  BLE.advertise();
}





// ############### RUNTIME CODE ###############

void loop() {

  // send the current work status after a given time interval
  if (lastUpdate + UPDATE_INTERVAL < millis()) {
    writeElapsedTimeCharacteristicStructure({0, UPDATE_INTERVAL, 0, 0, (uint8_t) session.workMode, 0});
    session.lastSessionDuration = millis() - session.startTime; // to-do: fix possible overflow error
    session.startTime = millis();
    lastUpdate = millis();
    if (INFO) printSessionUpdate();
  }

  // If a button has been pressed change the following:
  if (pin_size_t b = whichButtonPressed()) {
    led.turnOff();
    delay(100);

    // Update the work session info so the duration and time etc since the last mode change
    writeElapsedTimeCharacteristicStructure({0, (millis()-lastUpdate), 0, 0, (uint8_t) session.workMode, 0});
    session.workMode = b;
    session.lastSessionDuration = millis() - session.startTime; // to-do: fix possible overflow error
    session.startTime = millis();
    lastUpdate = millis();
    if (INFO) printSessionUpdate();
    
    // Update LED status and print
    led.setColor(findButtonColor(b));
    led.turnOn();
    if (INFO) printLEDUpdate();
    
    delay(BUTTON_COOLDOWN);
  }

  // Check for events
  BLE.poll();

  // Wait for a bit
  delay(300);
}





// ############### FUNCTIONS ###############

// Checks if one of the four buttons is currently pressed and returns the pressed one or 0 
pin_size_t whichButtonPressed() {
  if (!digitalRead(DW)) {
    return DW;
  } else if (!digitalRead(MT)) {
    return MT;
  } else if (!digitalRead(OO)) {
    return OO;
  } else if (!digitalRead(PT)) {
    return PT;
  } else {
    return 0;
  }
}

// Returns the corresponding color for each button
struct color findButtonColor(pin_size_t button) {
  switch (button) {
  case PT: 
    return PT_COLOR;
    break;
  case DW:
    return DW_COLOR;
    break;
  case MT:
    return MT_COLOR;
    break;
  case OO:
    return OO_COLOR;
    break;
  default:
    return {0, 0, 0};
    break;
  }
}

void printSessionUpdate() {
  Serial.println("Tempera > [INFO] Session Info has been updated:");
  Serial.print("Tempera > [INFO]    Current work mode: ");
  Serial.println(session.workMode);
  Serial.print("Tempera > [INFO]    Start time: ");
  Serial.println(session.startTime);
  Serial.print("Tempera > [INFO]    Last session duration: ");
  Serial.println(session.lastSessionDuration);
}

void printLEDUpdate() {
  Serial.println("Tempera > [INFO] LED state has been updated:");
  Serial.print("Tempera > [INFO]    Color (R-G-B): ");
  Serial.print(led.color.red);
  Serial.print(" ");
  Serial.print(led.color.green);
  Serial.print(" ");
  Serial.println(led.color.blue);
}

void blePeripheralConnectHandler(BLEDevice central) {
  Serial.println("Tempera > [INFO] Connected event, central: ");
  Serial.print("Tempera > [INFO]    ");
  Serial.println(central.address());
}

void blePeripheralDisconnectHandler(BLEDevice central) {
  Serial.println("Tempera > [INFO] Disconnected event, central: ");
  Serial.print("Tempera > [INFO]    ");
  Serial.println(central.address());
}

void readManufacturerName(BLEDevice central, BLECharacteristic characteristic) {
  Serial.println("Tempera > [INFO] Manufacturer Specifications have been read: ");
  Serial.print("Tempera > [INFO]    ");
  Serial.println(central.address()); 
}

void readElapsedTime(BLEDevice central, BLECharacteristic characteristic) {
  unsigned long time = 0; // to-do: add correct value
  Serial.println("Tempera > [INFO] Elapsed time in current work mode: ");
  Serial.print("Tempera > [INFO]    ");
  Serial.println(time);
}

void readSerialNumber(BLEDevice central, BLECharacteristic characteristic) {
  std::string serialNumber = "placeholder"; // to-do: add correct value
  Serial.println("Tempera > [INFO] Serial number has been read.");
  Serial.print("Tempera > [INFO]    ");
  Serial.println(serialNumber.c_str());
}

void writeElapsedTimeCharacteristicStructure(elapsedTimeCharacteristicStructure structure) {
  uint8_t buffer[sizeof(structure)];
  memcpy(buffer, &structure, sizeof(structure));
  if (!currentElapsedTimeCharacteristic.writeValue(buffer, sizeof(buffer)) && ERROR) {
    Serial.println("Tempera > [ERROR] Could not write to elapsed time characteristic.");
    return;
  }
  if (INFO) Serial.println("Tempera > [INFO] Elapsed time characteristic has been updated.");
}
