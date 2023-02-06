package com.dev.simonedipaolo.randomteamsgenerator.core.utils;

import android.content.Context;

import com.dev.simonedipaolo.randomteamsgenerator.core.bean.TeamName;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Simone Di Paolo on 27/01/2023.
 */
public class TeamNameGenerator {
    private static final Type REVIEW_TYPE = new TypeToken<ArrayList<String>>() {}.getType();

    private final List<TeamName> teamFullNames;
    private List<String> firstNamesFromJson;
    private List<String> secondNamesFromJson;

    public TeamNameGenerator(Context context) {
        this.teamFullNames = new ArrayList<>();
        try {
            //getGsonfromJson();
            firstNamesFromJson = Utils.readListFromJson(context, "firstNames.json");
            secondNamesFromJson = Utils.readListFromJson(context, "secondNames.json");
            populateTeamsNamesList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void populateTeamsNamesList() {
        Collections.shuffle(firstNamesFromJson);
        Collections.shuffle(secondNamesFromJson);
        for(int i=0; i<firstNamesFromJson.size(); i++) {
            teamFullNames.add(new TeamName(firstNamesFromJson.remove(0), secondNamesFromJson.remove(0)));
        }
    }

    public List<String> getFirstNamesFromJson() {
        return firstNamesFromJson;
    }

    public List<String> getSecondNamesFromJson() {
        return secondNamesFromJson;
    }

    public List<TeamName> getTeamFullNames() {
        return teamFullNames;
    }
}
