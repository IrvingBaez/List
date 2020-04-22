package com.example.list.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.list.R;
import com.example.list.compoundComponents.GenericRecyclerView;
import com.example.list.compoundComponents.ListView;
import com.example.list.databaseAccess.AccessLists;
import com.example.list.model.*;
import com.example.list.util.KeyboardUtils;

import java.util.ArrayList;
import java.util.Collections;

public class ListsActivity extends AppCompatActivity implements GenericRecyclerView.GenericRecyclerViewListener {
    private AccessLists accessLists;
    private ArrayList<EasyList> lists;
    private EditText newListTitle;
    private GenericRecyclerView<EasyList> recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        this.newListTitle = findViewById(R.id.activity_list_new);
        this.newListTitle.setOnEditorActionListener(submit);
        this.newListTitle.setOnKeyListener(backspace_pressed);
        this.newListTitle.setImeOptions(EditorInfo.IME_ACTION_DONE);
        this.newListTitle.setRawInputType(InputType.TYPE_CLASS_TEXT);

        this.accessLists = new AccessLists(this);
        this.recycler = findViewById(R.id.activity_list_recycler);
        this.recycler.setViewExample(new ListView(this));
        this.lists = accessLists.getLists();
        Collections.sort(lists);
        this.recycler.setDataList(lists);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ListView.setListTextClickable(true);
        refreshLists();
    }

    @Override
    protected void onPause() {
        super.onPause();
        KeyboardUtils.hideKeyboard(this);
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
            case R.id.menu_lists_info:
                Intent i = new Intent(this, InfoActivity.class);
                startActivity(i);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshLists(){
        this.lists.clear();
        this.lists.addAll(accessLists.getLists());
        Collections.sort(lists);
        this.recycler.notifyDataChange();
    }

    @Override
    public void onClearView(){
        int i = 0;

        for(EasyList list : this.lists) {
            list.setCustomIndex(i++);
            accessLists.updateList(list);
        }
    }

    private EditText.OnEditorActionListener submit = new EditText.OnEditorActionListener(){
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            String title = newListTitle.getText().toString();

            if (actionId == EditorInfo.IME_ACTION_DONE && !title.trim().isEmpty()) {
                EasyList newList = new EasyList(title, lists.size());
                accessLists.insertList(newList, lists.size());
                newListTitle.setText("");

                refreshLists();
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
}