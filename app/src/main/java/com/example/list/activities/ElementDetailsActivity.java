package com.example.list.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.list.R;
import com.example.list.compoundComponents.ConfirmDialog;
import com.example.list.compoundComponents.CustomSpinnerAdapter;
import com.example.list.compoundComponents.ElementView;
import com.example.list.databaseAccess.AccessElements;
import com.example.list.databaseAccess.AccessTags;
import com.example.list.model.ListElement;
import com.example.list.util.KeyboardUtils;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class ElementDetailsActivity extends AppCompatActivity
        implements ConfirmDialog.ConfirmDialogListener, AdapterView.OnItemSelectedListener {
    private ListElement element;
    private AccessTags accessTags;
    private AccessElements accessElements;

    private EditText title;
    private CheckBox checkBox;
    private EditText description;
    private MultiAutoCompleteTextView tags;
    private Button newElement;
    private LinearLayout subList;
    private Spinner colorSpinner;

    TextWatcher title_changed = new TextWatcher() {
        int selection;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            this.selection = title.getSelectionStart();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String currentTitle = title.getText().toString();
            if(currentTitle.contains(String.valueOf('\n'))) {
                title.setText(currentTitle.replace('\n', '\0'));
                title.setSelection(selection);
                KeyboardUtils.hideKeyboard(ElementDetailsActivity.this);
            }
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

        this.title = findViewById(R.id.activity_element_details_name);
        this.checkBox = findViewById(R.id.activity_element_details_checkbox);
        this.description = findViewById(R.id.activity_element_details_description);
        this.tags = findViewById(R.id.activity_element_details_tags);
        this.newElement = findViewById(R.id.activity_element_details_newElement_btn);
        this.subList = findViewById(R.id.activity_element_details_sublist);
        this.colorSpinner = findViewById(R.id.activity_element_details_colorSpinner);

        String[] colors = {"Red", "Orange", "Yellow", "Green", "Blue"};
        int[] icons = {android.R.drawable.btn_star, android.R.drawable.btn_star,
                android.R.drawable.btn_star, android.R.drawable.btn_star,android.R.drawable.btn_star};

        CustomSpinnerAdapter customSpinnerAdapter =
                new CustomSpinnerAdapter(getApplicationContext(), icons, colors);
        colorSpinner.setAdapter(customSpinnerAdapter);

        colorSpinner.setOnItemSelectedListener(this);
        title.addTextChangedListener(title_changed);

        this.fillFields();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_element_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        switch (id){
            case R.id.menu_element_details_delete:
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
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        element.setContent(title.getText().toString());
        element.setFinished(checkBox.isChecked());
        element.setDescription(description.getText().toString());
        accessElements.updateElement(element);

        saveTags();
        super.onBackPressed();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}