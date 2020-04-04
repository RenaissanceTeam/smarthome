
#include "user_properties.h"
#include "services.h"
#include "service.h"

#define RASPBERRY_PORT 8080
#define RASPBERRY_IP "192.168.0.102"
#define ARDUINO_PORT   80

// used services
#define ONOFF
#define TEMPERATURE_DHT22
#define ANALOG
#define HUMIDITY_DHT22
#define DIGITAL_ALERT

// pins for communication with esp8266
#define RX 8 // attach to TX of esp
#define TX 9 // attach to RX of esp

Service services[10];


// will be called on arduino 'setup()'
void setupConfiguration() {

  services[0].pin = A3;
  services[0].type = ANALOG_ID;
  services[0].serial = 1231;

  services[1].pin = 2;
  services[1].type = ANALOG_ID;
  services[1].serial = 1231;

  
  
  pinMode(A3, INPUT);
  pinMode(10, INPUT);
  pinMode(12, INPUT);
  pinMode(7, INPUT);
  pinMode(7, INPUT);
  pinMode(6, INPUT);
  pinMode(4, OUTPUT);
  pinMode(3, OUTPUT);
  pinMode(2, OUTPUT);
}
