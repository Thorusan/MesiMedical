package com.example.mesimedical.ui

import android.util.Log
import android.webkit.JavascriptInterface

class JsObject(private val webListener: WebListener) {
    /*fun doStuff() {
        webListener.showToastMessage(Message.EXTERNAL_LINKS)
        Log.e("TAG","Test link")
    }*/


    @JavascriptInterface
    fun doStuff() {
        webListener.showToastMessage(Message.EXTERNAL_LINKS)
        Log.e("TAG", "Test link")
    }

}