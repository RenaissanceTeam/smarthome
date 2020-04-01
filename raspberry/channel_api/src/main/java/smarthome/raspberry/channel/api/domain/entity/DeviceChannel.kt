package smarthome.raspberry.channel.api.domain.entity

import smarthome.raspberry.entity.controller.Controller

interface DeviceChannel {
    fun canWorkWith(type: String): Boolean
    fun read(controller: Controller): String
    fun write(controller: Controller, state: String): String
}