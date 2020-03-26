package com.example.list.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.list.R;
import com.example.list.databaseAccess.DatabaseAccess;
import com.example.list.model.Element;
import com.example.list.model.List;
import com.example.list.compoundComponents.ElementView;

public class ElementsActivity extends AppCompatActivity {
    private List displayedList;
    private DatabaseAccess databaseAccess;

    private LinearLayout container;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elements);

        this.container = findViewById(R.id.element_container);
        this.title = findViewById(R.id.txt_title_elementView);

        this.displayedList = getIntent().getParcelableExtra("selectedList");
        this.databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
        loadElements();
    }

    private void loadElements() {
        title.setText(displayedList.getTitle());

        if(displayedList.isEmpty())
            databaseAccess.setListChildren(displayedList);

        for(Element child : displayedList.getElements()) {
            ElementView view = new ElementView(this, child);
            container.addView(view);
        }
    }
}
