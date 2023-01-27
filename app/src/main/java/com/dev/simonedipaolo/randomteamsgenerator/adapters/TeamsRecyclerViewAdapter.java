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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.simonedipaolo.randomteamsgenerator.R;
import com.dev.simonedipaolo.randomteamsgenerator.core.bean.Flag;
import com.dev.simonedipaolo.randomteamsgenerator.core.bean.TeamName;
import com.dev.simonedipaolo.randomteamsgenerator.core.utils.RandomFlagGenerator;
import com.dev.simonedipaolo.randomteamsgenerator.core.utils.TeamNameGenerator;
import com.dev.simonedipaolo.randomteamsgenerator.models.Person;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by Simone Di Paolo on 14/01/2023.
 */
public class TeamsRecyclerViewAdapter extends RecyclerView.Adapter<TeamsRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<List<Person>> teams;

    private RandomFlagGenerator randomFlagGenerator;
    private List<Flag> flags;
    private TeamNameGenerator teamNameGenerator;
    private List<TeamName> teamNameList;

    public TeamsRecyclerViewAdapter(Context context, List<List<Person>> teams, boolean makeCenterColorWhite) {
        this.context = context;
        this.teams = teams;
        this.randomFlagGenerator = new RandomFlagGenerator(teams.size(), makeCenterColorWhite);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(
                R.layout.row_teams,
                parent,
                false
        );

        flags = randomFlagGenerator.getFlags();
        teamNameGenerator = new TeamNameGenerator(context);
        teamNameList = teamNameGenerator.getTeamFullNames();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Flag iFlag = flags.get(position);
        // creating the flag
        holder.flagFirstButton.setBackgroundColor(Color.parseColor(iFlag.getFirstColor()));
        holder.flagSecondButton.setBackgroundColor(Color.parseColor(iFlag.getSecondColor()));
        holder.flagThirdButton.setBackgroundColor(Color.parseColor(iFlag.getThirdColor()));

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
        private Button flagFirstButton;
        private Button flagSecondButton;
        private Button flagThirdButton;

        private AppCompatTextView membersNameTextView;
        private AppCompatTextView teamNumberTextView;

        private AppCompatTextView teamNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            flagFirstButton = itemView.findViewById(R.id.flagFirstButton);
            flagFirstButton.setClickable(false);

            flagSecondButton = itemView.findViewById(R.id.flagSecondButton);
            flagSecondButton.setClickable(false);

            flagThirdButton = itemView.findViewById(R.id.flagThirdButton);
            flagThirdButton.setClickable(false);

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
        String teamNames = StringUtils.EMPTY;
        List<Person> tempSingleTeam = teams.get(index);
        for (int i=0; i<tempSingleTeam.size(); i++) {
            teamNames += (i+1) + " - " + tempSingleTeam.get(i).getName() + "\n";
        }
        return teamNames;
    }

}
