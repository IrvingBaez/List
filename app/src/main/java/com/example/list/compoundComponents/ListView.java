package com.example.list.compoundComponents;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.list.R;
import com.example.list.activities.ElementsActivity;
import com.example.list.model.EasyList;

public class ListView extends GenericallyRecyclableView{
    private TextView title;
    private EasyList list;
    private AppCompatActivity parent;

    private static boolean ListTextClickable = true;

    public static void setListTextClickable(boolean clickable){
        ListView.ListTextClickable = clickable;
    }

    public ListView(Context context) {
        super(context);
        this.init(context);
    }

    public ListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init(context);
    }

    public ListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_list, this);
        this.parent = (AppCompatActivity)context;

        this.title = findViewById(R.id.view_list_name);
        this.title.setTextColor(Color.BLACK);
        this.title.setClickable(true);
        this.title.setOnClickListener(list_selection);
    }

    @Override
    public void setData(Object data) {
        this.list = (EasyList)data;
        this.title.setText(list.getTitle());
    }

    @Override
    Object getData() {
        return this.list;
    }

    private View.OnClickListener list_selection = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if(!ListView.ListTextClickable)
                return;

            Intent i = new Intent(parent, ElementsActivity.class);
            i.putExtra("list", list);
            parent.startActivity(i);

            ListView.ListTextClickable = false;
        }
    };
}