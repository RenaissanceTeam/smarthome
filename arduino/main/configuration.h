#include "user_properties.h"
#include "services.h"

#define DEVICE_NAME "arduino1"
#define RASPBERRY_PORT 8080
#define UDP_PORT       59743
#define ARDUINO_PORT   8080

// used services
#define TEMPERATURE
#define HUMIDITY
#define ONOFF
#define DIGITAL_ALERT
#define INIT_SERVICE // 107 bytes, without it 1381


// pins for services
#define onoff_1 2
#define onoff_2 3
#define onoff_3 4
#define DHT11_PIN 5
#define INIT_PIN 8



// pins for communication with esp8266
#define RX 6 // attach to TX of esp
#define TX 7 // attach to RX of esp

// static services array, pay attention to the defined pins
int SERVICES[] = {ONOFF_ID, ONOFF_ID, ONOFF_ID, TEMPERATURE_ID, HUMIDITY_ID, INIT_SERVICE_ID};
int PINS[] = {onoff_1, onoff_2, onoff_3, DHT11_PIN, DHT11_PIN, INIT_PIN};
#define SERVICES_STR "1001;1001;1001;1002;1003;1005"
#define SERVICES_COUNT 6
#define DIGITAL_ALERT_COUNT 0


// will be called on arduino 'setup()''
void setupConfiguration() {
	pinMode(onoff_1, OUTPUT);
	pinMode(onoff_2, OUTPUT);
	pinMode(onoff_3, OUTPUT);
	pinMode(DHT11_PIN, INPUT);
	pinMode(INIT_PIN, INPUT);
}
