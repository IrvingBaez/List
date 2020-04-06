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
import com.example.list.compoundComponents.TagView;
import com.example.list.databaseAccess.AccessLists;
import com.example.list.databaseAccess.AccessTags;
import com.example.list.model.EasyList;

public class ListDetailsActivity extends AppCompatActivity implements ConfirmDialog.ConfirmDialogListener {
    private EasyList list;
    private EditText listName;
    private TextView numberFinished;
    private LinearLayout tagContainer;
    private AccessTags accessTags;
    private AccessLists accessLists;
    private EditText newTagName;

    EditText.OnEditorActionListener submit = new EditText.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            String name = newTagName.getText().toString();

            if (actionId == EditorInfo.IME_ACTION_DONE && !name.trim().isEmpty()) {
                accessTags.insertTag(list, name);

                fillTags();
                return true;
            }else{
                newTagName.setText("");
                return false;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_details);
        this.list = getIntent().getParcelableExtra("list");
        this.accessLists = new AccessLists(this);
        this.accessTags = new AccessTags(this);

        this.listName = findViewById(R.id.activity_list_details_title);
        this.numberFinished = findViewById(R.id.activity_list_details_fraction);
        this.tagContainer = findViewById(R.id.activity_list_details_tags);

        this.accessLists.setListChildren(list);
        this.listName.setText(this.list.getTitle());
        this.numberFinished.setText(list.getFinishedCount() + "/" + list.getElements().size() +
                getResources().getString(R.string.number_elements_finished));
    }

    @Override
    protected void onResume(){
        super.onResume();
        fillTags();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_list_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        switch (id){
            case R.id.menu_list_details_newTag:
                newTagName = new EditText(this);
                newTagName.setHint(R.string.tag_name);
                newTagName.setBackground(null);
                newTagName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                newTagName.setSingleLine();
                newTagName.setOnEditorActionListener(submit);
                tagContainer.addView(newTagName);
                break;
            case R.id.menu_list_details_delete:
                ConfirmDialog confirm = new ConfirmDialog(R.string.confirm, R.string.wish_delete_list, R.string.no, R.string.yes);
                confirm.show(getSupportFragmentManager(), "confirm");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void fillTags(){
        tagContainer.removeAllViews();
        String[] tags = accessTags.getListTags(this.list);

        for(String t : tags){
            tagContainer.addView(new TagView(this, list, t));
        }
    }

    @Override
    public void onYesClicked() {
        (new AccessLists(this)).deleteList(list);

        Intent i = new Intent(this, ListsActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        list.setTitle(this.listName.getText().toString());
        (new AccessLists(this)).updateList(list);
        super.onBackPressed();
    }
}
