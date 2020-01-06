package smarthome.raspberry.authentication.di

import org.koin.core.module.Module
import org.koin.dsl.module

private val domain = module {

}


private val data = module {
}

val authenticationModule: List<Module> = listOf(
        domain,
    data
)