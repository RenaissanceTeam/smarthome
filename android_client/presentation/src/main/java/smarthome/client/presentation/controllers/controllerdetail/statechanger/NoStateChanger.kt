package smarthome.client.presentation.controllers.controllerdetail.statechanger

class NoStateChanger(type: String) : Throwable("No state changer found for type $type")