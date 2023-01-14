package com.dev.simonedipaolo.randomteamsgenerator.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.simonedipaolo.randomteamsgenerator.R;
import com.dev.simonedipaolo.randomteamsgenerator.models.Person;
import com.dev.simonedipaolo.randomteamsgenerator.utils.PersonRecyclerVIewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class NamesListFragment extends Fragment {

    private static final String FIRST_PERSON_KEY = "first_person";

    private Person firstPerson;
    private List<Person> personList;

    private RecyclerView recyclerView;


    public NamesListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        personList = new ArrayList<>();

        if (getArguments() != null) {
            firstPerson = (Person) getArguments().get(FIRST_PERSON_KEY);
            personList.add(firstPerson);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO aggiustare qui il layout della row
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_names_list, container, false);
        recyclerView = v.findViewById(R.id.namesRecyclerView);

        // setting layout
        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayout);

        recyclerView.setAdapter(new PersonRecyclerVIewAdapter(personList, getContext()));
        recyclerView.setHasFixedSize(true);
        return v;
    }

}