#include "user_properties.h"
#include "services.h"

#define DEVICE_NAME "16304dbf35834ef8a3952e274d17a2bd"
#define RASPBERRY_PORT 8080
#define UDP_PORT       59743
#define ARDUINO_PORT   8080

// used services
#define HUMIDITY_DHT22
#define INIT_SERVICE
#define TEMPERATURE_DHT22
#define ONOFF
#define DIGITAL_ALERT
#define ANALOG

// pins for communication with esp8266
#define RX 2 // attach to TX of esp
#define TX 3 // attach to RX of esp

// static services array, pay attention to the defined pins
int SERVICES[] = {ANALOG_ID, DIGITAL_ALERT_ID, DIGITAL_ALERT_ID, TEMPERATURE_DHT22_ID, HUMIDITY_DHT22_ID, INIT_SERVICE_ID, ONOFF_ID, ONOFF_ID, ONOFF_ID};
int PINS[] = {A0, 4, 5, 8, 8, 9, 10, 11, 12};
#define SERVICES_STR "1000;1004;1004;1006;1007;1005;1001;1001;1001" 
#define SERVICE_NAMES_STR "2c96e83d;01d71adf;9fa7fc78;e4453ca9;8f777aa8;d61d2fbb;fd2b10e4;152406c0;c40507dd" 
#define SERVICES_COUNT 9
#define DIGITAL_ALERT_COUNT 2
#define INIT_PIN 9 

// will be called on arduino 'setup()'
void setupConfiguration() {
  delay(2576);
  pinMode(A0, INPUT);
  pinMode(4, INPUT);
  pinMode(5, INPUT);
  pinMode(8, INPUT);
  pinMode(8, INPUT);
  pinMode(9, INPUT);
  pinMode(10, OUTPUT);
  pinMode(11, OUTPUT);
  pinMode(12, OUTPUT);
}
