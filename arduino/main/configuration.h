
#include "user_properties.h"
#include "services.h"

#define DEVICE_NAME "49189a1a48e94463b6694452c6adfb19"
#define RASPBERRY_PORT 8080
#define UDP_PORT       59743
#define ARDUINO_PORT   8080

// used services
#define INIT_SERVICE
#define ONOFF
#define TEMPERATURE_DHT22
#define ANALOG
#define HUMIDITY_DHT22
#define DIGITAL_ALERT

// pins for communication with esp8266
#define RX 8 // attach to TX of esp
#define TX 9 // attach to RX of esp

// static services array, pay attention to the defined pins
int SERVICES[] = {ANALOG_ID, DIGITAL_ALERT_ID, DIGITAL_ALERT_ID, TEMPERATURE_DHT22_ID, HUMIDITY_DHT22_ID, INIT_SERVICE_ID, ONOFF_ID, ONOFF_ID, ONOFF_ID};
int PINS[] = {A3, 10, 12, 7, 7, 6, 4, 3, 2};
#define SERVICES_STR "1000;1004;1004;1006;1007;1005;1001;1001;1001" 
#define SERVICE_NAMES_STR "16ed7920;eb5171ea;80c9203f;87fbca1f;f82ef27c;46212b1c;5d65da27;c46575b4;94dda242" 
#define SERVICES_COUNT 9
#define DIGITAL_ALERT_COUNT 2
#define INIT_PIN 6 

// will be called on arduino 'setup()'
void setupConfiguration() {
  delay(2548);
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
