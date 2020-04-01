package smarthome.raspberry.json.mapper

import kotlin.reflect.KClass

annotation class Mapper<T : Any>(val targetEntity: KClass<T>) {

}