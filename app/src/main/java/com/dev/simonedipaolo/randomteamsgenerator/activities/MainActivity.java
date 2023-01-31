package com.dev.simonedipaolo.randomteamsgenerator.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;

import com.dev.simonedipaolo.randomteamsgenerator.R;
import com.dev.simonedipaolo.randomteamsgenerator.fragments.MainFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomappbar.BottomAppBar;

import org.apache.commons.lang3.StringUtils;

public class MainActivity extends AppCompatActivity {

    private MaterialToolbar materialToolbar;

    private CoordinatorLayout coordinatorLayout;
    private BottomAppBar bottomAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        materialToolbar = findViewById(R.id.materialToolbar);
        materialToolbar.setTitle(StringUtils.EMPTY);
        setSupportActionBar(materialToolbar);

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        coordinatorLayout.setVisibility(View.INVISIBLE);
        bottomAppBar = findViewById(R.id.bottomAppBar);

    }

}
