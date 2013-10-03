package com.wiredin.anagramsolver.screens;

import android.app.ActivityOptions;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.wiredin.anagramsolver.R;
import com.wiredin.anagramsolver.util.Dawg;
import com.wiredin.anagramsolver.util.Util;
import com.wiredin.anagramsolver.webservice.AsyncWebService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MainScreen extends ListActivity implements View.OnClickListener, SearchView.OnQueryTextListener {

    private AdView adView;
    private List<String> wordList = new ArrayList<String>();
    private Button submitButton;
    private ImageButton clearEditField;
    private CharSequence querySequence;
    private SearchView searchView;
    private ArrayAdapter<String> listAdapter;
    private Dawg dawg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            boolean b = savedInstanceState.containsKey("rotationIssued");
            if (Boolean.TRUE.equals(b)) {
                querySequence = savedInstanceState.getCharSequence("searchViewQuery");
                // searchView.setQuery(querySequence, false);
                wordList = savedInstanceState.getStringArrayList("wordList");
            }
        }


        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, wordList);
        setListAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();


        AdRequest adRequest = new AdRequest();
        adRequest.addTestDevice("B0FAD6AAEEC389421519822D4A0FF35C");

        adView = new AdView(this, AdSize.BANNER, "ca-app-pub-9081322137461264/4444200032");
        adView.loadAd(adRequest);

        try {
            dawg = new Dawg(this, wordList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("oncreateoptions", wordList.toString());
    }

    private void submitEvent(final String input) {
        try {
            wordList.clear();
            HashMap<Integer, List<String>> wordMap = dawg.anagram(input);
            List<Integer> set = new ArrayList<Integer>(wordMap.keySet());
            Collections.sort(set);
            for(Integer i : set) {
                wordList.addAll(wordMap.get(i));
            }

            if(wordList.isEmpty()) {
                wordList.add("No matches found!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbar_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        searchView = (SearchView) searchItem.getActionView();
        setupSearchView(searchItem);

        if (querySequence != null) {
            Log.d("querySequence", querySequence.toString());
            searchView.setQuery(querySequence, false);
            searchView.setIconified(false);
        }


        searchView.clearFocus();
        Log.d("oncreateoptions", "CREATEOPTIONS");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                 Intent i = new Intent(this, SettingsActivity.class);
                 startActivity(i);
               // searchView.setQuery(querySequence, true);
                break;
            case R.id.menu_feedback:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"wiredin.dev@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "WordFinder - Feedback");
                intent.putExtra(Intent.EXTRA_TEXT, "Developer information: " + Build.VERSION.RELEASE + " " + Build.VERSION.SDK_INT + " " + Build.VERSION.INCREMENTAL);
                startActivity(Intent.createChooser(intent, ""));
                break;

        }

        return true;
    }

    private void setupSearchView(MenuItem searchItem) {

        if (isAlwaysExpanded()) {
            searchView.setIconifiedByDefault(false);
        } else {
            searchItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
        //searchView.setInputType(InputType.);
        searchView.setQueryHint("Solve!");
        searchView.setOnQueryTextListener(this);

    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
        Log.d("oncreateoptions", "destroy");
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        if (searchView != null) {
            submitEvent(Util.sanitize(searchView.getQuery().toString()));
            searchView.clearFocus();

            return true;
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    protected boolean isAlwaysExpanded() {
        return false;
    }

    @Override
    public void onClick(View view) {
        Log.d("oncreateoptions", view.getId() + "%%%");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("rotationIssued", true);
        savedInstanceState.putCharSequence("searchViewQuery", searchView.getQuery());
        savedInstanceState.putStringArrayList("wordList", (ArrayList) wordList);
        Log.d("oncreateoptions", "SAVED");

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
//        searchView.setQuery(savedInstanceState.getCharSequence("searchViewQuery"), false);
        Log.d("oncreateoptions RESTORE", savedInstanceState.getCharSequence("searchViewQuery").toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("oncreateoptions", "ONRESUME");
    }

    @Override
    public void onBackPressed() {
        Log.d("oncreateoptions", "back pressed");
        if (!searchView.isIconified()) {
            querySequence = searchView.getQuery();
            searchView.setQuery("", false);
            searchView.setIconified(true);
            searchView.setQuery(querySequence, false);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);



        Intent definition_activity = new Intent(MainScreen.this, DefinitionScreen.class);

        Bundle bundle = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.definition_anim, R.anim.definition_anim_2).toBundle();
        bundle.putString("USER_INPUT_CLEAN", Util.sanitize(wordList.get(position)));

        definition_activity.putExtras(bundle);
        startActivity(definition_activity, bundle);

    }

}
