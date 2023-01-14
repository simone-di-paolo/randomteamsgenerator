package com.dev.simonedipaolo.randomteamsgenerator.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.simonedipaolo.randomteamsgenerator.R;
import com.dev.simonedipaolo.randomteamsgenerator.models.Person;

import java.util.List;

/**
 * Created by Simone Di Paolo on 14/01/2023.
 */
public class PersonRecyclerVIewAdapter extends RecyclerView.Adapter<PersonRecyclerVIewAdapter.ViewHolder> {

    private List<Person> personList;
    private Context context;

    public PersonRecyclerVIewAdapter(List<Person> personList, Context context) {
        this.personList = personList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context.getApplicationContext()).inflate(
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
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        private TextView personName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            personName = itemView.findViewById(R.id.nameTextView);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.nameTextView:
                    Toast.makeText(context, "Clicked on name", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.editButton:
                    Toast.makeText(context, "Clicked on edit", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.deleteButton:
                    Toast.makeText(context, "Clicked on delete", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
