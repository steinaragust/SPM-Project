package com.iceartgrp.iceart.components

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageProxy
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.iceartgrp.iceart.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity() : AppCompatActivity() {
    companion object {
        var recentImage: ImageProxy? = null
    }

    private val mOnNavigationSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
        item ->
        when (item.itemId) {
            R.id.navigation_camera -> {
                val fragmentManager = supportFragmentManager
                if (fragmentManager.fragments.size == 0) {
                    fragmentManager.beginTransaction().add(R.id.fragment_container, CameraFragment.newInstance(), "cameraModule").commit()
                } else {
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, CameraFragment.newInstance(), "cameraModule").commit()
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
        navigationView.selectedItemId = R.id.navigation_camera
        hasPermissionAndOpenCamera()
    }

    /**
     * Check if you have Camera Permission
     *      if you don't have it then permission is requested
     */
    private fun hasPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
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
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        ActivityCompat.requestPermissions(this, permissions, PackageManager.PERMISSION_GRANTED)
    }
}
