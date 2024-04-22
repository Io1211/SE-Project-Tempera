
#include "../include/functions.h"

// Standard libraries
#include <stdint.h>

// Arduino libraries
#include <Arduino.h>
#include <ArduinoBLE.h>

// Include further headers
#include "../include/classesStructs.h"
#include "../include/parameters.h"



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
};

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
};

// Function used to handle a time overflow of the internal arduino clock
int fixTimeOverflow(timedSession session) {
  // ULONG_MAX
  return 4294967295 - session.startTime + millis();
};

void switchBuiltInLED(unsigned state) {
    digitalWrite(LED_BUILTIN, (state ? HIGH : LOW) );
};

void printSessionUpdate(timedSession session) {
  Serial.println("Tempera > [INFO] Session Info has been updated:");
  Serial.print("Tempera > [INFO]    Current work mode: ");
  Serial.println(session.workMode);
  Serial.print("Tempera > [INFO]    Start time: ");
  Serial.println(session.startTime);
  Serial.print("Tempera > [INFO]    Last session duration: ");
  Serial.println(session.lastSessionDuration);
};

void printLEDUpdate(LED led) {
  Serial.println("Tempera > [INFO] LED state has been updated:");
  Serial.print("Tempera > [INFO]    Color (R-G-B): ");
  Serial.print(led.color.red);
  Serial.print(" ");
  Serial.print(led.color.green);
  Serial.print(" ");
  Serial.println(led.color.blue);
};

void printRoomClimateDataUpdate(roomClimateUnionStructure roomClimateData) {
  Serial.println("Tempera > [INFO] Room climate data has been updated:");
  Serial.print("Tempera > [INFO]    Values: ");
  Serial.print(roomClimateData.temperature);
  Serial.print("   ");
  Serial.print(roomClimateData.irradiance);
  Serial.print("   ");
  Serial.print(roomClimateData.humidity);
  Serial.print("   ");
  Serial.print(roomClimateData.nmvoc);
  Serial.println();
};