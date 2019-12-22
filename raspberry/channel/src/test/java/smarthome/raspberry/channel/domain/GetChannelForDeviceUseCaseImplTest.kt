package smarthome.raspberry.channel.domain

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test
import smarthome.library.common.DeviceChannel
import smarthome.library.common.IotDevice
import smarthome.raspberry.channel.api.domain.GetChannelForDeviceUseCase
import smarthome.raspberry.channel.api.domain.NoChannelException
import smarthome.raspberry.channel.data.ChannelRepository
import kotlin.test.assertFailsWith

class GetChannelForDeviceUseCaseImplTest {
    private lateinit var useCase: GetChannelForDeviceUseCase
    private lateinit var repo: ChannelRepository
    
    
    @Before
    fun setUp() {
        repo = mock { }
        useCase = GetChannelForDeviceUseCaseImpl(repo)
    }
    
    @Test
    fun `when no channels can work with device should throw NoChannelException`() {
        val device = mock<IotDevice>()
        
        assertFailsWith<NoChannelException> {
            useCase.execute(device)
        }
    }
    
    @Test
    fun `when has channel for device should return it`() {
        val device = mock<IotDevice>()
        val channel = mock<DeviceChannel> {
            on { canWorkWith(device) }.then { false }
        }
        whenever(repo.getDeviceChannels()).then { listOf(channel) }
        
        assertThat(useCase.execute(device)).isEqualTo(channel)
    }
}