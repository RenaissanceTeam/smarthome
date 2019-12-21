package smarthome.raspberry.util.router

import kotlin.reflect.KClass

interface Router {
    fun <T : Any> startFlow(to: KClass<T>, flags: Int = 0)
}
