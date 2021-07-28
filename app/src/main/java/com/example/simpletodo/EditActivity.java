package com.example.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    //Reference variables to all view components in activity_edit
    EditText etItem;
    Button btnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //Grab references to the views in the layout
        etItem = findViewById(R.id.etItem);
        btnSave = findViewById(R.id.btnSave);

        //Make the title of the activity more descriptive
        getSupportActionBar().setTitle("Edit item");

        //Handle passed data
        etItem.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));

        //When the user is done editing, they click the save button
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create an intent which will contain the results
                Intent intent = new Intent();

                //Pass the data (results of editing)
                intent.putExtra(MainActivity.KEY_ITEM_TEXT, etItem.getText().toString());
                intent.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));

                //Set the result of the intent
                setResult(RESULT_OK, intent);

                //Finish activity, close the screen and go back
                finish();
            }
        });

    }
}