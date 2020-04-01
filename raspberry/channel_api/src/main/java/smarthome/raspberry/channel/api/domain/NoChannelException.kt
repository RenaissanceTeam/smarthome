package smarthome.raspberry.channel.api.domain

import smarthome.raspberry.entity.device.Device

class NoChannelException(device: Device) : Throwable("no channel that can work with $device")