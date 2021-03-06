package smarthome.client.presentation.homeserver

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.koin.core.inject
import smarthome.client.domain.api.homeserver.usecases.ChangeHomeServerUrlUseCase
import smarthome.client.domain.api.homeserver.usecases.ObserveActiveHomeServerUseCase
import smarthome.client.domain.api.homeserver.usecases.ObserveRecentServersUseCase
import smarthome.client.entity.HomeServer
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.NavigationLiveData
import smarthome.client.util.data
import smarthome.client.util.fold

class HomeServerViewModel : KoinViewModel() {
    private val changeHomeServerUrlUseCase by inject<ChangeHomeServerUrlUseCase>()
    private val observeHomeServerUrlUseCase: ObserveActiveHomeServerUseCase by inject()
    private val observeRecentServersUseCase: ObserveRecentServersUseCase by inject()
    val serverUrl = MutableLiveData("")
    val recents = MutableLiveData<List<HomeServer>>()
    val close = NavigationLiveData()
    val toLogin = NavigationLiveData()

    override fun onResume() {
        disposable.add(
                observeHomeServerUrlUseCase.execute().subscribe {
                    it.data?.let { serverUrl.postValue(it.url) }
                }
        )

        disposable.add(
                observeRecentServersUseCase.execute().subscribe {
                    recents.postValue(it)
                }
        )
    }

    fun save(url: String) {
        viewModelScope.launch {
            changeHomeServerUrlUseCase.execute(url).fold(
                    ifTrue = { toLogin.trigger() },
                    ifFalse = { close.trigger() }
            )
        }
    }

    fun selectRecent(server: HomeServer) {
        serverUrl.value = server.url
    }
}