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
import com.example.list.compoundComponents.ConfirmDialog;
import com.example.list.compoundComponents.GenericRecyclerView;
import com.example.list.databaseAccess.AccessElements;
import com.example.list.databaseAccess.AccessLists;
import com.example.list.model.ListElement;
import com.example.list.model.EasyList;
import com.example.list.compoundComponents.ElementView;
import com.example.list.util.KeyboardUtils;

import java.util.ArrayList;
import java.util.Collections;

public class ElementsActivity extends AppCompatActivity
        implements ConfirmDialog.ConfirmDialogListener, GenericRecyclerView.GenericRecyclerViewListener {
    private EasyList list;
    private AccessLists accessLists;
    private AccessElements accessElements;
    private GenericRecyclerView<ListElement> elementRecyclerView;
    private EditText newElementName;
    private ListElement deletedElement;

    private Operation confirmOperation;

    private ArrayList<ListElement> elements;

    private enum Operation {DELETE_LIST, DELETE_FINISHED};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elements);

        this.elementRecyclerView = findViewById(R.id.activity_elements_container_unfinished);
        this.elementRecyclerView.setViewExample(new ElementView(this));

        this.newElementName = findViewById(R.id.activity_elements_newElement);
        this.newElementName.setOnEditorActionListener(submit);
        this.newElementName.setOnKeyListener(backspace_pressed);
        this.newElementName.setImeOptions(EditorInfo.IME_ACTION_DONE);
        this.newElementName.setRawInputType(InputType.TYPE_CLASS_TEXT);

        this.list = getIntent().getParcelableExtra("list");
        this.accessLists = new AccessLists(this);
        this.accessElements = new AccessElements(this);

        elements = new ArrayList<>();
        this.elementRecyclerView.setDataList(elements);
        this.refreshData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ElementView.setElementTextClickable(true);

        accessLists.updateInstance(list);
        this.setTitle(list.getTitle());
        refreshData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_elements, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        ConfirmDialog confirm;
        Intent i;
        switch (item.getItemId()){
            case R.id.menu_elements_delete:
                this.confirmOperation = Operation.DELETE_LIST;
                confirm = new ConfirmDialog(R.string.confirm, R.string.wish_delete_list, R.string.no, R.string.yes);
                confirm.show(getSupportFragmentManager(), "confirm");
                break;
            case R.id.menu_elements_details:
                i = new Intent(this, ListDetailsActivity.class);
                i.putExtra("list", list);
                startActivity(i);
                break;
            case R.id.menu_elements_sort_alphabetical:
                list.setCompareMode(EasyList.CompareMode.ALPHABETICAL);
                accessLists.updateList(list);
                sortData();
                break;
            case R.id.menu_elements_sort_chronological:
                list.setCompareMode(EasyList.CompareMode.CHRONOLOGICAL);
                accessLists.updateList(list);
                sortData();
                break;
            case R.id.menu_elements_sort_custom:
                list.setCompareMode(EasyList.CompareMode.CUSTOM);
                accessLists.updateList(list);
                sortData();
                break;
            case R.id.menu_elements_recycle_finished:
                for(ListElement element : list.getElements()){
                    element.setFinished(false);
                    accessElements.updateElement(element);
                }
                sortData();
                break;
            case R.id.menu_elements_delete_finished:
                this.confirmOperation = Operation.DELETE_FINISHED;
                confirm = new ConfirmDialog(R.string.confirm, R.string.wish_delete_finishedElements, R.string.no, R.string.yes);
                confirm.show(getSupportFragmentManager(), "confirm");
                break;
            case R.id.menu_elements_search:
                i = new Intent(this, SearchActivity.class);
                i.putExtra("list", list);
                startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClearView() {
        int customIndex = 0;

        for(ListElement element : elements){
            element.setCustomIndex(customIndex++);
            accessElements.updateElement(element);
        }

        list.setCompareMode(EasyList.CompareMode.CUSTOM);
        accessLists.updateList(list);
        sortData();
    }

    public void refreshData() {
        this.elements.clear();
        this.accessLists.setListChildren(list);
        this.elements.addAll(list.getElements());
        this.elementRecyclerView.notifyDataChange();
        this.sortData();
    }

    public void sortData(){
        list.sortElements();
        Collections.sort(elements);
        ArrayList<ListElement> finishedList = new ArrayList<>();

        for(ListElement child : list.getElements()) {
            if(child.isFinished()){
                elements.remove(child);
                finishedList.add(child);
            }
        }

        elements.addAll(finishedList);

        this.elementRecyclerView.notifyDataChange();
    }

    @Override
    public void onYesClicked() {
        switch(this.confirmOperation){
            case DELETE_LIST:
                accessLists.deleteList(list);
                super.onBackPressed();
                break;
            case DELETE_FINISHED:
                int count = list.getElements().size();
                for(int e = 0; e < count; e++){
                    ListElement child = list.getElements().get(e);
                    if(child.isFinished()){
                        accessElements.deleteElement(child);
                        count--;
                        e--;
                    }
                }
                refreshData();
                break;
        }
        this.confirmOperation = null;
    }

    /**
     * Creates the new element when Intro is pressed on EditText newElementName.
     */
    EditText.OnEditorActionListener submit = new EditText.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            String name = newElementName.getText().toString();

            if(name.trim().isEmpty()){
                newElementName.setText("");
                return false;
            }

            if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {
                ListElement newElement = new ListElement(list, name, null);
                accessElements.insertElement(newElement);
                refreshData();
                newElementName.setText("");
                return true;
            }else{
                newElementName.setText("");
                return false;
            }
        }
    };

    /**
     * Hides keyboard when backspace is pressed on EditText.
     */
    View.OnKeyListener backspace_pressed = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if(keyCode == KeyEvent.KEYCODE_DEL){
                KeyboardUtils.hideKeyboard(ElementsActivity.this);
            }
            return false;
        }
    };
}