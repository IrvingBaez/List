package com.example.list.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.list.R;
import com.example.list.databaseAccess.DatabaseAccess;
import com.example.list.model.*;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private DatabaseAccess databaseAccess;
    private ArrayList<List> lists;
    private TextView title;
    private LinearLayout container;

    private View.OnClickListener list_selection = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Button b = (Button)v;
            List selected = lists.get(b.getId());

            Intent i = new Intent(MainActivity.this, ElementsActivity.class);
            i.putExtra("selectedList", selected);
            startActivity(i);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.title = findViewById(R.id.txt_title_main);
        this.container = findViewById(R.id.list_container);
        this.databaseAccess = DatabaseAccess.getInstance(this.getApplicationContext());
        loadLists();
    }

    private void loadLists(){
        this.lists = databaseAccess.getLists();
        title.setText(R.string.available_lists);
        container.removeAllViews();

        for (List list : this.lists){
            Button button = new Button(this);
            button.setText(list.getTitle());
            button.setId(this.lists.indexOf(list));
            button.setOnClickListener(list_selection);
            container.addView(button);
        }
    }
}