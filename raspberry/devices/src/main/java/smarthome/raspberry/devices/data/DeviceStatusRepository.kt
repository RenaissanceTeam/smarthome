package smarthome.raspberry.devices.data

import org.springframework.data.jpa.repository.JpaRepository
import smarthome.raspberry.devices.domain.entity.DeviceStatus

interface DeviceStatusRepository: JpaRepository<DeviceStatus, Long>