package smarthome.raspberry.util.persistence

import kotlin.reflect.KClass

class NoStoredPreference(key: String) : IllegalArgumentException("No stored value for key=$key.")

class TypeAccessException(from: KClass<*>, to: KClass<*>) :
    IllegalArgumentException("Can't convert from $from to $to")