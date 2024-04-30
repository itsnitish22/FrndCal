import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import java.util.Calendar

@BindingAdapter("yearPicker")
fun yearPicker(view: Spinner, selectedYear: Int?) {
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    val yearRange = (currentYear - 50)..(currentYear + 50)
    val yearList = yearRange.toList()

    val adapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_item, yearList)
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    view.adapter = adapter

    selectedYear?.let {
        if (it in yearRange) {
            val selectedIndex = yearList.indexOf(it)
            view.setSelection(selectedIndex)
        }
    }
}
