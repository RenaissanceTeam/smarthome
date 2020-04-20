package smarthome.raspberry.arduinodevices.controllers.data

import org.springframework.data.jpa.repository.JpaRepository
import smarthome.raspberry.arduinodevices.controllers.domain.entity.ArduinoController

interface ArduinoControllerRepository : JpaRepository<ArduinoController, Long>
