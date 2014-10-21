package com.wiredin.anagramsolver.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wiredin.anagramsolver.models.DefinitionItem;
import com.wordnik.client.api.WordApi;
import com.wordnik.client.common.ApiException;
import com.wordnik.client.model.Definition;
import com.wordnik.client.model.Example;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 8/21/2014.
 */
public class ExampleAsyncTask extends AsyncTask<String, Integer, Example> {

    public static final String WORDNIK_API_KEY = "23268040f291d722c528e2fa3f80a30fd465e6b895bcc2225";

    public final String REGEXP_SPACES = "[\\s]";

    public final String ENCODING_TYPE = "UTF-8";

    public final String DEFAULT_MESSAGE = "No definition found!";

    private Context context;
    private TextView textView;
    private DefinitionItem definitionItem;

    public ProgressBar mProgressBar;

    private WordApi wordApi;

    public ExampleAsyncTask(Context context, DefinitionItem definitionItem, TextView textView) {
        this.textView = textView;
        this.context = context;
        this.definitionItem = definitionItem;
    }
    public boolean activeNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public boolean init() {
        return activeNetwork();
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        wordApi = new WordApi();
        wordApi.addHeader("api_key", WORDNIK_API_KEY);
    }

    @Override
    protected Example doInBackground(String... strings) {
        if(isCancelled()) {
            return null;
        }
        String query = formatQuery(strings[0]);
        Example topExample = new Example();
        try {
            topExample = wordApi.getTopExample(query, null);
        } catch (ApiException e) {
            e.printStackTrace();
        }

        return topExample;
    }


    public String formatQuery(String query) {
        String word = query.toLowerCase();
        String encoded = word;
        try {
            encoded = URLEncoder.encode(word, ENCODING_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encoded;
    }

    @Override
    protected void onPostExecute(Example example) {
        definitionItem.example = example;
        textView.setText(definitionItem.getSpanned());
        mProgressBar.setVisibility(View.INVISIBLE);
    }
}
