package com.example.collegeproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.collegeproject.model.Player;

import java.util.List;

public class ListBatsmanCustomAdaptor extends ArrayAdapter<Player> {
    LayoutInflater layoutInflater;
    List<Player> listBatsmans;
    Context context;
    int resource;

    public ListBatsmanCustomAdaptor(Context context, int resource, List<Player> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.listBatsmans = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        layoutInflater = LayoutInflater.from(context);

        convertView = layoutInflater.inflate(resource, parent, false);

        Player list = listBatsmans.get(position);
        TextView batsmanListViewBatsmanName = convertView.findViewById(R.id.batsmanListViewBatsmanName);

        batsmanListViewBatsmanName.setText(list.getPlayers());

        return  convertView;
    }
}
