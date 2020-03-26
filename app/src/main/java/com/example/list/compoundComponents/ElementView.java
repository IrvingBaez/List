package com.example.list.compoundComponents;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.list.R;
import com.example.list.activities.ElementDetailsActivity;
import com.example.list.activities.ElementsActivity;
import com.example.list.databaseAccess.DatabaseAccess;
import com.example.list.model.Element;

public class ElementView extends LinearLayout {
    Element element;
    AppCompatActivity parent;
    DatabaseAccess databaseAccess;

    CheckBox checkBox;
    TextView view;

    private View.OnClickListener element_click = new View.OnClickListener(){
        @Override
        public void onClick(View sender){
            Intent i = new Intent(parent, ElementDetailsActivity.class);
            i.putExtra("element", element);
            parent.startActivity(i);
        }
    };

    private View.OnClickListener element_check = new View.OnClickListener() {
        @Override
        public void onClick(View sender) {
            CheckBox c = (CheckBox) sender;
            element.setFinished(c.isChecked());
            databaseAccess.updateElement(element);

            if(checkBox.isChecked()){
                view.setTextColor(Color.GRAY);
            }else{
                view.setTextColor(Color.BLACK);
            }
        }
    };

    public ElementView(Context context, Element element) {
        super(context);
        this.parent = (ElementsActivity)context;
        this.element = element;
        this.databaseAccess  = DatabaseAccess.getInstance(parent);

        init(context);
    }

    public ElementView(Context context) {
        super(context);
        init(context);
    }

    public ElementView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ElementView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.new_element_view, this);

        this.checkBox = this.findViewById(R.id.elementView_checkbox);
        this.view = findViewById(R.id.elementView_edit);

        checkBox.setChecked(element.isFinished());
        checkBox.setOnClickListener(element_check);

        if(checkBox.isChecked()){
            view.setTextColor(Color.GRAY);
        }else{
            view.setTextColor(Color.BLACK);
        }

        view.setText(element.getContent());
        view.setClickable(true);
        view.setOnClickListener(element_click);
    }

    @Override
    protected void onFinishInflate(){
        super.onFinishInflate();
        this.checkBox = this.findViewById(R.id.elementView_checkbox);
        this.view = findViewById(R.id.elementView_edit);

        checkBox.setChecked(element.isFinished());
        view.setText(element.getContent());
    }
}