package com.iceartgrp.iceart.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class Painting(
    @SerializedName("id") val id: Int,
    @SerializedName("info") val info: String,
    @SerializedName("title") val title: String,
    @SerializedName("image") val image: String
)

fun paintingFromJson(json: String): Painting {
    return Gson().fromJson(json, Painting::class.java) ?: throw IllegalArgumentException()
}
