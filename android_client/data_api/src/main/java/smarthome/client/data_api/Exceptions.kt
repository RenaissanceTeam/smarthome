package smarthome.client.data_api

class NoHomeid : HomeException("Can't use firestore, because no homeId for user is found")
