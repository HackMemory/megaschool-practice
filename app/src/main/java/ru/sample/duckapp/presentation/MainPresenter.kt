package ru.sample.duckapp.presentation

import okhttp3.ResponseBody
import ru.sample.duckapp.domain.Duck
import ru.sample.duckapp.domain.DucksInteractor

class MainPresenter(
    private val interactor: DucksInteractor,
    private val view: View
) {
    interface View {
        fun <T> displayDuck(duck: T?)
        fun displayError(message: String)
        fun showLoading()
        fun hideLoading()
    }

    fun onViewCreated() {
        interactor.getRandomDuck { duck, error ->
            if (duck != null) {
                view.displayDuck(duck)
            } else {
                view.displayError(error ?: "Unknown error")
            }
        }
    }

    fun onSendButtonClicked(code: String) {
        if (code.isBlank()) {
            interactor.getRandomDuck { duck, error ->
                if (duck != null) {
                    view.displayDuck(duck)
                } else {
                    view.displayError(error ?: "Unknown error")
                }
            }
        } else {
            view.showLoading()
            if (validateStatusCode(code)) {
                interactor.getDuckByStatusCode(code) { duck, error ->
                    view.hideLoading()
                    if (duck != null) {
                        view.displayDuck(duck)
                    } else {
                        view.displayError(error ?: "Unknown error")
                    }
                }
            } else {
                view.hideLoading()
                view.displayError("Invalid HTTP status code")
            }
        }
    }

    private fun validateStatusCode(code: String): Boolean {
        return code.toIntOrNull() in 100..599
    }
}
