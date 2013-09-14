package com.wiredin.anagramsolver;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.wiredin.anagramsolver.db.DataAdapter;
import com.wiredin.anagramsolver.unscramble.Unscramble;
import com.wiredin.anagramsolver.util.Dawg;
import com.wiredin.anagramsolver.util.Unscrambler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends ListActivity implements View.OnClickListener{

    //List to contain unscrambled words
    private List<String> wordList = new ArrayList<String>();
    private Button submitButton;

    private EditText inputBox;
    private DataAdapter mDbHelper;

    private Unscramble unscramble;

    //String adapter for wordList
    private ArrayAdapter<String> listAdapter;
    Dawg dawg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(this);

        inputBox = (EditText) findViewById(R.id.editText);

        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, wordList);
        setListAdapter(listAdapter);

        mDbHelper = new DataAdapter(this.getApplicationContext());
        mDbHelper.createDatabase();
        mDbHelper.open();
        try {
            dawg = new Dawg(this.getApplicationContext(), listAdapter, wordList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        unscramble = new Unscrambler(mDbHelper);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitButton:
                if(inputBox != null) {
                    submitEvent(inputBox.getText().toString());
                }
               // listAdapter.notifyDataSetChanged();
                Log.d("MainActivity", "Submit");
                break;
        }
    }

    private void submitEvent(String input) {
      // unscramble.findWords(input, wordList, listAdapter);
        try {
            wordList.clear();
            String output = dawg.anagram(input);

         //   wordList.add(output);
            listAdapter.notifyDataSetChanged();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
