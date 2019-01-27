#include "connection_impl.h"
#define DEBUG 1

SoftwareSerial esp_serial(RX, TX);
WiFiEspClient wifiClient;
HttpClient client(wifiClient, RASPBERRY_IP, RASPBERRY_PORT);  // eats 280 bytes of dynamic memory :(
WebServer server("", ARDUINO_PORT);

void setup()
{
  Serial    .begin(9600); // initialize serial for debugging
  esp_serial.begin(9600); // initialize serial for ESP module
  connectToWifi(esp_serial);        // blocking call, won't return until the wifi connection is established
  setupConfiguration();   // method from configuration.h

#if DEBUG > 0
  Serial.println(WiFi.localIP());
#endif

  runHttpServer(server);
  sendHomeInfoToServer(client);

#if DEBUG > 0
  Serial.println("setup end");
#endif
}

void loop() {
  server.processConnection();
}
