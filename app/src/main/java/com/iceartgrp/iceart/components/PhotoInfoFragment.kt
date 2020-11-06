package com.iceartgrp.iceart.components

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.iceartgrp.iceart.R
import com.iceartgrp.iceart.components.MainActivity.Companion.recentImage
import com.iceartgrp.iceart.network.ApiConsumer
import com.iceartgrp.iceart.utils.ImageUtils
import com.iceartgrp.iceart.utils.ImageUtils.Companion.imageFrom64Encoding
import kotlinx.android.synthetic.main.photo_info_fragment.*
import kotlinx.coroutines.delay
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit

class PhotoInfoFragment : Fragment() {

    private var mediaPlayer: MediaPlayer? = null
    companion object {
        fun newInstance() = PhotoInfoFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    private lateinit var viewModel: PhotoInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.photo_info_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val footer = activity?.findViewById<BottomNavigationView>(R.id.navigationView)
        if (footer != null) {
            footer.visibility = View.INVISIBLE
        }
        go_back.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.fragment_container, CameraFragment.newInstance(), "cameraModule")?.commit()
        }
        // get bitmap from image
        if (recentImage != null) {
            // TODO: send photo to api for recognition
            val encoded64Photo = ImageUtils.imageTo64Encoding(recentImage!!)
            ApiConsumer().getPaintingById(
                0,
                onSuccess = { painting ->
                    painting_title.text = painting.title
                    painting_image_view.setImageBitmap(imageFrom64Encoding(painting.image))
                    painting_info_text.text = painting.technique
                    painting_content.visibility = View.VISIBLE
                    loading_spinner?.visibility = View.GONE
                    play_clip()
                },
                onFailure = { statusCode ->
                    var errorMessage = "Something went wrong, please try again later"
                    if (statusCode == -1) {
                        errorMessage = "Connection failed, please check your internet connection"
                    }
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                    Log.e("Request Failure", statusCode.toString())
                }
            )
        }
    }

    override fun onStop() {
        super.onStop()
        val footer = activity?.findViewById<BottomNavigationView>(R.id.navigationView)
        if (footer != null) {
            footer.visibility = View.VISIBLE
        }
    }

    fun play_clip() {
        ApiConsumer().getMP3(
            "Test",
            onSuccess = { mp3 ->
                val tempMp3: File = File.createTempFile("tts", "mp3", activity!!.cacheDir)
                tempMp3.deleteOnExit()
                val fos = FileOutputStream(tempMp3)
                fos.write(mp3)
                fos.close()

                val fis = FileInputStream(tempMp3)

                var uri: Uri = Uri.fromFile(tempMp3)
                mediaPlayer = MediaPlayer.create(activity!!, uri)

                mediaPlayer?.setOnPreparedListener{ println("ready")}

                mediaPlayer?.start()

            },
            onFailure = { statusCode ->
            }
        )
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PhotoInfoViewModel::class.java)
        // TODO: Use the ViewModel
    }
}
