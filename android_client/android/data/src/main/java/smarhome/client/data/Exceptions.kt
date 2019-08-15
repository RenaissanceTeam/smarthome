package smarhome.client.data

import smarthome.client.domain.HomeException

class NoHomeid : HomeException("Can't use firestore, because no homeId for user is found")
