package com.dev.simonedipaolo.randomteamsgenerator.fragments;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.dev.simonedipaolo.randomteamsgenerator.R;
import com.dev.simonedipaolo.randomteamsgenerator.models.Person;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

public class MainFragment extends Fragment {

    private static final String FIRST_PERSON_KEY = "first_person";

    private NavController navController;
    private CoordinatorLayout coordinatorLayout;
    private MaterialToolbar materialToolbar;
    private FragmentActivity fragmentActivity;
    private ObjectAnimator scaleDown;
    private ConstraintLayout constraintLayout;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        fragmentActivity = getActivity();
        constraintLayout = v.findViewById(R.id.mainFragmentConstraintLayout);
        bottomAndTopBarInitializer();
        //setToolbarMenu();

        Button addNameButton = v.findViewById(R.id.addNameButton);
        addNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder dialog = createAddNameAlertDialog(getActivity());
                dialog.show();
                scaleDown.pause();
            }
        });

        // add button animation
        scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                addNameButton,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f));
        scaleDown.setDuration(500);

        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);

        scaleDown.start();

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentActivity activity = getActivity();
        if(ObjectUtils.isNotEmpty(activity)) {
            NavHostFragment navHostFragment = (NavHostFragment) activity.getSupportFragmentManager()
                    .findFragmentById(R.id.nav_host_fragment);
            if (ObjectUtils.isNotEmpty(navHostFragment)) {
                navController = navHostFragment.getNavController();
            }  else {
                Log.d("NamesListFragment", "navHostFragment it's empty");
            }
        } else {
            Log.d("NamesListFragment", "activity it's empty");
        }

    }

    // alert dialog
    private MaterialAlertDialogBuilder createAddNameAlertDialog(Context context) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        builder.setMessage("Type a name:");

        final EditText input = new EditText(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        input.setContentDescription(getResources().getString(R.string.name_string));
        input.setLayoutParams(lp);
        builder.setView(input);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String personName = input.getText().toString();
                if(StringUtils.isNotBlank(personName)) {
                    Person tempPerson = new Person(personName);
                    Person[] emptyList = new Person[0];
                    MainFragmentDirections.ActionMainFragmentToNamesListFragment action =
                            MainFragmentDirections.actionMainFragmentToNamesListFragment(tempPerson.getName(), emptyList);
                    navController.navigate(action);

                    materialToolbar.clearAnimation();
                    Animation animation = AnimationUtils.loadAnimation(fragmentActivity, R.anim.to_left);
                    animation.setFillAfter(true);
                    materialToolbar.startAnimation(animation);
                    materialToolbar.setClickable(false);
                } else {
                    Snackbar.make(constraintLayout, "Please insert a valid name", Snackbar.LENGTH_SHORT)
                            .setAction("Retry", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    MaterialAlertDialogBuilder dialog = createAddNameAlertDialog(getActivity());
                                    dialog.show();
                                }
                            })
                            .show();
                }
            }

        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                scaleDown.resume();
                dialog.cancel();
            }
        });

        return builder;
    }

    private void bottomAndTopBarInitializer() {
        if(ObjectUtils.isNotEmpty(fragmentActivity)) {

            // setup go back button in top bar
            materialToolbar = fragmentActivity.findViewById(R.id.materialToolbar);
            if (ObjectUtils.isNotEmpty(materialToolbar)) {
                AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
                if (ObjectUtils.isNotEmpty(appCompatActivity)) {
                    // toolbar
                    //materialToolbar.setNavigationIcon(null);
                    //materialToolbar.setClickable(false);
                    //materialToolbar.setTitle(StringUtils.EMPTY);
                    //materialToolbar.setVisibility(View.GONE);

                    materialToolbar.clearAnimation();
                    Animation animation = AnimationUtils.loadAnimation(fragmentActivity, R.anim.to_right);
                    animation.setFillAfter(true);
                    materialToolbar.startAnimation(animation);
                    materialToolbar.setClickable(false);

                    // hide icons after animation started
                    materialToolbar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            materialToolbar.setNavigationIcon(null);
                        }
                    }, 500);

                }
            }

            // bottom bar
            coordinatorLayout = fragmentActivity.findViewById(R.id.coordinatorLayout);
            coordinatorLayout.setVisibility(View.GONE);
            coordinatorLayout.setClickable(false);

        }
    }

}
