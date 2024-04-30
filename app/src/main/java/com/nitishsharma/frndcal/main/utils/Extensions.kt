package com.nitishsharma.frndcal.main.utils

import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import timber.log.Timber

fun Fragment.navigate(navDirections: NavDirections) {
    try {
        findNavController().navigate(navDirections)
    } catch (exc: Exception) {
        exc.message?.let { Timber.e(it) }
    }
}