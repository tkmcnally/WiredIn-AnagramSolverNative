package com.wiredin.anagramsolver.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.ads.AdView;
import com.wiredin.anagramsolver.R;
import com.wiredin.anagramsolver.models.DefinitionItem;
import com.wiredin.anagramsolver.util.DefinitionAsyncTask;
import com.wiredin.anagramsolver.util.ExampleAsyncTask;

/**
 * Created by Thomas on 9/25/13.
 */
public class DefinitionActivity extends Activity {

    // View name of the header image. Used for activity scene transitions
    public static final String VIEW_NAME_HEADER_TEXT = "detail:header:text";

    // View name of the header title. Used for activity scene transitions
    public static final String VIEW_NAME_HEADER_TITLE = "detail:header:title";

    private TextView wordTitle, definitionText;
    private AdView adView;
    private ExampleAsyncTask exampleAsyncTask;
    private DefinitionAsyncTask definitionAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.definition_screen);


        String query = getIntent().getStringExtra("query");
        String score = getIntent().getStringExtra("score");
        Bundle bundle = getIntent().getExtras();

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.definition_progress_bar);
        TextView scoreText = (TextView) findViewById(R.id.definition_score_text);
        scoreText.setText(score);

        wordTitle = (TextView) findViewById(R.id.definition_screen_title);
        wordTitle.setText(query);
        //wordTitle.setViewName(VIEW_NAME_HEADER_TITLE);

        definitionText = (TextView) findViewById(R.id.definition_screen_text);
        definitionText.setMovementMethod(new ScrollingMovementMethod());

        DefinitionItem definitionItem = new DefinitionItem();
        exampleAsyncTask = new ExampleAsyncTask(getApplicationContext(), definitionItem, definitionText);
        definitionAsyncTask = new DefinitionAsyncTask(getApplicationContext(), definitionItem, exampleAsyncTask);
        definitionAsyncTask.textView = wordTitle;
        definitionAsyncTask.mProgressBar = progressBar;
        definitionAsyncTask.execute(query);
        Log.d("Created", "Created");
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d("onHandler", "PAUSED");
        definitionAsyncTask.cancel(true);
        exampleAsyncTask.cancel(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("onHandler", "PAUSED");
        definitionAsyncTask.cancel(true);
        exampleAsyncTask.cancel(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onHandler", "RESUME");
    }
}
