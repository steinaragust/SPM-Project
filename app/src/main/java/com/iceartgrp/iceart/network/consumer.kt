package com.iceartgrp.iceart.network

import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.iceartgrp.iceart.models.Painting
import com.iceartgrp.iceart.models.paintingFromJson


class ApiConsumer (private val host: String) {
    fun getPaintingById(
        id: Int,
        onSuccess: (Request, Response, Painting) -> Unit,
        onFailure: (Request, Response, FuelError) -> Unit
    ) {
        val url = "$host/painting/$id"
        url.httpGet().responseString { request, response, result ->
            when (result) {
                is Result.Failure -> {
                    onFailure(request, response, result.getException())
                }
                is Result.Success -> {
                    onSuccess(request, response, paintingFromJson(result.get()))
                }
            }
        }
    }
}
