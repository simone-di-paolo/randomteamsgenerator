package com.dev.simonedipaolo.randomteamsgenerator.utils;

import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.simonedipaolo.randomteamsgenerator.R;

import org.apache.commons.lang3.ObjectUtils;

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

}
