package com.dev.simonedipaolo.randomteamsgenerator.core.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;

import com.dev.simonedipaolo.randomteamsgenerator.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.gson.Gson;

import org.apache.commons.lang3.ObjectUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Simone Di Paolo on 25/01/2023.
 */
public class Utils {


    public static void replaceFragment(FragmentActivity activity, Fragment fragment, boolean addToBackstack) {
        if(ObjectUtils.isNotEmpty(activity)) {
            FragmentManager supportFragmentManager = activity.getSupportFragmentManager();
            if (ObjectUtils.isNotEmpty(supportFragmentManager)) {
                FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, fragment);
                if(addToBackstack) {
                    fragmentTransaction.addToBackStack(null);
                }
                fragmentTransaction.commit();
            } else {
                Log.e("SUPPORT MANAGER IS NULL", "MainFragment");
            }
        } else {
            Log.e("activity it's null", "MainFragment");
        }
    }

    public synchronized static List<String> readListFromJson(Context context, String fileName) throws IOException {
        AssetManager assetManager = context.getAssets();
        InputStream is = assetManager.open(fileName);
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        String jsonString = new String(buffer, "UTF-8");
        Gson gson = new Gson();
        return gson.fromJson(jsonString, List.class);
    }

    public static void focusEditTextAndOpenKeyboard(EditText dialogEditText, FragmentActivity activity) {
        dialogEditText.setOnFocusChangeListener((view, b) -> dialogEditText.postDelayed(() -> {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(dialogEditText, InputMethodManager.SHOW_IMPLICIT);
        }, 300));
        dialogEditText.requestFocus();
    }

    /**
     *
     * @param fragmentActivity the fragmentActivity
     * @param navController the navController
     * @param navDirections the direction from A fragment to B fragment
     * @param toolbarTitleResId the resId of the title string
     */
    public static void initializeToolbar(FragmentActivity fragmentActivity, NavController navController, NavDirections navDirections, int toolbarTitleResId) {
        MaterialToolbar materialToolbar = fragmentActivity.findViewById(R.id.materialToolbar);
        if(ObjectUtils.isNotEmpty(materialToolbar)) {
            AppCompatActivity appCompatActivity = (AppCompatActivity) fragmentActivity;
            if(ObjectUtils.isNotEmpty(appCompatActivity)) {
                // toolbar
                materialToolbar.setNavigationIcon(R.drawable.ic_back_24dp);
                materialToolbar.setNavigationOnClickListener(view -> {
                    Animation animation = AnimationUtils.loadAnimation(appCompatActivity, R.anim.to_right);
                    animation.setFillAfter(true);
                    animation.setStartOffset(50);
                    materialToolbar.clearAnimation();
                    materialToolbar.startAnimation(animation);
                    materialToolbar.setClickable(false);

                    navController.navigate(navDirections);
                });

                materialToolbar.setTitle(toolbarTitleResId);
                materialToolbar.setVisibility(View.VISIBLE);
                materialToolbar.showOverflowMenu();
            }
        }
    }

}
