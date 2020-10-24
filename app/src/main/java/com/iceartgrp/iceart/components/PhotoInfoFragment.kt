package com.iceartgrp.iceart.components

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.iceartgrp.iceart.R
import com.iceartgrp.iceart.network.ApiConsumer
import kotlinx.android.synthetic.main.photo_info_fragment.*

private const val ARG_PHOTO_STRING = "photoString"

class PhotoInfoFragment : Fragment() {
    private var encoded64Photo: String? = null
    private var textView: TextView? = null
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
            ApiConsumer().getPaintingById(
                0,
                onSuccess = { painting ->
                    println(painting)
                    textView?.visibility = View.VISIBLE
                    loading_spinner?.visibility = View.GONE
                },
                onFailure = { statusCode ->
                    Log.e("Request Failure", statusCode.toString())
                    val errorMsg = "Error: $statusCode"
                }
            )
        }
        //apiConsumer.uploadImage(img)
    private lateinit var viewModel: PhotoInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var root = inflater.inflate(R.layout.photo_info_fragment, container, false)
        textView = root.findViewById<TextView>(R.id.test_textview)
        loader = root.findViewById<ProgressBar>(R.id.loading_spinner)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PhotoInfoViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
