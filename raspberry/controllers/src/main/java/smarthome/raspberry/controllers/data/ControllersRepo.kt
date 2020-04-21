package smarthome.raspberry.controllers.data

import smarthome.raspberry.core.ObservableJpaRepository
import smarthome.raspberry.entity.controller.Controller

interface ControllersRepo : ObservableJpaRepository<Controller, Long>