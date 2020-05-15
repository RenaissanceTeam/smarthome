package smarthome.raspberry.scripts.api.data

import io.reactivex.disposables.Disposable

interface RegisteredProtocolsRepository {
    fun register(scriptId: Long, disposable: Disposable)
    fun unregister(scriptId: Long)
    fun isRegistered(id: Long): Boolean
}