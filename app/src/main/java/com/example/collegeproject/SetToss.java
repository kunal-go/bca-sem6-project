package com.example.collegeproject;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.collegeproject.helper.DatabaseHelper;
import com.example.collegeproject.model.Team;

import java.util.ArrayList;
import java.util.List;

public class SetToss extends AppCompatActivity {

    ArrayList<Team> teamsList = new ArrayList<>();
    ListView listView;
    Cursor cursor;
    DatabaseHelper databaseHelper;
    int overs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_toss);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        listView = findViewById(R.id.setTossListView);

        cursor = databaseHelper.getTeams();
        while (cursor.moveToNext()) {
            teamsList.add(new Team(
                    cursor.getInt(0),
                    cursor.getString(1)
            ));
        }

        ListTeamCustomAdaptor listTeamCustomAdaptor = new ListTeamCustomAdaptor(getApplicationContext(), R.layout.team_list_list_view, teamsList);

        listView.setAdapter(listTeamCustomAdaptor);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EditText textOvers = findViewById(R.id.setTossOvers);
                if(textOvers.getText().toString().trim().length() > 0){
                    try{
                        overs = Integer.parseInt(textOvers.getText().toString().trim());
                    }
                    catch (Exception e){
                        Toast.makeText(SetToss.this, "Invalid Overs", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                else{
                    Toast.makeText(SetToss.this, "Enter Overs", Toast.LENGTH_SHORT).show();
                    return;
                }

                cursor.moveToPosition(position);
                if(databaseHelper.setInning(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)), overs)){
                    Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), Inning.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(SetToss.this, "Match is not started", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
