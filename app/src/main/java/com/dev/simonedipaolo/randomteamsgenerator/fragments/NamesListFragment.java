package com.dev.simonedipaolo.randomteamsgenerator.fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.simonedipaolo.randomteamsgenerator.R;
import com.dev.simonedipaolo.randomteamsgenerator.models.Person;
import com.dev.simonedipaolo.randomteamsgenerator.adapters.PersonRecyclerViewAdapter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class NamesListFragment extends Fragment implements PersonRecyclerViewAdapter.PersonEventListener {

    private static final String FIRST_PERSON_KEY = "first_person";

    private Person firstPerson;
    private List<Person> personList;
    private Person[] personArray;

    private PersonRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private FloatingActionButton addFAB;
    private Button generateTeamsButton;

    private NavController navController;
    private CoordinatorLayout coordinatorLayout;

    private Context context;

    public NamesListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_names_list, container, false);
        context = getContext();

        FragmentActivity activity = getActivity();

        if(ObjectUtils.isNotEmpty(activity)) {

            // setup go back button in top bar
            initializeToolbar(activity);
            // enable menu icosn
            addMenuProvider(activity);

            // bottom bar
            coordinatorLayout = activity.findViewById(R.id.coordinatorLayout);
            coordinatorLayout.setVisibility(View.VISIBLE);

            // FAB
            initializeFAB(activity);

            /// nav host fragment
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

        personList = new ArrayList<>();

        /*if (getArguments() != null) {
            String personName = (String) getArguments().get(FIRST_PERSON_KEY);
            firstPerson = new Person(personName);
            personList.add(firstPerson);
        }*/

        getArgumentsFromBundle();

        generateTeamsButton = v.findViewById(R.id.generateTeamsButton);
        generateTeamsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = generateTeamsDialog(getActivity()).create();
                dialog.show();
            }
        });

        // disabling until there are at least 3 people
        if(personList.size() < 3) {
            generateTeamsButton.setEnabled(false);
            generateTeamsButton.setVisibility(View.GONE);
        }

        // Inflate the layout for this fragment
        recyclerView = v.findViewById(R.id.namesRecyclerView);

        // setting layout
        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
        linearLayout.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayout);

        adapter = new PersonRecyclerViewAdapter(getActivity(), personList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        return v;
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

                if(personList != null) {
                    personList.add(tempPerson);
                    adapter.notifyDataSetChanged();
                    if (personList.size() >= 3) {
                        generateTeamsButton.setEnabled(true);
                        generateTeamsButton.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        return builder;
    }

    // alert dialog
    private AlertDialog.Builder generateTeamsDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Type a name:");

        final NumberPicker numberPicker = new NumberPicker(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        numberPicker.setLayoutParams(lp);

        // setting boundaries for number picker
        numberPicker.setMinValue(2);
        numberPicker.setMaxValue(calculateBoundaries());
        numberPicker.setWrapSelectorWheel(true);
        builder.setView(numberPicker);

        builder.setPositiveButton("GENERATE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // TODO loading animation
                personArray = personList.toArray(new Person[0]);
                NamesListFragmentDirections.ActionNamesListFragmentToGeneratedTeamsFragment action =
                        NamesListFragmentDirections.actionNamesListFragmentToGeneratedTeamsFragment(personArray, numberPicker.getValue());
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

    /**
     * Method implemented from inner interface of PersonEventListener
     * that make easy to disable GENERATE TEAMS button from the adapter
     */
    @Override
    public void disableGenerateTeams() {
        generateTeamsButton.setEnabled(false);
        generateTeamsButton.setVisibility(View.GONE);
    }


    private int calculateBoundaries() {
        final int MAX_BOUNDARY = 100;
        return personList.size() < MAX_BOUNDARY ? personList.size()-1 : MAX_BOUNDARY;
    }

    /**
     * This method will initialize toolbar
     * @param activity
     */
    private void initializeToolbar(FragmentActivity activity) {
        MaterialToolbar materialToolbar = activity.findViewById(R.id.materialToolbar);
        if(ObjectUtils.isNotEmpty(materialToolbar)) {
            AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
            if(ObjectUtils.isNotEmpty(appCompatActivity)) {
                // toolbar
                materialToolbar.setNavigationIcon(R.drawable.ic_back_24dp);
                materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        navController.navigate(NamesListFragmentDirections.actionNamesListFragmentToMainFragment());
                    }
                });

                materialToolbar.setTitle(R.string.names_title);
                materialToolbar.setVisibility(View.VISIBLE);
                materialToolbar.showOverflowMenu();
            }
        }
    }

    private void addMenuProvider(FragmentActivity activity) {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                for(int i=0; i<menu.size(); i++) {
                    menu.getItem(i).setCheckable(true);
                }
                menuInflater.inflate(R.menu.top_app_bar, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                int resId = menuItem.getItemId();
                if(resId == R.id.info) {
                    new MaterialAlertDialogBuilder(activity, R.style.MaterialAlertDialogInfo_App)
                            .setIcon(R.drawable.ic_help_24dp)
                            .setMessage(R.string.help_text)
                            .setPositiveButton(getResources().getString(R.string.continue_string), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // nothing
                                }
                            })
                            .show();
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    /**
     * This method will just initializeFAB
     * @param activity
     */
    private void initializeFAB(FragmentActivity activity) {
        addFAB = activity.findViewById(R.id.addBottomBarFloatingActionButton);
        addFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = createAddNameAlertDialog(getActivity()).create();
                dialog.show();
            }
        });
    }

    private void getArgumentsFromBundle() {
        Bundle bundle = getArguments();
        if(ObjectUtils.isNotEmpty(bundle)) {
            // getting array
            Person[] personArray = NamesListFragmentArgs.fromBundle(getArguments()).getPersonList();
            if(ArrayUtils.isNotEmpty(personArray)) {
                // LinkedList will be used because Arrays.asList will resturn an unmodificable wrapper of list
                personList = new LinkedList<>(Arrays.asList(personArray));
            } else {
                // getting personName because personArray it's empty
                String personName = (String) getArguments().get(FIRST_PERSON_KEY);
                firstPerson = new Person(personName);
                personList.add(firstPerson);
            }
        }
    }

}
