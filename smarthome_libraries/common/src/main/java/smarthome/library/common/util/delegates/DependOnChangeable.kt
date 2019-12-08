package smarthome.library.common.util.delegates

import smarthome.library.common.util.Holder
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class DependOnChangeable<R, CHANGEABLE, T>(
        val changeableHolder: Holder<CHANGEABLE>,
        val delegate: (CHANGEABLE) -> T
) : ReadOnlyProperty<R, T> {
    private var lastChangeable: CHANGEABLE? = null
    private var state: T? = null

    override fun getValue(thisRef: R, property: KProperty<*>): T {
        val currentChangeable = changeableHolder.get()
        if (currentChangeable == lastChangeable) {
            return state ?: updateState(currentChangeable)
        }
        lastChangeable = currentChangeable
        return updateState(currentChangeable)
    }

    private fun updateState(changeable: CHANGEABLE): T {
        return delegate(changeable).also { state = it }
    }
}