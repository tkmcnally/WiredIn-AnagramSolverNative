package com.wiredin.anagramsolver.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Thomas on 8/7/2014.
 */
public class WordItem implements Parcelable {

    public int viewType;
    public String text;

    public WordItem(int viewType, String text) {
        this.viewType = viewType;
        this.text = text;
    }

    public int score() {
        int score = 0;
        char[] scoreTable = {1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10};
        for (char c: text.toUpperCase().toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                score += scoreTable[c - 'A'];
            }
        }
        return score;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(viewType);
        dest.writeString(text);
    }

    /**
     * Constructs a WordItem from a Parcel
     * @param parcel Source Parcel
     */
    public WordItem (Parcel parcel) {
        this.viewType = parcel.readInt();
        this.text = parcel.readString();
    }

    // Method to recreate a Question from a Parcel
    public static Creator<WordItem> CREATOR = new Creator<WordItem>() {

        @Override
        public WordItem createFromParcel(Parcel source) {
            return new WordItem(source);
        }

        @Override
        public WordItem[] newArray(int size) {
            return new WordItem[size];
        }

    };

}
