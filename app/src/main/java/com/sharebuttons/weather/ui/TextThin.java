package com.sharebuttons.weather.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Monkey D Luffy on 7/21/2015.
 */
public class TextThin extends TextView {
    public TextThin(Context context) {
        super(context);
        roboToThin();
    }

    public TextThin(Context context, AttributeSet attrs) {
        super(context, attrs);
        roboToThin();
    }

    public TextThin(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        roboToThin();
    }

    private void roboToThin() {
        Typeface typeface = Typeface.createFromAsset(getResources().getAssets(), "fonts/JosefinSans-Thin.ttf");
        setTypeface(typeface);
    }
}
