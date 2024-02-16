package ru.sample.duckapp.data

import okhttp3.ResponseBody
import ru.sample.duckapp.domain.Duck
import ru.sample.duckapp.infra.Api

class DucksRepository {

    private val api = Api.ducksApi

    fun getRandomDuck(callback: (Duck?, String?) -> Unit) {
        api.getRandomDuck().enqueue(ApiCallback<Duck>(callback))
    }

    fun getDuckByStatusCode(code: String, callback: (ResponseBody?, String?) -> Unit) {
        api.getDuckByStatusCode(code).enqueue(ApiCallback<ResponseBody>(callback))
    }
}
