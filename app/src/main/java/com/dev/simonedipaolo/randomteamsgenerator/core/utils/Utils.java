package com.dev.simonedipaolo.randomteamsgenerator.core.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dev.simonedipaolo.randomteamsgenerator.R;
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

}
