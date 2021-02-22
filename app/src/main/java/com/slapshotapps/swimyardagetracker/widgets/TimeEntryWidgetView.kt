package com.slapshotapps.swimyardagetracker.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.slapshotapps.swimyardagetracker.R
import com.slapshotapps.swimyardagetracker.databinding.TimeEntryWidgetBinding
import com.slapshotapps.swimyardagetracker.ui.records.newRecord.TimeForPersonalRecord
import com.slapshotapps.swimyardagetracker.utils.TextWatcherHelper

class TimeEntryWidgetView(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    private var _binding: TimeEntryWidgetBinding? = null
    private val binding get() = _binding!!

    init {
        _binding = TimeEntryWidgetBinding.inflate(LayoutInflater.from(context), this)

        binding.minutesInput.addTextChangedListener(object : TextWatcherHelper() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                super.onTextChanged(s, start, before, count)
                clearError()
            }
        })

        binding.secondsInput.addTextChangedListener(object : TextWatcherHelper() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                super.onTextChanged(s, start, before, count)
                clearError()
            }
        })

        binding.millisecondsInput.addTextChangedListener(object : TextWatcherHelper() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                super.onTextChanged(s, start, before, count)
                clearError()
            }
        })
    }

    fun setError() {
        background = ContextCompat.getDrawable(context, R.drawable.error_box)
    }

    fun clearError() {
        background = null
    }

    fun getTimeEntered(): TimeForPersonalRecord {
        val minutes = binding.minutesInput.text.toString().toIntOrNull() ?: 0
        val seconds = binding.secondsInput.text.toString().toIntOrNull() ?: 0
        val milliseconds = binding.millisecondsInput.text.toString().toIntOrNull() ?: 0

        return TimeForPersonalRecord(minutes, seconds, milliseconds)
    }
}
