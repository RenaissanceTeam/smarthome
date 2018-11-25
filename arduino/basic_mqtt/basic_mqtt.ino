#include <WiFiEsp.h>
#include <WiFiEspClient.h>
#include <WiFiEspUdp.h>
#include "SoftwareSerial.h"
#include <PubSubClient.h>

//char deviceId = "smart_home_device_1";
char ssid[] = "NETGEAR";
char pass[] = "smarthome";
int status = WL_IDLE_STATUS;
int ledPin = 2;
SoftwareSerial esp(8, 9); // RX, TX
WiFiEspClient espClient;
PubSubClient client(espClient);
IPAddress server(192, 168, 1, 2);

void setup() {
  pinMode(ledPin, OUTPUT);
  Serial.begin(9600);
  esp.begin(9600);

  WiFi.init(&esp);

  while ( status != WL_CONNECTED) {
    Serial.print("Attempting to connect to WPA SSID: ");
    Serial.println(ssid);
    status = WiFi.begin(ssid, pass);
  }

  Serial.println("You're connected to the network");
  client.setServer(server, 1883);
  client.setCallback(callback);
}

//print any message received for subscribed topic
void callback(char* topic, byte* payload, unsigned int length) {
  
  Serial.print("Received: ");
  char msg[length + 1];
  for (int i = 0; i < length; i++) {
    msg[i] = (char)payload[i];
  } 
  msg[length] = '\0';
  Serial.println(msg);

  if (!strncmp((char *)payload, "on", length)) {
    Serial.println("turn on led");
    digitalWrite(ledPin, HIGH);
  } else {
    Serial.println("turn off led");
    digitalWrite(ledPin, LOW);
  }
}

void loop() {
  // put your main code here, to run repeatedly:
  if (!client.connected()) {
    reconnect();
  }
  client.loop();
}

void reconnect() {
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    // Attempt to connect, just a name to identify the client
    if (client.connect("1")) {
      Serial.println("connected");
      // Once connected, publish an announcement...
      client.publish("command", ""); // todo here should be services of this device
      client.subscribe("raspberry/devices/1");
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      // Wait 5 seconds before retrying
      delay(5000);
    }
  }
}
