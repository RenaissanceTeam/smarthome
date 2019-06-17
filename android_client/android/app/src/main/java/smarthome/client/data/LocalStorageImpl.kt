package smarthome.client.data

import io.reactivex.subjects.BehaviorSubject
import smarthome.library.common.IotDevice
import smarthome.library.common.scripts.Script

class LocalStorageImpl : LocalStorage {
//    private val devices = BehaviorSubject.create<MutableList<IotDevice>>()

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

    override suspend fun saveDevices(devices: List<IotDevice>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getDevices(): BehaviorSubject<MutableList<IotDevice>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun saveAppToken(newToken: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getAppToken(): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun updateDevice(device: IotDevice) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getPendingDevices(): BehaviorSubject<MutableList<IotDevice>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun updatePendingDevice(device: IotDevice) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getScripts(): BehaviorSubject<MutableList<Script>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun saveScript(script: Script) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}