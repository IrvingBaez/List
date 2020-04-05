package com.example.list.compoundComponents;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.list.R;
import com.example.list.activities.ElementDetailsActivity;
import com.example.list.activities.ElementsActivity;
import com.example.list.databaseAccess.AccessElements;
import com.example.list.model.ListElement;

public class ElementView extends GenericallyRecyclableView {
    private ListElement element;
    private AppCompatActivity parent;
    private AccessElements accessElements;

    private CheckBox checkBox;
    private TextView TextView;

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
            accessElements.updateElement(element);

            if(checkBox.isChecked()){
                TextView.setTextColor(Color.GRAY);
            }else{
                TextView.setTextColor(Color.BLACK);
            }

            if(parent instanceof ElementsActivity)
                ((ElementsActivity)parent).sortData();
        }
    };

    public ElementView(Context context, ListElement element) {
        super(context);
        this.parent = (AppCompatActivity)context;
        this.element = element;
        this.accessElements = new AccessElements(parent);

        init(context);
    }

    public ElementView(Context context) {
        super(context);
        this.parent = (AppCompatActivity)context;
        this.accessElements = new AccessElements(parent);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_element, this);

        this.checkBox = this.findViewById(R.id.view_element_checkbox);
        this.TextView = findViewById(R.id.view_element_name);
    }

    public ElementView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ElementView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setData(Object data) {
        setElement((ListElement) data);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_element, this);

        this.checkBox = this.findViewById(R.id.view_element_checkbox);
        this.TextView = findViewById(R.id.view_element_name);

        checkBox.setChecked(element.isFinished());
        checkBox.setOnClickListener(element_check);

        if(checkBox.isChecked()){
            TextView.setTextColor(Color.GRAY);
        }else{
            TextView.setTextColor(Color.BLACK);
        }

        TextView.setText(element.getContent());
        TextView.setClickable(true);

        if(this.parent instanceof ElementsActivity)
            TextView.setOnClickListener(element_click);
    }

    public void setElement(ListElement element) {
        this.element = element;
        checkBox.setChecked(element.isFinished());
        checkBox.setOnClickListener(element_check);

        if(checkBox.isChecked()){
            TextView.setTextColor(Color.GRAY);
        }else{
            TextView.setTextColor(Color.BLACK);
        }

        TextView.setText(element.getContent());
        TextView.setClickable(true);

        if(this.parent instanceof ElementsActivity)
            TextView.setOnClickListener(element_click);
    }
}