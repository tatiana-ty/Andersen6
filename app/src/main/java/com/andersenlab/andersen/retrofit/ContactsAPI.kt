package com.andersenlab.andersen.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ContactsAPI {
    @GET("api/")
    fun getContacts(
            @Query("results") results: Int,
            @Query("inc") inc: String
    ): Call<ContactsServerResponseData>
}
