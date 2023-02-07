package com.dev.simonedipaolo.randomteamsgenerator.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/**
 * Created by Simone Di Paolo on 14/01/2023.
 */
public class Person implements Parcelable {
    private String name;

    public Person(Parcel in) {
        this.name = in.readString();
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(name);
    }


    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
