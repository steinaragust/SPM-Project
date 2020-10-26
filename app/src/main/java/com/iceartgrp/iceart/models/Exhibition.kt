package com.iceartgrp.iceart.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class Exhibition(
    @SerializedName("id") val id: Int,
    @SerializedName("info") val info: String,
    @SerializedName("title") val title: String,
    @SerializedName("latitude") val latitude: Number,
    @SerializedName("longitude") val longitude: Number
)

data class Exhibitions(
    @SerializedName("exhibitions") val exhibitions: Array<Exhibition>
) {
    override fun equals(other: Any?): Boolean {
        return true
    }

    override fun hashCode(): Int {
        return 0
    }
}

fun exhibitionsFromJson(json: String): Exhibitions {
    return Gson().fromJson(json, Exhibitions::class.java) ?: throw IllegalArgumentException()
}
