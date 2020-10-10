package com.iceartgrp.iceart.components

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.iceartgrp.iceart.R
import com.iceartgrp.iceart.network.ApiConsumer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainbtn.setOnClickListener {
            ApiConsumer().getPaintingById(
                0,
                onSuccess = { painting ->
                    maintxt.text = painting.title
                },
                onFailure = { statusCode ->
                    Log.e("Request Failure", statusCode.toString())
                    val errorMsg = "Error: $statusCode"
                    maintxt.text = errorMsg
                }
            )
        }
    }
}
