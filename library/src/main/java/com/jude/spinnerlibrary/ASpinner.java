package com.jude.spinnerlibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;

public class ASpinner extends Spinner
{
    private OnItemSelectedListener listener;
    private OnSpinnerEventsListener mListener;
    private boolean mOpenInitiated = false;

    public ASpinner(Context context)
    {
        super(context);
    }

    public ASpinner(Context context, int mode)
    {
        super(context, mode);
    }

    public ASpinner(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    public void setSelection(int position, boolean animate)
    {
        boolean sameSelected = position == getSelectedItemPosition();
        super.setSelection(position, animate);
        if (sameSelected)
        {
            if (listener != null)
            {
                listener.onItemSelected(this, getSelectedView(), position, getSelectedItemId());
            }
        }
    }

    @Override
    public void setSelection(int position)
    {
        super.setSelection(position);
        if (listener != null)
        {
            listener.onItemSelected(this, getSelectedView(), position, getSelectedItemId());
        }
    }

    public void setOnItemSelectedEvenIfUnchangedListener(OnItemSelectedListener listener)
    {
        this.listener = listener;
    }

    // =======================================================

    public interface OnSpinnerEventsListener
    {
        void onSpinnerOpened(Spinner spin);
        void onSpinnerClosed(Spinner spin);
    }

    @Override
    public boolean performClick()
    {
        mOpenInitiated = true;
        if (mListener != null)
        {
            mListener.onSpinnerOpened(this);
        }
        return super.performClick();
    }

    public void setSpinnerEventsListener(OnSpinnerEventsListener onSpinnerEventsListener)
    {
        mListener = onSpinnerEventsListener;
    }

    public void performClosedEvent()
    {
        mOpenInitiated = false;
        if (mListener != null)
        {
            mListener.onSpinnerClosed(this);
        }
    }

    public boolean hasBeenOpened()
    {
        return mOpenInitiated;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus)
    {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasBeenOpened() && hasWindowFocus)
        {
            performClosedEvent();
        }
    }
}
