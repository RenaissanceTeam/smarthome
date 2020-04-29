#!/bin/bash

cd ../raspberry
./gradlew clean assemble
scp app/build/libs/app-*jar pi@192.168.0.102:~/smarthome/app.jar

