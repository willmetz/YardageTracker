package com.slapshotapps.swimyardagetracker.bindingadapters

import android.widget.Spinner
import androidx.databinding.BindingAdapter
import com.slapshotapps.swimyardagetracker.extensions.SpinnerItemSelectedListener
import com.slapshotapps.swimyardagetracker.extensions.setSpinnerEntries
import com.slapshotapps.swimyardagetracker.extensions.setSpinnerItemSelectedListener
import com.slapshotapps.swimyardagetracker.extensions.setSpinnerValue

@BindingAdapter("entries")
fun Spinner.setEntries(entries: List<Any>?) {
    setSpinnerEntries(entries)
}

@BindingAdapter("onItemSelected")
fun Spinner.setItemSelectedListener(itemSelectedListener: SpinnerItemSelectedListener?) {
    setSpinnerItemSelectedListener(itemSelectedListener)
}

@BindingAdapter("newValue")
fun Spinner.setNewValue(newValue: Any?) {
    setSpinnerValue(newValue)
}
