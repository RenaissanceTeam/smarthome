package smarthome.client.data.api.homeserver

import smarthome.client.domain.api.entity.HomeServer

interface HomeServerRepo {
    fun get(): HomeServer
    fun save(homeServer: HomeServer)
}