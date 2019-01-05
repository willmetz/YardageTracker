package com.slapshotapps.swimyardagetracker.bindingadapters

import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.slapshotapps.swimyardagetracker.utils.KeyboardActionButtonListener


@BindingAdapter("onKeyboardActionButton")
fun textWatcherBinding(textView: TextView, listener: KeyboardActionButtonListener?){
    textView.setOnEditorActionListener { v, actionId, event ->

        if(actionId == EditorInfo.IME_ACTION_DONE){
            listener?.onDoneSelected()
            true
        }else{
            false
        }

    }
}
