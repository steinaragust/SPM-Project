package com.iceartgrp.iceart.network

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import com.iceartgrp.iceart.models.Exhibition
import com.iceartgrp.iceart.models.Painting
import com.iceartgrp.iceart.models.exhibitionsFromJson
import com.iceartgrp.iceart.models.paintingFromJson

class ApiConsumer {
    companion object {
        var host = "http://192.168.3.6:5000"
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

    fun getNearbyExhibitions(
        latitude: Number,
        longitude: Number,
        onSuccess: (Array<Exhibition>) -> Unit,
        onFailure: (Int) -> Unit
    ) {
        val url = "$host/exhibition"
        val jsonBody = "{\"latitude\": $latitude, \"longitude\": $longitude}"
        url.httpPost().body(jsonBody).responseString { _, response, result ->
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
