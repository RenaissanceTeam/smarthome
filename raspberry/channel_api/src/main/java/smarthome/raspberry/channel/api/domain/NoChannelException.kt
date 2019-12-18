package smarthome.raspberry.channel.api.domain

import smarthome.library.common.IotDevice

class NoChannelException(device: IotDevice) : Throwable("no channel that can work with $device")