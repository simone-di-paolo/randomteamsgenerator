package com.dev.simonedipaolo.randomteamsgenerator.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.simonedipaolo.randomteamsgenerator.R;
import com.dev.simonedipaolo.randomteamsgenerator.fragments.MainFragment;
import com.dev.simonedipaolo.randomteamsgenerator.models.Person;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;

/**
 * Created by Simone Di Paolo on 14/01/2023.
 */
public class TeamsRecyclerViewAdapter extends RecyclerView.Adapter<TeamsRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Person> teams;

    public TeamsRecyclerViewAdapter(Context context, List<Person> teams) {
        this.context = context;
        this.teams = teams;
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
        // creating the flag
        holder.flagFirstButton.setBackgroundColor(1);
        holder.flagFirstButton.setBackgroundColor(1);
        holder.flagFirstButton.setBackgroundColor(1);
    }

    @Override
    public int getItemCount() {
        return teams.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private Button flagFirstButton;
        private Button flagSecondButton;
        private Button flagThirdButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            flagFirstButton = itemView.findViewById(R.id.flagFirstButton);
            flagSecondButton = itemView.findViewById(R.id.flagSecondButton);
            flagThirdButton = itemView.findViewById(R.id.flagThirdButton);
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

}
