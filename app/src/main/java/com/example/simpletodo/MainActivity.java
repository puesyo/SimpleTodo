package com.example.simpletodo;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Constants
    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;

    //This is our Model Class. A list containing all the items in our to-do-list.
    List<String> items;

    //Reference variables to all view components in activity main
    Button btnAdd;
    EditText edItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Define reference variables
        btnAdd = findViewById(R.id.btnAdd);
        edItem = findViewById(R.id.edItem);
        rvItems = findViewById(R.id.rvItems);



        //Add items to the list
        loadItems();
//        items.add("Pay electric bill");
//        items.add("Buy pet food");
//        items.add("Clean office");


        //Remove an item
        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                //Delete the item from the adapter
                items.remove(position);
                //Notify the adapter
                itemsAdapter.notifyItemRemoved(position);
                //Inform the user they have removed an item
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        //Update an item
        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                //Create the new activity
                Intent i = new Intent(MainActivity.this, EditActivity.class);

                //Pass the data being edited
                i.putExtra(KEY_ITEM_TEXT, items.get(position));
                i.putExtra(KEY_ITEM_POSITION, position);
                //Display the activity
                startActivityForResult(i, EDIT_TEXT_CODE);
            }
        };



        //Create an Adapter object and pass the list of items in the todoList
        itemsAdapter = new ItemsAdapter(items, onLongClickListener, onClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        //Add event listeners
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Grab the content from the edit text
                String todoItem = edItem.getText().toString();

                //Add item to the model
                items.add(todoItem);

                //Notify adapter than an item was inserted
                itemsAdapter.notifyItemInserted(items.size() - 1);

                //Clear the edit text
                edItem.setText("");

                //Call toast (pop up dialog) to notify the user an item was inserted
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();

                //Save items to the list
                saveItems();
            }
        });

    }

    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }

    //This function will load items by reading every line of the data file
    private void loadItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }

    //This function saves items by writing them into the data file
    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }
    }

    //This function handles the result of the edit activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            // Retrieve the updated value
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);

            //Extract the original position of the edited item from the position key
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);

            //Update the model at the right position with the new item text
            items.set(position, itemText);

            //Notify the adapter
            itemsAdapter.notifyItemChanged(position);

            //Persist the changes
            saveItems();
            Toast.makeText(getApplicationContext(), "Item updated successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }
    }
}