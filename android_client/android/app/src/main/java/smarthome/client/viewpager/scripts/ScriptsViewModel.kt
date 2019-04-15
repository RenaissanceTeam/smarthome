package smarthome.client.viewpager.scripts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import smarthome.client.MockAction
import smarthome.client.ControllerCondition
import smarthome.client.Script

class ScriptsViewModel : ViewModel() {
    private val _scripts = MutableLiveData<MutableList<Script>>()
    private val _refresh = MutableLiveData<Boolean>()

    val scripts: LiveData<MutableList<Script>>
        get() = _scripts

    val refresh: LiveData<Boolean>
        get() = _refresh


    init {
        val scripts = mutableListOf<Script>()
        val titles = listOf("Control light", "Curtains", "Check temperature", "Door opening",
                "Garage Light", "Main Light", "Alert check sensors", "Everyday check", "Light Sensor",
                "Alarm Curtains", "Rain sensor")
//        for (title in titles)
//            scripts.add(Script(title, mutableListOf(ControllerCondition()), mutableListOf(MockAction())))
        _scripts.value = scripts
    }

    fun onRefresh() {
        _refresh.value = false
    }


}