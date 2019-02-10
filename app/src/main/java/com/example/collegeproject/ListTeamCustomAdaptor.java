package com.example.collegeproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.collegeproject.model.Team;

import java.util.List;

public class ListTeamCustomAdaptor extends ArrayAdapter<Team> {
    LayoutInflater layoutInflater;
    List<Team> listTeams;
    Context context;
    int resource;

    public ListTeamCustomAdaptor(Context context, int resource, List<Team> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.listTeams = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        layoutInflater = LayoutInflater.from(context);

        convertView = layoutInflater.inflate(resource, parent, false);

        Team list = listTeams.get(position);
        TextView teamListViewTeamName = convertView.findViewById(R.id.teamListViewTeamName);

        teamListViewTeamName.setText(list.getName());

        return  convertView;
    }

}
