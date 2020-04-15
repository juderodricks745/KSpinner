package com.jude.spinnerlibrary

import android.view.View
import android.widget.AdapterView

/**
 * Created by Jude on 13/April/2020
 */
interface OnItemSelectedListener {
    fun onItemSelected(parent: AdapterView<*>, view: View, position: Int)
    fun onNothingSelected(isNothingSelected: Boolean)
    fun onOpenOrClose(openOrClose: Boolean)
}