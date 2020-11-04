package com.iceartgrp.iceart.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class Artist(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("info") val info: String,
    @SerializedName("image") val image: String
)

fun artistFromJson(json: String): Artist {
    return Gson().fromJson(json, Artist::class.java) ?: throw IllegalArgumentException()
}
