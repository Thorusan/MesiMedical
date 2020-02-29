package com.example.mesimedical.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.webkit.WebView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.album.common.Constants.Companion.BASE_URL
import com.example.mesimedical.R
import java.io.UnsupportedEncodingException
import java.net.URLDecoder


class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        webView = findViewById(R.id.webview)
        progressBar = findViewById(R.id.progress_circle);

        initWebView()

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

    fun showToast(message: String) {
        Toast.makeText(
            applicationContext,
            message,
            Toast.LENGTH_LONG
        ).show()
    }

}
