#include "user_properties.h"
#include "services.h"
#include "service.h"
#include "constants.h"
#include "jsonUtil.h"
#include "util.h"
#include "streamLengthCounter.h"

#ifdef __ets__
#include "ets_sys.h"
#include "osapi.h"
#endif

const IPAddress RASPBERRY_IP = IPAddress(192,168,0,102);
const int RASPBERRY_PORT  = 8080;
const int ARDUINO_PORT = 80;

const int DEVICE_SERIAL = 460902108;

#define ONOFF
#define DIGITAL_ALERT

// pins for communication with esp8266
#define RX 8 // attach to TX of esp
#define TX 9 // attach to RX of esp
Service services[3];
void setupConfiguration() {
  pinMode(4, OUTPUT);
  services[0].pin = 4;
  services[0].type = 1001;
  services[0].serial = 907747194;

  pinMode(3, OUTPUT);
  services[1].pin = 3;
  services[1].type = 1001;
  services[1].serial = -9200262;

  pinMode(2, OUTPUT);
  services[2].pin = 2;
  services[2].type = 1001;
  services[2].serial = -688587091;

}
