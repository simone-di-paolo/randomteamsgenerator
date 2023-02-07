package com.dev.simonedipaolo.randomteamsgenerator.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.simonedipaolo.randomteamsgenerator.R;
import com.dev.simonedipaolo.randomteamsgenerator.core.bean.Flag;
import com.dev.simonedipaolo.randomteamsgenerator.core.bean.Row;
import com.dev.simonedipaolo.randomteamsgenerator.core.bean.TeamName;
import com.dev.simonedipaolo.randomteamsgenerator.models.Person;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by Simone Di Paolo on 14/01/2023.
 */
public class TeamsRecyclerViewAdapter extends RecyclerView.Adapter<TeamsRecyclerViewAdapter.ViewHolder> {

    private final Context context;
    private final List<List<Person>> teams;

    private final List<Flag> flags;
    private final List<TeamName> teamNameList;
    private final List<Row> rows;

    public TeamsRecyclerViewAdapter(Context context, List<List<Person>> teams, List<TeamName> teamNameList, List<Flag> flags, List<Row> rows) {
        this.context = context;
        this.teams = teams;
        this.teamNameList = teamNameList;
        this.flags = flags;
        this.rows = rows;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(
                R.layout.row_teams,
                parent,
                false
        );

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Flag iFlag = flags.get(position);
        // creating the flag
        holder.flagFirstButton.setBackgroundColor(Color.parseColor(iFlag.getFirstColor()));
        holder.flagSecondButton.setBackgroundColor(Color.parseColor(iFlag.getSecondColor()));
        holder.flagThirdButton.setBackgroundColor(Color.parseColor(iFlag.getThirdColor()));

        // coloring constraint layout background
        holder.constraintLayout.setBackgroundColor(Color.parseColor(rows.get(position).getRowColor()));

        // create the team
        String teamNames = createStringWithNames(position);
        holder.membersNameTextView.setText(teamNames);

        holder.teamNumberTextView.setText(String.valueOf(position+1));

        holder.teamNameTextView.setText(teamNameList.get(position).getTeamFullName().toUpperCase());
    }

    @Override
    public int getItemCount() {
        return teams.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Button flagFirstButton;
        private final Button flagSecondButton;
        private final Button flagThirdButton;
        private final ConstraintLayout constraintLayout;

        private final AppCompatTextView membersNameTextView;
        private final AppCompatTextView teamNumberTextView;

        private final AppCompatTextView teamNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            flagFirstButton = itemView.findViewById(R.id.flagFirstButton);
            flagFirstButton.setClickable(false);

            flagSecondButton = itemView.findViewById(R.id.flagSecondButton);
            flagSecondButton.setClickable(false);

            flagThirdButton = itemView.findViewById(R.id.flagThirdButton);
            flagThirdButton.setClickable(false);

            constraintLayout = itemView.findViewById(R.id.constraintLayoutRow);

            membersNameTextView = itemView.findViewById(R.id.memeberNamesTextView);
            teamNumberTextView = itemView.findViewById(R.id.teamNumberTextView);
            teamNameTextView = itemView.findViewById(R.id.teamNameTextView);

        }

    }


    private void changeFragment(Fragment fragment) {
        FragmentActivity activity = (FragmentActivity) context;
        if(ObjectUtils.isNotEmpty(activity)) {
            FragmentManager supportFragmentManager = activity.getSupportFragmentManager();
            if (ObjectUtils.isNotEmpty(supportFragmentManager)) {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, fragment)
                        .commit();
            } else {
                Log.e("SUPPORT MANAGER IS NULL", "MainFragment");
            }
        } else {
            Log.e("activity it's null", "MainFragment");
        }
    }

    private String createStringWithNames(int index) {
        StringBuilder teamNames = new StringBuilder(StringUtils.EMPTY);
        List<Person> tempSingleTeam = teams.get(index);
        for (int i=0; i<tempSingleTeam.size(); i++) {
            teamNames.append(i + 1).append(" - ").append(tempSingleTeam.get(i).getName()).append("\n");
        }
        return teamNames.toString();
    }

}
