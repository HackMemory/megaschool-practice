package ru.sample.duckapp

import android.graphics.BitmapFactory
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import okhttp3.ResponseBody
import ru.sample.duckapp.domain.Duck
import ru.sample.duckapp.domain.DucksInteractor
import ru.sample.duckapp.presentation.MainPresenter

class MainActivity : AppCompatActivity(), MainPresenter.View {
    private lateinit var duckImageView: ImageView
    private lateinit var codeEditText: EditText
    private lateinit var sendButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeViews()
        initializePresenter()
        setupListeners()

        presenter.onViewCreated()
    }

    private fun initializeViews() {
        duckImageView = findViewById(R.id.duckImage)
        codeEditText = findViewById(R.id.codeEdit)
        sendButton = findViewById(R.id.sendButton)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun initializePresenter() {
        presenter = MainPresenter(DucksInteractor(), this)
    }

    private fun setupListeners() {
        sendButton.setOnClickListener {
            val code = codeEditText.text.toString()
            presenter.onSendButtonClicked(code)
        }
    }

    override fun <T> displayDuck(duck: T?) {
        when (duck) {
            is Duck -> {
                val duckObject = duck as Duck
                Glide.with(this)
                    .load(duckObject.url)
                    .placeholder(duckImageView.drawable)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>,
                            isFirstResource: Boolean
                        ): Boolean {
                            TODO("Not yet implemented")
                        }

                        override fun onResourceReady(
                            resource: Drawable,
                            model: Any,
                            target: Target<Drawable>?,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                        ): Boolean {
                            hideLoading()
                            return false;
                        }

                    })
                    .into(duckImageView)
            }
            is ResponseBody -> {
                val responseBody = duck as ResponseBody
                val inputStream = responseBody.byteStream()
                val bitmap = BitmapFactory.decodeStream(inputStream)
                Glide.with(this)
                    .load(bitmap)
                    .placeholder(duckImageView.drawable)
                    .into(duckImageView)
            }
            else -> {
                displayError("Failed to load duck image")
            }
        }
    }

    override fun displayError(message: String) {
        Glide.with(duckImageView).apply {  }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressBar.visibility = View.GONE
    }

    override fun blurImage() {
        duckImageView.drawable.setColorFilter(0xff555555.toInt(), PorterDuff.Mode.MULTIPLY)
    }

    override fun unblurImage() {
        duckImageView.drawable.colorFilter = null
    }
}