package smarthome.client.util

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrofitCallback<T> : Callback<T> {
    var onResponse: ((Response<T>) -> Unit)? = null
    var onFailure: ((t: Throwable?) -> Unit)? = null

    override fun onFailure(call: Call<T>, t: Throwable) {
        onFailure?.invoke(t)
    }

    override fun onResponse(call: Call<T>, response: Response<T>) {
        onResponse?.invoke(response)
    }

}

fun<T> Call<T>.enqueue(callback: RetrofitCallback<T>.() -> Unit) {
    val customCallback = RetrofitCallback<T>()
    callback.invoke(customCallback)
    this.enqueue(customCallback)
}