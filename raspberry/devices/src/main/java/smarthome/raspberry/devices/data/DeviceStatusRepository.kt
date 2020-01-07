package smarthome.raspberry.devices.data

import org.springframework.data.jpa.repository.JpaRepository
import smarthome.raspberry.entity.DeviceStatus

interface DeviceStatusRepository: JpaRepository<DeviceStatus, Long>