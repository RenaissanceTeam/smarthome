package smarthome.raspberry

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.koinApplication
import org.koin.test.KoinTest
import org.koin.test.check.checkModules
import smarthome.raspberry.di.appModules
import smarthome.raspberry.di.mocks.frameworkDependentClasses
import smarthome.raspberry.notification.R

class AppTest : KoinTest {
    private val mockSharedPrefs: SharedPreferences = mock {}
    private val mockResources: Resources = mock {
        on { it.getString(R.string.fcm_sender_url) }.thenReturn("http://mock.it")
        on { it.getString(any()) }.thenReturn("")
    }

    private val mockedContext = mock<Context> {
        on(it.getSharedPreferences(any(), any())).thenReturn(mockSharedPrefs)
        on(it.resources).thenReturn(mockResources)
    }

    @Test
    fun `koin modules should pass checkModules`() {
        koinApplication {
            androidContext(mockedContext)
            modules(appModules + frameworkDependentClasses)
        }.checkModules()
    }
}