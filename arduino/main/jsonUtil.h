#include "util.h"

String keyValueJson(const __FlashStringHelper *key, String value) {
  return String(FPSTR(quote)) + key + String(FPSTR(quote)) + String(FPSTR(colon)) + value;
}
