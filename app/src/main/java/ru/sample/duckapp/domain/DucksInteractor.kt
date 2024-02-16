package ru.sample.duckapp.domain

import okhttp3.ResponseBody
import ru.sample.duckapp.data.DucksRepository

class DucksInteractor {

    private val repository = DucksRepository()

    fun getRandomDuck(callback: (Duck?, String?) -> Unit) {
        repository.getRandomDuck(callback)
    }

    fun getDuckByStatusCode(code: String, callback: (ResponseBody?, String?) -> Unit) {
        repository.getDuckByStatusCode(code, callback)
    }
}
