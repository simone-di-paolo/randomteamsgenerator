package com.dev.simonedipaolo.randomteamsgenerator.core.bean;

/**
 * Created by Simone Di Paolo on 27/01/2023.
 */
public class Flag {

    private String firstColor;
    private String secondColor;
    private String thirdColor;

    public Flag(String firstColor, String secondColor, String thirdColor) {
        this.firstColor = firstColor;
        this.secondColor = secondColor;
        this.thirdColor = thirdColor;
    }

    public String getFirstColor() {
        return firstColor;
    }

    public void setFirstColor(String firstColor) {
        this.firstColor = firstColor;
    }

    public String getSecondColor() {
        return secondColor;
    }

    public void setSecondColor(String secondColor) {
        this.secondColor = secondColor;
    }

    public String getThirdColor() {
        return thirdColor;
    }

    public void setThirdColor(String thirdColor) {
        this.thirdColor = thirdColor;
    }
}
