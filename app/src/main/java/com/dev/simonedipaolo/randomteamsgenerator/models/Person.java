package com.dev.simonedipaolo.randomteamsgenerator.models;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * Created by Simone Di Paolo on 14/01/2023.
 */
public class Person implements Serializable {
    private String name;

    public Person() {
        this.name = StringUtils.EMPTY;
    }

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
