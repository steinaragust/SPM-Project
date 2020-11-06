package com.iceartgrp.iceart.network

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import com.iceartgrp.iceart.models.*

class ApiConsumer {
    companion object {
        var host = "http://172.20.10.6:5000"
    }

    fun getPaintingById(
        id: Int,
        onSuccess: (Painting) -> Unit,
        onFailure: (Int) -> Unit
    ) {
        val url = "$host/painting/$id"
        url.httpGet().responseString { _, response, result ->
            when (result) {
                is Result.Failure -> {
                    onFailure(response.statusCode)
                }
                is Result.Success -> {
                    onSuccess(paintingFromJson(result.get()))
                }
            }
        }
    }

    fun getArtistById(
        id: Int,
        onSuccess: (Artist) -> Unit,
        onFailure: (Int) -> Unit
    ) {
        val url = "$host/artist/$id"
        url.httpGet().responseString { _, response, result ->
            when (result) {
                is Result.Failure -> {
                    onFailure(response.statusCode)
                }
                is Result.Success -> {
                    onSuccess(artistFromJson(result.get()))
                }
            }
        }
    }

    fun getNearbyExhibitions(
        latitude: Number,
        longitude: Number,
        onSuccess: (Array<Exhibition>) -> Unit,
        onFailure: (Int) -> Unit
    ) {
        val url = "$host/exhibition"
        val jsonBody =
            """{"latitude": $latitude, "longitude": $longitude}"""
        url.httpPost().body(jsonBody).header("Content-Type" to "application/json").responseString { _, response, result ->
            when (result) {
                is Result.Failure -> {
                    onFailure(response.statusCode)
                }
                is Result.Success -> {
                    onSuccess(exhibitionsFromJson(result.get()).exhibitions)
                }
            }
        }
    }
}
