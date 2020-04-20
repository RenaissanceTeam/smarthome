package smarthome.raspberry.core

import java.util.*

fun <T> T?.toOptional(): Optional<T> = Optional.ofNullable(this)