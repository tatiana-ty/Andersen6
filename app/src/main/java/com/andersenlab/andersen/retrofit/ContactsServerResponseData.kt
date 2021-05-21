package com.andersenlab.andersen.retrofit

import com.google.gson.annotations.SerializedName

data class ContactsServerResponseData(
    @field:SerializedName("results") val results: List<Results>?
)

data class Results(
    @field:SerializedName("name") val name: Name,
    @field:SerializedName("phone") val phone: String,
    @field:SerializedName("picture") val picture: Picture
)

data class Name(
    @field:SerializedName("first") val first: String,
    @field:SerializedName("last") val last: String
)

data class Picture(
    @field:SerializedName("large") val large: String
)