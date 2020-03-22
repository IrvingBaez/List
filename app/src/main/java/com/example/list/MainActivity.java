package com.example.list;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.list.databaseAccess.DatabaseAccess;
import com.example.list.model.*;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private DatabaseAccess databaseAccess;
    private ArrayList<List> lists;
    private List displayedList;
    private TextView title;
    private LinearLayout container;

    private View.OnClickListener list_selection = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Button sender = (Button)v;
            List selected = lists.get(sender.getId());
            setDisplayedList(selected);
        }
    };

    private View.OnClickListener element_check = new View.OnClickListener() {
        @Override
        public void onClick(View sender) {
            CheckBox c = (CheckBox) sender;
            Element e = displayedList.getElements().get(c.getId());
            e.setFinished(c.isChecked());
            databaseAccess.updateElement(e);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.title = findViewById(R.id.txt_title);
        this.container = findViewById(R.id.ll_container);
        this.displayedList = null;
        this.databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
        loadLists();
    }

    private void loadLists(){
        this.lists = databaseAccess.getLists();
        displayedList = null;
        title.setText(R.string.listas_disponibles);
        container.removeAllViews();

        for (List list : this.lists){
            Button button = new Button(this);
            button.setText(list.getTitle());
            button.setId(this.lists.indexOf(list));
            button.setOnClickListener(list_selection);
            container.addView(button);
        }
    }

    private void setDisplayedList(List element){
        if(element == null){
            this.loadLists();
            return;
        }

        displayedList = element;
        title.setText(displayedList.getTitle());
        container.removeAllViews();

        if(displayedList.isEmpty())
            databaseAccess.setChildren(displayedList);

        for(Element child : displayedList.getElements()){
            CheckBox box = new CheckBox(this);
            box.setText(child.getContent());
            box.setId(displayedList.getElements().indexOf(child));
            box.setOnClickListener(element_check);
            box.setChecked(child.isFinished());
            container.addView(box);
        }
    }

    @Override
    public void onBackPressed(){
        if(this.displayedList != null) {
            setDisplayedList(null);
        }else{
            super.onBackPressed();
        }
    }
}