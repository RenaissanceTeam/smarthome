package smarthome.raspberry

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import org.junit.Test
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.check.checkModules
import smarthome.raspberry.di.appModules

class AppTest : KoinTest {
    private val mockedContext = module {
        val mockSharedPrefs: SharedPreferences = mock {}
        val mockResources: Resources =  mock {
            on { it.getString(any()) }.thenReturn("")
        }

        single {
            mock<Context> {
                on(it.getSharedPreferences(any(), any())).thenReturn(mockSharedPrefs)
                on(it.resources).thenReturn(mockResources)
            }
        }
    }

    @Test
    fun `koin modules should pass checkModules`() {
        koinApplication { modules(appModules + mockedContext) }.checkModules()
    }
}