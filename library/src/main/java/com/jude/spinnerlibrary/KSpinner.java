package com.jude.spinnerlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import java.util.List;

public class KSpinner extends RelativeLayout
{
    // region [GENERAL DECLARATIONS]
    private RelativeLayout relativeLayout;
    private ASpinner spinner;
    private TextView textView;
    private ArrowView arrowView;
    private enum STYLE {FILL, STROKE, FILL_STROKE}
    private enum PATH {OPEN, CLOSE}
    private int canvasBackColor = Color.BLACK;
    private int strokeColor = Color.WHITE;
    private float strokeWidth = 5f;
    private Context context;
    // endregion

    // region [CONSTRUCTORS]
    public KSpinner(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.context = context;
        initViews(context, attrs);
    }

    public KSpinner(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initViews(context, attrs);
    }
    // endregion

    // region [VIEW DECLARATIONS]
    private void initViews(Context context, AttributeSet attrs)
    {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.KSpinner, 0, 0);
        try
        {
            // declare fields here...
            /*View view = inflate(getContext(), R.layout.res_drpdown, null);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayout);
            spinner = (ASpinner) view.findViewById(R.id.spinner);
            textView = (TextView) view.findViewById(R.id.textView);
            arrowView = (ArrowView) view.findViewById(R.id.arrowView);*/

            relativeLayout = new RelativeLayout(context);
            spinner = new ASpinner(context);
            textView = new TextView(context);
            arrowView = new ArrowView(context);

            spinner.setId(1000);
            textView.setVisibility(GONE);
            textView.setGravity(Gravity.CENTER|Gravity.LEFT);
            textView.setBackgroundColor(Color.WHITE);
            spinner.setBackgroundColor(Color.WHITE);
            relativeLayout.setBackgroundColor(Color.WHITE);

            // Fetch values from typed array and set to fields...
            Paint paint = new Paint();
            strokeColor = typedArray.getColor(R.styleable.KSpinner_arrowColor, strokeColor);
            canvasBackColor = typedArray.getColor(R.styleable.KSpinner_backgroundColor, canvasBackColor);
            strokeWidth = typedArray.getDimension(R.styleable.KSpinner_arrowWidth, strokeWidth);
            int styling = typedArray.getInt(R.styleable.KSpinner_style, 0);
            int typo = typedArray.getInt(R.styleable.KSpinner_path, 0);
            STYLE style = STYLE.values()[styling];
            PATH path = PATH.values()[typo];
            switch (style)
            {
                case FILL:
                    paint.setStyle(Paint.Style.FILL);
                    break;
                case STROKE:
                    paint.setStyle(Paint.Style.STROKE);
                    break;
                case FILL_STROKE:
                    paint.setStyle(Paint.Style.FILL_AND_STROKE);
                    break;
            }

            switch (path)
            {
                case CLOSE:
                    arrowView.setPathType(true);
                    break;
                case OPEN:
                    arrowView.setPathType(false);
                    break;
            }
            //==============================
            paint.setColor(strokeColor);
            paint.setStrokeWidth(strokeWidth);
            arrowView.setArrowPaint(paint);
            arrowView.setBackColor(canvasBackColor);
            arrowView.invalidate();
            addView(relativeLayout);
            mPerformEvents();
        }
        finally
        {
            typedArray.recycle();
        }
    }
    // endregion

    // region [METHODS]
    private boolean isTouch, isItemSelected;
    private void mPerformEvents()
    {
        spinner.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                isTouch = true;
                arrowView.setAnim(1);
                return false;
            }
        });

        spinner.setOnItemSelectedEvenIfUnchangedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if (isTouch)
                {
                    if(listener != null)
                    {
                        isItemSelected = true;
                        listener.onItemSelected(parent, view, position);
                    }

                    if(textView.getVisibility() == View.VISIBLE)
                    {
                        textView.setText((String) spinner.getItemAtPosition(position));
                    }
                    isTouch = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        spinner.setSpinnerEventsListener(new ASpinner.OnSpinnerEventsListener()
        {
            @Override
            public void onSpinnerOpened(Spinner spin)
            {
                if(listener != null)
                {
                    listener.onOpenOrClose(true);
                }
            }

            @Override
            public void onSpinnerClosed(Spinner spin)
            {
                arrowView.setAnim(0);
                if (listener != null)
                {
                    listener.onOpenOrClose(false);
                    listener.onNothingSelected(!isItemSelected);
                    isItemSelected = false;
                }
            }
        });
    }

    private String[] strings;
    public void setAdapter(String[] strings)
    {
        textView.setVisibility(View.VISIBLE);
        this.strings = new String[strings.length];
        this.strings = strings;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, this.strings);
        spinner.setAdapter(adapter);
        textView.setText(spinner.getAdapter().getItem(0).toString());
    }

    public void setAdapter(int array)
    {
        this.strings = getResources().getStringArray(array);
        setAdapter(this.strings);
    }

    public void setSpinnerText(String mValue)
    {
        if(this.strings == null)
        {
            throw new RuntimeException("String array cannot be null; Have you set values using 'setAdapter'");
        }
        else if(this.strings.length == 0)
        {
            throw new RuntimeException("Strings array length is '0'. Please set appropriate values");
        }
        if (mValue != null || mValue.trim().length() > 0)
        {
            textView.setVisibility(VISIBLE);
        }
        else
        {
            textView.setVisibility(GONE);
        }
        String[] strings1 = this.strings;
        for (int i = 0; i < strings1.length; i++)
        {
            String string = strings1[i];
            if (string.equals(mValue))
            {
                textView.setText(string);
                spinner.setSelection(i);
                return;
            }
        }
        textView.setText(mValue);
    }

    private OnItemSelectedListener listener;
    public void setOnItemSelectedListener(OnItemSelectedListener listener)
    {
        this.listener = listener;
    }

    public String getDropDownText()
    {
        return textView.getText().toString();
    }

    public Object getItemAtPosition(int position)
    {
        if (spinner.getSelectedItem() instanceof String)
        {
            return spinner.getSelectedItem().toString();
        }
        else
        {
            return spinner.getAdapter().getItem(position);
        }
    }

    public void setSelection(int position)
    {
        spinner.setSelection(position);
        textView.setText(spinner.getAdapter().getItem(position).toString());
    }

    public ArrowView getArrowView()
    {
        return arrowView;
    }

    public void setTextColor(int color)
    {
        textView.setTextColor(getResources().getColor(color));
    }

    public void setTextBackGroundColor(int color)
    {
        textView.setBackgroundColor(getResources().getColor(color));
    }

    public static float pxToDp(final Context context, final float px)
    {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float dpToPx(final Context context, final float dp)
    {
        return dp * context.getResources().getDisplayMetrics().density;
    }
    // endregion

    // region [INTERFACE]
    public interface OnItemSelectedListener
    {
        void onItemSelected(AdapterView<?> parent, View view, int position);
        void onNothingSelected(boolean isNothingSelected);
        void onOpenOrClose(boolean isOpened);
    }
    // endregion

    // region [ONSIZE CHANGED]
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);

        LayoutParams spinnerParams = new LayoutParams(w - h, h);
        spinnerParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        LayoutParams textViewParams = new LayoutParams(w - h, h);
        textViewParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        LayoutParams successParams = new LayoutParams(h, h);
        successParams.addRule(RelativeLayout.RIGHT_OF, spinner.getId());

        textView.setTextSize(dpToPx(context, h/4));
        textView.setPadding(h/4 , 0, 0, 0);

        spinner.setDropDownVerticalOffset(h);
        relativeLayout.removeAllViews();
        relativeLayout.addView(spinner, spinnerParams);
        relativeLayout.addView(textView, textViewParams);
        relativeLayout.addView(arrowView, successParams);
    }
    // endregion
}
