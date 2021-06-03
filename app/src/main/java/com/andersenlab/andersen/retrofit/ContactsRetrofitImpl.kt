package com.andersenlab.andersen.retrofit

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class ContactsRetrofitImpl {

    private val baseUrl = "https://randomuser.me/"

    fun getRetrofitImpl(): ContactsAPI {
        val contactsRetrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
                .client(createOkHttpClient(ContactsInterceptor()))
                .build()
        return contactsRetrofit.create(ContactsAPI::class.java)
    }

    private fun createOkHttpClient(interceptor: Interceptor): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(interceptor)
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpClient.addInterceptor(httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        })
        return httpClient.build()
    }

    inner class ContactsInterceptor : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            return chain.proceed(chain.request())
        }
    }
}
