package com.wiredin.anagramsolver.unscramble;

import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by Thomas on 9/14/13.
 */
public interface Unscramble {

    public void findWords(String anagram, List<String> wordList, ArrayAdapter<String> listView);
    public List<String> getAnagrams(String anagram);
    public String buildQuery(String anagram);
    public void addAndUpdate(String word);


}
