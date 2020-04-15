package com.jude.spinnerlibrary

import android.content.Context
import android.util.AttributeSet
import android.widget.Spinner



class ASpinner : Spinner {
    private var listener: OnItemSelectedListener? = null
    private var mListener: SpinnerEventsListener? = null
    private var mOpenInitiated = false

    constructor(context: Context) : super(context) {}

    constructor(context: Context, mode: Int): super(context, mode) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    override fun setSelection(position: Int, animate: Boolean) {
        val sameSelected = position == selectedItemPosition
        super.setSelection(position, animate)
        if (sameSelected) {
            listener?.onItemSelected(this, selectedView, position, selectedItemId)
        }
    }

    override fun setSelection(position: Int) {
        super.setSelection(position)
        listener?.onItemSelected(this, selectedView, position, selectedItemId)
    }

    fun setOnItemSelectedEvenIfUnchangedListener(listener: OnItemSelectedListener) {
        this.listener = listener
    }

    override fun performClick(): Boolean {
        mOpenInitiated = true
        mListener?.onSpinnerOpened(this)
        return super.performClick()
    }

    fun setSpinnerEventsListener(onSpinnerEventsListener: SpinnerEventsListener) {
        mListener = onSpinnerEventsListener
    }

    private fun performClosedEvent() {
        mOpenInitiated = false
        mListener?.onSpinnerClosed(this)
    }

    private fun hasBeenOpened(): Boolean {
        return mOpenInitiated
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (hasBeenOpened() && hasWindowFocus) {
            performClosedEvent()
        }
    }
}
