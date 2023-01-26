package com.dev.simonedipaolo.randomteamsgenerator.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.simonedipaolo.randomteamsgenerator.R;
import com.dev.simonedipaolo.randomteamsgenerator.models.Person;
import com.dev.simonedipaolo.randomteamsgenerator.utils.PersonRecyclerViewAdapter;
import com.dev.simonedipaolo.randomteamsgenerator.utils.TeamsRecyclerViewAdapter;

import java.util.Arrays;
import java.util.List;

public class GeneratedTeamsFragment extends Fragment {

    private RecyclerView recyclerView;
    private TeamsRecyclerViewAdapter adapter;
    private List<Person> personList;

    public GeneratedTeamsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_generated_teams, container, false);

        Person[] personArray = GeneratedTeamsFragmentArgs.fromBundle(getArguments()).getPersonList();
        personList = Arrays.asList(personArray);

        recyclerViewInit(view);

        return view;
    }


    private void recyclerViewInit(View view) {
        // Inflate the layout for this fragment
        recyclerView = view.findViewById(R.id.generatedTeamsRecyclerView);
        // setting layout
        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
        linearLayout.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayout);

        adapter = new TeamsRecyclerViewAdapter(getActivity(), personList);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }

}
