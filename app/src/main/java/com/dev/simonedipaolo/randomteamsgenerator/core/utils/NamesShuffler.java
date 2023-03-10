package com.dev.simonedipaolo.randomteamsgenerator.core.utils;

import com.dev.simonedipaolo.randomteamsgenerator.models.Person;

import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Simone Di Paolo on 11/01/2023.
 */
public class NamesShuffler {

    private final List<Person> unmodifiebleNamesList;
    private List<Person> names;
    private int howManyTeams;

    private final List<List<Person>> teams;
    private final int howManyMissingNames;

    public NamesShuffler() {
        unmodifiebleNamesList = new ArrayList<>();
        teams = new ArrayList<>();
        howManyTeams = 2;
        howManyMissingNames = 0;
    }

    public NamesShuffler(List<Person> unmodifiebleNamesList, int howManyTeams) {
        this.unmodifiebleNamesList = unmodifiebleNamesList;
        names = new ArrayList<>();
        names.addAll(unmodifiebleNamesList);
        this.howManyTeams = howManyTeams;
        teams = new ArrayList<>();
        howManyMissingNames = 0;
        generateTeams();
    }

    private void generateTeams() {
        Collections.shuffle(names);
        if(names.size() > howManyTeams) {

            // creating n new empty teams
            for(int i=0; i<howManyTeams; i++) {
                teams.add(new ArrayList<>());
            }

            int i=0;
            while(ObjectUtils.isNotEmpty(names)) {
                int teamIndex = i%howManyTeams;
                if(names.size() > 1) {
                    Collections.shuffle(names);
                }
                teams.get(teamIndex).add(names.remove(0));
                i++;
            }

        }
    }

    // GETTERS

    public List<List<Person>> getTeams() {
        return teams;
    }

    public int getHowManyMissingNames() {
        return howManyMissingNames;
    }


    // SETTERS

    public void setNames(List<Person> names) {
        this.names = names;
    }

    public void setHowManyTeams(int howManyTeams) {
        this.howManyTeams = howManyTeams;
    }
}
