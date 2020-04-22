package com.example.list.compoundComponents;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.list.R;
import com.example.list.activities.ListDetailsActivity;
import com.example.list.databaseAccess.AccessTags;
import com.example.list.model.EasyList;
import com.example.list.model.ListElement;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TagView extends LinearLayout {
    private EasyList list;
    private ListElement element;
    private String tagName;
    private Context context;
    private TagViewListener parent;
    private TextView nameView;
    private FloatingActionButton button;
    private ButtonAction action;

    public enum ButtonAction {TOTAL_DELETE, DELETE, ADD};

    public TagView(Context context, EasyList list, String tagName){
        super(context);
        this.context = context;
        this.parent = (TagViewListener) context;
        this.list = list;
        this.element = null;
        this.tagName = tagName;
        init(context);
    }

    public TagView(Context context, ListElement element, String tagName){
        super(context);
        this.context = context;
        this.parent = (TagViewListener) context;
        this.list = element.getParent();
        this.element = element;
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
        inflater.inflate(R.layout.view_tag, this);

        this.button = findViewById(R.id.view_tag_button);
        this.button.setOnClickListener(button_clicked);

        this.nameView = this.findViewById(R.id.view_tag_text);
        this.nameView.setText(this.tagName);
    }

    public void setButtonAction(ButtonAction action){
        this.action = action;

        if(action == ButtonAction.ADD){
            this.button.setImageResource(android.R.drawable.ic_input_add);
        }else{
            this.button.setImageResource(android.R.drawable.ic_delete);
        }
    }

    public String getTagName(){
        return this.tagName;
    }

    View.OnClickListener button_clicked = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            AccessTags accessTags = new AccessTags(context);

            switch (action){
                case TOTAL_DELETE:
                    parent.onButtonPressed(TagView.this);
                    break;
                case DELETE:
                    accessTags.deleteTag(element, tagName);
                    parent.onButtonPressed(TagView.this);
                    break;
                case ADD:
                    accessTags.addTag(element, tagName);
                    parent.onButtonPressed(TagView.this);
                    break;
            }
        }
    };

    public interface TagViewListener{
        void onButtonPressed(TagView tagView);
    }
}
