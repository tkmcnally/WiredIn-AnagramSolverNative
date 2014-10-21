package com.wiredin.anagramsolver.util;

import com.wiredin.anagramsolver.models.WordItem;

import java.util.Comparator;

/**
 * Created by Thomas on 10/19/2014.
 */
public class WordListComparators {

    public static final Comparator<WordItem> AlphabeticalSort = new Comparator<WordItem>() {
        @Override
        public int compare(WordItem lhs, WordItem rhs) {
            if(lhs.viewType == 0) {
                return -1;
            }
            if(rhs.viewType == 0) {
                return 1;
            }
            return rhs.text.compareTo(lhs.text);
        }
    };

    public static final Comparator<WordItem> PointSort = new Comparator<WordItem>() {
        @Override
        public int compare(WordItem lhs, WordItem rhs) {
            if(lhs.viewType == 0) {
                return -1;
            }
            if(rhs.viewType == 0) {
                return 1;
            }
            return Integer.compare(rhs.score(), lhs.score());
        }
    };

    public static final Comparator<WordItem> LengthSort = new Comparator<WordItem>() {
        @Override
        public int compare(WordItem lhs, WordItem rhs) {
            if(lhs.viewType == 0) {
                return -1;
            }
            if(rhs.viewType == 0) {
                return 1;
            }
            return Integer.compare(rhs.text.length(), lhs.text.length());
        }
    };

}
