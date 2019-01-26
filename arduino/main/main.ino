#include "WebServer.h"
#include "configuration.h"
#include <HttpClient.h>
#include "SoftwareSerial.h"
#define DEBUG 1

#ifdef TEMPERATURE || HUMIDITY
#include <dht.h>
dht DHT;
#endif

SoftwareSerial esp_serial(RX, TX);
WiFiEspClient wifiClient;
HttpClient client(wifiClient, RASPBERRY_IP, RASPBERRY_PORT);  // eats 280 bytes of dynamic memory :(
WebServer server("", ARDUINO_PORT);
int connectionStatus = WL_IDLE_STATUS;

#define home_info "name=" DEVICE_NAME "&services=" SERVICES_STR

void connectToWifi() {
  // start communication with esp on selected serial
  WiFi.init(&esp_serial);

  // check for the presence of the shield
  if (WiFi.status() == WL_NO_SHIELD) {
    Serial.println("WiFi shield not present");
    // don't continue
    while (true);
  }

  // actual connection to wifi
  while (connectionStatus != WL_CONNECTED) {
#if DEBUG > 0
    Serial.print("Attempting to connect to WPA SSID: ");
    Serial.println(WIFI_SSID);
#endif
    // Connect to WPA/WPA2 network
    connectionStatus = WiFi.begin(WIFI_SSID, PASSWORD);
  }
}

void runHttpServer() {
  server.setDefaultCommand(&homePage);    // callback to home page request
  server.addCommand("service", &service); // smart home server request to do something with service
  server.begin();
}

void sendHomeInfoToServer() {
  // smart home server will process this info
  // and will be able to work with this device
  Serial.println(home_info);
  client.post("/init?" home_info, "text", ""); 
}

void setup()
{
  Serial    .begin(9600); // initialize serial for debugging
  esp_serial.begin(9600); // initialize serial for ESP module
  connectToWifi();        // blocking call, won't return until the wifi connection is established
  setupConfiguration();   // method from configuration.h
#if DEBUG > 0
  printWifiStatus();
#endif

  runHttpServer();
  sendHomeInfoToServer();

#if DEBUG > 0
  Serial.println("setup end");
#endif
}

void baseResponse(int val) {
  Serial.println(val);
  server.httpSuccess();
  server.print("{\"response\" : \"");
  server.print(val);
  server.print("\"}");
  server.print(CRLF);
}

void baseResponse(double val) {
  Serial.println(val);
  server.httpSuccess();
  server.print("{\"response\" : \"");
  server.print(val);
  server.print("\"}");
  server.print(CRLF);
}

void homePage(WebServer &server, WebServer::ConnectionType type, char * params, bool complete)
{
  server.httpSuccess();
  server.print(home_info);
}

int parseIntParam(char *from, int& shift, char key[], int &val) {
  int i;
  for (i = 0; i < strlen(key); ++i) {
    if (key[i] != from[i + shift]) {
      return -1;
    }
  }
  char ch;
  val = 0;
  ch = from[i + shift];
  //  Serial.print("Start reading: ");
  //  Serial.print(ch);
  while (ch >= '0' && ch <= '9')
  {
    val = val * 10 + ch - '0';
    ch = from[++i + shift];
    //    Serial.print(ch);
  }
  //  Serial.println();
  return i;
}

void service(WebServer &server, WebServer::ConnectionType type, char * params, bool complete)
{
#if DEBUG > 1
  Serial.print("service params=");
  Serial.println(params);
#endif

  int serviceIndex = -1;
  int parsedValue = -1;

  int shift = 0;
  shift = parseIntParam(params, shift, "index=", serviceIndex);
#if DEBUG > 1
  Serial.print("read index = ");
  Serial.println(serviceIndex);
#endif
  if (type == WebServer::POST) {
    // skip '&'
    ++shift;
    shift = parseIntParam(params, shift, "value=", parsedValue);
    if (shift < 0) {
#if DEBUG > 1
      Serial.println("Failed to read value");
#endif
      server.httpFail();
      return;
    }
  }

  if (serviceIndex >= 0 && serviceIndex < SERVICES_COUNT) {

    if (type == WebServer::GET) {
      if (SERVICES[serviceIndex] == ONOFF_ID) {
#if DEBUG > 1
        Serial.print("read ON_OFF on pin ");
        Serial.println(PINS[serviceIndex]);
#endif
        baseResponse(digitalRead(PINS[serviceIndex]));
        return;
      }



      if (SERVICES[serviceIndex] == ANALOG_ID) {
#if DEBUG > 1
        Serial.print("read ANALOG on pin ");
        Serial.println(PINS[serviceIndex]);
#endif
        baseResponse(analogRead(PINS[serviceIndex]));
        return;
      }



#ifdef TEMPERATURE
      if (SERVICES[serviceIndex] == TEMPERATURE_ID) {
#if DEBUG > 1
        Serial.print("read TEMPERATURE on pin ");
        Serial.println(PINS[serviceIndex]);
#endif
        int chk = DHT.read11(DHT11_PIN);
        baseResponse(DHT.temperature);
        return;
      }
#endif




#ifdef HUMIDITY
      if (SERVICES[serviceIndex] == HUMIDITY_ID) {
#if DEBUG > 1
        Serial.print("read HUMIDITY on pin ");
        Serial.println(PINS[serviceIndex]);
#endif
        baseResponse(DHT.humidity);
        return;
      }
#endif


    } else if (type == WebServer::POST) {
      if (SERVICES[serviceIndex] == ONOFF_ID) {
#if DEBUG > 1
        Serial.print("set ON_OFF on pin ");
        Serial.print(PINS[serviceIndex]);
        Serial.print(" to value ");
        Serial.println(parsedValue);
#endif
        digitalWrite(PINS[serviceIndex], parsedValue);
        baseResponse(parsedValue);
        return;

#ifdef HUMIDITY
      if (SERVICES[serviceIndex] == HUMIDITY_ID) {
#if DEBUG > 1
        Serial.print("read HUMIDITY on pin ");
        Serial.println(PINS[serviceIndex]);
#endif
        baseResponse(DHT.humidity);
        return;
      }
#endif


      }
    }
    // todo read other types

  } else {
#if DEBUG > 0
    Serial.println("index out of bounds");
    Serial.println("Failed to read value");
#endif
    server.httpFail();
    return;
  }

  server.httpSuccess();
}


void printWifiStatus()
{
  Serial.println(WiFi.localIP());
}

void loop() {
  server.processConnection();
}
