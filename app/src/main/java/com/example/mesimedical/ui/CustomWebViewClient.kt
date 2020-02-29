package com.example.mesimedical.ui

import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat.startActivity

import com.example.album.common.Constants.Companion.HOST_URL
import com.example.mesimedical.R

internal open class CustomWebViewClient(activity: MainActivity) : WebViewClient() {
    private val activity = activity

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        activity.showProgressBar()

        disableZoom(view)
        disableTextSelection(view)
        disallowFileDownload(view)
    }

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        // Opening external pages is not allowed
        if (url!!.startsWith("mailto:")) {
            activity.showToast(activity.getString(R.string.email_not_supported))
            view!!.reload()
            return true
        } else if (!Uri.parse(url).getHost()!!.contains(HOST_URL)) {
            activity.showToast(activity.getString(R.string.external_links_not_allowed))
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
        view!!.setDownloadListener { url,
                                     userAgent,
                                     contentDisposition,
                                     mimetype,
                                     contentLength ->
            activity.showToast(activity.getString(R.string.action_not_supported))
        }
    }


}
