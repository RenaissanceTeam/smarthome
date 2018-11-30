#include <WiFiEsp.h>
#include <WiFiEspClient.h>
#include <WiFiEspUdp.h>
#include "SoftwareSerial.h"
#include <PubSubClient.h>

char ssid[] = "NETGEAR";
char pass[] = "smarthome";

char DEVICE_NAME[] = "random name"; // hardware name of the device
char IOT_INITIALIZE[] = "iot/initialize";  // wait till raspberry wants to know about IoT devices
char IOT_DEVICE_IP[] = "iot/device/ip/";   // with ip - wait for client actions requests
char RASP_WELCOME[] = "raspberry/welcome"; // publish services & ip to this theme
char RASP_RESPONSE[] = "iot/device/result/ip/"; // publish response of the request here 
int status = WL_IDLE_STATUS;
int MQTT_SERVER_PORT = 1883;
int QUALITY_OF_SERVICE = 1;

int TEMPERATURE = 0;
int ON_OFF = 1;

int SERVICES_COUNT = 1;
int SERVICES[] = { ON_OFF };

// PINS
int ledPin = 2;
int WIFI_RX_PIN = 8;
int WIFI_TX_PIN = 9;


SoftwareSerial esp(WIFI_RX_PIN, WIFI_TX_PIN);
WiFiEspClient espClient;
PubSubClient client(espClient);
IPAddress server(192, 168, 1, 5);

void setup() {
  pinMode(ledPin, OUTPUT);
  Serial.begin(9600);
  esp.begin(9600);
  WiFi.init(&esp);

  connectToWifi();
  
  client.setServer(server, MQTT_SERVER_PORT);
  client.setCallback(callback);
}

void connectToWifi() {
  while (status != WL_CONNECTED) {
    Serial.print("Attempting to connect to WPA SSID: ");
    Serial.println(ssid);
    status = WiFi.begin(ssid, pass);
  }

  Serial.println("You're connected to the network");
}

void printWifiData()
{
  IPAddress ip = WiFi.localIP();
  Serial.print("IP Address: ");
  Serial.println(ip);

  // print your MAC address
  byte mac[6];
  WiFi.macAddress(mac);
  char buf[20];
  sprintf(buf, "%02X:%02X:%02X:%02X:%02X:%02X", mac[5], mac[4], mac[3], mac[2], mac[1], mac[0]);
  Serial.print("MAC address: ");
  Serial.println(buf);
}

void loop() {
  if (!client.connected()) {
    reconnect(); // blocking call, return only after connection established
    subscribeToThemes();
  }
  client.loop();
}

void callback(char* topic, byte* raw_payload, unsigned int length) {
  Serial.println("callback:");
  Serial.println(topic);
  
  char payload[length + 1];
  memcpy(payload, raw_payload, length);
  payload[length] = '\0';
  Serial.println(payload);

  if (strcmp(topic, IOT_INITIALIZE) == 0) {
    String respPayload = "name=\"" + String(DEVICE_NAME) + "\";ip=\"" +
        toString(WiFi.localIP()) + "\"'services=[" + int_array_to_string(SERVICES, SERVICES_COUNT) + "];";
    Serial.print("initialize request, send \"");
    Serial.print(respPayload);
    Serial.println("\" to mqtt server");
    int n = respPayload.length();
    char buffer[n];
    respPayload.toCharArray(buffer, n); 
    client.publish(RASP_WELCOME, buffer);
  } else if (String(topic) == IOT_DEVICE_IP + toString(WiFi.localIP())) {
    // take action
    Serial.println("Take action");
  }
} 

String int_array_to_string(int int_array[], int size_of_array) {
  String returnstring = "";
  for (int temp = 0; temp < size_of_array - 1; temp++)
    returnstring += String(int_array[temp]) + ", ";

  returnstring += String(int_array[size_of_array - 1]);
  return returnstring;
}

void reconnect() {
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    if (client.connect(DEVICE_NAME)) {
      Serial.println("connected");
      return;
    } 

    Serial.print("failed, rc=");
    Serial.print(client.state());
    Serial.println(" try again in 5 seconds");
    // Wait 5 seconds before retrying
   
    delay(5000);
  }
}

String toString(IPAddress _address)
{
    String str = String(_address[0]);
    str += ".";
    str += String(_address[1]);
    str += ".";
    str += String(_address[2]);
    str += ".";
    str += String(_address[3]);
    return str;
}

void subscribeToThemes() {
  Serial.print("Subscribe to themes:");
  Serial.print(IOT_INITIALIZE);
  Serial.print(", ");
  String theme = IOT_DEVICE_IP + toString(WiFi.localIP());
  char buff_iot_device_theme[theme.length() + 1];
  theme.toCharArray(buff_iot_device_theme, theme.length() + 1);
  buff_iot_device_theme[theme.length()] = '\0';
  Serial.println(buff_iot_device_theme);
  client.subscribe(IOT_INITIALIZE); // to send ip & services, when raspberry asks to
  client.subscribe(buff_iot_device_theme);  // to perform some action, initiated by raspberry or client
  Serial.println("Subscribed");
}
