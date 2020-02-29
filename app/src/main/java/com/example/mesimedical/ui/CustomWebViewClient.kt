package com.example.mesimedical.ui

import android.graphics.Bitmap
import android.view.View
import android.webkit.DownloadListener
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.example.mesimedical.R

internal open class CustomWebViewClient(activity: MainActivity): WebViewClient() {
    private val activity = activity

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        activity.showProgressBar()

        disableZoom(view)
        disableTextSelection(view)
        disallowFileDownload(view)
    }

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        if (url!!.contains("facebook")) {
            activity.showToastActinNotSupported()
            return true;
        } else {
            view?.loadUrl(url)
        }
        return true
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        activity.hideProgressBar()
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
        activity.hideProgressBar()
    }

    private fun disableTextSelection(view: WebView?) {
        // disable text selection on long click
        view!!.setOnLongClickListener(View.OnLongClickListener { true })
        view.setLongClickable(false);
        view.setHapticFeedbackEnabled(false);
    }

    private fun disableZoom(view: WebView?) {
        // Disable zoom-in controls (pinch to zoom)
        view!!.getSettings().setSupportZoom(false);
        view.getSettings().setBuiltInZoomControls(false);
        view.getSettings().setDisplayZoomControls(false);
    }

    private fun disallowFileDownload(view: WebView?) {
        view!!.setDownloadListener(DownloadListener { url,
                                                      userAgent,
                                                      contentDisposition,
                                                      mimetype,
                                                      contentLength ->
            activity.showToastActinNotSupported()
        })
    }
}