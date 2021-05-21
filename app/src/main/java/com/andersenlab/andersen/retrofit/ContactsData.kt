package com.andersenlab.andersen.retrofit

sealed class ContactsData {
    data class Success(val serverResponseData: ContactsServerResponseData) :
        ContactsData()

    data class Error(val error: Throwable) : ContactsData()
    data class Loading(val progress: Int?) : ContactsData()
}
