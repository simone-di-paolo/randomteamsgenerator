package com.dev.simonedipaolo.randomteamsgenerator.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.simonedipaolo.randomteamsgenerator.R;
import com.dev.simonedipaolo.randomteamsgenerator.core.bean.Flag;
import com.dev.simonedipaolo.randomteamsgenerator.core.bean.TeamName;
import com.dev.simonedipaolo.randomteamsgenerator.core.utils.NamesShuffler;
import com.dev.simonedipaolo.randomteamsgenerator.core.utils.RandomFlagGenerator;
import com.dev.simonedipaolo.randomteamsgenerator.core.utils.TeamNameGenerator;
import com.dev.simonedipaolo.randomteamsgenerator.fragments.GeneratedTeamsFragmentDirections;
import com.dev.simonedipaolo.randomteamsgenerator.models.Person;
import com.dev.simonedipaolo.randomteamsgenerator.adapters.TeamsRecyclerViewAdapter;
import com.google.android.material.appbar.MaterialToolbar;

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
    private NamesShuffler namesShuffler;

    // navigation
    private NavController navController;

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

        // TODO remove the + (add) button

        if (ObjectUtils.isNotEmpty(activity)) {
            // initializing personList and recycler view
            Bundle bundle = getArguments();
            if(ObjectUtils.isNotEmpty(bundle)) {
                Person[] personArray = GeneratedTeamsFragmentArgs.fromBundle(getArguments()).getPersonList();
                int howManyTeams = GeneratedTeamsFragmentArgs.fromBundle(getArguments()).getHowManyTeams();

                // LinkedList will be used because Arrays.asList will resturn an unmodificable wrapper of list
                personList = new LinkedList<>(Arrays.asList(personArray));
                namesShuffler = new NamesShuffler(personList, howManyTeams);
                teams = namesShuffler.getTeams();

            }

            recyclerViewInit(view);

            // ======== TOOL BAR ========

            //
            initializeToolbar(activity);

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

            // generate flags
            RandomFlagGenerator randomFlagGenerator = new RandomFlagGenerator(teams.size(), true);
            List<Flag> flags = randomFlagGenerator.getFlags();

            // generate teams
            TeamNameGenerator teamNameGenerator = new TeamNameGenerator(activity);
            List<TeamName> teamNameList = teamNameGenerator.getTeamFullNames();

            adapter = new TeamsRecyclerViewAdapter(activity, teams, teamNameList, flags);
            recyclerView.setAdapter(adapter);
            recyclerView.setHasFixedSize(true);
        } else {
            Log.i("GeneratedTeamsFragment", "recyclerViewInit - Activity it's empty.");
        }
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
                materialToolbar.setTitle(R.string.teams_title);
                materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Person[] personArray = new Person[personList.size()];
                        for(int i=0; i<personList.size(); i++) {
                            personArray[i] = personList.get(i);
                        }
                        GeneratedTeamsFragmentDirections.ActionGeneratedTeamsFragmentToNamesListFragment action
                                = GeneratedTeamsFragmentDirections.actionGeneratedTeamsFragmentToNamesListFragment(StringUtils.EMPTY, personArray);
                        navController.navigate(action);
                    }
                });

                materialToolbar.setTitle(R.string.names_title);
                materialToolbar.setVisibility(View.VISIBLE);
                materialToolbar.showOverflowMenu();
            }
        }
    }

}
