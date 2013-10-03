package com.wiredin.anagramsolver.util;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Dawg {

    private static final int CHILD_BIT_SHIFT = 5;
    private static final int CHILD_INDEX_BIT_MASK = 0X003FFFE0;
    private static final int LETTER_BIT_MASK = 0X0000001F;
    private static final int END_OF_WORD_BIT_MASK = 0X00800000;
    private static final int END_OF_LIST_BIT_MASK = 0X00400000;
    private static final int INPUT_SIZE_LIMIT = 100;
    private static final char LOWER_IT = 32;

    private List<String> wordList;
    private HashMap<Integer, List<String>> wordMap = new HashMap<Integer, List<String>>();

    private int numberOfNodes;

    private int[] theDawgArray;

    public Dawg(Context context, List<String> list) throws Exception {
        this.wordList = list;
        //DataInputStream dawgDataFile = new DataInputStream(new BufferedInputStream(getClass().getResourceAsStream("Traditional_Dawg_For_Word-List.dat")));
        DataInputStream dawgDataFile = new DataInputStream(new BufferedInputStream(context.getAssets().open("Traditional_Dawg_For_Word-List.dat")));
        numberOfNodes = endianConversion(dawgDataFile.readInt());
        theDawgArray = new int[numberOfNodes];

        for (int x = 0; x < numberOfNodes; x++) {
            theDawgArray[x] = endianConversion(dawgDataFile.readInt());
        }
        dawgDataFile.close();
    }

    private int endianConversion(int thisInteger) {
        return ((thisInteger & 0x000000ff) << 24) + ((thisInteger & 0x0000ff00) << 8) + ((thisInteger & 0x00ff0000) >>> 8) + ((thisInteger & 0xff000000) >>> 24);
    }

    // These methods are used to extract information from the "theDawgArray" nodes.
    private char nodeLetter(int index) {
        return (char)((theDawgArray[index]&LETTER_BIT_MASK) + 'A');
    }
    private boolean nodeEndOfWord(int index) {
        return ((theDawgArray[index]&END_OF_WORD_BIT_MASK) != 0);
    }
    private int nodeNext(int index) {
        return ((theDawgArray[index]&END_OF_LIST_BIT_MASK) == 0)? (index + 1): 0;
    }
    private int nodeChild(int index) {
        return ((theDawgArray[index]&CHILD_INDEX_BIT_MASK)>>>CHILD_BIT_SHIFT);
    }

    // This method is the core program component.  It requires that "unusedChars" be in alphabetical order because the traditional "Dawg" is a list based structure.
    private String anagramRecurse(int currentIndex, char[] toyWithMe, int fillThisPosition, char[] unusedChars, int sizeOfBank, int[] forTheCounter, boolean onWildcard){
        char previousChar = '\0';
        char currentChar;
        int tempIndex = nodeChild(currentIndex);
        String holder;
        String wordAccumulator = "";

        toyWithMe[fillThisPosition] = nodeLetter(currentIndex);
        if ( onWildcard ) toyWithMe[fillThisPosition] += LOWER_IT;
        if ( nodeEndOfWord(currentIndex) ) {
            forTheCounter[0] += 1;
            wordAccumulator = new String(toyWithMe, 0, fillThisPosition + 1);
            addAndUpdate(wordAccumulator);
           // wordAccumulator = "|" + forTheCounter[0] + "| - " + wordAccumulator + "";
            //if ( sizeOfBank == 0 ) wordAccumulator += " ********->\n";
           // else wordAccumulator +="\n";
        }

        if ( (sizeOfBank > 0) && (tempIndex != 0) ) {
            for ( int x = 0; x < sizeOfBank; x++ ) {
                currentChar = unusedChars[x];
                if ( currentChar == previousChar ) continue;
                do {
                    if ( currentChar == nodeLetter(tempIndex) || currentChar == '?') {
                        removeCharFromArray(unusedChars, x, sizeOfBank);
                        holder = anagramRecurse(tempIndex, toyWithMe, fillThisPosition + 1, unusedChars, sizeOfBank - 1, forTheCounter, (currentChar == '?')? true: false);
                        wordAccumulator += holder;
                        insertCharIntoArray(unusedChars, x, currentChar, sizeOfBank);
                        if ( currentChar != '?' ) {
                            tempIndex = nodeNext(tempIndex);
                            break;
                        }
                    }
                    else if ( currentChar < nodeLetter(tempIndex) ) break;
                } while ( (tempIndex = nodeNext(tempIndex)) != 0 );
                if ( currentChar == '?' ) tempIndex = nodeChild(currentIndex);
                if ( tempIndex == 0 ) break;
                previousChar = currentChar;
            }
        }
        return wordAccumulator;
    }

    // The "toScrambleUp" String may contain '?' wildcards, so indicate these wildcards as lower case letters.
    public HashMap<Integer, List<String>> anagram(String toScrambleUp) {
        wordMap.clear();
        String upperString = toScrambleUp.toUpperCase();
        int numberOfLetters = upperString.length();
        char[] inputCharArray = new char[INPUT_SIZE_LIMIT];
        char[] theWordSoFar = new char[INPUT_SIZE_LIMIT];
        upperString.getChars(0, numberOfLetters, inputCharArray, 0);
        Arrays.sort(inputCharArray,0,numberOfLetters);
        String traversalResult = "";
        String holder;

        char previousChar = '\0';
        char currentChar;
        int[] forTheCount = new int[1];
        forTheCount[0] = 0;

        for ( int x = 0; x < numberOfLetters; x++){
            currentChar = inputCharArray[x];
            if ( currentChar == previousChar ) continue;
            removeCharFromArray(inputCharArray, x, numberOfLetters);
            if ( currentChar != '?' ) {
                holder = "-----------------------------\n";
                holder += anagramRecurse(currentChar - '@', theWordSoFar, 0, inputCharArray, numberOfLetters - 1, forTheCount, false);
                traversalResult += holder;
            }
            else {
                for ( int y = 'A'; y <= 'Z'; y++  ) {
                    holder = "-----------------------------\n";
                    holder += anagramRecurse(y - '@', theWordSoFar, 0, inputCharArray, numberOfLetters - 1, forTheCount, true);
                    traversalResult += holder;
                }
            }
            insertCharIntoArray(inputCharArray, x, currentChar, numberOfLetters);
            previousChar = currentChar;
        }
        traversalResult = "Anagramming this:  |" + upperString + "|\nResults in |" + forTheCount[0] + "| words.\n" + traversalResult;

        return wordMap;
    }

    private void removeCharFromArray(char[] thisArray, int thisPosition, int size) {
        System.arraycopy(thisArray, thisPosition + 1, thisArray, thisPosition, (size - thisPosition - 1));
    }

    private void insertCharIntoArray(char[] thisArray, int thisPosition, char thisChar, int size) {
        System.arraycopy(thisArray, thisPosition, thisArray, thisPosition + 1, (size - thisPosition - 1));
        thisArray[thisPosition] = thisChar;
    }

    public void addAndUpdate(String word) {
        if(wordMap.containsKey(word.length())) {
            wordMap.get(word.length()).add(word);
        } else {
            wordMap.put(word.length(), new ArrayList<String>());
            wordMap.get(word.length()).add(word);
        }
      //  wordList.add(word);
    }
}