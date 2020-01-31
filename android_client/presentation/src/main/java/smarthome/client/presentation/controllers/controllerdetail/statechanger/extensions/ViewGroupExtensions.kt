package smarthome.client.presentation.controllers.controllerdetail.statechanger.extensions

import android.view.View
import android.view.ViewGroup

fun ViewGroup.inflate(layout: Int): View {
    return View.inflate(context, layout, this)
}