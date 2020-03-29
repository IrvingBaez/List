package com.example.list.compoundComponents;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.list.R;
import com.example.list.activities.ElementDetailsActivity;
import com.example.list.activities.ListDetailsActivity;
import com.example.list.databaseAccess.AccessTags;
import com.example.list.model.List;

public class TagView extends LinearLayout {
    private List list;
    private String tagName;
    private AppCompatActivity parent;
    private TextView nameView;
    private Button delete;

    View.OnClickListener delete_click = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            AccessTags accessTags = new AccessTags(parent);
            accessTags.deleteTag(list, tagName);
            ((ListDetailsActivity)parent).fillTags();
        }
    };

    public TagView(Context context, List list, String tagName){
        super(context);
        this.parent = (AppCompatActivity) context;
        this.list = list;
        this.tagName = tagName;
        init(context);
    }
    public TagView(Context context) {
        super(context);
    }

    public TagView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TagView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.tag_view, this);

        this.nameView = this.findViewById(R.id.tagView_text);
        this.delete = findViewById(R.id.tagView_button);

        nameView.setText(this.tagName);

        if(this.parent instanceof ListDetailsActivity){
            this.delete.setOnClickListener(delete_click);
        }else if(this.parent instanceof ElementDetailsActivity) {
            //Delete from element_tags.
        }
    }
}
