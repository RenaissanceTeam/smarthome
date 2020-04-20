package smarthome.raspberry.controllers.data

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.reactive.RxJava2CrudRepository
import smarthome.raspberry.core.ObservableJpaRepository
import smarthome.raspberry.entity.controller.Controller

interface ControllersRepo : ObservableJpaRepository<Controller, Long>