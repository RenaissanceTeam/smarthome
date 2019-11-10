package smarthome.raspberry

import org.junit.Test
import org.koin.dsl.koinApplication
import org.koin.test.KoinTest
import org.koin.test.check.checkModules
import smarthome.raspberry.di.appModules

class AppTest: KoinTest {
    
    @Test
    fun `koin modules should pass checkModules`() {
        koinApplication {
            appModules
        }.checkModules()
    }
}