package com.example.mesimedical.ui

import android.app.ProgressDialog
import android.graphics.Bitmap
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.mesimedical.R


internal open class CustomWebViewClient(
    private val activity: MainActivity
) : WebViewClient() {

    private lateinit var lastUrl: String

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        activity.showProgressBar()

        lastUrl = url!!


        disableZoom(view)
        disableTextSelection(view)
        disallowFileDownload(view)
    }

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        lastUrl = url!! // remember last url in case of an error

        // Send email not supported
        if (url!!.startsWith("mailto:")) {
            activity.showToastMessage(activity.getString(R.string.email_not_supported))
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
        activity.hideProgressBar()
    }


    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
        activity.hideProgressBar()
        activity.showSnackbarNoInternet(lastUrl)
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
                activity.showToastMessage(activity.getString(R.string.pdf_viewer))
                view.loadUrl("https://docs.google.com/gview?embedded=true&url=$pdf") // open in google docs
            } else {
                activity.showToastMessage(activity.getString(R.string.action_not_supported))
            }

        }
    }

    private fun showExternalLinksNotAllowedToast() {
        activity.showToastMessage(activity.getString(R.string.external_links_not_allowed))
    }

}
