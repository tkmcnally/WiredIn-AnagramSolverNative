package com.wiredin.anagramsolver.models;

import android.text.Html;
import android.text.Spanned;

import com.wordnik.client.model.Definition;
import com.wordnik.client.model.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 8/21/2014.
 */
public class DefinitionItem {

    public List<Definition> definitions;

    public Example example;

    public DefinitionItem(final List<Definition> definitions, final Example example) {
        this.definitions = definitions;
        this.example = example;
    }

    public DefinitionItem() {
        this.definitions = new ArrayList<Definition>();
        this.example = new Example();
    }

    public Spanned getSpanned() {
        StringBuilder builder = new StringBuilder();

        Definition definition = definitions.get(0);

            builder.append(
                    "<h1>" + definition.getPartOfSpeech() + "</h1>" +
                    "<p>"  + definition.getText() + "</p>" +
                    "<i>\"" + example.getText() + "\"</i>"
            );

        Spanned spanned = Html.fromHtml(builder.toString());

        return spanned;
    }

}
