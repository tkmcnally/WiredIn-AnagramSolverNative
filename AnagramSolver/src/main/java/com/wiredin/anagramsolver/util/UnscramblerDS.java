package com.wiredin.anagramsolver.util;

import android.widget.ArrayAdapter;

import com.wiredin.anagramsolver.unscramble.Unscramble;

import java.util.List;

/**
 * Created by Thomas on 9/14/13.
 */
public class UnscramblerDS implements Unscramble{
    @Override
    public void findWords(String anagram, List<String> wordList, ArrayAdapter<String> listView) {

    }

    @Override
    public List<String> getAnagrams(String anagram) {
        return null;
    }

    @Override
    public String buildQuery(String anagram) {
        return null;
    }

    @Override
    public void addAndUpdate(String word) {

    }
}
