package com.dev.simonedipaolo.randomteamsgenerator.preference;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceViewHolder;

/**
 * Created by Simone Di Paolo on 08/02/2023.
 */
public class CustomPreferenceCategory extends PreferenceCategory {

    public CustomPreferenceCategory(Context context) {
        super(context);
    }

    public CustomPreferenceCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomPreferenceCategory(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onBindViewHolder(@NonNull PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        //TextView textView = (TextView) holder.findViewById(android.R.id.title);
    }
}
