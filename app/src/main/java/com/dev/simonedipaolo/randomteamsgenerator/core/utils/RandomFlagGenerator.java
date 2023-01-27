package com.dev.simonedipaolo.randomteamsgenerator.core.utils;

import com.dev.simonedipaolo.randomteamsgenerator.core.bean.Flag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by Simone Di Paolo on 27/01/2023.
 */
public class RandomFlagGenerator {

    private static final String[] colors = new String[] {"#d32f2f", "#e91e63", "#9c27b0", "#673ab7",
            "#303f9f", "#1976d2", "#039be5", "#00bcd4", "#00897b", "#4caf50", "#8bc34a", "#cddc39",
            "#ffeb3b", "#ffc107", "#ff9800", "#e65100", "#ff5722", "#795548", "#607d8b"};
    private List<Flag> flags;
    private boolean makeCenterColorWhite;

    public RandomFlagGenerator(int howManyFlags, boolean makeCenterColorWhite) {
        flags = new ArrayList<>();
        this.makeCenterColorWhite = makeCenterColorWhite;
        generateFlags(howManyFlags);
    }

    private void generateFlags(int howManyFlags) {
        List<String> colorsList = new LinkedList<>(Arrays.asList(colors));

        Random random = new Random();
        for(int i=0; i<howManyFlags; i++) {

            // refilling colors list
            if(colorsList.size() < 3) {
                colorsList = new LinkedList<>(Arrays.asList(colors));
            }

            String firstColor = colorsList.remove(random.nextInt(colorsList.size()));
            String secondColor = "#ffffff";
            if(!makeCenterColorWhite) {
                secondColor = colorsList.remove(random.nextInt(colorsList.size()));
            }
            String thirdColor = colorsList.remove(random.nextInt(colorsList.size()));

            Flag flag = new Flag(firstColor, secondColor, thirdColor);
            flags.add(flag);
        }
    }

    public List<Flag> getFlags() {
        return flags;
    }
}
