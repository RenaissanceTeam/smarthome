package smarthome.client.data

import smarthome.client.model.Model
import smarthome.library.common.scripts.Script

class LocalStorageImpl : LocalStorage {

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
}