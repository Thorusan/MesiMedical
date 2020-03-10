package com.example.mesimedical.ui

import android.util.Log

open class JavascriptInterface(private val webListener: WebListener) {
    fun doStuff() {
        webListener.showToastMessage(Message.EXTERNAL_LINKS)
        Log.e("TAG","Test link")
    }
}