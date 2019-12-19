package smarthome.raspberry.home.presentation.main

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.rxkotlin.subscribeBy
import smarthome.raspberry.authentication.api.domain.AuthStatus
import smarthome.raspberry.authentication.api.domain.GetAuthStatusUseCase
import smarthome.raspberry.authentication.api.flow.SignInFlowLauncher
import smarthome.raspberry.home.api.domain.GetHomeInfoUseCase
import smarthome.raspberry.home.api.domain.LaunchUseCase

class MainPresenterImpl(
        private val getAuthStatusUseCase: GetAuthStatusUseCase,
        private val signInFlowLauncher: SignInFlowLauncher,
        private val getHomeInfoUseCase: GetHomeInfoUseCase,
        private val launchUseCase: LaunchUseCase,
        private val view: MainView
) : MainPresenter {

    @SuppressLint("CheckResult")
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreateView() {
        Log.d("SMARTHOME", "onCreate()")
        getAuthStatusUseCase.execute()
            .subscribeBy {
                if (it == AuthStatus.NOT_SIGNED_IN) signInFlowLauncher.launch()
                
                view.setAuthStatus(it.toString())
            }
        
        getHomeInfoUseCase.execute().subscribeBy { view.setHomeInfo(it) }
        launchUseCase.execute()
    }
}