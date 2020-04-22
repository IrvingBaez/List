package com.example.list.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.list.R;
import com.example.list.compoundComponents.ColorChooser;
import com.example.list.compoundComponents.ConfirmDialog;
import com.example.list.compoundComponents.ElementView;
import com.example.list.compoundComponents.TagView;
import com.example.list.databaseAccess.AccessElements;
import com.example.list.databaseAccess.AccessTags;
import com.example.list.model.ListElement;
import com.example.list.util.KeyboardUtils;

import java.util.ArrayList;

public class ElementDetailsActivity extends AppCompatActivity
        implements ConfirmDialog.ConfirmDialogListener, TagView.TagViewListener {
    private ListElement element;
    private AccessTags accessTags;
    private AccessElements accessElements;

    private EditText title;
    private CheckBox checkBox;
    private EditText description;
    private EditText newTags;
    private LinearLayout tags;
    private TextView colorLabel;
    private ColorChooser chooser;
    private TextView subListLabel;
    private Button newElement;
    private LinearLayout subList;

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
        this.newTags = findViewById(R.id.activity_element_details_tags);
        this.tags = findViewById(R.id.activity_element_details_tags_view);
        this.colorLabel = findViewById(R.id.activity_element_details_colorLabel);
        this.chooser = findViewById(R.id.activity_element_details_colorChooser);
        this.subListLabel = findViewById(R.id.activity_element_details_sublist_lbl);
        this.newElement = findViewById(R.id.activity_element_details_newElement_btn);
        this.subList = findViewById(R.id.activity_element_details_sublist);

        //To later addition.
        colorLabel.setVisibility(View.GONE);
        chooser.setVisibility(View.GONE);
        subListLabel.setVisibility(View.GONE);
        newElement.setVisibility(View.GONE);
        subList.setVisibility(View.GONE);

        title.addTextChangedListener(title_changed);
        newTags.setSingleLine();
        newTags.setOnEditorActionListener(tag_submit);

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

        //Filling tags.
        this.tags.removeAllViews();

        ArrayList<String> elementTags = new ArrayList<>();
        for(String tag : this.element.getTags()){
            elementTags.add(tag);
            TagView tagView = new TagView(this, element, tag);
            tagView.setButtonAction(TagView.ButtonAction.DELETE);
            this.tags.addView(tagView);
        }

        for(String tag : accessTags.getListTags(element.getParent())){
            if(!elementTags.contains(tag)){
                TagView tagView = new TagView(this, element, tag);
                tagView.setButtonAction(TagView.ButtonAction.ADD);
                this.tags.addView(tagView);
            }
        }

        //Filling sublist.
        accessElements.setChildrenToElement(this.element);
        for(ListElement e : this.element.getChildrenElements()){
            this.subList.addView(new ElementView(this, e));
        }
    }

    EditText.OnEditorActionListener tag_submit = new EditText.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if(actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE){
                String tag = newTags.getText().toString().trim();

                if(!tag.isEmpty()) {
                    accessTags.addTag(element, tag);
                    accessTags.setTags(element);
                    ElementDetailsActivity.this.fillFields();

                    newTags.setText("");
                    KeyboardUtils.hideKeyboard(ElementDetailsActivity.this);
                    return true;
                }
            }

            return false;
        }
    };

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

        super.onBackPressed();
    }

    @Override
    public void onButtonPressed(TagView tagView) {
        accessTags.setTags(this.element);
        this.fillFields();
    }
}