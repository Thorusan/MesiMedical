package com.example.mesimedical.ui


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.WebView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.album.common.Constants.Companion.BASE_URL
import com.example.mesimedical.R
import com.example.mesimedical.utils.Utility
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity(), WebListener {

    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        webView = findViewById(R.id.webview)
        progressBar = findViewById(R.id.progress_circle);

        if (!Utility.isNetworkAvailable(this)) {
            showSnackbarNoInternet(BASE_URL)
        } else {
            initWebView(BASE_URL)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            when (keyCode) {
                KeyEvent.KEYCODE_BACK -> {
                    if (webView.canGoBack()) {
                        webView.goBack()
                    } else {
                        finish()
                    }
                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    @SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
    private fun initWebView(url: String) {
        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(JsObject(this), "injectedObject")
        webView.webViewClient = CustomWebViewClient(this)

        webView.loadUrl(BASE_URL)

        //webView!!.loadUrl("javascript:alert('test test test')");
    }

    override fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    override fun showToastMessage(message: Message) {
        var textId: Int
        when (message) {
            Message.EXTERNAL_LINKS -> textId = R.string.external_links_not_allowed
            Message.EMAIL -> textId = R.string.email_not_supported
            Message.ACTION -> textId = R.string.action_not_supported
            Message.PDF -> textId = R.string.pdf_viewer
        }

        Toast.makeText(
            applicationContext,
            textId,
            Toast.LENGTH_LONG
        ).show()
    }

    override fun showSnackbarNoInternet(lastUrl: String) {
        val snack = Snackbar.make(
            webView, getString(R.string.error_no_internet_connection),
            Snackbar.LENGTH_INDEFINITE
        )
        snack.setAction(getString(R.string.try_again), View.OnClickListener {
            // executed when TRY AGAIN is clicked
            if (lastUrl.equals(BASE_URL)) {
                initWebView(lastUrl)
            } else {
                webView.loadUrl(lastUrl)
            }
        })
        snack.show()
    }

}
