package com.dev.simonedipaolo.randomteamsgenerator.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.dev.simonedipaolo.randomteamsgenerator.R;
import com.dev.simonedipaolo.randomteamsgenerator.core.utils.CommonConstants;
import com.dev.simonedipaolo.randomteamsgenerator.core.utils.Utils;
import com.google.android.material.appbar.MaterialToolbar;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialToolbar materialToolbar = findViewById(R.id.materialToolbar);
        if(ObjectUtils.isNotEmpty(materialToolbar)) {
            materialToolbar.setTitle(StringUtils.EMPTY);
        }

        setSupportActionBar(materialToolbar);

        CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinatorLayout);
        if(ObjectUtils.isNotEmpty(coordinatorLayout)) {
            coordinatorLayout.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        initializeTheme(newBase);
    }

    private void initializeTheme(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CommonConstants.MODE_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        boolean isDarkThemeEnabled = sharedPreferences.getBoolean(CommonConstants.ENABLE_DARK_THEME_KEY,false);
        // change to dark mode
        Utils.changeTheme(isDarkThemeEnabled);
    }

}
