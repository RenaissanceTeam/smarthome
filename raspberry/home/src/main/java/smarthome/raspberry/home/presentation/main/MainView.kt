package smarthome.raspberry.home.presentation.main

import smarthome.raspberry.entity.HomeInfo

interface MainView {
    fun setAuthStatus(toString: String)
    fun setHomeInfo(homeInfo: HomeInfo)
}