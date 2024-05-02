package com.nitishsharma.frndcal.main.application.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nitishsharma.frndcal.main.utils.LoadingModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class CommonViewModel @Inject constructor() : ViewModel() {
    private val _loadingModel: MutableLiveData<LoadingModel> = MutableLiveData()
    val loadingModel: LiveData<LoadingModel> get() = _loadingModel


    fun updateLoadingModel(loadingModel: LoadingModel) {
        _loadingModel.postValue(loadingModel)
    }
}