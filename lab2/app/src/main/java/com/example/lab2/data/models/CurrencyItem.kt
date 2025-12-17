package com.example.lab2.data.models

import com.google.gson.annotations.SerializedName

data class CurrencyItem(
    @SerializedName("CharCode")
    val charCode: String,

    @SerializedName("Nominal")
    val nominal: Int,

    @SerializedName("Value")
    val value: Double
)