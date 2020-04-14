#include "streamLengthCounter.h"
#include "HardwareSerial.h"

int c = 0;
size_t PrintLengthCounter::write(uint8_t character) { 
  Serial.print((char)character);
  c++;
  return 1;
}

void PrintLengthCounter::reset() {
  c = 0;
}

int PrintLengthCounter::len() {
  return c;
}
