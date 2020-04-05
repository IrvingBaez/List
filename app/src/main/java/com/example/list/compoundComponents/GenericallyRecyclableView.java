package com.example.list.compoundComponents;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public abstract class GenericallyRecyclableView extends LinearLayout {
    public GenericallyRecyclableView(Context context) {
        super(context);
    }

    public GenericallyRecyclableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GenericallyRecyclableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setData(Object data){

    }
}
