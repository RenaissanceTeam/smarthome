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

const int DEVICE_SERIAL = 1813591620;

#define ANALOG
#define DIGITAL_ALERT
#define ONOFF
#define TEMPERATURE_DHT22
#define HUMIDITY_DHT22

// pins for communication with esp8266
#define RX 8 // attach to TX of esp
#define TX 9 // attach to RX of esp
Service services[7];
void setupConfiguration() {
  pinMode(A3, INPUT);
  services[0].pin = A3;
  services[0].type = 1000;
  services[0].serial = -1337885633;

  pinMode(10, INPUT);
  services[1].pin = 10;
  services[1].type = 1004;
  services[1].serial = 2103997532;

  pinMode(7, INPUT);
  services[2].pin = 7;
  services[2].type = 1006;
  services[2].serial = -1666786372;

  pinMode(7, INPUT);
  services[3].pin = 7;
  services[3].type = 1007;
  services[3].serial = 1591789012;

  pinMode(4, OUTPUT);
  services[4].pin = 4;
  services[4].type = 1001;
  services[4].serial = 1684434460;

  pinMode(3, OUTPUT);
  services[5].pin = 3;
  services[5].type = 1001;
  services[5].serial = -1577555649;

  pinMode(2, OUTPUT);
  services[6].pin = 2;
  services[6].type = 1001;
  services[6].serial = 413384935;

}
