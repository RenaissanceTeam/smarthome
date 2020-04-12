#include "util.h"
#include <ArduinoJson.h>

String keyValueJson(const __FlashStringHelper *key, String value) {
  String result = "";

  result += String(FPSTR(quote));
  result += String(key);
  result += String(FPSTR(quote));
  result += String(FPSTR(colon));
  result += value;

  return result;
}

void printKeyValueJson(const __FlashStringHelper *key, String value, Print& out) {
  String result = String(FPSTR(quote))
                  + String(key)
                  + String(FPSTR(quote))
                  + String(FPSTR(colon))
                  + String(value);

  out.print(result);
}
