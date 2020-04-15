package com.jude.spinnerlibrary

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class ArrowView : View {
    private var anim = 0
    private var p: Path? = null
    private lateinit var mPaint: Paint
    private var backColor = Color.BLACK
    private var shouldClose: Boolean = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        p = Path()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(backColor)

        val height = height
        val width = width
        p?.reset()
        p?.moveTo((width / 4).toFloat(), (height / 2).toFloat())
        if (anim == 0) {
            p?.lineTo((width / 2).toFloat(), (height - height / 4).toFloat())
        } else {
            p?.lineTo((width / 2).toFloat(), (height / 4).toFloat())
        }
        p?.lineTo((width - width / 4).toFloat(), (height / 2).toFloat())
        if (shouldClose) {
            p?.close()
        }
        canvas.drawPath(p!!, mPaint)
    }

    fun setAnim(anim: Int) {
        this.anim = anim
        invalidate()
    }

    fun setBackColor(color: Int) {
        backColor = color
        invalidate()
    }

    fun setArrowPaint(mPaint: Paint) {
        this.mPaint = mPaint
        invalidate()
    }

    fun setArrowView(mPaint: Paint, backColor: Int) {
        this.mPaint = mPaint
        this.backColor = backColor
        invalidate()
    }

    fun setPathType(shouldClose: Boolean) {
        this.shouldClose = shouldClose
        invalidate()
    }
}
