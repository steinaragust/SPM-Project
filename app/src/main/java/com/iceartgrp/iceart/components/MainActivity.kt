package com.iceartgrp.iceart.components

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.florent37.camerafragment.CameraFragment
import com.github.florent37.camerafragment.PreviewActivity
import com.github.florent37.camerafragment.listeners.CameraFragmentResultListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.iceartgrp.iceart.R
import com.iceartgrp.iceart.network.ApiConsumer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val mOnNavigationSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
        item -> when(item.itemId) {
            R.id.navigation_camera -> {
                val fragmentManager = supportFragmentManager
                if (fragmentManager.fragments.size == 0) {
                    fragmentManager.beginTransaction().add(R.id.fragment_container, com.iceartgrp.iceart.components.CameraFragment.newInstance(), "cameraModule").commit()
                } else {
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, com.iceartgrp.iceart.components.CameraFragment.newInstance(), "cameraModule").commit()
                }
                true
            }
            R.id.navigation_discover -> {
                val fragmentManager = supportFragmentManager
                if (fragmentManager.fragments.size == 0) {
                    fragmentManager.beginTransaction().add(R.id.fragment_container, DiscoverFragment.newInstance(), "discoverModule").commit()
                } else {
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, DiscoverFragment.newInstance(), "discoverModule").commit()
                }
                  true
            }
            else -> false
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationSelectedListener)
        hasPermissionAndOpenCamera()
        // navigationView.setSelectedItemId(R.id.navigation_camera)
//        mainbtn.setOnClickListener {
//            ApiConsumer().getPaintingById(
//                0,
//                onSuccess = { painting ->
//                    maintxt.text = painting.title
//                },
//                onFailure = { statusCode ->
//                    Log.e("Request Failure", statusCode.toString())
//                    val errorMsg = "Error: $statusCode"
//                    maintxt.text = errorMsg
//                }
//            )
//        }
    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        Toast.makeText(this, "Camera success!", Toast.LENGTH_SHORT).show()
//    }

    /**
     * Check if you have Camera Permission
     *      if you don't have it then permission is requested
     */
    private fun hasPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermission()
        }
    }

    /**
     * Requesting permission to open the Camera and show Dialog on screen
     */
    private fun requestPermission() {
        val permissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        ActivityCompat.requestPermissions(this, permissions, PackageManager.PERMISSION_GRANTED)
    }
}
