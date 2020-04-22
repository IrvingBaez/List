package com.example.list.compoundComponents;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.list.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ColorChooser extends LinearLayout {
    private FloatingActionButton color1;
    private FloatingActionButton color2;
    private FloatingActionButton color3;
    private FloatingActionButton color4;
    private FloatingActionButton color5;
    private FloatingActionButton color6;

    public ColorChooser(Context context) {
        super(context);
        this.init(context);
    }

    public ColorChooser(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init(context);
    }

    public ColorChooser(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_color_chooser, this);

        color1 = findViewById(R.id.view_color_chooser_1);
        color2 = findViewById(R.id.view_color_chooser_2);
        color3 = findViewById(R.id.view_color_chooser_3);
        color4 = findViewById(R.id.view_color_chooser_4);
        color5 = findViewById(R.id.view_color_chooser_5);
        color6 = findViewById(R.id.view_color_chooser_6);
    }
}
