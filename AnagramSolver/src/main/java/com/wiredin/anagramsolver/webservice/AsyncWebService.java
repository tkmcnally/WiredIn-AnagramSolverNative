package com.wiredin.anagramsolver.webservice;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.wiredin.anagramsolver.screens.DefinitionScreen;
import com.wiredin.anagramsolver.screens.MainScreen;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.List;

/**
 * Created by Thomas on 9/25/13.
 */
public class AsyncWebService extends AsyncTask<String, Void, String> {

    private DefinitionScreen activity;
    private ProgressDialog progDailog;

    public AsyncWebService(DefinitionScreen activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progDailog = new ProgressDialog(this.activity);
        progDailog.setMessage("Loading...");
        progDailog.setIndeterminate(false);
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDailog.setCancelable(true);
        progDailog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        return callWebService(strings[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        activity.setDefinitionText(result);
        progDailog.dismiss();
    }

    public String callWebService(final String word) {

        HttpClient client = new DefaultHttpClient();
        String URL = "http://services.aonaware.com/DictService/DictService.asmx/DefineInDict?dictId=wn&word=" + word.toLowerCase().toString();
        String httpReturn;
        try {
            // Create Request to server and get response

            HttpGet httpget = new HttpGet(URL);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            httpReturn = client.execute(httpget, responseHandler);
            // Show response on activity

            return httpReturn;
        } catch (Exception ex) {
            httpReturn = "exception";
            return httpReturn;
        }
    }

}
