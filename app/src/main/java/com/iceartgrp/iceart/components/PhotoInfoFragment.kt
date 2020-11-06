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
import com.iceartgrp.iceart.utils.ImageUtils
import com.iceartgrp.iceart.utils.ImageUtils.Companion.imageFrom64Encoding
import kotlinx.android.synthetic.main.photo_info_fragment.*
import kotlinx.android.synthetic.main.photo_info_fragment.go_back
import kotlinx.android.synthetic.main.photo_info_fragment.loading_spinner

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

    private fun dpToPx(dp: Int): Float {
        val density = requireContext().resources.displayMetrics.density
        return dp * density
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            photo_id_arg = it.getInt(ARG_PHOTO_ID)
        }
    }

    private lateinit var viewModel: PhotoInfoViewModel

    private var maxWH: Float? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        maxWH = dpToPx(300)
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
        if (recentImage != null) {
            val encoded64Photo = ImageUtils.imageTo64Encoding(recentImage!!)
            ApiConsumer().getMostSimilarPainting(
                encoded64Photo,
                onSuccess = { painting ->
                    painting_title.text = painting.title
                    painting_year.text = painting.year.toString()

                    val image = imageFrom64Encoding(painting.image)

                    if (image.height - image.width > 0) {
                        val ratio = ((image.height - image.width).toFloat() / image.height.toFloat())
                        painting_image_view.layoutParams.width = (maxWH!! - (maxWH!! * ratio)).toInt()
                    } else if (image.height - image.width < 0) {
                        val ratio = ((image.width - image.height).toFloat() / image.width.toFloat())
                        painting_image_container.layoutParams.height = (maxWH!! - (maxWH!! * ratio)).toInt()
                    }

                    painting_image_view.setImageBitmap(image)
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
        } else {
            // TODO: send photo to api for recognition
            ApiConsumer().getPaintingById(
                photo_id_arg!!,
                onSuccess = { painting ->
                    painting_title.text = painting.title
                    painting_year.text = painting.year.toString()
                    val image = imageFrom64Encoding(painting.image)

                    if (image.height - image.width > 0) {
                        val ratio = ((image.height - image.width).toFloat() / image.height.toFloat())
                        painting_image_view.layoutParams.width = (maxWH!! - (maxWH!! * ratio)).toInt()
                    } else if (image.height - image.width < 0) {
                        val ratio = ((image.width - image.height).toFloat() / image.width.toFloat())
                        painting_image_container.layoutParams.height = (maxWH!! - (maxWH!! * ratio)).toInt()
                    }

                    painting_image_view.setImageBitmap(image)
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
        recentImage = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PhotoInfoViewModel::class.java)
        // TODO: Use the ViewModel
    }
}
