#include "WebServer.h"
#include <HttpClient.h>
#include "SoftwareSerial.h"
#define DEBUG 1

//////////////////CONFIGURATION//////////////////////////////
// same as in raspberry!!
int ANALOG = 1000;
int ON_OFF = 1001;
char DEVICE_NAME[] = "arduino1";
char raspberry[] = "192.168.1.3"; // raspberry is there (hopefully:)

int SERVICES[] = {1000, 1001, 1001};
char SERVICES_STR[] = "1000;1001;1001";
int PINS[] = {A0, 2, 3};
int services_count = 3;

//////////////////END OF CONFIGURATION///////////////////////



SoftwareSerial Serial1(6, 7); // RX, TX

int port = 8080;

WiFiEspClient wifi;
HttpClient client(wifi, raspberry, port);  // eats 280 bytes of dynamic memory :(
WebServer server("", 8080);

char ssid[] = "NETGEAR";            // your network SSID (name)
char pass[] = "smarthome";        // your network password
int status = WL_IDLE_STATUS;     // the Wifi radio's status
int reqCount = 0;                // number of requests received




String get_home_info() {
  return "name=" + String(DEVICE_NAME) + "&services=" + String(SERVICES_STR);
}

void setup()
{
  // initialize serial for debugging
  Serial.begin(9600);
  pinMode(2, OUTPUT);
  pinMode(3, OUTPUT);
  // initialize serial for ESP module
  Serial1.begin(9600);
  // initialize ESP module
  WiFi.init(&Serial1);

  // check for the presence of the shield
  if (WiFi.status() == WL_NO_SHIELD) {
    Serial.println("WiFi shield not present");
    // don't continue
    while (true);
  }

  // attempt to connect to WiFi network
  while ( status != WL_CONNECTED) {
#if DEBUG > 0
    Serial.print("Attempting to connect to WPA SSID: ");
#endif
#if DEBUG > 0
    Serial.println(ssid);
#endif
    // Connect to WPA/WPA2 network
    status = WiFi.begin(ssid, pass);
  }


#if DEBUG > 0
  printWifiStatus();
#endif
  server.setDefaultCommand(&home);
  server.addCommand("service", &service);
  server.begin();
  client.post("/init?" + get_home_info(), "text", "");

  delay(2000);

#if DEBUG > 0
  Serial.println("setup end");
#endif
}

void home(WebServer &server, WebServer::ConnectionType type, char * params, bool complete)
{
  server.httpSuccess();
  server.print(get_home_info());
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

  if (serviceIndex >= 0 && serviceIndex < services_count) {

    if (type == WebServer::GET) {
      if (SERVICES[serviceIndex] == ON_OFF) {
#if DEBUG > 1
        Serial.print("read ON_OFF on pin ");
        Serial.println(PINS[serviceIndex]);
#endif
        server.httpSuccess();
        server.print("{\"response\":\"");
        server.print(digitalRead(PINS[serviceIndex]));
        server.print("\"}");
        server.print(CRLF);
        return;
      }
      else if (SERVICES[serviceIndex == ANALOG]) {
#if DEBUG > 1
        Serial.print("read ANALOG on pin ");
        Serial.println(PINS[serviceIndex]);
#endif

        server.httpSuccess();
        server.print("{\"response\":\"");
        server.print(analogRead(PINS[serviceIndex]));
        server.print("\"}");
        server.print(CRLF);
        return;
      }
    } else if (type == WebServer::POST) {
      if (SERVICES[serviceIndex] == ON_OFF) {
#if DEBUG > 1
        Serial.print("set ON_OFF on pin ");
        Serial.print(PINS[serviceIndex]);
        Serial.print(" to value ");
        Serial.println(parsedValue);
#endif
        digitalWrite(PINS[serviceIndex], parsedValue);
        server.httpSuccess();
        server.print("{\"response\":\"");
        server.print(parsedValue);
        server.print("\"}");
        server.print(CRLF);
        return;
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
  // print your WiFi shield's IP address

  Serial.print("IP Address: ");
  Serial.println(WiFi.localIP());
}

void loop() {
  server.processConnection();
}
