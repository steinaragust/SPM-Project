package com.iceartgrp.iceart.components

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PhotoInfoViewModel : ViewModel() {
    private val data = MutableLiveData<Any>()

    fun setDataCommunicator(newData: Any) {
        data.value = newData
    }
}
