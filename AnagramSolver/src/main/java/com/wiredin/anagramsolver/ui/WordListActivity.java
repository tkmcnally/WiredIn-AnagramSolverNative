package com.wiredin.anagramsolver.ui;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Outline;
import android.media.Image;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.support.v7.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.wiredin.anagramsolver.R;
import com.wiredin.anagramsolver.adapters.WordListAdapter;
import com.wiredin.anagramsolver.listeners.NotifyDialogListener;
import com.wiredin.anagramsolver.models.WordItem;
import com.wiredin.anagramsolver.ui.fragments.SortDialogFragment;
import com.wiredin.anagramsolver.util.Dawg;
import com.wiredin.anagramsolver.util.Util;
import com.wiredin.anagramsolver.util.WordListComparators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Thomas on 8/7/2014.
 */
public class WordListActivity extends ActionBarActivity implements NotifyDialogListener {

    private ArrayList<WordItem> wordList;
    private Dawg dawg;
    private SearchView searchView;
    private RecyclerView rView;
    private RecyclerView.Adapter adapter;

    private ImageButton sortingModeButton, sortingDirectionButton;
    private ArrayAdapter<CharSequence> spinnerAdapter;
    private  DialogFragment sortDialog;

    private String[] SORTING_MODES = new String[]{"Alphabetical", "Length", "Points"};

    @Override
    public void onCreate(Bundle savedBundle) {
        super.onCreate(savedBundle);
        setContentView(R.layout.word_list_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setClickable(true);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    return false;
                }

            });
        }


        RecyclerView rView = (RecyclerView) findViewById(R.id.word_list_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rView.setLayoutManager(mLayoutManager);

        if(wordList == null) {
            // Data set used by the adapter. This data will be displayed.
            wordList = new ArrayList<WordItem>();
        }
        try {
            dawg = new Dawg(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        adapter = new WordListAdapter(WordListActivity.this, wordList, clickListener);
        ((WordListAdapter) adapter).mSortModeOnClickListener = getSortModeClickListener();
        rView.setAdapter(adapter);

        rView.setItemAnimator(new DefaultItemAnimator());

        sortDialog = new SortDialogFragment();
        //bindSortingDirectionListeners();
        //bindSortingModeListeners();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbar_search, menu);
        //getSupportActionBar().setDisplayUseLogoEnabled(false);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        //SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        setupSearchView(searchItem);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void submitEvent(final String input) {
        try {
            wordList.clear();
            HashMap<Integer, List<WordItem>> wordMap = dawg.anagram(input);
            List<Integer> set = new ArrayList<Integer>(wordMap.keySet());
            wordList.add(new WordItem(0, searchView.getQuery() + ""));
            for(Integer i : set) {
                wordList.addAll(wordMap.get(i));
            }

            if(wordList.isEmpty()) {
                wordList.add(new WordItem(0, "No matches found!"));
            }

            sortWordList(((SortDialogFragment) sortDialog).getSelectedSortMode());
            setSortDirectionWordList(((SortDialogFragment) sortDialog).getSelectedSortDirection());
            adapter.notifyDataSetChanged();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupSearchView(MenuItem searchItem) {


        if(searchView != null) {
            searchView.setQueryHint("Solve!");
            searchView.setOnQueryTextListener(searchListener);
            searchView.clearFocus();

            searchView.setIconified(false);
        }

    }

    SearchView.OnQueryTextListener searchListener = new SearchView.OnQueryTextListener() {
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
    };

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!(view instanceof LinearLayout)) {
                RelativeLayout itemContainer = (RelativeLayout) view;
                TextView itemWord = (TextView) itemContainer.findViewById(R.id.word_list_item_text);
                TextView scoreView = (TextView) itemContainer.findViewById(R.id.word_list_heading_text_1);
                showDefinitionActivity(itemContainer, itemWord, scoreView);
            }
        }
    };

    public void showDefinitionActivity(RelativeLayout itemContainer, TextView textView, TextView scoreView) {
        Intent intent = new Intent();
        intent.setClass(this, DefinitionActivity.class);
        intent.putExtra("query", String.valueOf(textView.getText()));
        intent.putExtra("score", String.valueOf(scoreView.getText()));
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,
                new Pair<View, String>(
                        itemContainer.findViewById(R.id.word_list_item_text),
                        DefinitionActivity.VIEW_NAME_HEADER_TITLE));

        startActivity(intent, options.toBundle());
    }

    public void showSortDialog() {

        sortDialog.show(getSupportFragmentManager(), "SortDialogFragment");
    }


    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        sortWordList(((SortDialogFragment) dialog).getSelectedSortMode());
        setSortDirectionWordList(((SortDialogFragment) dialog).getSelectedSortDirection());
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    public void sortWordList(SortDialogFragment.SortMode sortMode) {

        switch (sortMode) {
            case POINTS:
                ((WordListAdapter) adapter).sort(WordListComparators.PointSort);
                break;
            case ALPHABETICAL:
                ((WordListAdapter) adapter).sort(WordListComparators.AlphabeticalSort);
                break;
            case LENGTH:
                ((WordListAdapter) adapter).sort(WordListComparators.LengthSort);
                break;
        }
    }

    public void setSortDirectionWordList(SortDialogFragment.SortDirection sortDirection) {
        switch (sortDirection) {
            case ASCENDING:
                Collections.reverse(wordList);
                wordList.add(0, wordList.get(wordList.size() - 1));
                wordList.remove(wordList.size() - 1);
                break;
            case DESCENDING:
                break;
        }
        adapter.notifyDataSetChanged();
    }

   /* public void bindSortingDirectionListeners() {


        if (findViewById(R.id.sorting_mode_spinner) != null) {
            ViewOutlineProvider outlineProvider = new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    int size = getResources().getDimensionPixelSize(R.dimen.fab_size);
                    outline.setOval(0, 0, size, size);
                }
            };

            findViewById(R.id.sortingDirectionButton).setOutlineProvider(outlineProvider);
            sortingDirectionButton = (ImageButton) findViewById(R.id.sortingDirectionButton);
            sortingDirectionButton.setTag(R.drawable.ic_action_sort_by_size);
            sortingModeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if((Integer) v.getTag() == R.drawable.ic_action_sort_by_size) {
                        ((ImageButton) v).setImageResource(R.drawable.ic_action_sort_by_size_inv);
                        setSortDirectionWordList(SortDialogFragment.SortDirection.SMALLEST);
                    } else {
                        ((ImageButton) v).setImageResource(R.drawable.ic_action_sort_by_size);
                        setSortDirectionWordList(SortDialogFragment.SortDirection.LARGEST);
                    }
                }
            });

        } else {
            Log.d("main", "CANNOT BIND SORTING MODE LISTENERS. IT IS NULL");
        }
    }
*/
    public View.OnClickListener getSortModeClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSortDialog();
            }
        };
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.d("SAVED", "CALLED");

        savedInstanceState.putParcelableArrayList("wordList", wordList);

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("RESTORED", "CALLED");
        wordList = savedInstanceState.getParcelableArrayList("wordList");
        adapter.notifyDataSetChanged();
    }



}
