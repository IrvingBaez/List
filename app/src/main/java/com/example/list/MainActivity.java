package com.example.list;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public EditText id;
    public Button query;
    public TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        id = findViewById(R.id.txt_index);
        query = findViewById(R.id.btn_query);
        result = findViewById(R.id.txt_result);
    }

    public void query(View view){
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();

        String index = id.getText().toString();
        String content = databaseAccess.getContent(index);

        result.setText(content);

        databaseAccess.close();
    }
}
