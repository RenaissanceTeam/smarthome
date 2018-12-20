#include <dht.h>
#define DHT11_PIN 8

dht DHT;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
}

void loop() {
  int chk = DHT.read11(DHT11_PIN);
  double tmp = DHT.temperature;
  Serial.print("Temperature = ");
  Serial.println(tmp);
  Serial.print("Humidity = ");
  Serial.println(DHT.humidity);
  delay(5000);
}
