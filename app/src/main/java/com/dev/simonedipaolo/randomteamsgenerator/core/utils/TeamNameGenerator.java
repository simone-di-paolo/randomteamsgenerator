package com.dev.simonedipaolo.randomteamsgenerator.core.utils;

import android.content.Context;
import android.content.res.AssetManager;

import com.dev.simonedipaolo.randomteamsgenerator.core.bean.TeamName;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Simone Di Paolo on 27/01/2023.
 */
public class TeamNameGenerator {
    private static final Type REVIEW_TYPE = new TypeToken<ArrayList<String>>() {}.getType();

    private List<TeamName> teamFullNames;
    private List<String> firstNamesFromJson;
    private List<String> secondNamesFromJson;

    private Context context;

    public TeamNameGenerator(Context context) {
        this.context = context;
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

    private void getGsonfromJson() throws IOException {

        // first file
        AssetManager assetManager = context.getAssets();
        InputStream is = assetManager.open("firstNames.json");
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        String jsonString = new String(buffer, "UTF-8");
        Gson gson = new Gson();
        firstNamesFromJson = gson.fromJson(jsonString, List.class);

        is = assetManager.open("secondNames.json");
        size = is.available();
        buffer = new byte[size];
        is.read(buffer);
        is.close();
        jsonString = new String(buffer, "UTF-8");
        secondNamesFromJson = gson.fromJson(jsonString, List.class);

        populateTeamsNamesList();
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
