package com.dev.simonedipaolo.randomteamsgenerator.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.dev.simonedipaolo.randomteamsgenerator.R;
import com.dev.simonedipaolo.randomteamsgenerator.models.Person;
import com.dev.simonedipaolo.randomteamsgenerator.utils.PersonRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class NamesListFragment extends Fragment implements PersonRecyclerViewAdapter.PersonEventListener {

    private static final String FIRST_PERSON_KEY = "first_person";

    private Person firstPerson;
    private List<Person> personList;

    private PersonRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private Button addButton;
    private Button generateTeamsButton;


    public NamesListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_names_list, container, false);

        personList = new ArrayList<>();

        if (getArguments() != null) {
            String personName = (String) getArguments().get(FIRST_PERSON_KEY);
            firstPerson = new Person(personName);
            personList.add(firstPerson);
        }

        addButton = v.findViewById(R.id.addOtherNamesButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = createAddNameAlertDialog(getActivity()).create();
                dialog.show();
            }
        });

        generateTeamsButton = v.findViewById(R.id.generateTeamsButton);
        // disabling until there are at least 3 people
        generateTeamsButton.setEnabled(false);

        // Inflate the layout for this fragment
        recyclerView = v.findViewById(R.id.namesRecyclerView);

        // setting layout
        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
        linearLayout.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayout);

        adapter = new PersonRecyclerViewAdapter(personList, getActivity(), this);
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

    /**
     * Method implemented from inner interface of PersonEventListener
     * that make easy to disable GENERATE TEAMS button from the adapter
     */
    @Override
    public void disableGenerateTeams() {
        generateTeamsButton.setEnabled(false);
    }
}