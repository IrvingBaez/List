package com.example.list.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.list.R;
import com.example.list.compoundComponents.ConfirmDialog;
import com.example.list.databaseAccess.AccessElements;
import com.example.list.databaseAccess.AccessLists;
import com.example.list.model.ListElement;
import com.example.list.model.List;
import com.example.list.compoundComponents.ElementView;

public class ElementsActivity extends AppCompatActivity implements ConfirmDialog.ConfirmDialogListener {
    private List displayedList;
    private AccessLists accessLists;
    private EditText newElementName;
    private LinearLayout container;

    EditText.OnEditorActionListener submit = new EditText.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            String name = newElementName.getText().toString();

            if (actionId == EditorInfo.IME_ACTION_DONE && !name.trim().isEmpty()) {
                ListElement newElement = new ListElement(displayedList, name, null);
                AccessElements accessElements = new AccessElements(ElementsActivity.this);
                accessElements.insertElement(newElement);

                loadElements();
                return true;
            }else{
                newElementName.setText("");
                return false;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elements);

        this.container = findViewById(R.id.element_container);

        this.displayedList = getIntent().getParcelableExtra("selectedList");
        this.setTitle(displayedList.getTitle());
        this.accessLists = new AccessLists(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadElements();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.list_overflow_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        switch (id){
            case R.id.list_menu_delete:
                ConfirmDialog confirm = new ConfirmDialog(R.string.confirm, R.string.wish_delete_list, R.string.no, R.string.yes);
                confirm.show(getSupportFragmentManager(), "confirm");
                break;
            case R.id.list_menu_new:
                newElementName = new EditText(this);
                newElementName.setHint(R.string.element_name);
                newElementName.setBackground(null);
                newElementName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                newElementName.setSingleLine();
                newElementName.setOnEditorActionListener(submit);
                container.addView(newElementName);
                break;
            case R.id.list_menu_details:
                Intent i = new Intent(this, ListDetailsActivity.class);
                i.putExtra("selectedList", displayedList);
                startActivity(i);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadElements() {
        container.removeAllViews();

        accessLists.setListChildren(displayedList);

        for(ListElement child : displayedList.getElements()) {
            container.addView(new ElementView(this, child));
        }
    }

    @Override
    public void onYesClicked() {
        accessLists.deleteList(displayedList);
        super.onBackPressed();
    }
}
