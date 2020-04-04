#ifdef DIGITAL_ALERT
int* digitalAlertStates;
int period = 1000;
unsigned long time_now = 0;

void alertSetup() {
  int alertCount = 0;
  for (int i = 0; i < SIZE(services); ++i) {
    if (services[i].type == DIGITAL_ALERT_ID) alertCount++;
  }
  digitalAlertStates = new int [alertCount];

	for (int i = 0; i < alertCount; ++i) {
		digitalAlertStates[i] = LOW;
	}
}

void notifyIfStateChanged(int cur, int serviceIndex) {
	
	int newState = digitalRead(services[serviceIndex].pin);
	if (newState != digitalAlertStates[cur]) {
		digitalAlertStates[cur] = newState; 
		Serial.print("Notify about state change on ");
		Serial.print(services[serviceIndex].pin);
		Serial.print(", new state is "); Serial.println(newState);

		sendAlertToServer(serviceIndex, newState);
	}
}

void notifyIfHighOnAnyDigitalAlert() {
	int cur = 0; // index in states array
	for (int i = 0; i < SIZE(services); ++i) {
		if (services[i].type == DIGITAL_ALERT_ID) {
			notifyIfStateChanged(cur, i);
			++cur;
		}
	}
}

bool timeToCheckAlerts() {
	if (millis() > time_now + period) {
        time_now = millis();
        return true;
    }
    return false;
}
#endif
