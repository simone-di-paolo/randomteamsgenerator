package com.dev.simonedipaolo.randomteamsgenerator.fragments;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.dev.simonedipaolo.randomteamsgenerator.R;
import com.dev.simonedipaolo.randomteamsgenerator.core.utils.Utils;
import com.dev.simonedipaolo.randomteamsgenerator.models.Person;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

public class MainFragment extends Fragment {

    private static final String FIRST_PERSON_KEY = "first_person";

    private NavController navController;
    private MaterialToolbar materialToolbar;
    private FragmentActivity fragmentActivity;
    private ObjectAnimator scaleDown;
    private ConstraintLayout constraintLayout;
    private EditText dialogEditText;
    private Button firstFragmentSettingsButton;

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

        firstFragmentSettingsButton = v.findViewById(R.id.firstFragmentSettingsButton);

        Button addNameButton = v.findViewById(R.id.addNameButton);
        addNameButton.setOnClickListener(view -> {
            MaterialAlertDialogBuilder dialog = createDialog(getActivity());
            setDialogOnClickListeners(dialog, dialogEditText);
            AlertDialog alertDialog = dialog.show();
            dialogEditText.setOnEditorActionListener((textView, i, keyEvent) -> {
                confirmAndCandelDialogListener();
                alertDialog.dismiss();
                return false;
            });

            // focus edit text and open keyboard
            Utils.focusEditTextAndOpenKeyboard(dialogEditText, getActivity());
            scaleDown.pause();
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

        firstFragmentSettingsButton.setOnClickListener(view1 -> {
            if(ObjectUtils.isNotEmpty(navController)) {
                navController.navigate(MainFragmentDirections.actionMainFragmentToSettingsFragment());
            }
        });

    }

    private MaterialAlertDialogBuilder createDialog(Context context) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        builder.setMessage(R.string.type_a_name_string);
        builder.setCancelable(false);
        dialogEditText = new EditText(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        dialogEditText.setLayoutParams(lp);
        dialogEditText.setSingleLine(true);
        dialogEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        builder.setView(dialogEditText);
        return builder;
    }

    private void setDialogOnClickListeners(MaterialAlertDialogBuilder builder, EditText dialogEditText) {
        builder.setPositiveButton(R.string.add_lowercase_string, (dialog, id) -> confirmAndCandelDialogListener());
        builder.setNegativeButton(R.string.cancel_string, (dialogInterface, i) -> {
            // do nothing
            scaleDown.resume();
        });
    }

    private void confirmAndCandelDialogListener() {

        String personName = dialogEditText.getText().toString();
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
            Snackbar.make(constraintLayout, R.string.please_valid_name_string, Snackbar.LENGTH_SHORT)
                    .setAction(R.string.retry_string, view -> {
                        scaleDown.pause();
                        MaterialAlertDialogBuilder dialog = createDialog(getActivity());
                        setDialogOnClickListeners(dialog, dialogEditText);
                        AlertDialog alertDialog = dialog.show();
                        dialogEditText.setOnEditorActionListener((textView, i, keyEvent) -> {
                            confirmAndCandelDialogListener();
                            alertDialog.dismiss();
                            return false;
                        });
                        Utils.focusEditTextAndOpenKeyboard(dialogEditText, getActivity());
                    })
                    .show();
            scaleDown.resume();
        }
    }

    private void bottomAndTopBarInitializer() {
        if(ObjectUtils.isNotEmpty(fragmentActivity)) {

            // setup go back button in top bar
            materialToolbar = fragmentActivity.findViewById(R.id.materialToolbar);
            if (ObjectUtils.isNotEmpty(materialToolbar)) {
                AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
                if (ObjectUtils.isNotEmpty(appCompatActivity)) {

                    // toolbar
                    materialToolbar.clearAnimation();
                    Animation animation = AnimationUtils.loadAnimation(fragmentActivity, R.anim.to_right);
                    animation.setFillAfter(true);
                    materialToolbar.startAnimation(animation);
                    materialToolbar.setClickable(false);

                    // hide icons after animation started
                    materialToolbar.postDelayed(() -> materialToolbar.setNavigationIcon(null), 500);

                }
            }

            // bottom bar
            CoordinatorLayout coordinatorLayout = fragmentActivity.findViewById(R.id.coordinatorLayout);
            coordinatorLayout.setVisibility(View.GONE);
            coordinatorLayout.setClickable(false);

        }
    }

}
