package com.jude.spinnerlibrary

import android.widget.Spinner

/**
 * Created by Jude on 13/April/2020
 */
interface SpinnerEventsListener {
    fun onSpinnerOpened(spin: Spinner)
    fun onSpinnerClosed(spin: Spinner)
}