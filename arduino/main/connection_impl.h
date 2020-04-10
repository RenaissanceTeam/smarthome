#include "WebServer.h"
#include "SoftwareSerial.h"
#include <ArduinoHttpClient.h>
#include "configuration.h"

#ifdef INIT_SERVICE
#include <WiFiEspUdp.h>
#endif

#define DEBUG 0

WiFiEspClient wifiClient;
HttpClient client = HttpClient(wifiClient, RASPBERRY_IP, RASPBERRY_PORT);
String baseControllersUrl = "iot/api/arduino/";
String responseStart = "{\"response\":";
String responseEnd = "}";


void baseResponse(WebServer& server, int val) {
  Serial.println(val);
  server.httpSuccess();
  server.print(responseStart);
  server.print(val);
  server.print(responseEnd);
  server.print(CRLF);
  server.flushBuf();
}

void baseResponse(WebServer& server, double val) {
  Serial.println(val);
  server.httpSuccess();
  server.print(responseStart);
  server.print(val);
  server.print(responseEnd);
  server.print(CRLF);
  server.flushBuf();
}

// ==========================================================================
// DHT11
// ==========================================================================
#ifdef TEMPERATURE_DHT11 || HUMIDITY_DHT11

#include <dht.h>

dht DHT11;

void humidityDht11GetRequest(WebServer& server, int serviceIndex) {
#if DEBUG > 1
  Serial.print("read HUMIDITY on pin ");
  Serial.println(services[serviceIndex].pin);
#endif
  DHT11.read11(services[serviceIndex].pin);
  baseResponse(server, DHT11.humidity);
}


void temperatureDht11GetRequest(WebServer& server, int serviceIndex) {
#if DEBUG > 1
  Serial.print("read TEMPERATURE on pin ");
  Serial.println(services[serviceIndex].pin);
#endif
  DHT11.read11(services[serviceIndex].pin);
  baseResponse(server, DHT11.temperature);
}

#endif
// ==========================================================================
// ==========================================================================

// ==========================================================================
// DHT22
// ==========================================================================
#ifdef TEMPERATURE_DHT22 || HUMIDITY_DHT22

#include <dht.h>


dht DHT22;

void humidityDht22GetRequest(WebServer& server, int serviceIndex) {
#if DEBUG > 1
  Serial.print("read HUMIDITY on pin ");
  Serial.println(services[serviceIndex].pin);
#endif
  DHT22.read22(services[serviceIndex].pin);
  baseResponse(server, DHT22.humidity);
}


void temperatureDht22GetRequest(WebServer& server, int serviceIndex) {
#if DEBUG > 1
  Serial.print("read TEMPERATURE on pin ");
  Serial.println(services[serviceIndex].pin);
#endif
  DHT22.read22(services[serviceIndex].pin);
  baseResponse(server, DHT22.temperature);
}

#endif
// ==========================================================================
// ==========================================================================






// ==========================================================================
// ONOFF
// ==========================================================================
#ifdef ONOFF
void onoffGetRequest(WebServer& server, int serviceIndex) {
#if DEBUG > 1
  Serial.print("read ON_OFF on pin ");
  Serial.println(services[serviceIndex].pin);
#endif
  baseResponse(server, digitalRead(services[serviceIndex].pin));
}

void onoffPostRequest(WebServer& server, int serviceIndex, int value) {
#if DEBUG > 0
  Serial.print("set ON_OFF on pin ");
  Serial.print(services[serviceIndex].pin);
  Serial.print(" to value ");
  Serial.println(value);
#endif
  digitalWrite(services[serviceIndex].pin, value);
  baseResponse(server, value);
}

#endif
// ==========================================================================
// ==========================================================================


// ==========================================================================
// ANALOG
// ==========================================================================
#ifdef ANALOG
void analogGetRequest(WebServer& server, int serviceIndex) {
#if DEBUG > 1
  Serial.print("read ANALOG on pin ");
  Serial.println(services[serviceIndex].pin);
#endif
  baseResponse(server, analogRead(services[serviceIndex].pin));
}

#endif
// ==========================================================================
// ==========================================================================


// ==========================================================================
// DIGITAL ALERT
// ==========================================================================
#ifdef DIGITAL_ALERT
void digitalAlertGetRequest(WebServer& server, int serviceIndex) {
#if DEBUG > 1
  Serial.print("read digitalAlert on pin ");
  Serial.println(services[serviceIndex].pin);
#endif
  baseResponse(server, digitalRead(services[serviceIndex].pin));
}

#endif
// ==========================================================================
// ==========================================================================




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

bool tryParseRequestValues(WebServer &server, WebServer::ConnectionType type,
                           char * params, int& serviceIndex, int& parsedValue) {
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
      return false;
    }
  }

  if (serviceIndex < 0 || serviceIndex >= SIZE(services)) {
    return false;
  }

  // todo: check post param
  return true;

}




void service(WebServer &server, WebServer::ConnectionType type, char * params, bool complete)
{
#if DEBUG > 1
  Serial.print("service params=");
  Serial.println(params);
#endif

  int serviceIndex = -1;
  int parsedValue = -1;
  if (!tryParseRequestValues(server, type, params, serviceIndex, parsedValue)) return;


  if (type == WebServer::GET) {

#ifdef ONOFF
    if (services[serviceIndex].type == ONOFF_ID) {
      onoffGetRequest(server, serviceIndex);
      return;
    }
#endif

#ifdef ANALOG
    if (services[serviceIndex].type == ANALOG_ID) {
      analogGetRequest(server, serviceIndex);
      return;
    }
#endif


#ifdef TEMPERATURE_DHT11
    if (services[serviceIndex].type == TEMPERATURE_DHT11_ID) {
      temperatureDht11GetRequest(server, serviceIndex);
      return;
    }
#endif

#ifdef HUMIDITY_DHT11
    if (services[serviceIndex].type == HUMIDITY_DHT11_ID) {
      humidityDht11GetRequest(server, serviceIndex);
      return;
    }
#endif

#ifdef TEMPERATURE_DHT22
    if (services[serviceIndex].type == TEMPERATURE_DHT22_ID) {
      temperatureDht22GetRequest(server, serviceIndex);
      return;
    }
#endif

#ifdef HUMIDITY_DHT22
    if (services[serviceIndex].type == HUMIDITY_DHT22_ID) {
      humidityDht22GetRequest(server, serviceIndex);
      return;
    }
#endif

#ifdef DIGITAL_ALERT
    if (services[serviceIndex].type == DIGITAL_ALERT_ID) {
      digitalAlertGetRequest(server, serviceIndex);
      return;
    }
#endif

  } else if (type == WebServer::POST) {

#ifdef ONOFF
    if (services[serviceIndex].type == ONOFF_ID) {
      onoffPostRequest(server, serviceIndex, parsedValue);
      return;
    }
#endif
  } else {
#if DEBUG > 0
    Serial.println("index out of bounds");
    Serial.println("Failed to read value");
#endif
    server.httpFail();
    return;
  }

  server.httpFail();
}




void connectToWifi(SoftwareSerial& esp_serial) {

  // start communication with esp on selected serial
  WiFi.init(&esp_serial);

  // check for the presence of the shield
  if (WiFi.status() == WL_NO_SHIELD) {
    Serial.println("WiFi shield not present");
    while (true);
  }

  int connectionStatus = WL_IDLE_STATUS;
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


void printService(WebServer &server, Service service) {
  server.print("{\"type\"=");
  server.print(service.type);
  server.print(",\"serial\"=");
  server.print(service.serial);
  server.print("}");
}

void printAllServices(WebServer &server) {
  server.print("[");
  int all = SIZE(services);
  for (int i = 0; i < all; ++i) {
    printService(server, services[i]);
    if (i != all - 1) server.print(",");
  }
  server.print("]");
}

void homePage(WebServer &server, WebServer::ConnectionType type,
              char* params, bool complete)
{
  server.httpSuccess();
  printAllServices(server);
  server.printCRLF();
  server.flushBuf();
}

String serviceToJson(Service service) {
  return "{\"type\"=" + String(service.type) + ",\"serial\"=" + service.serial + "}";
}

String servicesToJson() {
  String result = "";
  int all = SIZE(services);

  result += "{";
  for (int i = 0; i < all; ++i) {
    result += serviceToJson(services[i]);
    if (i != all - 1) result += ",";
  }
  result += "}";

  return result;
}


void doPost(String url, String postData) {
  client.beginRequest();
  client.post(baseControllersUrl + url);
  client.sendHeader(HTTP_HEADER_CONTENT_TYPE, JSON_CONTENT_TYPE);
  client.sendHeader(HTTP_HEADER_CONTENT_LENGTH, postData.length());
//  client.sendHeader("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhIiwiaWF0IjowLCJleHAiOjI1ODY2OTc4OTV9.O9mqWMTV9CYVN0SrnOp9TE_1HySl9bf4D719-gzBnMChXw7iqnVTTUZf9IHjL5Dm4tfeUgM8wo2hWAm8O9KEeg");
  client.print(postData);
  client.flush();
  client.responseStatusCode();
  client.responseBody();
  client.stop();
}

void sendInitToServer() {
  doPost("init", servicesToJson());
}

void runHttpServer(WebServer& server) {
  server.setDefaultCommand(&homePage);    // callback to home page request
  server.addCommand("service", &service); // smart home server request to do something with service
  server.begin();
}

#ifdef INIT_SERVICE
void sendUdpInitToHomeServer() {
  Serial.println("send udp");
  WiFiEspUDP udpClient;
  IPAddress broadcastIp(192, 168, 1, 255);
  udpClient.begin(UDP_PORT);
  udpClient.beginPacket(broadcastIp, UDP_PORT);
  udpClient.write(DEVICE_NAME); // todo some key instead (encryption needed)
  udpClient.endPacket();
  udpClient.flush();
  udpClient.stop();
}
#endif

#ifdef DIGITAL_ALERT
void sendAlertToServer(int serviceIndex, int value) {
  String alert = "{\"serial\"=" + String(services[serviceIndex].serial) + ",\"state\"="  + value + "}";
  doPost("alert", alert);
}
#endif
