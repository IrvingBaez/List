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
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.list.R;
import com.example.list.databaseAccess.AccessLists;
import com.example.list.model.*;
import com.example.list.util.KeyboardUtils;

import java.util.ArrayList;

public class ListsActivity extends AppCompatActivity {
    private AccessLists accessLists;
    private ArrayList<EasyList> lists;
    private EditText newListTitle;
    private LinearLayout listContainer;

    private Button experimental;

    private View.OnClickListener list_selection = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            TextView b = (TextView)v;
            EasyList selected = lists.get(b.getId());

            Intent i = new Intent(ListsActivity.this, ElementsActivity.class);
            i.putExtra("selectedList", selected);
            startActivity(i);
        }
    };

    private EditText.OnEditorActionListener submit = new EditText.OnEditorActionListener(){
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        String title = newListTitle.getText().toString();

        if (actionId == EditorInfo.IME_ACTION_DONE && !title.trim().isEmpty()) {
            EasyList newList = new EasyList(title, lists.size());
            accessLists.insertList(newList, lists.size());

            loadLists();
            return true;
        }else{
            newListTitle.setText("");
            return false;
        }
        }
    };

    View.OnKeyListener backspace_pressed = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if(keyCode == KeyEvent.KEYCODE_DEL){
                KeyboardUtils.hideKeyboard(ListsActivity.this);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        this.listContainer = findViewById(R.id.activity_lists_container);
        this.accessLists = new AccessLists(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLists();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_lists, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        switch (id){
            case R.id.menu_lists_settings:
                //Open settings activity.
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadLists(){
        this.lists = accessLists.getLists();
        listContainer.removeAllViews();

        for (EasyList list : this.lists){
            TextView view = new TextView(this);
            view.setText(list.getTitle());
            view.setId(this.lists.indexOf(list));
            view.setOnClickListener(list_selection);
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
            view.setTextColor(Color.BLACK);
            listContainer.addView(view);
        }

        newListTitle = new EditText(ListsActivity.this);
        newListTitle.setHint(R.string.list_title);
        newListTitle.setBackground(null);
        newListTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        newListTitle.setSingleLine();
        newListTitle.setOnEditorActionListener(submit);
        newListTitle.setOnKeyListener(backspace_pressed);
        listContainer.addView(newListTitle);
    }
}