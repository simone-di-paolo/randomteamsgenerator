package com.dev.simonedipaolo.randomteamsgenerator.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.simonedipaolo.randomteamsgenerator.R;
import com.dev.simonedipaolo.randomteamsgenerator.fragments.MainFragment;
import com.dev.simonedipaolo.randomteamsgenerator.fragments.NamesListFragment;
import com.dev.simonedipaolo.randomteamsgenerator.models.Person;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.util.EventListener;
import java.util.List;

/**
 * Created by Simone Di Paolo on 14/01/2023.
 */
public class PersonRecyclerViewAdapter extends RecyclerView.Adapter<PersonRecyclerViewAdapter.ViewHolder> {

    private List<Person> personList;
    private Context context;
    private PersonEventListener listener;

    public PersonRecyclerViewAdapter(List<Person> personList, Context context, PersonEventListener listener) {
        this.personList = personList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(
                R.layout.row_names,
                parent,
                false
        );

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Person person = personList.get(position);
        holder.personName.setText(person.getName());
        holder.personName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = editNameAlertDialog(holder.getLayoutPosition());
                alertDialog.show();
            }
        });

        holder.deleteImageButton.setImageResource(R.drawable.ic_baseline_delete_24);
        holder.deleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                personList.remove(holder.getLayoutPosition());
                notifyItemRemoved(holder.getLayoutPosition());

                if(CollectionUtils.isEmpty(personList)) {
                    changeFragment(new MainFragment());
                } else {
                    if(personList.size() < 3) {
                        listener.disableGenerateTeams();
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return personList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView personName;
        private AppCompatImageButton deleteImageButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            personName = itemView.findViewById(R.id.nameTextView);
            deleteImageButton = itemView.findViewById(R.id.deleteButton);
        }

    }

    // alert dialog
    private AlertDialog.Builder editNameAlertDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Type a name:");

        final EditText input = new EditText(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        input.setLayoutParams(lp);
        builder.setView(input);

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String personName = input.getText().toString();
                Person tempPerson = new Person(personName);

                if(personList != null) {
                    personList.set(position, tempPerson);
                    notifyItemChanged(position);
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        return builder;
    }

    private void changeFragment(Fragment fragment) {
        FragmentActivity activity = (FragmentActivity) context;
        if(ObjectUtils.isNotEmpty(activity)) {
            FragmentManager supportFragmentManager = activity.getSupportFragmentManager();
            if (ObjectUtils.isNotEmpty(supportFragmentManager)) {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentAnchor, fragment)
                        .commit();
            } else {
                Log.e("SUPPORT MANAGER IS NULL", "MainFragment");
            }
        } else {
            Log.e("activity it's null", "MainFragment");
        }
    }

    public interface PersonEventListener {
        void disableGenerateTeams();
    }
}
