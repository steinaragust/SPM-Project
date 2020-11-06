package com.iceartgrp.iceart.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class RelatedPainting(
    @SerializedName("id") val id: Int,
    @SerializedName("image") val image: String,
)

data class Artist(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("info") val info: String,
    @SerializedName("image") val image: String,
    @SerializedName("paintings") val paintings: Array<RelatedPainting>
) {
    override fun equals(other: Any?): Boolean {
        return true
    }

    override fun hashCode(): Int {
        return 0
    }
}

fun artistFromJson(json: String): Artist {
    return Gson().fromJson(json, Artist::class.java) ?: throw IllegalArgumentException()
}
