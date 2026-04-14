package com.dev.simonedipaolo.randomteamsgenerator.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
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
        // Enable Edge-to-Edge for Android 15+ support
        EdgeToEdge.enable(this);

        // Initialize theme before super.onCreate
        initializeTheme(this);
        
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

    /**
     * Initialize theme with light or dark.
     * If no shared pref configured, then follow the system configs.
     * If shared pref exists, then, use it.
     * @param context => the context where to retrieve the shared preferences.
     */
    private void initializeTheme(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CommonConstants.MODE_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(CommonConstants.ENABLE_DARK_THEME_KEY)) {
            boolean isDarkThemeEnabled = sharedPreferences.getBoolean(CommonConstants.ENABLE_DARK_THEME_KEY, false);
            Utils.changeTheme(isDarkThemeEnabled);
        } else {
            // Default to system settings if no preference is set
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }

}
