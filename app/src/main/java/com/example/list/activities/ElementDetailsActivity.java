package com.example.list.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.example.list.R;
import com.example.list.compoundComponents.ConfirmDialog;
import com.example.list.compoundComponents.ElementView;
import com.example.list.databaseAccess.AccessElements;
import com.example.list.databaseAccess.AccessTags;
import com.example.list.model.ListElement;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class ElementDetailsActivity extends AppCompatActivity implements ConfirmDialog.ConfirmDialogListener {
    private ListElement element;
    private AccessTags accessTags;
    private AccessElements accessElements;

    private EditText title;
    private CheckBox checkBox;
    private EditText description;
    private MultiAutoCompleteTextView tags;
    private Button newElement;
    private LinearLayout subList;
    private Button done;

    View.OnClickListener done_click = new View.OnClickListener() {
        @Override
        public void onClick(View sender) {
            element.setContent(title.getText().toString());
            element.setFinished(checkBox.isChecked());
            element.setDescription(description.getText().toString());
            accessElements.updateElement(element);

            saveTags();
            ElementDetailsActivity.this.onBackPressed();
        }
    };

    View.OnClickListener newElement_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Create new element.
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_element_details);

        this.accessTags = new AccessTags(this);
        this.accessElements = new AccessElements(this);

        this.element = getIntent().getParcelableExtra("element");
        this.setTitle(element.getContent());

        this.title = findViewById(R.id.details_title);
        this.checkBox = findViewById(R.id.details_checkbox);
        this.description = findViewById(R.id.details_description);
        this.tags = findViewById(R.id.details_tags);
        this.newElement = findViewById(R.id.details_newElement);
        this.subList = findViewById(R.id.details_sublist);
        this.done = findViewById(R.id.details_done);

        this.fillFields();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.element_details_overflow_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        switch (id){
            case R.id.element_details_menu_delete:
                ConfirmDialog confirm = new ConfirmDialog(R.string.confirm, R.string.wish_delete_list, R.string.no, R.string.yes);
                confirm.show(getSupportFragmentManager(), "confirm");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void fillFields(){
        //Filling text editors.
        this.title.setText(this.element.getContent());
        this.checkBox.setChecked(this.element.isFinished());
        this.description.setText(this.element.getDescription());

        //Filling MultiAutoCompleteTextView Text
        String registeredTags = "";
        for(String s : element.getTags()){
            registeredTags += s + ", ";
        }
        this.tags.setText(registeredTags);

        //Filling MultiAutoCompleteTextView Suggestions
        String[] listTags = accessTags.getListTags(this.element.getParent());
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listTags);
        tags.setAdapter(adapter);
        tags.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        //Filling sublist.
        accessElements.setChildrenToElement(this.element);
        for(ListElement e : this.element.getChildrenElements()){
            this.subList.addView(new ElementView(this, e));
        }

        //Listeners
        this.done.setOnClickListener(done_click);
        this.newElement.setOnClickListener(newElement_click);
    }

    private void saveTags(){
        StringTokenizer tokenizer = new StringTokenizer(this.tags.getText().toString(), ",");
        ArrayList<String> tagList = new ArrayList<>();

        while(tokenizer.hasMoreTokens()){
            String tag = tokenizer.nextToken();
            tag = tag.replace('\n', '\0').replace(',', '\0').trim();
            tagList.add(tag);
        }

        ArrayList<String> uniqueTags = new ArrayList<>();
        for(String s : tagList){
            if(!uniqueTags.contains(s) && s.length() > 0)
                uniqueTags.add(s);
        }

        String[] tagArray = new String[uniqueTags.size()];
        for(int i = 0; i < uniqueTags.size(); i++){
            tagArray[i] = uniqueTags.get(i);
        }

        accessTags.insertTag(this.element, tagArray);
    }

    @Override
    public void onYesClicked() {
        accessElements.deleteElement(element);
        ElementDetailsActivity.this.onBackPressed();
    }
}