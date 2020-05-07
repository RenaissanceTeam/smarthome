#include "connection_impl.h"
#include <AltSoftSerial.h>

#ifdef DIGITAL_ALERT 
#include "sensor_checker.h"
#endif

#ifdef INIT_SERVICE
#include "init_checker.h"
#endif

#define DEBUG 1

AltSoftSerial esp_serial(RX, TX);
WebServer server("", ARDUINO_PORT);

void setup()
{
  Serial    .begin(9600);           // initialize serial for debugging
  esp_serial.begin(9600);           // initialize serial for ESP module
  
  setupConfiguration();             // method from configuration.h
   

  connectToWifi(esp_serial);        // blocking call, won't return until the wifi connection is established
  
  delay(100);
  sendInitToServer();
  delay(100);
  
  runHttpServer(server);  
  

 

  #ifdef DIGITAL_ALERT 
  alertSetup();
#endif
#if DEBUG > 0
  Serial.println("setup end");
#endif
}

void loop() {
  server.processConnection();
  
  #ifdef DIGITAL_ALERT
  if (timeToCheckAlerts()) notifyIfHighOnAnyDigitalAlert();
  #endif

  #ifdef INIT_SERVICE
  if (timeToCheckInitButton()) notifyIfHighOnInitButton();
  #endif
}
