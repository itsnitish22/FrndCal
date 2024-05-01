package com.nitishsharma.frndcal.main.tasklist.binding

import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.nitishsharma.frndcal.main.utils.LoadingModel
import com.nitishsharma.frndcal.main.utils.LoadingState

@BindingAdapter("updateSwipeRefresh")
fun updateSwipeRefresh(view: SwipeRefreshLayout, loadingObj: LoadingModel?) {
    if (loadingObj?.loadingModel != LoadingState.LOADING) {
        view.isRefreshing = false
    }
}