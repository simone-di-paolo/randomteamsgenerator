package com.dev.simonedipaolo.randomteamsgenerator.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.dev.simonedipaolo.randomteamsgenerator.R;
import com.dev.simonedipaolo.randomteamsgenerator.models.Person;
import com.google.android.material.appbar.MaterialToolbar;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

public class MainFragment extends Fragment {

    private static final String FIRST_PERSON_KEY = "first_person";

    private NavController navController;
    private CoordinatorLayout coordinatorLayout;

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

        bottomBarInitializer();
        //setToolbarMenu();

        Button addNameButton = v.findViewById(R.id.addNameButton);
        addNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = createAddNameAlertDialog(getActivity()).create();
                dialog.show();
            }
        });

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
    private AlertDialog.Builder createAddNameAlertDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Type a name:");

        final EditText input = new EditText(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        input.setLayoutParams(lp);
        builder.setView(input);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String personName = input.getText().toString();
                Person tempPerson = new Person(personName);

                Person[] emptyList = new Person[0];
                MainFragmentDirections.ActionMainFragmentToNamesListFragment action =
                        MainFragmentDirections.actionMainFragmentToNamesListFragment(tempPerson.getName(), emptyList);
                navController.navigate(action);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        return builder;
    }

    private void bottomBarInitializer() {
        FragmentActivity fragmentActivity = getActivity();
        if(ObjectUtils.isNotEmpty(fragmentActivity)) {

            // setup go back button in top bar
            MaterialToolbar materialToolbar = fragmentActivity.findViewById(R.id.materialToolbar);
            if (ObjectUtils.isNotEmpty(materialToolbar)) {
                AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
                if (ObjectUtils.isNotEmpty(appCompatActivity)) {
                    // toolbar
                    materialToolbar.setNavigationIcon(null);
                    materialToolbar.setClickable(false);
                    materialToolbar.setTitle(StringUtils.EMPTY);
                    materialToolbar.setVisibility(View.GONE);

                    // name title in toolbar
                    //materialToolbar.setTitle(R.string.main_activity_app_title);
                }
            }

            // bottom bar
            coordinatorLayout = fragmentActivity.findViewById(R.id.coordinatorLayout);
            coordinatorLayout.setVisibility(View.GONE);
            coordinatorLayout.setClickable(false);

        }
    }


/*
    private void setToolbarMenu() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.top_app_bar, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();

                if(itemId == R.id.info) {
                    Toast.makeText(getActivity(), "Info", Toast.LENGTH_LONG)
                            .show();
                }

                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }
*/

}
