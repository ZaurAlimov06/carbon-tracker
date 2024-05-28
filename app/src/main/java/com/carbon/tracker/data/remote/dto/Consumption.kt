package com.carbon.tracker.data.remote.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Consumption(
    val date: String = "",
    val bus: String = "",
    val metro: String = "",
    val tram: String = "",
    val trolley: String = "",
    val car: String = "",
    val bike: String = "",
    val food: String = ""
) : Parcelable
