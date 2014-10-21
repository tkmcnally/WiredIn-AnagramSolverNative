package com.wiredin.anagramsolver.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Thomas on 10/20/2014.
 */
public class WordListParceable implements Parcelable {

    private int mData;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static final Parcelable.Creator<WordListParceable> CREATOR
            = new Parcelable.Creator<WordListParceable>() {
        public WordListParceable createFromParcel(Parcel in) {
            return new WordListParceable(in);
        }

        public WordListParceable[] newArray(int size) {
            return new WordListParceable[size];
        }
    };

    private WordListParceable(Parcel in) {
        mData = in.readInt();
    }
}