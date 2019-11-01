package smarthome.client.data_api

import smarthome.client.entity.HomeException

class NoHomeid : HomeException("Can't use firestore, because no homeId for user is found")
