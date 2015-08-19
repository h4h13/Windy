package com.sharebuttons.weather.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Monkey D Luffy on 7/17/2015.
 */
public class TextLight extends TextView {
    public TextLight(Context context) {
        super(context);
        setTypeFace();
    }

    public TextLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeFace();
    }

    public TextLight(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeFace();
    }

    private void setTypeFace() {
        Typeface typeface = Typeface.createFromAsset(getResources().getAssets(), "fonts/WorkSans-Thin.ttf");
        setTypeface(typeface);
    }
}
