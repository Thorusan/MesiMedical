package com.example.mesimedical.ui


import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.album.common.Constants.Companion.BASE_URL
import com.example.mesimedical.R
import com.example.mesimedical.utils.Utility
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        webView = findViewById(R.id.webview)
        progressBar = findViewById(R.id.progress_circle);

        if (!Utility.isNetworkAvailable(this)) {
            showSnackbarNoInternet()
        } else {
            initWebView()
        }
    }


    private fun initWebView() {
        webView.webViewClient = CustomWebViewClient(this)
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(BASE_URL)
    }

    fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    fun showToastMessage(message: String) {
        Toast.makeText(
            applicationContext,
            message,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showSnackbarNoInternet() {
        val snack = Snackbar.make(
            webView, getString(R.string.error_no_internet_connection),
            Snackbar.LENGTH_INDEFINITE
        )
        snack.setAction(getString(R.string.try_again), View.OnClickListener {
            // executed when TRY AGAIN is clicked
            initWebView()
        })
        snack.show()
    }

}
