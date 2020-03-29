package com.example.list.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.list.R;
import com.example.list.databaseAccess.AccessLists;
import com.example.list.model.*;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private AccessLists accessLists;
    private ArrayList<List> lists;
    private EditText newListTitle;
    private LinearLayout container;

    private View.OnClickListener list_selection = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            TextView b = (TextView)v;
            List selected = lists.get(b.getId());

            Intent i = new Intent(MainActivity.this, ElementsActivity.class);
            i.putExtra("selectedList", selected);
            startActivity(i);
        }
    };

    private EditText.OnEditorActionListener submit = new EditText.OnEditorActionListener(){
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        String title = newListTitle.getText().toString();

        if (actionId == EditorInfo.IME_ACTION_DONE && !title.trim().isEmpty()) {
            List newList = new List(title, lists.size());
            accessLists.insertList(newList, lists.size());

            loadLists();
            return true;
        }else{
            newListTitle.setText("");
            return false;
        }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.container = findViewById(R.id.list_container);
        this.accessLists = new AccessLists(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLists();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_overflow_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        switch (id){
            case R.id.main_menu_new:
        newListTitle = new EditText(MainActivity.this);
        newListTitle.setHint(R.string.list_title);
        newListTitle.setBackground(null);
        newListTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        newListTitle.setSingleLine();
        newListTitle.setOnEditorActionListener(submit);
        container.addView(newListTitle);
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadLists(){
        this.lists = accessLists.getLists();
        container.removeAllViews();

        for (List list : this.lists){
            TextView view = new TextView(this);
            view.setText(list.getTitle());
            view.setId(this.lists.indexOf(list));
            view.setOnClickListener(list_selection);
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            view.setTextColor(Color.BLACK);
            container.addView(view);
        }
    }
}