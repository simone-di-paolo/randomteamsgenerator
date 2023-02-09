package com.dev.simonedipaolo.randomteamsgenerator.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.dev.simonedipaolo.randomteamsgenerator.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomappbar.BottomAppBar;

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
        //BottomAppBar bottomAppBar = findViewById(R.id.bottomAppBar);

    }

}
