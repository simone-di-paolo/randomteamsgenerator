package com.dev.simonedipaolo.randomteamsgenerator.core;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Simone Di Paolo on 11/01/2023.
 */
public class NamesShuffler {

    private List<String> names;
    private int howManyTeams;

    private List<List<String>> teams;
    private int howManyMissingNames;

    public NamesShuffler() {
        names = new ArrayList<>();
        teams = new ArrayList<>();
        howManyTeams = 2;
        howManyMissingNames = 0;
    }

    public NamesShuffler(List<String> names, int howManyTeams) {
        this.names = names;
        this.howManyTeams = howManyTeams;
        teams = new ArrayList<>();
        howManyMissingNames = 0;
    }

    public void generateTeams(boolean forceEvenWithMissingNames) {
        Collections.shuffle(names);
        if(names.size() > howManyTeams) {
            if (names.size() % howManyTeams == 0 || forceEvenWithMissingNames) {
                // teams that needs to be created
                int namesPerTeam = names.size() / howManyTeams;
                Random random = new Random();

                for(int i=0; i<howManyTeams; i++) {
                    List<String> tempNameList = new ArrayList<>();

                    for(int j=0; j<namesPerTeam; j++) {
                        if (CollectionUtils.isNotEmpty(names)) {
                            int randomNameIndex = random.nextInt(names.size() + 1);
                            tempNameList.add(names.remove(randomNameIndex));
                        }
                    }
                    teams.add(tempNameList);
                }
            } else {
                howManyMissingNames = names.size() % howManyTeams;
            }
        }
    }

    // GETTERS

    public List<List<String>> getTeams() {
        return teams;
    }

    public int getHowManyMissingNames() {
        return howManyMissingNames;
    }


    // SETTERS

    public void setNames(List<String> names) {
        this.names = names;
    }

    public void setHowManyTeams(int howManyTeams) {
        this.howManyTeams = howManyTeams;
    }
}
