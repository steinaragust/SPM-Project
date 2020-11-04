package com.iceartgrp.iceart.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class Painting(
    @SerializedName("id") val id: Int,
    @SerializedName("technique") val technique: String,
    @SerializedName("title") val title: String,
    @SerializedName("image") val image: String,
    @SerializedName("year") val year: Int,
    @SerializedName("artist") val artistId: Int
)

fun paintingFromJson(json: String): Painting {
    return Gson().fromJson(json, Painting::class.java) ?: throw IllegalArgumentException()
}
