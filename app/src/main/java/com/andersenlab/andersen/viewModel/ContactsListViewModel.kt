package com.andersenlab.andersen.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andersenlab.andersen.retrofit.ContactsData
import com.andersenlab.andersen.retrofit.ContactsRetrofitImpl
import com.andersenlab.andersen.retrofit.ContactsServerResponseData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactsListViewModel(
    private val liveDataForViewToObserve: MutableLiveData<ContactsData> = MutableLiveData(),
    private val retrofitImpl: ContactsRetrofitImpl = ContactsRetrofitImpl()
) :
    ViewModel() {

    fun getData(): LiveData<ContactsData> {
        sendServerRequest()
        return liveDataForViewToObserve
    }

    private fun sendServerRequest() {

        liveDataForViewToObserve.value = ContactsData.Loading(null)
        retrofitImpl.getRetrofitImpl().getContacts(100, "name,phone,picture").enqueue(object :
            Callback<ContactsServerResponseData> {
            override fun onResponse(
                call: Call<ContactsServerResponseData>,
                response: Response<ContactsServerResponseData>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    liveDataForViewToObserve.value =
                        ContactsData.Success(
                            response.body()!!
                        )
                } else {
                    val message = response.message()
                    if (message.isNullOrEmpty()) {
                        liveDataForViewToObserve.value =
                            ContactsData.Error(
                                Throwable("Unidentified error")
                            )
                    } else {
                        liveDataForViewToObserve.value =
                            ContactsData.Error(
                                Throwable(message)
                            )
                    }
                }
            }

            override fun onFailure(call: Call<ContactsServerResponseData>, t: Throwable) {
                liveDataForViewToObserve.value =
                    ContactsData.Error(
                        t
                    )
            }
        }
        )
    }
}