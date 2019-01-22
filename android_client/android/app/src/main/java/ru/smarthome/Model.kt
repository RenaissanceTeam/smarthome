package ru.smarthome

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.smarthome.constants.Constants
import ru.smarthome.library.BaseController
import ru.smarthome.library.IotDevice
import ru.smarthome.library.RaspberryResponse
import ru.smarthome.library.SmartHome

// todo should not be a singleton
object Model {
    private val TAG = Model::class.java.simpleName

    private val mutableControllers = MutableLiveData<MutableList<BaseController>>()

    private var controllers = mutableListOf<BaseController>()
    private var smartHomeState: SmartHome? = null

    private val raspberryApi: RaspberryApi
        get() {
            val gson = GsonBuilder().setLenient().create()

            val retrofit = Retrofit.Builder()
                    .baseUrl(Constants.RASPBERRY_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()

            return retrofit.create(RaspberryApi::class.java)
        }

    fun getHomeState() : LiveData<MutableList<BaseController>> {
        mutableControllers.value?.let { return mutableControllers }
        return requestHomeStateFromRaspberry()
    }

    fun requestHomeStateFromRaspberry(): LiveData<MutableList<BaseController>> {
        if (BuildConfig.DEBUG) Log.d(TAG, "make http request to raspberry to get state")

        raspberryApi.getSmartHomeState().enqueue {
            onResponse = { response ->
                if (BuildConfig.DEBUG) Log.d(TAG, "response: $response")
                if (response.isSuccessful) {
                    if (BuildConfig.DEBUG) Log.d(TAG, "responseBody= ${response.body()}")
                    response.body()?.let { newHomeState ->
                        smartHomeState = newHomeState
                        controllers = getAllControllersFrom(newHomeState)
                        mutableControllers.value = controllers
                        if (BuildConfig.DEBUG) Log.d(TAG, "set newHome value to ${mutableControllers.value}")
                    }
                    // todo notify about error
                } else {
                    // todo notify about error
                }
            }

            onFailure = {
                handleRequestFailure(it)
            }
        }
        return mutableControllers
    }

    fun getDevice(controller: BaseController) : IotDevice? {
        return smartHomeState?.devices?.find { it.controllers.contains(controller) }
    }

    private fun getAllControllersFrom(sh: SmartHome): MutableList<BaseController> {
        val controllers = mutableListOf<BaseController>()
        sh.devices.forEach { controllers.addAll(it.controllers) }
        return controllers
    }

    // todo should return LiveData<ControllerState> but now String is controller state
    fun readController(controller: BaseController): LiveData<MutableList<BaseController>> {
        if (BuildConfig.DEBUG) Log.d(TAG, "read $controller")
        val controllerGuid = controller.guid

        raspberryApi.readControllerState(controllerGuid).enqueue {
            onResponse = { response ->
                if (response.isSuccessful) {
                    handleReadControllerStateResponse(controller, response)
                } else {
                    handleResponseError(response)
                }
            }

            onFailure = { handleRequestFailure(it) }
        }
        return mutableControllers
    }

    fun changeControllerState(controller: BaseController,
                              value: String): LiveData<MutableList<BaseController>> {
        if (BuildConfig.DEBUG) Log.d(TAG, "change ${controller.state} to $value in $controller")
        val controllerGuid = controller.guid

        raspberryApi.changeControllerState(controllerGuid, value).enqueue {
            onResponse = { response ->
                if (response.isSuccessful) {
                    handleReadControllerStateResponse(controller, response)
                } else {
                    handleResponseError(response)
                }
            }

            onFailure = { handleRequestFailure(it) }
        }
        return mutableControllers
    }

    private fun handleReadControllerStateResponse(controller: BaseController,
                                                  response: Response<RaspberryResponse>) {
        if (BuildConfig.DEBUG) Log.d(TAG, "change state in mutable controllersLD")
        controllers.find { it == controller }?.state = response.body()?.response
        mutableControllers.value = controllers
    }

    private fun handleResponseError(response: Response<RaspberryResponse>) {
        val message = "Returned code: ${response.code()}, body=${response.raw().body()?.string()}"
        Log.d(TAG, "returned not successful response $message")
        // todo notify user about error
    }

    private fun handleRequestFailure(it: Throwable?) {
        Log.d(TAG, "onFailure: $it")
        // todo notify user about error
    }
}