package smarhome.client.data

import android.annotation.SuppressLint
import android.content.Context
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import smarthome.client.data_api.LocalStorage
import smarthome.library.common.IotDevice
import smarthome.library.common.scripts.Script

class LocalStorageImpl(appContext: Context) : LocalStorage {
    private val devices = BehaviorSubject.create<MutableList<IotDevice>>()
    private val scripts = BehaviorSubject.create<MutableList<Script>>()
    private val pendingDevices = BehaviorSubject.create<MutableList<IotDevice>>()
    private val appTokenPrefs = appContext.getSharedPreferences(TOKEN_PREFS, Context.MODE_PRIVATE)
    private val homeIdPrefs = appContext.getSharedPreferences(HOME_ID_PREFS, Context.MODE_PRIVATE)

//    override fun saveScript(script: Script) {
//        if (Model.scripts.contains(script)) {
//            Model.scripts[Model.scripts.indexOf(script)] = script
//        } else {
//            Model.scripts.add(script)
//        }
//        Model._scriptsObservable?.onNext(Model.scripts)
//    }
//
//    override fun getScript(guid: Long): Script? {
//        return Model.scripts.find { it.guid == guid }
//    }

    override suspend fun saveDevices(devices: MutableList<IotDevice>) {
        this.devices.onNext(devices)
    }

    override suspend fun getDevices(): BehaviorSubject<MutableList<IotDevice>> {
        return devices
    }

    @SuppressLint("ApplySharedPref")
    override suspend fun saveAppToken(newToken: String) {
        withContext(Dispatchers.IO) {
            appTokenPrefs.edit().putString(TOKEN_KEY, newToken).commit()
        }
    }

    override suspend fun getAppToken(): String? {
        return withContext(Dispatchers.IO) {
            appTokenPrefs.getString(TOKEN_KEY, null)
        }
    }

    override suspend fun updateDevice(device: IotDevice) {
        updateObjectInSource(device, devices)
    }

    private fun <T>updateObjectInSource(obj: T, source: BehaviorSubject<MutableList<T>> ) {
        val objects = source.value ?: return

        val objIndex = objects.indexOf(obj)
        objects[objIndex] = obj

        source.onNext(objects)
    }

    override suspend fun getSavedHomeId(): String? {
        return homeIdPrefs.getString(HOME_ID_KEY,  null)
    }

    override suspend fun getPendingDevices(): BehaviorSubject<MutableList<IotDevice>> {
        return pendingDevices
    }

    override suspend fun updatePendingDevice(device: IotDevice) {
        updateObjectInSource(device, pendingDevices)
    }

    override suspend fun getScripts(): BehaviorSubject<MutableList<Script>> {
        return scripts
    }

    override suspend fun saveScript(script: Script) {
        updateObjectInSource(script, scripts)
    }
}