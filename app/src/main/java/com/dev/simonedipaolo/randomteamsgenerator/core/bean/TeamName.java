package com.dev.simonedipaolo.randomteamsgenerator.core.bean;

/**
 * Created by Simone Di Paolo on 27/01/2023.
 */
public class TeamName {

    private final String firstName;
    private final String secondName;
    private final String teamFullName;

    public TeamName(String firstName, String secondName) {
        this.firstName = firstName;
        this.secondName = secondName;
        teamFullName = firstName + " " + secondName;
    }

    public String getTeamFullName() {
        return teamFullName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

}
