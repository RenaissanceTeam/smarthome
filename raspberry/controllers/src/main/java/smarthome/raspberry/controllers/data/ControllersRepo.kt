package smarthome.raspberry.controllers.data

import org.springframework.data.jpa.repository.JpaRepository
import smarthome.raspberry.entity.Controller

interface ControllersRepo : JpaRepository<Controller, Long>