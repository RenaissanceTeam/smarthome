#ifdef INIT_SERVICE
int initState = LOW;
int init_button_period = 1000;
unsigned long init_button_time_now = 0;

void notifyIfHighOnInitButton() {
	int newState = digitalRead(INIT_PIN);
	if (newState != initState) {
		initState = newState; 
		if (newState == HIGH) {

			Serial.print("Init button is pushed");
			sendUdpInitToHomeServer();
		}
	}
}

bool timeToCheckInitButton() {
    if (millis() > init_button_time_now + init_button_period) {
        init_button_time_now = millis();
        return true;
    }
    return false;
}
#endif