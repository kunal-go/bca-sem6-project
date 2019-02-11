package com.example.collegeproject.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.collegeproject.SetToss;
import com.example.collegeproject.model.Team;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Meta Data
    public static final String DATABASE_NAME = "Scoreboard.db";
    public static final int DATABASE_VERSION = 8;

    //Tables
    public static final String TABLE_TEAM = "teams";
    public static final String TABLE_PLAYER = "players";
    public static final String TABLE_INNING = "innings";
    public static final String TABLE_BATSMAN = "batsmans";
    public static final String TABLE_BOWLER = "bowlers";

    //Common Columns
    public static final String COLUMN_ID = "_id";

    //Table Team Columns
    public static final String COLUMN_TEAM_NAME = "name";

    //Table Players Columns
    public static final String COLUMN_PLAYER_NAME = "name";
    public static final String COLUMN_PLAYER_TEAM = "team";
    public static final String COLUMN_PLAYER_CAPTAIN = "captain";

    //Table Innings Columns
    public static final String COLUMN_INNING_TEAM = "team";
    public static final String COLUMN_INNING_OVERS = "overs";
    public static final String COLUMN_INNING_DECLARE = "declare";
    public static final String COLUMN_INNING_RUNS = "runs";
    public static final String COLUMN_INNING_WICKETS = "wickets";
    public static final String COLUMN_INNING_BALLS = "balls";

    //Table Batsmans Columns
    public static final String COLUMN_BATSMAN_PLAYER = "player";
    public static final String COLUMN_BATSMAN_TEAM = "team";
    public static final String COLUMN_BATSMAN_INNING = "inning";
    public static final String COLUMN_BATSMAN_POSITION = "position";
    public static final String COLUMN_BATSMAN_RUNS = "runs";
    public static final String COLUMN_BATSMAN_BALLS = "balls";
    public static final String COLUMN_BATSMAN_STATUS = "status";

    //Table Bowlers Columns
    public static final String COLUMN_BOWLER_PLAYER = "player";
    public static final String COLUMN_BOWLER_TEAM = "team";
    public static final String COLUMN_BOWLER_INNING = "inning";
    public static final String COLUMN_BOWLER_BALLS = "balls";
    public static final String COLUMN_BOWLER_RUNS = "runs";
    public static final String COLUMN_BOWLER_WICKETS = "wickets";

    //Create Table Queries
    public static final String CREATE_TABLE_TEAM = "CREATE TABLE IF NOT EXISTS " + TABLE_TEAM + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_TEAM_NAME + " TEXT" +
            ")";

    public static final String CREATE_TABLE_PLAYER = "CREATE TABLE IF NOT EXISTS " + TABLE_PLAYER + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_PLAYER_NAME + " TEXT," +
            COLUMN_PLAYER_TEAM + " NUMBER," +
            COLUMN_PLAYER_CAPTAIN + " NUMBER DEFAULT 0" +
            ")";

    public static final String CREATE_TABLE_INNING = "CREATE TABLE IF NOT EXISTS " + TABLE_INNING + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_INNING_TEAM + " NUMBER," +
            COLUMN_INNING_OVERS + " NUMBER," +
            COLUMN_INNING_DECLARE + " NUMBER NOT NULL DEFAULT 0," +
            COLUMN_INNING_RUNS + " NUMBER NOT NULL DEFAULT 0," +
            COLUMN_INNING_WICKETS + " NUMBER NOT NULL DEFAULT 0," +
            COLUMN_INNING_BALLS + " NUMBER NOT NULL DEFAULT 0" +
            ")";

    public static final String CREATE_TABLE_BATSMAN = "CREATE TABLE IF NOT EXISTS " + TABLE_BATSMAN + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_BATSMAN_PLAYER + " NUMBER," +
            COLUMN_BATSMAN_TEAM + " NUMBER," +
            COLUMN_BATSMAN_INNING + " NUMBER," +
            COLUMN_BATSMAN_POSITION + " NUMBER," +
            COLUMN_BATSMAN_RUNS + " NUMBER DEFAULT 0," +
            COLUMN_BATSMAN_BALLS + " NUMBER DEFAULT 0," +
            COLUMN_BATSMAN_STATUS + " NUMBER DEFAULT 0" +
            ")";

    public static final String CREATE_TABLE_BOWLER = "CREATE TABLE IF NOT EXISTS " + TABLE_BOWLER + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_BOWLER_PLAYER + " NUMBER," +
            COLUMN_BOWLER_TEAM + " NUMBER," +
            COLUMN_BOWLER_INNING + " NUMBER," +
            COLUMN_BOWLER_BALLS + " NUMBER," +
            COLUMN_BOWLER_RUNS + " NUMBER DEFAULT 0," +
            COLUMN_BOWLER_WICKETS + " NUMBER DEFAULT 0" +
            ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TEAM);
        db.execSQL(CREATE_TABLE_PLAYER);
        db.execSQL(CREATE_TABLE_INNING);
        db.execSQL(CREATE_TABLE_BATSMAN);
        db.execSQL(CREATE_TABLE_BOWLER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INNING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BATSMAN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOWLER);

        onCreate(db);
    }

    public long addTeam(Team team, String captain, ArrayList<String> playersList){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, team.getId());
        values.put(COLUMN_TEAM_NAME, team.getName());

        db.beginTransaction();
        try {
            long teamId = db.insert(TABLE_TEAM, null, values);

            addCaptain(teamId, captain);

            for (String player : playersList) {
                addPlayer(teamId, player);
            }

            db.setTransactionSuccessful();
            return teamId;
        }
        catch (Exception e){
            return 0;
        }
        finally {
            db.endTransaction();
        }
    }

    public long addPlayer(long team, String name){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYER_NAME, name);
        values.put(COLUMN_PLAYER_TEAM, team);

        return db.insert(TABLE_PLAYER, null, values);
    }

    public boolean hasMatchStarted(){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_INNING, new String[]{COLUMN_ID}, null, null, null, null, null);

        if(cursor.getCount() > 0){
            return true;
        }
        return false;
    }

    public long addCaptain(long team, String name){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYER_NAME, name);
        values.put(COLUMN_PLAYER_TEAM, team);
        values.put(COLUMN_PLAYER_CAPTAIN, 1);

        return db.insert(TABLE_PLAYER, null, values);
    }

    public boolean isTeamsSet(){
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT " + COLUMN_ID +
                " FROM " + TABLE_TEAM +
                " WHERE " + COLUMN_ID + " = ? OR " + COLUMN_ID + " = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{1 + "", 2 + ""});
        if(cursor.getCount() == 2){
            return true;
        }
        return false;
    }

    public Cursor getTeams(){
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * " +
                " FROM " + TABLE_TEAM ;
        return db.rawQuery(sql, null);
    }

    public boolean isInningDeclared(int inning){
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT " + COLUMN_INNING_DECLARE +
                " FROM " + TABLE_INNING +
                " WHERE " + COLUMN_ID + " = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{inning + ""});

        if(!cursor.moveToFirst()) {
            return false;
        }
        else if(cursor.getInt(cursor.getColumnIndex(COLUMN_INNING_DECLARE)) == 1){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean setInning(int bat_first_team, int overs){
        SQLiteDatabase db = getWritableDatabase();
        int bat_second = 1;

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, 1);
        values.put(COLUMN_INNING_TEAM, bat_first_team);
        values.put(COLUMN_INNING_OVERS, overs);

        if(bat_first_team == 1){
            bat_second = 2;
        }

        ContentValues values2 = new ContentValues();
        values2.put(COLUMN_ID, 2);
        values2.put(COLUMN_INNING_TEAM, bat_second);
        values2.put(COLUMN_INNING_OVERS, overs);

        db.beginTransaction();
        try {
            if(db.insert(TABLE_INNING, null, values) > 0){
                if(db.insert(TABLE_INNING, null, values2) > 0){
                    return true;
                }
            }
            throw new Exception();
        }
        catch (Exception e){
            return false;
        }
        finally {
            db.endTransaction();
        }
    }

    public Cursor getCurrentBatsmans(int inning){
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * " +
                " FROM " + TABLE_BATSMAN +
                " WHERE " + COLUMN_BATSMAN_INNING + " = ? AND " +
                COLUMN_BATSMAN_STATUS + " = ?";
        return db.rawQuery(sql, new String[]{inning + "", "0"});
    }

    public Cursor getAllBatsmans(int inning){
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * " +
                " FROM " + TABLE_PLAYER +
                " WHERE " + COLUMN_PLAYER_TEAM + " = " + " (SELECT " + COLUMN_INNING_TEAM +" FROM "+ TABLE_INNING +" WHERE "+ COLUMN_ID+" = ?) ";
        return db.rawQuery(sql, new String[]{inning + ""});
    }

    public int getNewBatsmanPosition(int inning){
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT MAX(" + COLUMN_BATSMAN_POSITION + ") AS max_position" +
                " FROM " + TABLE_BATSMAN +
                " WHERE " + COLUMN_BATSMAN_INNING + " = ? ";
        Cursor cursor = db.rawQuery(sql, new String[]{inning + ""});
        cursor.moveToNext();
        return cursor.getInt(cursor.getColumnIndex("max_position")) + 1;
    }

    public boolean declareInning(int inning){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_INNING_DECLARE, 1);

        try {
            db.update(TABLE_INNING, values, COLUMN_ID + " = ?", new String[]{inning + ""});
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public void resetDatabase(){
        SQLiteDatabase db = getWritableDatabase();

        db.delete(TABLE_TEAM, null, null);
        db.delete(TABLE_PLAYER, null, null);
    }
}
