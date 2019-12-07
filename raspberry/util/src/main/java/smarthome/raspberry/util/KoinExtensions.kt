package smarthome.raspberry.util

import org.koin.core.module.Module

fun List<Module>.addMockIfIsInMockMode(isMock: Boolean, mockModule: () -> Module): List<Module> {
    return if (isMock) this + mockModule() else this
}
