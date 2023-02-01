package com.dev.simonedipaolo.randomteamsgenerator.core.utils;

import android.content.Context;
import android.util.Log;

import com.dev.simonedipaolo.randomteamsgenerator.core.bean.Row;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Simone Di Paolo on 01/02/2023.
 */
public class RowGenerator {

    private Context context;
    private List<Row> rows;
    private int howMany;

    public RowGenerator() {
        rows = new ArrayList<>();
        howMany = 0;
        context = null;
    }

    public RowGenerator(Context context, int howMany) {
        rows = new ArrayList<>();
        this.howMany = howMany;
        this.context = context;
        createRows();
    }

    private void createRows() {
        try {
            List<String> colors = Utils.readListFromJson(context, "colors.json");
            Collections.shuffle(colors);
            for(int i=0; i<howMany; i++) {
                rows.add(new Row(colors.get(i)));
            }
            Collections.shuffle(rows);
        } catch (IOException e){
            e.printStackTrace();
            Log.i("RowGenerator", "IOException launched and cached.");
        }
    }

    // ===== GETTERS & SETTERS =====

    public List<Row> getRows() {
        return rows;
    }

}
