package smarthome.raspberry.devices.data.storage

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import org.junit.Before
import org.junit.Test
import smarthome.library.common.BaseController
import smarthome.library.common.DeviceServeState
import smarthome.library.common.Id
import smarthome.library.common.IotDevice
import smarthome.raspberry.util.SharedPreferencesHelper

class LocalStorageImplTest {
    
    private lateinit var sharedPreferences: SharedPreferencesHelper
    private lateinit var localStorage: LocalStorage
    
    
    @Before
    fun setUp() {
        sharedPreferences = mock {}
        localStorage = LocalStorageImpl(sharedPreferences)
    }
    
    @Test
    fun `when add device should result in adding device to map of devices`() {
        val id = Id("s")
        val device = IotDevice(id = id, controllers = mutableListOf())
        
        localStorage.addDevice(device, IotDeviceGroup.ACTIVE)
        assertThat(localStorage.getDevices(IotDeviceGroup.ACTIVE)).contains(device)
    }
    
    @Test
    fun `when get devices in group should return added devices`() {
        val id = Id("s")
        val device = IotDevice(id = id, controllers = mutableListOf())
    
        localStorage.addDevice(device, IotDeviceGroup.ACTIVE)
        assertThat(localStorage.getDevices(IotDeviceGroup.PENDING)).isEmpty()
        assertThat(localStorage.getDevices(IotDeviceGroup.ACTIVE)).contains(device)
    }
    
    @Test
    fun `when removeDevice() should remove it from added devices`() {
        val id = Id("s")
        val device = IotDevice(id = id, controllers = mutableListOf())
    
        localStorage.addDevice(device, IotDeviceGroup.ACTIVE)
        localStorage.removeDevice(device, IotDeviceGroup.ACTIVE)
        assertThat(localStorage.getDevices(IotDeviceGroup.ACTIVE)).isEmpty()
    }
    
    @Test
    fun `when update device should find and replace it in map of devices`() {
        val id = Id("same")
        
        val controllersBefore = mutableListOf<BaseController>()
        val device = IotDevice(id = id, controllers = controllersBefore)
        localStorage.addDevice(device, IotDeviceGroup.ACTIVE)
        
        val controllersAfter = mutableListOf<BaseController>()
        val updateDevice =IotDevice(id = id, controllers = controllersAfter)
        
        localStorage.updateDevice(updateDevice, IotDeviceGroup.ACTIVE)
        assertThat(localStorage.getDevices().find { it == device }?.controllers).isEqualTo(controllersAfter)
    }
    
}