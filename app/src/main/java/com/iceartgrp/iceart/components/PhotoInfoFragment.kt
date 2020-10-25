package com.iceartgrp.iceart.components

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.iceartgrp.iceart.R
import com.iceartgrp.iceart.components.MainActivity.Companion.recentImage
import com.iceartgrp.iceart.network.ApiConsumer
import com.iceartgrp.iceart.utils.ImageUtils
import com.iceartgrp.iceart.utils.ImageUtils.Companion.imageFrom64Encoding
import kotlinx.android.synthetic.main.photo_info_fragment.*

private const val ARG_PHOTO_STRING = "photoString"

class PhotoInfoFragment : Fragment() {
    private var encoded64Photo: String? = null
    private var mainLayout: FlexboxLayout? = null
    private var loader: ProgressBar? = null

    companion object {
        fun newInstance(photoString: String) = PhotoInfoFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PHOTO_STRING, photoString)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            encoded64Photo = it.getString(ARG_PHOTO_STRING)
        }
    }
        //apiConsumer.uploadImage(img)
    private lateinit var viewModel: PhotoInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.photo_info_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var footer = activity?.findViewById<BottomNavigationView>(R.id.navigationView)
        if (footer != null) {
            footer.visibility = View.INVISIBLE;
        }
        mainLayout = activity?.findViewById<FlexboxLayout>(R.id.painting_content)
        loader = activity?.findViewById<ProgressBar>(R.id.loading_spinner)
        activity?.findViewById<ImageButton>(R.id.go_back)?.setOnClickListener {
            var fragmentManager = fragmentManager;
            if (fragmentManager != null) {
                fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, CameraFragment.newInstance(), "Nothing")
                    .commit()
            }
        }
        // get bitmap from image
        if (recentImage != null) {
            val img = ImageUtils.imageTo64Encoding(recentImage!!)
            ApiConsumer().getPaintingById(
                0,
                onSuccess = { painting ->
                    println(painting)
                    var title = activity?.findViewById<TextView>(R.id.painting_title)
                    if (title != null) {
                        title.text = painting.title
                    }
                    var photo = activity?.findViewById<ImageView>(R.id.painting_image_view)
                    if (photo != null) {
                        val image = imageFrom64Encoding(painting.image)
                        photo.setImageBitmap(image)
                    }
                    var info = activity?.findViewById<TextView>(R.id.painting_info_text)
                    if (info != null) {
                        info.text = painting.info
                    }
                    mainLayout?.visibility = View.VISIBLE
                    loading_spinner?.visibility = View.GONE
                },
                onFailure = { statusCode ->
                    Log.e("Request Failure", statusCode.toString())
                    val errorMsg = "Error: $statusCode"
                }
            )
        }
    }

    override fun onStop() {
        super.onStop()
        var footer = activity?.findViewById<BottomNavigationView>(R.id.navigationView)
        if (footer != null) {
            footer.visibility = View.VISIBLE;
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PhotoInfoViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
