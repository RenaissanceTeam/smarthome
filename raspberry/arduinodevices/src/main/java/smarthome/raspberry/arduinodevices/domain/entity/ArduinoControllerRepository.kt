package smarthome.raspberry.arduinodevices.domain.entity

import org.springframework.data.jpa.repository.JpaRepository

interface ArduinoControllerRepository : JpaRepository<ArduinoController, Long>
