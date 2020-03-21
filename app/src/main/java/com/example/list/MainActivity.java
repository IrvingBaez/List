package com.example.list;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.list.databaseAccess.DatabaseAccess;
import com.example.list.model.ListElement;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private DatabaseAccess databaseAccess;
    private ArrayList<ListElement> lists;
    private ListElement displayed;
    private TextView title;
    private LinearLayout container;

    private View.OnClickListener button_click = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Button sender = (Button)v;
            ListElement selected = lists.get(sender.getId());
            setDisplayed(selected);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.title = findViewById(R.id.txt_title);
        this.container = findViewById(R.id.ll_container);
        this.displayed = null;
        this.databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
        loadLists();
    }

    private void loadLists(){
        this.lists = databaseAccess.getFirstChildren();
        displayed = null;
        title.setText(R.string.listas_disponibles);
        container.removeAllViews();

        for (ListElement element : this.lists){
            Button button = new Button(this);
            button.setText(element.getContent());
            button.setId(this.lists.indexOf(element));
            button.setOnClickListener(button_click);
            container.addView(button);
        }
    }

    private void setDisplayed(ListElement element){
        if(element == null){
            this.loadLists();
            return;
        }

        displayed = element;
        title.setText(displayed.getContent());
        container.removeAllViews();

        if(displayed.isEmpty())
            databaseAccess.setChildren(displayed);

        for(ListElement child : displayed.getElements()){
            Button button = new Button(this);
            button.setText(child.getContent());
            button.setId(lists.indexOf(child));
            button.setOnClickListener(button_click);
            container.addView(button);
        }
    }

    @Override
    public void onBackPressed(){
        if(this.displayed != null) {
            setDisplayed(displayed.getParent());
        }else{
            super.onBackPressed();
        }
    }
}