package com.wiredin.anagramsolver.util;

import android.database.Cursor;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.wiredin.anagramsolver.db.DataAdapter;
import com.wiredin.anagramsolver.unscramble.Unscramble;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by Thomas on 9/13/13.
 */
public class Unscrambler implements Unscramble{

    private List<String> restricted;
    private DataAdapter dataAdapter;

    public Unscrambler(DataAdapter adapter) {
        this.dataAdapter = adapter;
        makeRestrictions();

    }

    private void makeRestrictions() {
        restricted = new ArrayList<String>();
        restricted.add("V");
        restricted.add("W");
        restricted.add("X");
        restricted.add("Y");
    }
    public List<String> stripList(List<String> list) {
        List<String> newList = new ArrayList<String>();
        int markerOne = 0;
        for(int i = 0; i < list.size(); i++) {
            for(int k = 0; k < list.get(i).length(); k++) {
                if(k == list.get(i).length() - 1) {
                    newList.add(list.get(i).substring(markerOne, k + 1));
                }
                if(list.get(i).charAt(k) == '?') {
                    newList.add(list.get(i).substring(markerOne, k));
                    markerOne = k + 1;
                }
            }
        }
        return newList;
    }

    @Override
    public void findWords(String anagram, List<String> wordList, ArrayAdapter<String> listAdapter) {

        List<String> testing;
        List<String> combinations = Combinations.getCombinations(anagram);
        Log.d("COMBINATIONS", combinations.toString());

        for(int i = 0; i < combinations.size(); i++) {
            testing = new ArrayList<String>();
            testing.addAll(getAnagrams(combinations.get(i)));
            if(testing.size() > 0) {
                wordList.addAll(testing);
                Collections.sort(wordList, new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        if (o1.length() > o2.length()) {
                            return 1;
                        } else if (o1.length() < o2.length()) {
                            return -1;
                        }
                        return o1.compareTo(o2);
                    }
                });

                listAdapter.notifyDataSetChanged();
            }
        }


    }

    @Override
    public List<String> getAnagrams(String anagram) {
        List<String> returnList = new ArrayList<String>();
        if(!restricted.contains(Character.toString(anagram.charAt(0)).toUpperCase())) {

            Cursor cursor = dataAdapter.doQuery(buildQuery(anagram));
            if (cursor.moveToFirst()) {
                returnList.add(cursor.getString(cursor.getColumnIndex("orig")));
            } else {
            }
            returnList = stripList(returnList);
        }
        return returnList;
    }

    @Override
    public String buildQuery(String anagram) {
        return "SELECT orig FROM wordlist WHERE sorted ='" + anagram.toUpperCase() + "'";
    }

    @Override
    public void addAndUpdate(String word) {

    }
}
