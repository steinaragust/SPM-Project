package com.iceartgrp.iceart.components

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
import com.iceartgrp.iceart.utils.ImageUtils.Companion.imageFrom64Encoding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.photo_info_fragment.*

private const val ARG_PHOTO_ID = "photo_id_arg"

class PhotoInfoFragment : Fragment() {
    private var photo_id_arg: Int? = null
    companion object {
        fun newInstance(photo_id_arg: Int?) = PhotoInfoFragment().apply {
            arguments = Bundle().apply {
                if (photo_id_arg != null) {
                    putInt(ARG_PHOTO_ID, photo_id_arg)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            photo_id_arg = it.getInt(ARG_PHOTO_ID)
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
//        if (recentImage != null) {
//            ApiConsumer().getPaintingById(
//                0,
//                onSuccess = { painting ->
//                    painting_title.text = painting.title
//                    painting_year.text = painting.year.toString()
//                    painting_image_view.setImageBitmap(imageFrom64Encoding(painting.image))
//                    painting_info_text.text = painting.technique
//                    painting_content.visibility = View.VISIBLE
//                    loading_spinner?.visibility = View.GONE
//                    artist_info_button.setOnClickListener {
//                        fragmentManager?.beginTransaction()?.replace(R.id.fragment_container, ArtistFragment.newInstance(painting.artistId), "artistView")?.commit()
//                    }
//                },
//                onFailure = { statusCode ->
//                    var errorMessage = "Something went wrong, please try again later"
//                    if (statusCode == -1) {
//                        errorMessage = "Connection failed, please check your internet connection"
//                    }
//                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
//                    Log.e("Request Failure", statusCode.toString())
//                }
//            )
//        }
        // get bitmap from image
        if (photo_id_arg != null) {
            // TODO: send photo to api for recognition
            ApiConsumer().getPaintingById(
                photo_id_arg!!,
                onSuccess = { painting ->
                    painting_title.text = painting.title
                    painting_year.text = painting.year.toString()
                    painting_image_view.setImageBitmap(imageFrom64Encoding(painting.image))
                    painting_info_text.text = painting.technique
                    painting_content.visibility = View.VISIBLE
                    loading_spinner?.visibility = View.GONE
                    artist_info_button.setOnClickListener {
                        fragmentManager?.beginTransaction()?.replace(R.id.fragment_container, ArtistFragment.newInstance(painting.artistId), "artistView")?.commit()
                    }
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PhotoInfoViewModel::class.java)
        // TODO: Use the ViewModel
    }
}
