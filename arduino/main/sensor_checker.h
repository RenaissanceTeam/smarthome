#ifdef DIGITAL_ALERT
int digitalAlertStates[DIGITAL_ALERT_COUNT];
int period = 1000;
unsigned long time_now = 0;

void alertSetup() {
	for (int i = 0; i < DIGITAL_ALERT_COUNT; ++i) {
		digitalAlertStates[i] = LOW;
	}
}

void notifyIfStateChanged(int cur, int serviceIndex) {
	
	int newState = digitalRead(PINS[serviceIndex]);
	if (newState != digitalAlertStates[cur]) {
		digitalAlertStates[cur] = newState; 
		Serial.print("Notify about state change on ");
		Serial.print(PINS[serviceIndex]);
		Serial.print(", new state is "); Serial.println(newState);

		sendAlertToServer(serviceIndex, newState);
	}
}

void notifyIfHighOnAnyDigitalAlert() {
	int cur = 0; // index in states array
	for (int i = 0; i < SERVICES_COUNT; ++i) {
		if (SERVICES[i] == DIGITAL_ALERT_ID) {
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
