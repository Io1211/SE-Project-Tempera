
#ifndef FUNCTIONS
#define FUNCTIONS

// Standard libraries
#include <stdint.h>

// Arduino libraries
#include <Arduino.h>
#include <ArduinoBLE.h>

// Include custom headers
#include "classesStructs.h"



// ############### FUNCTIONS DECLARATIONS ############### 

pin_size_t whichButtonPressed();
struct color findButtonColor(pin_size_t button);

void printSessionUpdate(timedSession session);
void printLEDUpdate(LED led);

#endif