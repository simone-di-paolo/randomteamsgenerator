package com.dev.simonedipaolo.randomteamsgenerator.core.bean;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Simone Di Paolo on 01/02/2023.
 */
public class Row {

    private final String rowColor;

    public Row() {
        rowColor = StringUtils.EMPTY;
    }

    public Row(String rowColor) {
        this.rowColor = rowColor;
    }

    public String getRowColor() {
        return rowColor;
    }

}
