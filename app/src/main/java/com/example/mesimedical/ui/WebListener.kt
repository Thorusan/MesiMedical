package com.example.mesimedical.ui

interface WebListener {
    fun showToastMessage(message: Message)
    fun showProgressBar()
    fun hideProgressBar()
    fun showSnackbarNoInternet(lastUrl: String)
}