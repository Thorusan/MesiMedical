package com.example.mesimedical.ui

import android.graphics.Bitmap
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient


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
        } else if (url.contains("facebook")) {
            showExternalLinksNotAllowedToast()
            return true;
        } else if (url.contains("twitter")) {
            showExternalLinksNotAllowedToast()
            return true;
        } else if (url.contains("linkedin")) {
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


    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
        webListener.hideProgressBar()
        webListener.showSnackbarNoInternet(lastUrl)
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
