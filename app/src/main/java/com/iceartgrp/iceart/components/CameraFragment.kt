package com.iceartgrp.iceart.components

import android.annotation.SuppressLint
import android.media.MediaActionSound
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.iceartgrp.iceart.R
import com.iceartgrp.iceart.components.MainActivity.Companion.recentImage
import kotlinx.android.synthetic.main.fragment_camera.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * A simple [Fragment] subclass.
 * Use the [CameraFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CameraFragment : Fragment() {
    private lateinit var cameraExecutor: ExecutorService
    private var imageCapture: ImageCapture? = null

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        startCamera()
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_take_photo.setOnClickListener { takePhoto() }
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

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(
            {
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(viewFinder.surfaceProvider)
                }
                imageCapture = ImageCapture.Builder().build()
                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(this, CameraSelector.DEFAULT_BACK_CAMERA, preview, imageCapture)
                } catch (exc: Exception) {
                    // TODO: Error handle, close app
                }
            },
            ContextCompat.getMainExecutor(requireContext())
        )
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        MediaActionSound().play(MediaActionSound.SHUTTER_CLICK)
        imageCapture.takePicture(
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    recentImage = image
                    val fragmentManager = fragmentManager
                    fragmentManager?.beginTransaction()?.replace(R.id.fragment_container, PhotoInfoFragment.newInstance(), "Nothing")?.commit()
                }

                override fun onError(exception: ImageCaptureException) {
                    // TODO: handle error
                }
            }
        )
    }
}
