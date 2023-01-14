package com.dev.simonedipaolo.randomteamsgenerator.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.dev.simonedipaolo.randomteamsgenerator.R;
import com.dev.simonedipaolo.randomteamsgenerator.models.Person;

import org.apache.commons.lang3.ObjectUtils;

public class MainFragment extends Fragment {

    private static final String FIRST_PERSON_KEY = "first_person";

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

                NamesListFragment namesListFragment = new NamesListFragment();
                Bundle args = new Bundle();
                args.putSerializable(FIRST_PERSON_KEY, tempPerson);
                namesListFragment.setArguments(args);
                FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
                if(ObjectUtils.isNotEmpty(supportFragmentManager)) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentAnchor, namesListFragment)
                            .addToBackStack(null)
                            .commit();
                } else {
                    Log.e("SOPPORT MANAGER IS NULL", "MainFragment");
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

}