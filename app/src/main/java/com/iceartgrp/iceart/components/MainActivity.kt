package com.iceartgrp.iceart.components

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.iceartgrp.iceart.R
import com.iceartgrp.iceart.network.ApiConsumer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val mOnNavigationSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
        item -> when(item.itemId) {
            R.id.navigation_camera -> {
                true
            }
            R.id.navigation_search -> {
                val fragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                true
            }
            R.id.navigation_discover -> {
                true
            }
            R.id.navigation_liked -> {
                true
            }
            else -> false
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // implementation 'com.google.android.material:material:1.2.1'
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationSelectedListener)
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
