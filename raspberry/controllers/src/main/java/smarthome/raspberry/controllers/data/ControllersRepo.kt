package smarthome.raspberry.controllers.data

import org.springframework.data.jpa.repository.JpaRepository
import smarthome.raspberry.entity.controller.Controller

interface ControllersRepo : JpaRepository<Controller, Long>