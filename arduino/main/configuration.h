#include "user_properties.h"
#include "services.h"

#define DEVICE_NAME "arduino1"
#define RASPBERRY_IP "192.168.1.3"
#define RASPBERRY_PORT 8080
#define ARDUINO_PORT   8080

// used services
#define TEMPERATURE
#define HUMIDITY
#define ONOFF


// pins for services
#define onoff_1 2
#define onoff_2 3
#define onoff_3 4
#define DHT11_PIN 5

// pins for communication with esp8266
#define RX 6 // attach to TX of esp
#define TX 7 // attach to RX of esp

// static services array, pay attention to the defined pins
int SERVICES[] = {ONOFF_ID, ONOFF_ID, ONOFF_ID, TEMPERATURE_ID, HUMIDITY_ID};
int PINS[] = {onoff_1, onoff_2, onoff_3, DHT11_PIN, DHT11_PIN};
#define SERVICES_STR "1001;1001;1001;1002;1003"
#define SERVICES_COUNT 5

// will be called on arduino 'setup()''
void setupConfiguration() {
	pinMode(onoff_1, OUTPUT);
	pinMode(onoff_2, OUTPUT);
	pinMode(onoff_3, OUTPUT);
	pinMode(DHT11_PIN, INPUT);
}