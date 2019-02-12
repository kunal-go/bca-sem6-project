package com.example.collegeproject.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.collegeproject.model.Team;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Meta Data
    public static final String DATABASE_NAME = "Scoreboard.db";
    public static final int DATABASE_VERSION = 13;

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
    public static final String COLUMN_BATSMAN_STRIKE = "strike";

    //Table Bowlers Columns
    public static final String COLUMN_BOWLER_PLAYER = "player";
    public static final String COLUMN_BOWLER_TEAM = "team";
    public static final String COLUMN_BOWLER_INNING = "inning";
    public static final String COLUMN_BOWLER_BALLS = "balls";
    public static final String COLUMN_BOWLER_RUNS = "runs";
    public static final String COLUMN_BOWLER_WICKETS = "wickets";
    public static final String COLUMN_BOWLER_STATUS = "status";

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
            COLUMN_BATSMAN_RUNS + " NUMBER NOT NULL DEFAULT 0," +
            COLUMN_BATSMAN_BALLS + " NUMBER NOT NULL DEFAULT 0," +
            COLUMN_BATSMAN_STATUS + " NUMBER NOT NULL DEFAULT 0," +
            COLUMN_BATSMAN_STRIKE + " NUMBER NOT NULL DEFAULT 0" +
            ")";

    public static final String CREATE_TABLE_BOWLER = "CREATE TABLE IF NOT EXISTS " + TABLE_BOWLER + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_BOWLER_PLAYER + " NUMBER," +
            COLUMN_BOWLER_TEAM + " NUMBER," +
            COLUMN_BOWLER_INNING + " NUMBER," +
            COLUMN_BOWLER_BALLS + " NUMBER NOT NULL DEFAULT 0," +
            COLUMN_BOWLER_RUNS + " NUMBER NOT NULL DEFAULT 0," +
            COLUMN_BOWLER_WICKETS + " NUMBER NOT NULL DEFAULT 0," +
            COLUMN_BOWLER_STATUS + " NUMBER NOT NULL DEFAULT 0" +
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

        String sql = "SELECT *" +
                " FROM " + TABLE_INNING +
                " WHERE " + COLUMN_ID + " = " + inning +
                " AND " + COLUMN_INNING_DECLARE + " = 1";
        Cursor cursor = db.rawQuery(sql, null);

        if(cursor.getCount() > 0){
            return true;
        }
        return false;
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

        String sql = "INSERT INTO " + TABLE_INNING + " ("+ COLUMN_ID+", "+ COLUMN_INNING_TEAM +", "+ COLUMN_INNING_OVERS +")" +
                " VALUES ("+ 1 +", "+ bat_first_team +", "+ overs +")";

        String sql2 = "INSERT INTO " + TABLE_INNING + " ("+ COLUMN_ID+", "+ COLUMN_INNING_TEAM +", "+ COLUMN_INNING_OVERS +")" +
                " VALUES ("+ 2 +", "+ bat_second +", "+ overs +")";

        try {
            db.execSQL(sql);
            db.execSQL(sql2);
            return true;
        }
        catch (Exception e){
            return false;
        }

    }

    public Cursor getCurrentBatsmans(int inning){
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * " +
                " FROM " + TABLE_BATSMAN +
                " WHERE " + COLUMN_BATSMAN_INNING + " = ? AND " +
                COLUMN_BATSMAN_STATUS + " = ?" +
                " ORDER BY " + COLUMN_BATSMAN_STRIKE + " DESC";
        return db.rawQuery(sql, new String[]{inning + "", "0"});
    }

    public Cursor getAllBatsmans(int inning){
        SQLiteDatabase db = getReadableDatabase();

        String sql2 = "SELECT * " +
                " FROM " + TABLE_PLAYER +
                " WHERE " + COLUMN_PLAYER_TEAM + " = ? ";
        return db.rawQuery(sql2, new String[]{getTeamId(inning) + ""});
    }

    public long getTeamId(int inning){
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT " + COLUMN_INNING_TEAM +" FROM "+ TABLE_INNING +" WHERE "+ COLUMN_ID +" = ?";
        Cursor cursor = db.rawQuery(sql , new String[]{inning + ""});
        cursor.moveToNext();
        return cursor.getLong(cursor.getColumnIndex(COLUMN_INNING_TEAM));
    }

    public long getBowlingTeamId(int inning){
        int otherInning = 2;
        if(inning == 2){
            otherInning = 1;
        }
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT " + COLUMN_INNING_TEAM +" FROM "+ TABLE_INNING +" WHERE "+ COLUMN_ID +" = ?";
        Cursor cursor = db.rawQuery(sql , new String[]{otherInning + ""});
        cursor.moveToNext();
        return cursor.getLong(cursor.getColumnIndex(COLUMN_INNING_TEAM));
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

        String sql = "UPDATE " + TABLE_INNING +
                " SET " + COLUMN_INNING_DECLARE + " = 1" +
                " WHERE " + COLUMN_ID + " = " + inning;

        try {
            db.execSQL(sql);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public boolean isBatsmanAdded(int inning, int player){
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT " + COLUMN_ID +
                " FROM " + TABLE_BATSMAN +
                " WHERE " + COLUMN_BATSMAN_INNING + " = ? " +
                " AND " + COLUMN_BATSMAN_PLAYER + " = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{
                inning + "",
                player + ""
        });

        if(cursor.getCount() > 0){
            return true;
        }
        return false;
    }

    public boolean isBowlerAdded(int inning, int player){
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT " + COLUMN_ID +
                " FROM " + TABLE_BOWLER +
                " WHERE " + COLUMN_BOWLER_INNING + " = ? " +
                " AND " + COLUMN_BOWLER_PLAYER + " = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{
                inning + "",
                player + ""
        });

        if(cursor.getCount() > 0){
            return true;
        }
        return false;
    }

    public String getPlayerName(int player){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT " + COLUMN_PLAYER_NAME +
                " FROM " + TABLE_PLAYER +
                " WHERE " + COLUMN_ID + " = " + player;
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToNext();
        return cursor.getString(cursor.getColumnIndex(COLUMN_PLAYER_NAME));
    }

    public boolean addBatsman(int inning, int player){
        if(isBatsmanAdded(inning, player)){
            return false;
        }

        SQLiteDatabase db = getWritableDatabase();
        String sql = "INSERT INTO " + TABLE_BATSMAN + " ("+ COLUMN_BATSMAN_PLAYER +", "+ COLUMN_BATSMAN_TEAM +", "+ COLUMN_BATSMAN_INNING +", "+ COLUMN_BATSMAN_POSITION +")" +
                " VALUES ("+ player +", "+ getTeamId(inning) +", "+ inning +", "+ getNewBatsmanPosition(inning) +")";
        try{
            db.execSQL(sql);
            return true;
        }
        catch(Exception e){
            return false;
        }
    }

    public Cursor getCurrentBowlers(int inning){
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * " +
                " FROM " + TABLE_BOWLER +
                " WHERE " + COLUMN_BOWLER_INNING + " = ? AND " +
                COLUMN_BOWLER_STATUS + " = ?";
        return db.rawQuery(sql, new String[]{inning + "", "1"});
    }

    public Cursor getAllBowlers(int inning){
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * " +
                " FROM " + TABLE_PLAYER +
                " WHERE " + COLUMN_PLAYER_TEAM + " = ? ";
        return db.rawQuery(sql, new String[]{getBowlingTeamId(inning) + ""});
    }

    public Cursor getInningScore(int inning){
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * " +
                " FROM " + TABLE_INNING +
                " WHERE " + COLUMN_ID + " = " + inning;
        return db.rawQuery(sql, null);
    }

    public boolean addBowler(int inning, int player){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "INSERT INTO " + TABLE_BOWLER + " ("+ COLUMN_BOWLER_PLAYER +", "+ COLUMN_BOWLER_TEAM +", "+ COLUMN_BOWLER_INNING +", "+ COLUMN_BOWLER_STATUS +")" +
                " VALUES ("+ player +", "+ getBowlingTeamId(inning) +", "+ inning +", 1)";

        if(isBowlerAdded(inning, player)){
           sql = "UPDATE " + TABLE_BOWLER + " SET " + COLUMN_BOWLER_STATUS + " = 1" +
                   " WHERE " + COLUMN_BOWLER_PLAYER + " = " + player;
        }

        String sql2 = "UPDATE " + TABLE_BOWLER + " SET " + COLUMN_BOWLER_STATUS + " = 0";

        db.beginTransaction();
        try{
            db.execSQL(sql2);
            db.execSQL(sql);

            db.setTransactionSuccessful();
            return true;
        }
        catch(Exception e){
            return false;
        }
        finally {
            db.endTransaction();
        }
    }

    public void changeStrike(int inning, int batsman){
        SQLiteDatabase db = getWritableDatabase();

        String sql = "UPDATE " + TABLE_BATSMAN +
                " SET " + COLUMN_BATSMAN_STRIKE + " = 0" +
                " WHERE " + COLUMN_BATSMAN_INNING + " = " + inning;
        String sql2 = "UPDATE " + TABLE_BATSMAN +
                " SET " + COLUMN_BATSMAN_STRIKE + " = 1" +
                " WHERE " + COLUMN_ID + " = " + batsman;

        db.beginTransaction();
        try {
            db.execSQL(sql);
            db.execSQL(sql2);
        }
        catch (Exception e){
            db.setTransactionSuccessful();
        }
        finally {
            db.endTransaction();
        }
    }

    public void batsmanOut(int batsman){
        SQLiteDatabase db = getWritableDatabase();

        String sql = "UPDATE " + TABLE_BATSMAN +
            " SET " + COLUMN_BATSMAN_STATUS + " = 1" +
            " WHERE " + COLUMN_ID + " = " + batsman;
        db.execSQL(sql);
    }

    public boolean submitBall(int inning, int striker, int nonStriker, int bowler, int delivery, int runsThrough, int wicket, int runs){
        if(isInningDeclared(inning)){
            return false;
        }

        SQLiteDatabase db = getWritableDatabase();

        int teamRuns = 0,
            teamWicket = 0,
            batsmanRuns = 0,
            batsmanBall= 0,
            bowlerRuns = 0,
            bowlerBall = 0,
            bowlerWicket = 0;

        int ballsLimit, ballsPlayed;

        Cursor cursorInning = getInningScore(inning);
        cursorInning.moveToNext();

        int oversLimit = cursorInning.getInt(cursorInning.getColumnIndex(COLUMN_INNING_OVERS));
        ballsLimit = oversLimit * 6;
        ballsPlayed = cursorInning.getInt(cursorInning.getColumnIndex(COLUMN_INNING_BALLS));

        if(ballsPlayed >= ballsLimit){
            declareInning(inning);
        }

        teamRuns += runs;
        if(delivery == 1){
            bowlerBall++;
        }
        else if(delivery == 2){
            teamRuns++;
            bowlerRuns++;
            bowlerRuns += runs;
        }
        else if(delivery == 3){
            teamRuns++;
            bowlerRuns++;
            bowlerRuns += runs;
        }

        if(delivery == 1 || delivery == 3){
            if(runsThrough == 1){
                bowlerRuns += runs;
                batsmanRuns += runs;
            }
            batsmanBall++;
            teamRuns += runs;
        }

        String sqlTeam = "UPDATE " + TABLE_INNING +
                " SET " + COLUMN_INNING_RUNS + " = " + COLUMN_INNING_RUNS + " + " + teamRuns + ", " +
                COLUMN_INNING_BALLS + " = " + COLUMN_INNING_BALLS + " + " + bowlerBall + ", " +
                COLUMN_INNING_WICKETS + " = " + COLUMN_INNING_WICKETS + " + " + teamWicket +
                " WHERE " + COLUMN_ID + " = " + inning;
        String sqlBatsman = "UPDATE " + TABLE_INNING +
                " SET " + COLUMN_INNING_RUNS + " = " + COLUMN_INNING_RUNS + " + " + teamRuns + ", " +
                COLUMN_INNING_BALLS + " = " + COLUMN_INNING_BALLS + " + " + bowlerBall + ", " +
                COLUMN_INNING_WICKETS + " = " + COLUMN_INNING_WICKETS + " + " + teamWicket +
                " WHERE " + COLUMN_ID + " = " + inning;                  ;

        db.beginTransaction();
        try {
//            if (wicket > 0) {
//                teamWicket++;
//                if(wicket == 2){
//                    bowlerWicket++;
//                }
//                if (wicket == 4) {
//                    batsmanOut(nonStriker);
//                } else {
//                    batsmanOut(striker);
//                }
//            }
//
//            db.execSQL(sqlTeam);
//
//            db.setTransactionSuccessful();
            return true;
        }
        catch (Exception e){
            return false;
        }
        finally {
            db.endTransaction();
        }
    }

    public void resetDatabase(){
        SQLiteDatabase db = getWritableDatabase();

        db.delete(TABLE_TEAM, null, null);
        db.delete(TABLE_PLAYER, null, null);
        db.delete(TABLE_INNING, null , null );
        db.delete(TABLE_BATSMAN, null , null );
        db.delete(TABLE_BOWLER, null , null );
    }
}
