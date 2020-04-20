package smarthome.raspberry.controllers.data

import org.springframework.data.repository.reactive.RxJava2CrudRepository
import smarthome.raspberry.entity.controller.Controller

interface ControllersRepo : RxJava2CrudRepository<Controller, Long>