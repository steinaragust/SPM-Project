package com.iceartgrp.iceart.components

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.iceartgrp.iceart.R
import com.iceartgrp.iceart.network.ApiConsumer
import com.iceartgrp.iceart.utils.ImageUtils
import kotlinx.android.synthetic.main.fragment_artist.*
import kotlinx.android.synthetic.main.fragment_artist.go_back
import kotlinx.android.synthetic.main.fragment_artist.loading_spinner

private const val ARG_ARTIST_ID = "artist_id_arg"

/**
 * A simple [Fragment] subclass.
 * Use the [ArtistFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ArtistFragment : Fragment() {
    private var artist_id_arg: Int? = null
    private var biography_selected: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            artist_id_arg = it.getInt(ARG_ARTIST_ID)
        }
    }

    fun dpToPx(dp: Int): Float {
        val density = context!!.resources.displayMetrics.density
        return dp * density
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_artist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        go_back.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.fragment_container, CameraFragment.newInstance(), "cameraModule")?.commit()
        }
        biography_button.setOnClickListener {
            if (biography_selected == false) {
                biography_button.background.setTint(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                biography_button.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                known_work_button.background.setTint(ContextCompat.getColor(requireContext(), R.color.white))
                known_work_button.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                biography_content.visibility = View.VISIBLE
                known_work_content.visibility = View.GONE
                biography_selected = true
            }
        }
        known_work_button.setOnClickListener {
            if (biography_selected == true) {
                known_work_button.background.setTint(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                known_work_button.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                biography_button.background.setTint(ContextCompat.getColor(requireContext(), R.color.white))
                biography_button.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                known_work_content.visibility = View.VISIBLE
                biography_content.visibility = View.GONE
                biography_selected = false
            }
        }
        val footer = activity?.findViewById<BottomNavigationView>(R.id.navigationView)
        if (footer != null) {
            footer.visibility = View.INVISIBLE
        }
        ApiConsumer().getArtistById(
            0,
            onSuccess = { artist ->
                artist_title.text = artist.title
                var image = ImageUtils.imageFrom64Encoding(artist.image)
                val maxWH = dpToPx(200)

                if (image.height - image.width > 0) {
                    val ratio = ((image.height - image.width).toFloat() / image.height.toFloat())
                    artist_image_view.layoutParams.width = (maxWH - (maxWH * ratio)).toInt()
                } else if (image.height - image.width < 0) {
                    val ratio = ((image.width - image.height).toFloat() / image.width.toFloat())
                    artist_image_container.layoutParams.height = (maxWH - (maxWH * ratio)).toInt()
                }
                artist_image_view.setImageBitmap(image)
                artist_info.text = artist.info

                val IMAX_W = 100
                val IMAX_H = 155

                if (artist.paintings.isNotEmpty()) {
                    image = ImageUtils.imageFrom64Encoding(artist.paintings[0].image)
                    known_work_image1.setImageBitmap(image)
                    known_work_text1.text = artist.paintings[0].name
                    known_work_container1.setOnClickListener {
                        val fragmentManager = fragmentManager
                        fragmentManager?.beginTransaction()?.replace(R.id.fragment_container, PhotoInfoFragment.newInstance(artist.paintings[0].id), "photoInfoFragment")?.commit()
                    }
                } else {
                    known_work_container1.visibility = View.INVISIBLE
                }

                if (image.height - image.width > 0) {
                    val ratio = ((image.height - image.width).toFloat() / image.height.toFloat())
                    known_work_image1.layoutParams.width = (IMAX_W - (IMAX_W * ratio)).toInt()
                } else if (image.height - image.width < 0) {
                    val ratio = ((image.width - image.height).toFloat() / image.width.toFloat())
                    known_work_container1.layoutParams.height = (IMAX_H - (IMAX_H * ratio)).toInt()
                }

                if (artist.paintings.size > 1) {
                    image = ImageUtils.imageFrom64Encoding(artist.paintings[1].image)
                    known_work_image2.setImageBitmap(image)
                    known_work_text2.text = artist.paintings[1].name
                    known_work_container2.setOnClickListener {
                        val fragmentManager = fragmentManager
                        fragmentManager?.beginTransaction()?.replace(R.id.fragment_container, PhotoInfoFragment.newInstance(artist.paintings[1].id), "photoInfoFragment")?.commit()
                    }
                } else {
                    known_work_container2.visibility = View.INVISIBLE
                }

                if (artist.paintings.size > 2) {
                    image = ImageUtils.imageFrom64Encoding(artist.paintings[2].image)
                    known_work_image3.setImageBitmap(image)
                    known_work_text3.text = artist.paintings[2].name
                    known_work_container3.setOnClickListener {
                        val fragmentManager = fragmentManager
                        fragmentManager?.beginTransaction()?.replace(R.id.fragment_container, PhotoInfoFragment.newInstance(artist.paintings[2].id), "photoInfoFragment")?.commit()
                    }
                } else {
                    known_work_container3.visibility = View.INVISIBLE
                }

                artist_content.visibility = View.VISIBLE
                loading_spinner?.visibility = View.GONE
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

    override fun onStop() {
        super.onStop()
        val footer = activity?.findViewById<BottomNavigationView>(R.id.navigationView)
        if (footer != null) {
            footer.visibility = View.VISIBLE
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param artist_id_arg Id for artist argument.
         * @return A new instance of fragment ArtistFragment.
         */
        @JvmStatic
        fun newInstance(artist_id_arg: Int) =
            ArtistFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_ARTIST_ID, artist_id_arg)
                }
            }
    }
}
