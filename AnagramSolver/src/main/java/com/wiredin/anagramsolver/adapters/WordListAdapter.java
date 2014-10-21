package com.wiredin.anagramsolver.adapters;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Outline;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wiredin.anagramsolver.R;
import com.wiredin.anagramsolver.models.WordItem;
import com.wiredin.anagramsolver.ui.WordListActivity;
import com.wiredin.anagramsolver.ui.fragments.SortDialogFragment;
import com.wordnik.client.model.WordList;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Thomas on 8/7/2014.
 */
public class WordListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEWTYPE_TITLE = 0;
    private static final int VIEWTYPE_WORD_HEADING = 1;
    private static final int VIEWTYPE_WORD_ITEM = 2;

    public ArrayList<WordItem> mDataset;
    private static Context mContext;

    public View.OnClickListener clickListener;

    public AdapterView.OnClickListener mSortModeOnClickListener;

    public WordListAdapter(Context context, ArrayList<WordItem> wordDataset, View.OnClickListener clickListener) {
        mDataset = wordDataset;
        mContext = context;
        this.clickListener = clickListener;
    }

    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mDataset.get(position).viewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh = null;
        switch(viewType) {
                case VIEWTYPE_TITLE: {
                    View v = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.word_list_title, parent, false);

                    vh = new TitleViewHolder(v, clickListener);
                    break;
                }
                case VIEWTYPE_WORD_HEADING: {
                    View v = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.word_list_heading, parent, false);

                    vh = new WordHeadingViewHolder(v, clickListener);
                    break;
                }
                case VIEWTYPE_WORD_ITEM: {

                    View v = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.word_list_item, parent, false);
                    // set the view's size, margins, paddings and layout parameters

                    vh = new WordItemViewHolder(v, clickListener);
                    break;
                }

        }

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof TitleViewHolder) {
            TitleViewHolder vHolder = (TitleViewHolder) holder;

            vHolder.mTextView1.setText("\"" + mDataset.get(position).text + "\"");
            vHolder.mTextView2.setText(mDataset.get(position).text.length() + " characters long");
            vHolder.mTextView3.setText(getItemCount() - 1 + " combinations");


        } else if(holder instanceof WordHeadingViewHolder) {
            WordHeadingViewHolder vHolder = (WordHeadingViewHolder) holder;

            vHolder.mTextView1.setText(mDataset.get(position).score() + "");
            vHolder.mTextView2.setText(mDataset.get(position).text);
        } else {
            WordItemViewHolder vHolder = (WordItemViewHolder) holder;
            vHolder.mTextView.setText(mDataset.get(position).text);
        }

    }

    public class TitleViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public Spinner mSpinner;
        public ArrayAdapter<CharSequence> mArrayAdapter;
        public TitleViewHolder(View v, View.OnClickListener clickListener) {
            super(v);
            v.setOnClickListener(clickListener);
            mTextView1 = (TextView) v.findViewById(R.id.word_list_title_text);
            mTextView2 = (TextView) v.findViewById(R.id.word_list_title_sub_text);
            mTextView3 = (TextView) v.findViewById(R.id.word_list_title_sub_text_2);
            bindSortingModeListeners(v);
           /* mSpinner = (Spinner) v.findViewById(R.id.sorting_mode_spinner);
            mArrayAdapter = new ArrayAdapter<CharSequence>(mContext, android.R.layout.simple_spinner_dropdown_item, WordListActivity.SORTING_MODES);
            mSpinner.setAdapter(mArrayAdapter);
            mSpinner.setOnItemSelectedListener(onItemSelectedListener);*/
        }
    }

    public class WordHeadingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTextView1;
        public TextView mTextView2;
        public WordHeadingViewHolder(View v, View.OnClickListener clickListener) {
            super(v);
            v.setOnClickListener(clickListener);
            mTextView1 = (TextView) v.findViewById(R.id.word_list_heading_text_1);
            mTextView2 = (TextView) v.findViewById(R.id.word_list_item_text);
        }

        @Override
        public void onClick(View view) {
            Log.d("Anagram", getPosition() + " " + mTextView2.getText());
        }
    }

    public class WordItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTextView;
        public WordItemViewHolder(View v, View.OnClickListener clickListener) {
            super(v);
            v.setOnClickListener(clickListener);
            mTextView = (TextView) v.findViewById(R.id.word_list_item_text);
        }

        @Override
        public void onClick(View view) {
            Log.d("Anagram", getPosition() + " " + mTextView.getText());
        }
    }

    public void sort(Comparator comparator) {
        Collections.sort(mDataset, comparator);
        notifyDataSetChanged();
    }

    public void bindSortingModeListeners(View v) {
        if (v.findViewById(R.id.sortingModeButton) != null) {

            ViewOutlineProvider outlineProvider = new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    int size = mContext.getResources().getDimensionPixelSize(R.dimen.fab_size);
                    outline.setOval(0, 0, size, size);
                }
            };

            v.findViewById(R.id.sortingModeButton).setOutlineProvider(outlineProvider);
            ImageButton sortingModeButton = (ImageButton) v.findViewById(R.id.sortingModeButton);
            sortingModeButton.setOnClickListener(mSortModeOnClickListener);
        } else {
            Log.d("main", "CANNOT BIND SORTING MODE LISTENERS. IT IS NULL");
        }
    }
}
