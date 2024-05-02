package com.nitishsharma.frndcal.main.home.homeNew.binding

import android.os.Build
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import com.nitishsharma.frndcal.R
import com.nitishsharma.frndcal.main.home.homeNew.HomeFragmentNewViewModel
import timber.log.Timber
import java.util.Calendar

@BindingAdapter("setCalendarYear")
fun setCalendarYear(
    spinner: Spinner,
    viewModel: HomeFragmentNewViewModel
) {
    try {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR) + 8
        val yearList = mutableListOf<Int>()
        for (i in currentYear downTo 2014) {
            yearList.add(i)
        }
        setYearSpinnerAdapterAndHeight(spinner, yearList)
        yearList.forEachIndexed { index, _year ->
            if (viewModel.yearLiveData.value == _year)
                spinner.setSelection(index, true)
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewModel.updateYearLiveData(p0?.selectedItem.toString().toInt())
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    } catch (e: Exception) {
        Timber.e(e.message.toString())
    }
}

fun setYearSpinnerAdapterAndHeight(
    spinner: Spinner,
    yearList: MutableList<Int>
) {

    val arrayAdapter: ArrayAdapter<*> =
        ArrayAdapter<Any?>(
            spinner.context, R.layout.simple_spinner_item,
            yearList as List<Any?>
        )
    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    spinner.adapter = arrayAdapter
}

//@RequiresApi(Build.VERSION_CODES.O)
//@BindingAdapter(value = ["viewModel", "previousMonthClicked"], requireAll = false)
//fun setCalendarMonth(
//    spinner: Spinner,
//    viewModel: HomeFragmentNewViewModel,
//    previousMonthClicked: Boolean = false
//) {
//    val monthList =
//        ArrayList(spinner.context.resources.getStringArray(R.array.month_names).toMutableList())
//    setMonthSpinnerAdapterAndHeight(spinner, monthList)
//    viewModel.monthLiveData.value?.let {
//        if (monthList.size >= it) {
//            if (previousMonthClicked) {
//                spinner.setSelection(11)
//            } else {
//                spinner.setSelection(it - 1, true)
//            }
//        } else {
//            spinner.setSelection(0, true)
//        }
//    }
//    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//            viewModel.updateMonthLiveData(p0?.selectedItemPosition?.plus(1) ?: 1)
//        }
//
//        override fun onNothingSelected(p0: AdapterView<*>?) {
//        }
//    }
//}

@RequiresApi(Build.VERSION_CODES.O)
@BindingAdapter(value = ["viewModel", "selectedYear", "previousMonthClicked"], requireAll = false)
fun setCalendarMonth(
    spinner: Spinner,
    viewModel: HomeFragmentNewViewModel,
    selectedYear: Int,
    previousMonthClicked: Boolean = false
) {
    try {
        val monthList =
            ArrayList(spinner.context.resources.getStringArray(R.array.month_names).toMutableList())
        setMonthSpinnerAdapterAndHeight(
            spinner,
            selectedYear,
            monthList,
            2032,
            viewModel.currentMonth
        )
        viewModel.monthLiveData.value?.let {
            if (monthList.size >= it) {
                if (previousMonthClicked) {
                    spinner.setSelection(11)
                } else {
                    spinner.setSelection(it - 1, true)
                }
            } else {
                spinner.setSelection(0, true)
            }
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewModel.updateMonthLiveData(p0?.selectedItemPosition?.plus(1) ?: 1)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        if (spinner.selectedItemPosition == 0) {
            spinner.onItemSelectedListener?.onItemSelected(spinner, spinner.selectedView, 0, 0)
        }
    } catch (e: Exception) {
        Timber.e(e.message.toString())
    }
}


fun setMonthSpinnerAdapterAndHeight(
    spinner: Spinner,
    selectedYear: Int,
    monthList: java.util.ArrayList<String>,
    currentYear: Int,
    currentMonth: Int
) {
    if (selectedYear == currentYear) {
        monthList.subList((currentMonth), 12).clear()
    }
    val arrayAdapter: ArrayAdapter<*> =
        ArrayAdapter<Any?>(
            spinner.context,
            android.R.layout.simple_spinner_item,
            monthList.toList()
        )
    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    spinner.adapter = arrayAdapter
}
