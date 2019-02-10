package com.example.collegeproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.collegeproject.helper.DatabaseHelper;
import com.example.collegeproject.model.Team;

import java.util.ArrayList;

public class SetTeam extends AppCompatActivity {

    long teamId;

    EditText teamName, captain;
    ArrayList<EditText> playersEditText = new ArrayList<>();

    ArrayList<String> playersList = new ArrayList<>();
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_team);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        teamId = getIntent().getExtras().getInt("id");

        teamName = findViewById(R.id.textTeamName);
        captain = findViewById(R.id.textTeamCaptain);
        playersEditText.add((EditText)findViewById(R.id.textTeamp2));
        playersEditText.add((EditText)findViewById(R.id.textTeamp3));
        playersEditText.add((EditText)findViewById(R.id.textTeamp4));
        playersEditText.add((EditText)findViewById(R.id.textTeamp5));
        playersEditText.add((EditText)findViewById(R.id.textTeamp6));
        playersEditText.add((EditText)findViewById(R.id.textTeamp7));
        playersEditText.add((EditText)findViewById(R.id.textTeamp8));
        playersEditText.add((EditText)findViewById(R.id.textTeamp9));
        playersEditText.add((EditText)findViewById(R.id.textTeamp10));
        playersEditText.add((EditText)findViewById(R.id.textTeamp11));

    }

    public void submitTeam(View view) {
        if(teamName.getText().toString().trim().length() > 0){
            if(captain.getText().toString().trim().length() > 0){
                for(EditText textPlayer: playersEditText){
                    if(textPlayer.getText().toString().trim().length() > 0){
                        playersList.add(textPlayer.getText().toString().trim());
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Players Name is Empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                Team team = new Team(teamId, teamName.getText().toString().trim());
                long id = databaseHelper.addTeam(team,captain.getText().toString().trim(), playersList);

                if(id > 0){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), id + "", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(getApplicationContext(), "Captain Name is Empty", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        else{
            Toast.makeText(getApplicationContext(), "Team Name is Empty!", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
