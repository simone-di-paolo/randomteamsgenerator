package com.dev.simonedipaolo.randomteamsgenerator.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.dev.simonedipaolo.randomteamsgenerator.R;
import com.dev.simonedipaolo.randomteamsgenerator.core.utils.Utils;
import com.dev.simonedipaolo.randomteamsgenerator.preference.CustomSwitchPreference;
import com.google.android.material.appbar.MaterialToolbar;

import org.apache.commons.lang3.ObjectUtils;

public class SettingsFragment extends PreferenceFragmentCompat {

    private static final String SETTINGS_SHARED_PREFERENCES = "is_dark_theme_enabled";
    private static final String ENABLE_DARK_THEME_KEY = "enable_dark_theme";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor myPrefsPrefsEditor;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        FragmentActivity fragmentActivity = getActivity();
        if(ObjectUtils.isNotEmpty(fragmentActivity)) {
            sharedPreferences = fragmentActivity.getSharedPreferences(SETTINGS_SHARED_PREFERENCES, Context.MODE_PRIVATE);

            NavController navController = null;
            NavHostFragment navHostFragment = (NavHostFragment) fragmentActivity.getSupportFragmentManager()
                    .findFragmentById(R.id.nav_host_fragment);
            if (ObjectUtils.isNotEmpty(navHostFragment)) {
                navController = navHostFragment.getNavController();
            }  else {
                Log.d("NamesListFragment", "navHostFragment it's empty");
            }

            // initialize toolbar
            NavDirections navDirections = SettingsFragmentDirections.actionSettingsFragmentToMainFragment();
            Utils.initializeToolbar(fragmentActivity, navController, navDirections, R.string.settings_string);

            // disabling bottom bar
            CoordinatorLayout coordinatorLayout = fragmentActivity.findViewById(R.id.coordinatorLayout);
            coordinatorLayout.setVisibility(View.GONE);
            coordinatorLayout.setClickable(false);

            CustomSwitchPreference darkModeSwitch = findPreference("theme_preference");
            if (ObjectUtils.isNotEmpty(darkModeSwitch)) {

                // initializing value of the shared preferences. defaultValue it's false
                boolean isDarkThemeEnabled = sharedPreferences.getBoolean(ENABLE_DARK_THEME_KEY, false);
                darkModeSwitch.setChecked(isDarkThemeEnabled);

                // switch listener
                darkModeSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                        darkModeSwitch.setChecked((Boolean) newValue);

                        // saving new value in shared preferences
                        myPrefsPrefsEditor = sharedPreferences.edit();
                        myPrefsPrefsEditor.putBoolean(ENABLE_DARK_THEME_KEY, (Boolean) newValue);
                        myPrefsPrefsEditor.commit();

                        return true;
                    }
                });
            }
        }
    }

}