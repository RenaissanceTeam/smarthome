#include "WebServer.h"
#include "SoftwareSerial.h"
SoftwareSerial Serial1(6, 7); // RX, TX

char ssid[] = "NETGEAR";            // your network SSID (name)
char pass[] = "smarthome";        // your network password
int status = WL_IDLE_STATUS;     // the Wifi radio's status
int reqCount = 0;                // number of requests received

int ON_OFF = 0;
int TEMPERATURE = 1;

int ON_OFF_PIN_1 = 2;
int ON_OFF_PIN_2 = 3;


struct SERVICE {
  int serviceId;
  int pin;
  SERVICE(int serviceId, int pin) : serviceId(serviceId), pin(pin) {}
};
SERVICE on_off_1(ON_OFF, ON_OFF_PIN_1);
SERVICE on_off_2(ON_OFF, ON_OFF_PIN_2);

SERVICE services[] = {on_off_1, on_off_2};
int services_count = 2;


// Initialize the Ethernet client object

WebServer server("", 8080);

void setup()
{
  // initialize serial for debugging
  Serial.begin(9600);
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
    Serial.print("Attempting to connect to WPA SSID: ");
    Serial.println(ssid);
    // Connect to WPA/WPA2 network
    status = WiFi.begin(ssid, pass);
  }

  // you're connected now, so print out the data
  Serial.println("You're connected to the network");

  printWifiStatus();
  server.setDefaultCommand(&home);
  server.addCommand("service", &service);
  server.begin();
  Serial.println("Server started");
}

void home(WebServer &server, WebServer::ConnectionType type, char * params, bool complete)
{
  server.httpSuccess();
  // todo return here info about device with its services, etc..
}

int parseIntParam(char *from, int shift, char key[], int &val) {
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
  Serial.print("service params=");
  Serial.println(params);

  int serviceIndex = -1;
  int parsedValue = -1;

  int shift = 0;
  shift = parseIntParam(params, shift, "index=", serviceIndex);

  Serial.print("read index = ");
  Serial.println(serviceIndex);

  if (type == WebServer::POST) {
    // skip '&'
    ++shift;
    shift = parseIntParam(params, shift, "value=", parsedValue);
    if (shift < 0) {
      Serial.println("Failed to read value");
      server.httpFail();
      return;
    }
  }

  if (serviceIndex >= 0 && serviceIndex < services_count) {
    if (services[serviceIndex].serviceId == ON_OFF) {
      if (type == WebServer::GET) {
        Serial.print("read ON_OFF on pin ");
        Serial.println(services[serviceIndex].pin);
      } else if (type == WebServer::POST) {
        Serial.print("set ON_OFF on pin ");
        Serial.print(services[serviceIndex].pin);
        Serial.print(" to value ");
        Serial.println(parsedValue);
      }
    }
    // todo read other types

  } else {
    Serial.println("index out of bounds");
    Serial.println("Failed to read value");
    server.httpFail();
    return;
  }
 
  server.httpSuccess();
  
}


void printWifiStatus()
{
  // print the SSID of the network you're attached to
  Serial.print("SSID: ");
  Serial.println(WiFi.SSID());

  // print your WiFi shield's IP address
  IPAddress ip = WiFi.localIP();
  Serial.print("IP Address: ");
  Serial.println(ip);

  // print the received signal strength
  long rssi = WiFi.RSSI();
  Serial.print("Signal strength (RSSI):");
  Serial.print(rssi);
  Serial.println(" dBm");
}

void loop() {
  server.processConnection();
}
