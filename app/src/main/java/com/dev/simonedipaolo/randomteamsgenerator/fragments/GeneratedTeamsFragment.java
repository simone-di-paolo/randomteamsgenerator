package com.dev.simonedipaolo.randomteamsgenerator.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.simonedipaolo.randomteamsgenerator.R;
import com.dev.simonedipaolo.randomteamsgenerator.adapters.TeamsRecyclerViewAdapter;
import com.dev.simonedipaolo.randomteamsgenerator.core.bean.Flag;
import com.dev.simonedipaolo.randomteamsgenerator.core.bean.Row;
import com.dev.simonedipaolo.randomteamsgenerator.core.bean.TeamName;
import com.dev.simonedipaolo.randomteamsgenerator.core.utils.NamesShuffler;
import com.dev.simonedipaolo.randomteamsgenerator.core.utils.RandomFlagGenerator;
import com.dev.simonedipaolo.randomteamsgenerator.core.utils.RowGenerator;
import com.dev.simonedipaolo.randomteamsgenerator.core.utils.TeamNameGenerator;
import com.dev.simonedipaolo.randomteamsgenerator.models.Person;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class GeneratedTeamsFragment extends Fragment {

    private RecyclerView recyclerView;
    private TeamsRecyclerViewAdapter adapter;
    private List<Person> personList;
    private List<List<Person>> teams;
    private List<Flag> flags;
    private List<Row> rows;
    private List<TeamName> teamNameList;
    private int howManyTeams;

    // navigation
    private NavController navController;
    private MaterialToolbar materialToolbar;

    private FragmentActivity activity;

    public GeneratedTeamsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_generated_teams, container, false);

        teams = new ArrayList<>();

        activity = getActivity();

        if (ObjectUtils.isNotEmpty(activity)) {
            // initializing personList and recycler view
            Bundle bundle = getArguments();
            if(ObjectUtils.isNotEmpty(bundle)) {
                Person[] personArray = GeneratedTeamsFragmentArgs.fromBundle(getArguments()).getPersonList();
                howManyTeams = GeneratedTeamsFragmentArgs.fromBundle(getArguments()).getHowManyTeams();

                // managing go back
                OnBackPressedCallback callback = new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        Person[] personArray = new Person[personList.size()];
                        for(int i=0; i<personList.size(); i++) {
                            personArray[i] = personList.get(i);
                        }
                        GeneratedTeamsFragmentDirections.ActionGeneratedTeamsFragmentToNamesListFragment action
                                = GeneratedTeamsFragmentDirections.actionGeneratedTeamsFragmentToNamesListFragment(StringUtils.EMPTY, personArray);
                        navController.navigate(action);
                    }
                };
                requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

                // LinkedList will be used because Arrays.asList will resturn an unmodificable wrapper of list
                personList = new LinkedList<>(Arrays.asList(personArray));
                generateFlagsAndTeams();
            }

            recyclerViewInit(view);

            // ======== TOOL BAR ========

            //
            addMenuProvider(activity);
            initializeToolbar(activity);

            // intro anim
            Animation animation = AnimationUtils.loadAnimation(activity, R.anim.from_right);
            animation.setFillAfter(true);
            animation.setStartOffset(25);
            materialToolbar.clearAnimation();
            materialToolbar.startAnimation(animation);

            initializeFAB(activity);

            // ======== NAV CONTROLLER ========
            NavHostFragment navHostFragment = (NavHostFragment) activity.getSupportFragmentManager()
                    .findFragmentById(R.id.nav_host_fragment);
            if (ObjectUtils.isNotEmpty(navHostFragment)) {
                navController = navHostFragment.getNavController();
            }  else {
                Log.d("NamesListFragment", "navHostFragment it's empty");
            }

            // ======== END ========
        }else {
            Log.i("GeneratedTeamsFragment", "onCreateView  - Activity it's empty.");
        }

        return view;
    }


    private void recyclerViewInit(View view) {
        // Inflate the layout for this fragment
        recyclerView = view.findViewById(R.id.generatedTeamsRecyclerView);
        // setting layout
        if(ObjectUtils.isNotEmpty(activity)) {

            LinearLayoutManager linearLayout = new LinearLayoutManager(activity);
            linearLayout.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayout);

            adapter = new TeamsRecyclerViewAdapter(activity, teams, teamNameList, flags, rows);
            recyclerView.setAdapter(adapter);
            recyclerView.setHasFixedSize(true);
        } else {
            Log.i("GeneratedTeamsFragment", "recyclerViewInit - Activity it's empty.");
        }
    }

    /**
     * This method will initialize toolbar
     * @param activity the activity
     */
    private void initializeToolbar(FragmentActivity activity) {
        materialToolbar = activity.findViewById(R.id.materialToolbar);
        if(ObjectUtils.isNotEmpty(materialToolbar)) {
            AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
            if(ObjectUtils.isNotEmpty(appCompatActivity)) {
                // toolbar
                materialToolbar.setNavigationIcon(R.drawable.ic_back_24dp);
                materialToolbar.setTitle(R.string.teams_title);
                materialToolbar.setNavigationOnClickListener(view -> {
                    Person[] personArray = new Person[personList.size()];
                    for(int i=0; i<personList.size(); i++) {
                        personArray[i] = personList.get(i);
                    }
                    GeneratedTeamsFragmentDirections.ActionGeneratedTeamsFragmentToNamesListFragment action
                            = GeneratedTeamsFragmentDirections.actionGeneratedTeamsFragmentToNamesListFragment(StringUtils.EMPTY, personArray);
                    navController.navigate(action);


                    Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.to_right);
                    animation.setFillAfter(true);
                    animation.setStartOffset(25);
                    materialToolbar.clearAnimation();
                    materialToolbar.startAnimation(animation);
                });

                materialToolbar.setTitle(R.string.teams_title);
                materialToolbar.setVisibility(View.VISIBLE);
                materialToolbar.showOverflowMenu();
            }
        }
    }

    /**
     * This method will just initializeFAB (hiding)
     * @param activity the activity
     */
    private void initializeFAB(FragmentActivity activity) {
        FloatingActionButton addFAB = activity.findViewById(R.id.addBottomBarFloatingActionButton);
        addFAB.setClickable(false);
        addFAB.setVisibility(View.GONE);
    }

    private void addMenuProvider(FragmentActivity activity) {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.generated_teams_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                int resId = menuItem.getItemId();
                if(resId == R.id.update) {
                    new MaterialAlertDialogBuilder(activity, R.style.MaterialAlertDialogInfo_App)
                            .setIcon(R.drawable.ic_info_24dp)
                            .setMessage(R.string.alert_update_text)
                            .setPositiveButton(getResources().getString(R.string.confirm_string), (dialogInterface, i) -> {
                                generateFlagsAndTeams();
                                adapter = new TeamsRecyclerViewAdapter(activity, teams, teamNameList, flags, rows);
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            })
                            .setNegativeButton(getResources().getString(R.string.cancel_string), (dialogInterface, i) -> {
                                // nothing
                            })
                            .show();
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    private void generateFlagsAndTeams() {
        // generate new team member names
        NamesShuffler namesShuffler = new NamesShuffler(personList, howManyTeams);
        teams = namesShuffler.getTeams();

        // generate flags
        RandomFlagGenerator randomFlagGenerator = new RandomFlagGenerator(teams.size(), true);
        flags = randomFlagGenerator.getFlags();

        RowGenerator rowGenerator = new RowGenerator(activity, teams.size());
        rows = rowGenerator.getRows();

        // generate teams
        TeamNameGenerator teamNameGenerator = new TeamNameGenerator(activity);
        teamNameList = teamNameGenerator.getTeamFullNames();
    }

}
