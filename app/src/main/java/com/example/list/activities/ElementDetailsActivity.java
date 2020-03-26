package com.example.list.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.example.list.R;
import com.example.list.databaseAccess.DatabaseAccess;
import com.example.list.model.Element;

public class ElementDetailsActivity extends AppCompatActivity {
    private Element element;
    private DatabaseAccess databaseAccess;
    private EditText title;
    private CheckBox checkBox;
    private EditText description;
    private MultiAutoCompleteTextView tags;
    private Button newElement;
    private LinearLayout subList;
    private Button done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_element_details);

        this.databaseAccess = DatabaseAccess.getInstance(this);

        this.title = findViewById(R.id.details_title);
        this.checkBox = findViewById(R.id.details_checkbox);
        this.description = findViewById(R.id.details_description);
        this.tags = findViewById(R.id.details_tags);
        this.newElement = findViewById(R.id.details_newElement);
        this.subList = findViewById(R.id.details_sublist);
        this.done = findViewById(R.id.details_done);

        this.element = getIntent().getParcelableExtra("element");
        this.title.setText(this.element.getContent());
        this.checkBox.setChecked(this.element.isFinished());
        this.description.setText(this.element.getDescription());

        String[] tagsFound = databaseAccess.getListTags(this.element.getParent());
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tagsFound);
        tags.setAdapter(adapter);
        tags.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        //Fill sublist pending.
    }
}
