package smarthome.client.presentation.controllers.controllerdetail.statechanger.extensions

import com.dd.processbutton.iml.ActionProcessButton

fun ActionProcessButton.loading() {
    progress = 50
}

fun ActionProcessButton.complete() {
    progress = 100
}

fun ActionProcessButton.error() {
    progress = -1
}

fun ActionProcessButton.idle() {
    progress = 0
}




