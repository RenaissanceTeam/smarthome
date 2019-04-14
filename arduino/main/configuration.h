#include "user_properties.h"
#include "services.h"

#define DEVICE_NAME "arduinonano"
#define RASPBERRY_PORT 8080
#define UDP_PORT       59743
#define ARDUINO_PORT   8080

// used services
#define INIT_SERVICE
#define TEMPERATURE_DHT22
#define DIGITAL_ALERT
#define HUMIDITY_DHT22
#define ONOFF
#define ANALOG

// pins for communication with esp8266
#define RX 2 // attach to TX of esp
#define TX 3 // attach to RX of esp

// static services array, pay attention to the defined pins
int SERVICES[] = {ANALOG_ID, ANALOG_ID, ANALOG_ID, ANALOG_ID, ANALOG_ID, ANALOG_ID, DIGITAL_ALERT_ID, DIGITAL_ALERT_ID, DIGITAL_ALERT_ID, DIGITAL_ALERT_ID, TEMPERATURE_DHT22_ID, HUMIDITY_DHT22_ID, INIT_SERVICE_ID, ONOFF_ID, ONOFF_ID, ONOFF_ID};
int PINS[] = {A0, A1, A2, A3, A4, A5, 4, 5, 6, 7, 8, 8, 9, 10, 11, 12};
#define SERVICES_STR "1000;1000;1000;1000;1000;1000;1004;1004;1004;1004;1006;1007;1005;1001;1001;1001" 
#define SERVICES_COUNT 16
#define DIGITAL_ALERT_COUNT 4
#define INIT_PIN 9
// will be called on arduino 'setup()'
void setupConfiguration() {
  pinMode(A0, INPUT);
  pinMode(A1, INPUT);
  pinMode(A2, INPUT);
  pinMode(A3, INPUT);
  pinMode(A4, INPUT);
  pinMode(A5, INPUT);
  pinMode(4, INPUT);
  pinMode(5, INPUT);
  pinMode(6, INPUT);
  pinMode(7, INPUT);
  pinMode(8, INPUT);
  pinMode(8, INPUT);
  pinMode(9, INPUT);
  pinMode(10, OUTPUT);
  pinMode(11, OUTPUT);
  pinMode(12, OUTPUT);
}
