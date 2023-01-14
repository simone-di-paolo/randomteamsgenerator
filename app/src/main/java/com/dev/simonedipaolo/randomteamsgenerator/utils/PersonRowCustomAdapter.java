package com.dev.simonedipaolo.randomteamsgenerator.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dev.simonedipaolo.randomteamsgenerator.R;
import com.dev.simonedipaolo.randomteamsgenerator.models.Person;

import org.apache.commons.lang3.ObjectUtils;

import java.util.List;

/**
 * Created by Simone Di Paolo on 14/01/2023.
 */
public class PersonRowCustomAdapter extends ArrayAdapter<Person> implements View.OnClickListener {

    private List<Person> personList;
    private Context context;
    private int lastPosition = -1;

    public PersonRowCustomAdapter(List<Person> personList, Context context) {
        super(context, R.layout.row_names, personList);
        this.personList = personList;
        this.context = context;
    }

    @Override
    public void onClick(View view) {
        int position = (Integer) view.getTag();
        Person person = (Person) getItem(position);

        switch(view.getId()) {
            case R.id.nameTextView:
                // TODO do something like open modal for update name/something else
                Toast.makeText(context, "Clicked on text view", Toast.LENGTH_SHORT).show();
                break;
            case R.id.editButton:
                // TODO do something like open modal for update name/something else
                Toast.makeText(context, "Clicked on edit button", Toast.LENGTH_SHORT).show();
                break;
            case R.id.deleteButton:
                // TODO do something like open modal for update name/something else
                Toast.makeText(context, "Clicked on delete button", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // get data item for this position
        Person person = (Person) getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;

        final View result;

        if(ObjectUtils.isEmpty(convertView)) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_names, parent, false);
            viewHolder.personName = (TextView) convertView.findViewById(R.id.nameTextView);

            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.personName.setText(person.getName());
        return convertView;
    }

    // inner class
    private static class ViewHolder {
        TextView personName;
    }

}
