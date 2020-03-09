package com.example.mesimedical.ui

import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import com.example.album.common.Constants.Companion.HOST_URL


internal open class CustomWebViewClient(private val webListener: WebListener) : WebViewClient() {

    private lateinit var lastUrl: String

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        webListener.showProgressBar()

        lastUrl = url!!


        disableZoom(view)
        disableTextSelection(view)
        disallowFileDownload(view)
    }

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        lastUrl = url!! // remember last url in case of an error

        // Send email not supported
        if (url!!.startsWith("mailto:")) {
            webListener.showToastMessage(Message.EMAIL)
            return true
            // Opening external pages is not allowed
        } else if (!url.contains(HOST_URL)) {
            showExternalLinksNotAllowedToast()
            return true;
        } else {
            view?.loadUrl(url)
        }

        return true
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        webListener.hideProgressBar()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)

        webListener.hideProgressBar()

        Log.d("ON_RECEIVED_ERROR", "Error has occured on loading page:" + error!!.description)
        if (error!!.errorCode == ERROR_HOST_LOOKUP) {
            webListener.showSnackbarNoInternet(lastUrl)
        }
    }

    override fun onReceivedError(
        view: WebView?,
        errorCode: Int,
        description: String?,
        failingUrl: String?
    ) {
        super.onReceivedError(view, errorCode, description, failingUrl)

        webListener.hideProgressBar()

        if (errorCode == ERROR_HOST_LOOKUP) {
            webListener.showSnackbarNoInternet(lastUrl)
        }
    }

    /**
     * Disable text selection on long click
     */
    private fun disableTextSelection(view: WebView?) {
        view!!.setOnLongClickListener(View.OnLongClickListener { true })
        view.isLongClickable = false;
        view.isHapticFeedbackEnabled = false;
    }

    /**
     * Disable zoom-in controls (pinch to zoom)
     */
    private fun disableZoom(view: WebView?) {
        view!!.settings.setSupportZoom(false);
        view.settings.builtInZoomControls = false;
        view.settings.displayZoomControls = false;
    }

    /**
     * Dont allow File download, except it is .PDF
     */
    private fun disallowFileDownload(view: WebView?) {
        view!!.setDownloadListener { url,
                                     userAgent,
                                     contentDisposition,
                                     mimetype,
                                     contentLength ->

            if (mimetype.equals("application/pdf")) {
                // https://www.mesimedical.com/app/uploads/2019/11/ABPIMD_IFU_ENG_v7-2_2019-07-05_web.pdf
                val pdf = url
                //view.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=$pdf")
                webListener.showToastMessage(Message.PDF)
                view.loadUrl("https://docs.google.com/gview?embedded=true&url=$pdf") // open in google docs
            } else {
                webListener.showToastMessage(Message.ACTION)
            }

        }
    }

    private fun showExternalLinksNotAllowedToast() {
        webListener.showToastMessage(Message.EXTERNAL_LINKS)
    }

}
