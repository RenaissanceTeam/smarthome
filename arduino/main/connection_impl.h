#include "WebServer.h"
#include "SoftwareSerial.h"
#include <ArduinoHttpClient.h>
#include "configuration.h"

#ifdef INIT_SERVICE
#include <WiFiEspUdp.h>
#endif

#define DEBUG 0

#define home_info "name=" DEVICE_NAME "&services=" SERVICES_STR "&names=" SERVICE_NAMES_STR

WiFiEspClient wifiClient;
HttpClient* client;
char ip[IP_BUFFER_LENGTH];



void baseResponse(WebServer& server, int val) {
  Serial.println(val);
  server.httpSuccess();
  server.print("{\"response\" : \"");
  server.print(val);
  server.print("\"}");
  server.print(CRLF);
  server.flushBuf();
}

void baseResponse(WebServer& server, double val) {
  Serial.println(val);
  server.httpSuccess();
  server.print("{\"response\" : \"");
  server.print(val);
  server.print("\"}");
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
      Serial.println(PINS[serviceIndex]);
#endif
      DHT11.read11(PINS[serviceIndex]);
      baseResponse(server, DHT11.humidity);
}


void temperatureDht11GetRequest(WebServer& server, int serviceIndex) {
  #if DEBUG > 1
      Serial.print("read TEMPERATURE on pin ");
      Serial.println(PINS[serviceIndex]);
#endif
      DHT11.read11(PINS[serviceIndex]);
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
      Serial.println(PINS[serviceIndex]);
#endif
      DHT22.read22(PINS[serviceIndex]);
      baseResponse(server, DHT22.humidity);
}


void temperatureDht22GetRequest(WebServer& server, int serviceIndex) {
  #if DEBUG > 1
      Serial.print("read TEMPERATURE on pin ");
      Serial.println(PINS[serviceIndex]);
#endif
      DHT22.read22(PINS[serviceIndex]);
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
      Serial.println(PINS[serviceIndex]);
#endif
      baseResponse(server, digitalRead(PINS[serviceIndex]));
} 

void onoffPostRequest(WebServer& server, int serviceIndex, int value) {
#if DEBUG > 1
      Serial.print("set ON_OFF on pin ");
      Serial.print(PINS[serviceIndex]);
      Serial.print(" to value ");
      Serial.println(value);
#endif
      digitalWrite(PINS[serviceIndex], value);
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
      Serial.println(PINS[serviceIndex]);
#endif
      baseResponse(server, analogRead(PINS[serviceIndex]));
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
      Serial.println(PINS[serviceIndex]);
#endif
      baseResponse(server, digitalRead(PINS[serviceIndex]));
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

  if (serviceIndex < 0 || serviceIndex >= SERVICES_COUNT) {
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
    if (SERVICES[serviceIndex] == ONOFF_ID) {
      onoffGetRequest(server, serviceIndex);
      return;
    }
#endif

#ifdef ANALOG
    if (SERVICES[serviceIndex] == ANALOG_ID) {
      analogGetRequest(server, serviceIndex);
      return;
    }
#endif


#ifdef TEMPERATURE_DHT11
    if (SERVICES[serviceIndex] == TEMPERATURE_DHT11_ID) {
      temperatureDht11GetRequest(server, serviceIndex);  
      return;
    }
#endif

#ifdef HUMIDITY_DHT11
    if (SERVICES[serviceIndex] == HUMIDITY_DHT11_ID) {
      humidityDht11GetRequest(server, serviceIndex);
      return;
    }
#endif

#ifdef TEMPERATURE_DHT22
    if (SERVICES[serviceIndex] == TEMPERATURE_DHT22_ID) {
      temperatureDht22GetRequest(server, serviceIndex);  
      return;
    }
#endif

#ifdef HUMIDITY_DHT22
    if (SERVICES[serviceIndex] == HUMIDITY_DHT22_ID) {
      humidityDht22GetRequest(server, serviceIndex);
      return;
    }
#endif

#ifdef DIGITAL_ALERT
    if (SERVICES[serviceIndex] == DIGITAL_ALERT_ID) {
      digitalAlertGetRequest(server, serviceIndex);
      return;
    }
#endif

  } else if (type == WebServer::POST) {

#ifdef ONOFF   
    if (SERVICES[serviceIndex] == ONOFF_ID) {
      onoffPostRequest(server, serviceIndex, parsedValue);
      return;
    }
#endif

    // todo read other types

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
    // don't continue
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

void homePage(WebServer &server, WebServer::ConnectionType type,
 			  char* params, bool complete)
{
  server.httpSuccess();
  server.print(home_info);
  server.flushBuf();
}


void init(WebServer &server, WebServer::ConnectionType type, char * params, bool complete) {
  server.httpSuccess();
  Serial.println(home_info);
  server.getRemoteIp(ip);
  Serial.print(F("remote ip="));
  Serial.println(ip);
  
  
  if (client != 0) delete client;
  client = new HttpClient(wifiClient, ip, RASPBERRY_PORT);
  client->post("/init?" home_info, "text", "");
  client->flush();
  client->stop();
}

void runHttpServer(WebServer& server) {
  server.setDefaultCommand(&homePage);    // callback to home page request
  server.addCommand("service", &service); // smart home server request to do something with service
  server.addCommand("init", &init);
  server.begin();
}

#ifdef INIT_SERVICE
void sendUdpInitToHomeServer() {
  Serial.println("send udp");
  WiFiEspUDP udpClient;
  IPAddress broadcastIp(192,168,1,255);
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
	if (client == 0) {
	  return;
	}
	client->post("/alert?ind=" + String(serviceIndex) + "&value=" + value, "text", "");
  client->flush();
  client->stop();
}
#endif
