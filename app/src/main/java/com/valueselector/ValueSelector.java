package com.valueselector;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by asmaasamir1 on 11/12/17.
 */

public class ValueSelector extends RelativeLayout {

    View rootView;
    TextView valueTextView;
    View minusButton, plusButton;

    private int minValue = Integer.MIN_VALUE;
    private int maxValue = Integer.MAX_VALUE;


    Handler handler = new Handler();
    private boolean plusButtonIsPressed, minusButtonIsPressed;
    private int REPEAT_INTERVAL_MS = 100;

    public ValueSelector(Context context) {
        super(context);
        init(context);
    }

    public ValueSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    private void init(Context context) {

        rootView = inflate(context, R.layout.layout_value_selector, this);
        valueTextView = (TextView) rootView.findViewById(R.id.valueTextView);

        minusButton = rootView.findViewById(R.id.minusButton);
        plusButton = rootView.findViewById(R.id.plusButton);

        minusButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                decrementValue();
            }
        });

        plusButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                incrementValue();

            }
        });

        plusButton.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                plusButtonIsPressed = true;
                handler.post(new AutoIncrementer());
                return false;
            }
        });

        plusButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if ((motionEvent.getAction() == MotionEvent.ACTION_UP || motionEvent.getAction() == MotionEvent.ACTION_CANCEL)) {
                    plusButtonIsPressed = false;
                }

                return false;
            }
        });
    }

    private void decrementValue() {
        int currentValue = getCurrentSelectedValue();
        currentValue--;
        setValue(currentValue);
    }

    private void incrementValue() {

        int currentValue = getCurrentSelectedValue();
        currentValue++;
        setValue(currentValue);
    }


    public int getCurrentSelectedValue() {
        return Integer.parseInt(valueTextView.getText().toString());
    }

    public void setValue(int newValue) {

        if (newValue < minValue)
            newValue = minValue;
        else if (newValue > maxValue)
            newValue = maxValue;
        valueTextView.setText(String.valueOf(newValue));
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }


    private class AutoIncrementer implements Runnable {

        @Override
        public void run() {

            if (plusButtonIsPressed)
                handler.postDelayed(new AutoIncrementer(), REPEAT_INTERVAL_MS);
        }
    }

    private class AutoDecrementer implements Runnable {

        @Override
        public void run() {
            if (minusButtonIsPressed)
                handler.postDelayed(new AutoDecrementer(), REPEAT_INTERVAL_MS);
        }
    }
}
