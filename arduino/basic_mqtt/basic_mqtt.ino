#include <WiFiEsp.h>
#include <WiFiEspClient.h>
#include <WiFiEspUdp.h>
#include "SoftwareSerial.h"
#include <PubSubClient.h>

char ssid[] = "NETGEAR";
char password[] = "smarthome";

char DEVICE_NAME[] = "random_name"; // hardware name of the device
char IOT_INITIALIZE[] = "iot/initialize";  // wait till raspberry wants to know about IoT devices
char IOT_DEVICE_IP[] = "iot/device/ip/";   // with ip - wait for client actions requests
char RASP_WELCOME[] = "raspberry/welcome"; // publish services & ip to this theme
char RASP_RESPONSE[] = "iot/device/result/ip/"; // publish response of the request here 
int status = WL_IDLE_STATUS;
int MQTT_SERVER_PORT = 1883;
int QUALITY_OF_SERVICE = 1;

int TEMPERATURE = 1000;
int ON_OFF = 1001;


// PINS
int ledPin = 2;
int WIFI_RX_PIN = 8;
int WIFI_TX_PIN = 9;


int SERVICES_COUNT = 1;
int SERVICES[] = { ON_OFF };
int SERVICE_PINS[] = { ledPin };


SoftwareSerial esp(WIFI_RX_PIN, WIFI_TX_PIN);
WiFiEspClient espClient;
PubSubClient client(espClient);
IPAddress server(192, 168, 1, 5);

void setup() {
  pinMode(ledPin, OUTPUT);
  Serial.begin(9600);
  esp.begin(9600);
  WiFi.init(&esp);

  setup_wifi();
  
  client.setServer(server, MQTT_SERVER_PORT);
  client.setCallback(callback);
}

void setup_wifi() {
  delay(10);
  // We start by connecting to a WiFi network
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);

  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  randomSeed(micros());

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
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
  memcpy(payload, (char*)raw_payload, length);
  payload[length] = '\0';
  Serial.println(payload);

  if (strcmp(topic, IOT_INITIALIZE) == 0) {
    String respPayload = "type=0;name=" + String(DEVICE_NAME) + ";ip=" +
        toString(WiFi.localIP()) + ";services=[" + int_array_to_string(SERVICES, SERVICES_COUNT) + "]; ";
    Serial.print("initialize request, send \"");
    Serial.print(respPayload);
    Serial.println("\" to mqtt server");
    int n = respPayload.length();
    char buffer[n + 1];
    buffer[n] = '\0';
    respPayload.toCharArray(buffer, n); 
    
    bool sendResult = false;
    do {
      Serial.println("Trying to send message");
      client.publish(RASP_WELCOME, buffer);
    } while (sendResult);
    Serial.println("Successfully published message");
  } else if (String(topic) == getMainActionsTheme()) {
    // message should be something like "service={service index};{value}"
    // take action
    Serial.println("Take action");
    String index;
    String value;
    bool isParsingIndex = true;
    for (int i = 0; i < length; ++i) {
      if (raw_payload[i] == ';') {
        isParsingIndex = false;
        continue;
      }
      if (isParsingIndex) {
        index += (char)raw_payload[i];
      } else {
        value += (char)raw_payload[i];
      }
    }
    Serial.print("Parsed index:");
    Serial.println(index);
    
    Serial.print("Parsed value:");
    Serial.println(value);

    int parsedIndex = index.toInt();
    int parsedValue = value.toInt();

    if (SERVICES[parsedIndex] == ON_OFF) {
      digitalWrite(SERVICE_PINS[parsedIndex], parsedValue);  
      String respPayload = "service_index=" + index + ";result=" + value + ";";
      Serial.print("action request, send \"");
      Serial.print(respPayload);
      Serial.println("\" to mqtt server");
      int n = respPayload.length();
      char messageBuffer[n + 1];
//      messageBuffer[n] = '\0';
      respPayload.toCharArray(messageBuffer, n + 1);  

      String responseTopic = "raspberry/device/result/ip/" + toString(WiFi.localIP());
      int responseLength = responseTopic.length();
      char topicBuffer[responseLength + 1];
//      topicBuffer[n] = '\0';
      responseTopic.toCharArray(topicBuffer, responseLength + 1);
      
      bool sendResult = false;
      do {
        Serial.println("Trying to send message");
        client.publish(topicBuffer, messageBuffer);
      } while (sendResult);
    }

    
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

String getMainActionsTheme() {
  return IOT_DEVICE_IP + toString(WiFi.localIP()); 
}

void subscribeToThemes() {
  Serial.print("Subscribe to themes:");
  Serial.print(IOT_INITIALIZE);
  Serial.print(", ");
  String theme = getMainActionsTheme();
  char buff_iot_device_theme[theme.length() + 1];
  theme.toCharArray(buff_iot_device_theme, theme.length() + 1);
  buff_iot_device_theme[theme.length()] = '\0';
  Serial.println(buff_iot_device_theme);
  client.subscribe(IOT_INITIALIZE, 1); // to send ip & services, when raspberry asks to
  client.subscribe(buff_iot_device_theme, 1);  // to perform some action, initiated by raspberry or client
  Serial.println("Subscribed");
}
