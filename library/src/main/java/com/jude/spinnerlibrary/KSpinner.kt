package com.jude.spinnerlibrary

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.*

class KSpinner : RelativeLayout {

    // region [GENERAL DECLARATIONS]
    private var spinner: ASpinner? = null
    private var arrowView: ArrowView? = null
    private var relativeLayout: RelativeLayout? = null

    private var spinnerID: Int = 1000
    private var strokeWidth = 5f
    private lateinit var paint: Paint
    private var strokeColor = Color.BLACK
    private var backGroundColor = Color.TRANSPARENT

    private var isTouch: Boolean = false
    private var isItemSelected: Boolean = false

    private var strings: List<String> = mutableListOf()
    private var listener: OnItemSelectedListener? = null
    // endregion

    // region [ENUMS]
    enum class STYLE {
        FILL, STROKE, FILL_STROKE
    }

    enum class PATH {
        OPEN, CLOSE
    }
    // endregion

    // region [CONSTRUCTORS]
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initViews(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initViews(context, attrs)
    }
    // endregion

    // region [VIEW DECLARATIONS]
    private fun initViews(context: Context, attrs: AttributeSet) {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.KSpinner, 0, 0)
        try {
            relativeLayout = RelativeLayout(context)
            spinner = ASpinner(context)
            arrowView = ArrowView(context)

            spinner?.id = spinnerID

            // Fetch values from typed array and set to fields...
            strokeColor = typedArray.getColor(R.styleable.KSpinner_arrowColor, strokeColor)
            strokeWidth = typedArray.getDimension(R.styleable.KSpinner_arrowWidth, strokeWidth)
            backGroundColor = typedArray.getColor(R.styleable.KSpinner_backgroundColor, backGroundColor)

            val typo = typedArray.getInt(R.styleable.KSpinner_path, 0)
            val path = PATH.values()[typo]

            val styling = typedArray.getInt(R.styleable.KSpinner_style, 0)
            val style = STYLE.values()[styling]

            paint = Paint().apply {
                color = strokeColor
                strokeWidth = strokeWidth
            }
            setArrowPath(path) // Sets the arrow path
            setArrowStyle(style) // Sets the arrow style
            arrowView?.setArrowPaint(paint)
            arrowView?.setBackColor(backGroundColor)
            arrowView?.invalidate()

            addView(relativeLayout)
            performSpinnerEvents()
        } catch (e: java.lang.Exception) {
            Log.e("Log", "", e)
        } finally {
            typedArray.recycle()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun performSpinnerEvents() {
        spinner?.setOnTouchListener { _, _ ->
            isTouch = true
            arrowView?.setAnim(1)
            false
        }

        spinner?.setOnItemSelectedEvenIfUnchangedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (isTouch) {
                    isTouch = false
                    isItemSelected = true
                    listener?.onItemSelected(parent, view, position)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        })

        spinner?.setSpinnerEventsListener(object : SpinnerEventsListener {
            override fun onSpinnerOpened(spin: Spinner) {
                listener?.onOpenOrClose(true)
            }

            override fun onSpinnerClosed(spin: Spinner) {
                arrowView?.setAnim(0)
                listener?.onOpenOrClose(false)
                listener?.onNothingSelected(isItemSelected)
                isItemSelected = false
            }
        })
    }

    fun setAdapter(array: Int) {
        this.strings = resources.getStringArray(array).toList()
        setAdapter(this.strings)
    }

    fun setAdapter(list: List<String>) {
        val adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, android.R.id.text1, list)
        spinner?.adapter = adapter
    }

    fun setAdapter(strings: Array<String>) {
        this.strings = strings.toList()
        val adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, android.R.id.text1, this.strings)
        spinner?.adapter = adapter
    }

    fun setSpinnerWithIndex(value: String?) {
        if (strings.isEmpty()) {
            throw RuntimeException("Please provide proper values; Have you set values using 'setAdapter'?")
        }
        val index = strings.indexOfFirst { it == value }
        spinner?.setSelection(index ?: 0)
    }

    fun getItemAtPosition(position: Int): Any? {
        return if (spinner?.selectedItem is String) {
            spinner?.selectedItem.toString()
        } else {
            spinner?.adapter?.getItem(position)
        }
    }

    fun setSelection(position: Int) {
        spinner?.setSelection(position)
    }
    // endregion

    // region [PRIVATE METHODS]
    fun setArrowStyle(style: STYLE) {
        when (style) {
            STYLE.FILL -> paint.style = Paint.Style.FILL
            STYLE.STROKE -> paint.style = Paint.Style.STROKE
            STYLE.FILL_STROKE -> paint.style = Paint.Style.FILL_AND_STROKE
        }
    }

    fun setArrowPath(path: PATH) {
        when (path) {
            PATH.CLOSE -> arrowView?.setPathType(true)
            PATH.OPEN -> arrowView?.setPathType(false)
        }
    }

    fun setArrowStroke(strokeWidth: Float) {
        this.strokeWidth = strokeWidth
        arrowView?.invalidate()
    }
    // endregion

    // region [OVERRIDDEN METHODS]
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        Handler().postDelayed({
            val spinnerParams = LayoutParams(w - h, h)
            spinnerParams.addRule(ALIGN_PARENT_LEFT)

            val arrowParams = LayoutParams(h, h)
            arrowParams.addRule(RIGHT_OF, spinner!!.id)

            relativeLayout?.addView(spinner, spinnerParams)
            relativeLayout?.addView(arrowView!!, arrowParams)

            // Set Spinner properties
            spinner?.background = null
            spinner?.dropDownVerticalOffset = h
        }, 10)
    }
    // endregion

    private fun setSListener(spinnerEventsListener: OnItemSelectedListener) {
        listener = spinnerEventsListener
    }

    fun setListener(func: KSpinnerEventsListener.() -> Unit) {
        val listener = KSpinnerEventsListener()
        listener.func()
        setSListener(listener)
    }

    companion object {
        fun pxToDp(context: Context, px: Float): Float {
            return px / context.resources.displayMetrics.density
        }

        fun dpToPx(context: Context, dp: Float): Float {
            return dp * context.resources.displayMetrics.density
        }
    }

    inner class KSpinnerEventsListener : OnItemSelectedListener {

        private var onOpenOrClose: ((openOrClose: Boolean) -> Unit)? = null
        private var onNothingSelected: ((isNothingSelected: Boolean) -> Unit)? = null
        private var onItemSelected: ((parent: AdapterView<*>, view: View, position: Int) -> Unit)? = null

        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int) {
            onItemSelected?.invoke(parent, view, position)
        }

        override fun onNothingSelected(isNothingSelected: Boolean) {
            onNothingSelected?.invoke(isNothingSelected)
        }

        override fun onOpenOrClose(openOrClose: Boolean) {
            onOpenOrClose?.invoke(openOrClose)
        }

        fun onOpenClose(func: (openOrClose: Boolean) -> Unit) {
            onOpenOrClose = func
        }

        fun onNoSelection(func: (isNothingSelected: Boolean) -> Unit) {
            onNothingSelected = func
        }

        fun onItemSelection(func: (parent: AdapterView<*>, view: View, position: Int) -> Unit) {
            onItemSelected = func
        }
    }
}
