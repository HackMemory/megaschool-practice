package ru.sample.duckapp.data

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.sample.duckapp.domain.Duck

class ApiCallback<T>(private val callback: (T?, String?) -> Unit) : Callback<T> {

    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (response.isSuccessful) {
            val duck = response.body()
            callback(duck, null)
        } else {
            callback(null, "Error: ${response.code()}")
        }
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        callback(null, "Failure: ${t.message}")
    }
}
