#include "WebServer.h"
#include "SoftwareSerial.h"
SoftwareSerial Serial1(6, 7); // RX, TX

char ssid[] = "NETGEAR";            // your network SSID (name)
char pass[] = "smarthome";        // your network password
int status = WL_IDLE_STATUS;     // the Wifi radio's status
int reqCount = 0;                // number of requests received


// Initialize the Ethernet client object

WebServer server("/", 8080);

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
  server.setDefaultCommand(&process);
  server.begin();
  Serial.println("Server started");
}

void process(WebServer &server, WebServer::ConnectionType type, char * params, bool complete)
{
  Serial.println("process request");
  Serial.print("params: ");
  Serial.println(params);
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
