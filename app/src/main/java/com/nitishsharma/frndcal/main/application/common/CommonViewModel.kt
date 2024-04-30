package com.nitishsharma.frndcal.main.application.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.nitishsharma.frndcal.main.utils.Event
import com.nitishsharma.frndcal.main.utils.LoadingModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class CommonViewModel @Inject constructor() : ViewModel() {
    private val _loadingModel: MutableLiveData<LoadingModel> = MutableLiveData()
    val loadingModel: LiveData<LoadingModel> get() = _loadingModel

    private val _navDirection: MutableLiveData<Event<NavDirections>> = MutableLiveData()
    val navDirections: LiveData<Event<NavDirections>> = _navDirection


    fun updateLoadingModel(loadingModel: LoadingModel) {
        _loadingModel.postValue(loadingModel)
    }

    fun updateNavDirection(navDirections: NavDirections) {
        _navDirection.postValue(Event(navDirections))
    }
}