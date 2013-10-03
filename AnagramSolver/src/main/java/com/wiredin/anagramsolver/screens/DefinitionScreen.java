package com.wiredin.anagramsolver.screens;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.wiredin.anagramsolver.R;
import com.wiredin.anagramsolver.util.Util;
import com.wiredin.anagramsolver.webservice.AsyncWebService;

import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

/**
 * Created by Thomas on 9/25/13.
 */
public class DefinitionScreen extends Activity {

    private TextView textView;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definition);


        AdRequest adRequest = new AdRequest();
        adRequest.addTestDevice("B0FAD6AAEEC389421519822D4A0FF35C");

        adView = new AdView(this, AdSize.BANNER, "ca-app-pub-9081322137461264/4444200032");
        adView.loadAd(adRequest);


        Bundle bundle = getIntent().getExtras();

        textView = (TextView) findViewById(R.id.definition_textView);
        textView.setMovementMethod(new ScrollingMovementMethod());


        new AsyncWebService(this).execute(bundle.getString("USER_INPUT_CLEAN"));

    }

    public void setDefinitionText(final String httpString) {
        try {
            textView.setText(Util.parseXML(httpString));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }


}
