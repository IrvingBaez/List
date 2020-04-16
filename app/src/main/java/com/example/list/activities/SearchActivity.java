package com.example.list.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.list.R;
import com.example.list.compoundComponents.ElementView;
import com.example.list.databaseAccess.AccessElements;
import com.example.list.model.EasyList;
import com.example.list.model.ListElement;
import com.example.list.util.KeyboardUtils;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private EasyList list;
    private EditText searchText;
    private LinearLayout searchResult;

    private AccessElements accessElements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        this.list = getIntent().getParcelableExtra("list");
        this.accessElements = new AccessElements(this);

        this.searchText = findViewById(R.id.activity_search_text);
        this.searchText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        this.searchText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        this.searchResult = findViewById(R.id.activity_search_result);

        this.searchText.addTextChangedListener(searchText_changed);
        this.searchText.setOnKeyListener(backspace_pressedOnSearch);
    }

    TextWatcher searchText_changed = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            searchResult.removeAllViews();
            if(searchText.getText().toString().trim().isEmpty())
                return;

            ArrayList<ListElement> result = accessElements.search(list, searchText.getText().toString());

            ArrayList<ListElement> resultUnique = new ArrayList<>();
            for(ListElement element : result){
                if(!resultUnique.contains(element))
                    resultUnique.add(element);
            }

            for(ListElement element : resultUnique){
                searchResult.addView(new ElementView(SearchActivity.this, element));
            }
        }
    };

    View.OnKeyListener backspace_pressedOnSearch = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            EditText view = (EditText)v;
            if(keyCode == KeyEvent.KEYCODE_DEL && view.getText().toString().isEmpty()){
                KeyboardUtils.hideKeyboard(SearchActivity.this);
                SearchActivity.super.onBackPressed();
            }
            return false;
        }
    };
}