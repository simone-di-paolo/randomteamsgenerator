package com.dev.simonedipaolo.randomteamsgenerator.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.dev.simonedipaolo.randomteamsgenerator.activities.MainActivity;
import com.dev.simonedipaolo.randomteamsgenerator.adapters.PersonRecyclerViewAdapter;
import com.dev.simonedipaolo.randomteamsgenerator.core.utils.Utils;
import com.dev.simonedipaolo.randomteamsgenerator.models.Person;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

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
    private EditText dialogEditText;
    private boolean generateButtonAnimationFromBottomAlreadyTriggered;
    private boolean generateButtonAnimationToBottomAlreadyTriggered;
    private Button generateTeamsButton;
    private MaterialToolbar materialToolbar;

    private NavController navController;
    private CoordinatorLayout coordinatorLayout;
    private ConstraintLayout constraintLayout;
    private Context context;

    public NamesListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_names_list, container, false);
        constraintLayout = v.findViewById(R.id.fragmentNamesConstraintLayout);
        context = getContext();

        FragmentActivity activity = getActivity();
        generateButtonAnimationFromBottomAlreadyTriggered = false;
        generateButtonAnimationToBottomAlreadyTriggered = false;

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

        generateTeamsButton = v.findViewById(R.id.generateTeamsButton);
        getArgumentsFromBundle(activity);

        generateTeamsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder dialog = generateTeamsDialog(getActivity());
                dialog.show();
            }
        });

        // disabling until there are at least 3 people
        if(personList.size() < 3) {
            generateTeamsButton.setEnabled(false);
            //generateTeamsButton.setVisibility(View.GONE);

            if(generateTeamsButton.getVisibility() == View.VISIBLE) {
                Animation animation = AnimationUtils.loadAnimation(activity, R.anim.to_bottom);
                animation.setFillAfter(true);
                generateTeamsButton.clearAnimation();
                generateTeamsButton.setAnimation(animation);
                generateTeamsButton.startAnimation(animation);
            }
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
        builder.setPositiveButton(R.string.add_lowercase_string, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                confirmAndCandelDialogListener();
            }
        });
        builder.setNegativeButton(R.string.cancel_string, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // do nothing
                addFAB.setVisibility(View.VISIBLE);
            }
        });
    }

    // alert dialog
    private MaterialAlertDialogBuilder generateTeamsDialog(Context context) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        builder.setMessage(R.string.type_a_name_string);

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

        builder.setPositiveButton(R.string.generate_button_text, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // TODO loading animation
                personArray = personList.toArray(new Person[0]);
                NamesListFragmentDirections.ActionNamesListFragmentToGeneratedTeamsFragment action =
                        NamesListFragmentDirections.actionNamesListFragmentToGeneratedTeamsFragment(personArray, numberPicker.getValue());
                navController.navigate(action);

                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.to_left);
                animation.setFillAfter(true);
                animation.setStartOffset(200);

                materialToolbar.clearAnimation();
                materialToolbar.startAnimation(animation);
            }
        });

        builder.setNegativeButton(R.string.cancel_string, new DialogInterface.OnClickListener() {
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
        if(!generateButtonAnimationToBottomAlreadyTriggered) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.to_bottom);
            animation.setFillAfter(true);
            generateTeamsButton.setAnimation(animation);
            generateTeamsButton.startAnimation(animation);
            generateButtonAnimationFromBottomAlreadyTriggered = false;
            generateButtonAnimationToBottomAlreadyTriggered = true;
        }
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
        materialToolbar = activity.findViewById(R.id.materialToolbar);
        if(ObjectUtils.isNotEmpty(materialToolbar)) {
            AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
            if(ObjectUtils.isNotEmpty(appCompatActivity)) {
                // toolbar
                materialToolbar.setNavigationIcon(R.drawable.ic_back_24dp);
                materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Animation animation = AnimationUtils.loadAnimation(appCompatActivity, R.anim.to_right);
                        animation.setFillAfter(true);
                        animation.setStartOffset(50);
                        materialToolbar.clearAnimation();
                        materialToolbar.startAnimation(animation);
                        materialToolbar.setClickable(false);

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
        addFAB.setClickable(true);
        addFAB.setVisibility(View.VISIBLE);
        addFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder dialog = createDialog(getActivity());
                setDialogOnClickListeners(dialog, dialogEditText);
                AlertDialog alertDialog = dialog.show();
                dialogEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        confirmAndCandelDialogListener();
                        alertDialog.dismiss();
                        return false;
                    }
                });
                addFAB.setVisibility(View.GONE);

                // focus edit text and open keyboard
                Utils.focusEditTextAndOpenKeyboard(dialogEditText, activity);
            }
        });
    }

    private void confirmAndCandelDialogListener() {
        String personName = dialogEditText.getText().toString();

        if(StringUtils.isNotBlank(personName)) {
            Person tempPerson = new Person(personName);

            if (personList != null) {
                personList.add(tempPerson);
                adapter.notifyDataSetChanged();
                if (personList.size() >= 3) {
                    generateTeamsButton.setEnabled(true);
                    generateTeamsButton.setVisibility(View.VISIBLE);

                    if (!generateButtonAnimationFromBottomAlreadyTriggered) {
                        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.from_bottom);
                        animation.setFillAfter(true);
                        generateTeamsButton.clearAnimation();
                        generateTeamsButton.setAnimation(animation);
                        generateTeamsButton.startAnimation(animation);
                        generateButtonAnimationFromBottomAlreadyTriggered = true;
                        generateButtonAnimationToBottomAlreadyTriggered = false;
                    }
                }
            }
        } else {
            Snackbar.make(constraintLayout, R.string.please_valid_name_string, Snackbar.LENGTH_SHORT)
                    .setAction(R.string.retry_string, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            MaterialAlertDialogBuilder dialog = createDialog(getActivity());
                            setDialogOnClickListeners(dialog, dialogEditText);
                            AlertDialog alertDialog = dialog.show();
                            dialogEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                @Override
                                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                                    confirmAndCandelDialogListener();
                                    alertDialog.dismiss();
                                    return false;
                                }
                            });
                            addFAB.setVisibility(View.GONE);
                            // focus edit text and open keyboard
                            Utils.focusEditTextAndOpenKeyboard(dialogEditText, getActivity());
                        }
                    })
                    .show();
        }
        addFAB.setVisibility(View.VISIBLE);
    }

    private void getArgumentsFromBundle(FragmentActivity activity) {
        Bundle bundle = getArguments();
        if(ObjectUtils.isNotEmpty(bundle)) {
            // getting array
            Person[] personArray = NamesListFragmentArgs.fromBundle(getArguments()).getPersonList();
            if(ArrayUtils.isNotEmpty(personArray)) {
                // LinkedList will be used because Arrays.asList will resturn an unmodificable wrapper of list
                personList = new LinkedList<>(Arrays.asList(personArray));
                generateButtonAnimationFromBottomAlreadyTriggered = true;

                Animation animation = AnimationUtils.loadAnimation(activity, R.anim.from_left);
                animation.setFillAfter(true);
                animation.setStartOffset(30);
                materialToolbar.clearAnimation();
                materialToolbar.startAnimation(animation);
            } else {
                // getting personName because personArray it's empty
                String personName = (String) getArguments().get(FIRST_PERSON_KEY);
                firstPerson = new Person(personName);
                personList.add(firstPerson);

                Animation animation = AnimationUtils.loadAnimation(activity, R.anim.from_right);
                animation.setFillAfter(true);
                animation.setStartOffset(30);
                materialToolbar.clearAnimation();
                materialToolbar.startAnimation(animation);

                generateTeamsButton.setVisibility(View.GONE);
            }
        }
    }

}
