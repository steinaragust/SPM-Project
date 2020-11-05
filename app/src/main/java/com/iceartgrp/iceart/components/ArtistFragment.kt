package com.iceartgrp.iceart.components

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
            fragmentManager?.beginTransaction()?.replace(R.id.fragment_container, PhotoInfoFragment.newInstance(), "photoInfoFragment")?.commit()
        }
        val footer = activity?.findViewById<BottomNavigationView>(R.id.navigationView)
        if (footer != null) {
            footer.visibility = View.INVISIBLE
        }
        ApiConsumer().getArtistById(
            0,
            onSuccess = { artist ->
                artist_title.text = artist.title
                val image = ImageUtils.imageFrom64Encoding(artist.image)
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
