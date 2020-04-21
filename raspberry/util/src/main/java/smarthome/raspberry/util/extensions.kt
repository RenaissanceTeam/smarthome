package smarthome.raspberry.util

infix fun <K, V> Map<K, V>.has(key: K) = this[key] != null