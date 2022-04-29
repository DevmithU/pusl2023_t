package com.example.pusl2023_t.models

import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

@Parcelize

data class Book (
    val user_id: String = "",
    val user_name: String = "",
    val user_address: String = "",
    val user_phone: String = "",
    val title: String = "",
    val description: String = "",
    val image: String = "",
    var book_id: String = "",
    var status: String = "",
    var borrower_id: String = "",
    var borrower_address: String = "",
    var borrower_phone: String = "",

    ) : Parcelable