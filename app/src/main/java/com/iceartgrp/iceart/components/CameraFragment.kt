package com.iceartgrp.iceart.components

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.github.florent37.camerafragment.PreviewActivity
import com.github.florent37.camerafragment.configuration.Configuration
import com.github.florent37.camerafragment.listeners.CameraFragmentResultListener
import com.iceartgrp.iceart.R
import kotlinx.android.synthetic.main.fragment_camera.*

/**
 * A simple [Fragment] subclass.
 * Use the [CameraFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CameraFragment : Fragment() {
    private lateinit var cameraFragment : com.github.florent37.camerafragment.CameraFragment
    // TODO: Rename and change types of parameters

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        cameraFragment = com.github.florent37.camerafragment.CameraFragment.newInstance(
            Configuration.Builder().build())
        var fragmentManager = fragmentManager;
        if (fragmentManager != null) {
            fragmentManager.beginTransaction().replace(R.id.content, cameraFragment, "Nothing")
                .commit()
        }
    }

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_take_photo.setOnClickListener { takeNewPhoto() }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment CameraFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            CameraFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
    private fun takeNewPhoto() {
        println("photo taken")
    }
}
