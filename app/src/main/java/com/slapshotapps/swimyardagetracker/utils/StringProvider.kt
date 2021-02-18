package com.slapshotapps.swimyardagetracker.utils

import android.app.Application
import androidx.annotation.StringRes
import javax.inject.Inject

class StringProvider @Inject constructor(val app: Application) {

    fun getString(@StringRes id: Int): String = app.getString(id)

    fun getString(@StringRes id: Int, vararg arguments: Any): String = app.getString(id, arguments)
}
