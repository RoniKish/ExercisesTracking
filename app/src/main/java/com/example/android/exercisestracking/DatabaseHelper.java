package com.example.android.exercisestracking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;
import java.util.ArrayList;


public class DatabaseHelper extends SQLiteOpenHelper implements IModel {

    private static final String DATABASE_NAME = "exercises_db";
    private static final String TABLE_NAME = "trains_table";
    private static final String TABLE_NAME2 = "exercises_table";
    private static final String TABLE_NAME3 = "commited_exercises";
    private static final String EXERCISE_TYPE = "exercise_type";
    private static final String TRAIN_TYPE = "train_type";
    private static final String TIME = "time";
    private static final String DISTANCE = "distance";

    private Context sqlHelper;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.sqlHelper = context;
        Log.i("helper", "helper created");
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        createDbTables(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private void createDbTables(SQLiteDatabase database) {
        createTrainsTable(database);
        createExerciseTable(database);
        createCommittedExercisesTable(database);
    }

    private void createTrainsTable(SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        database.execSQL("CREATE TABLE "+ TABLE_NAME
                +" ("+ BaseColumns._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " + TRAIN_TYPE +" TEXT);");
        values.put(TRAIN_TYPE, "Aerobic");
        database.insert(TABLE_NAME, null, values);
        values.put(TRAIN_TYPE, "Anaerobic");
        database.insert(TABLE_NAME, null, values);
        values.put(TRAIN_TYPE, "Anaerobic - Weightlifting");
        database.insert(TABLE_NAME, null, values);
    }

    private void createExerciseTable(SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        database.execSQL("CREATE TABLE "+ TABLE_NAME2
                +" ("+ BaseColumns._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " + TRAIN_TYPE +" TEXT, " + EXERCISE_TYPE +" TEXT);");
        values.put(TRAIN_TYPE, "Aerobic");
        values.put(EXERCISE_TYPE, "Running");
        database.insert(TABLE_NAME2, null, values);
        values.put(TRAIN_TYPE, "Aerobic");
        values.put(EXERCISE_TYPE, "Walking");
        database.insert(TABLE_NAME2, null, values);
        values.put(TRAIN_TYPE, "Aerobic");
        values.put(EXERCISE_TYPE, "Swimming");
        database.insert(TABLE_NAME2, null, values);
        values.put(TRAIN_TYPE, "Aerobic");
        values.put(EXERCISE_TYPE, "Cycling");
        database.insert(TABLE_NAME2, null, values);
        values.put(TRAIN_TYPE, "Aerobic");
        values.put(EXERCISE_TYPE, "Boxing");
        database.insert(TABLE_NAME2, null, values);
        values.put(TRAIN_TYPE, "Anaerobic");
        values.put(EXERCISE_TYPE, "Push-ups");
        database.insert(TABLE_NAME2, null, values);
        values.put(TRAIN_TYPE, "Anaerobic");
        values.put(EXERCISE_TYPE, "Pull-ups");
        database.insert(TABLE_NAME2, null, values);
        values.put(TRAIN_TYPE, "Anaerobic");
        values.put(EXERCISE_TYPE, "Squats");
        database.insert(TABLE_NAME2, null, values);
        values.put(TRAIN_TYPE, "Anaerobic - Weightlifting");
        values.put(EXERCISE_TYPE, "Bench-press");
        database.insert(TABLE_NAME2, null, values);
        values.put(TRAIN_TYPE, "Anaerobic - Weightlifting");
        values.put(EXERCISE_TYPE, "Overhead-press");
        database.insert(TABLE_NAME2, null, values);
        values.put(TRAIN_TYPE, "Anaerobic - Weightlifting");
        values.put(EXERCISE_TYPE, "Push-press");
        database.insert(TABLE_NAME2, null, values);
        values.put(TRAIN_TYPE, "Anaerobic - Weightlifting");
        values.put(EXERCISE_TYPE, "Deadlift");
        database.insert(TABLE_NAME2, null, values);
    }

    private void createCommittedExercisesTable(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE "+ TABLE_NAME3
                +" ("+ BaseColumns._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " + TRAIN_TYPE +" TEXT, " + EXERCISE_TYPE +" TEXT, " +TIME+" TEXT, " +DISTANCE+" TEXT);");
    }

    public String[] getTrainTypes() {
        ArrayList<String> trainsList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                TRAIN_TYPE
        };

        Cursor cursor = db.query(
                TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        //extracting the data to a list
        if(cursor.moveToFirst()) {
            do {
                trainsList.add(cursor.getString(cursor.getColumnIndex("train_type")));
            } while(cursor.moveToNext());
        }
        //convert the list to array
        String[] trainsArr = new String[trainsList.size()];
        trainsArr = trainsList.toArray(trainsArr);

        cursor.close();
        db.close();
        return trainsArr;
    }

    public String[] getExercises(String value) {
        ArrayList<String> exercisesList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                EXERCISE_TYPE,
                TRAIN_TYPE
        };

        String selection = TRAIN_TYPE + " = ?";
        String[] selectionArgs = { value };

        String sortOrder = EXERCISE_TYPE + " ASC";

        Cursor cursor = db.query(
                TABLE_NAME2,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        //extracting the data to a list
        if(cursor.moveToFirst()) {
            do {
                exercisesList.add(cursor.getString(cursor.getColumnIndex("exercise_type")));
            } while(cursor.moveToNext());
        }
        //convert the list to array
        String[] exercisesArr = new String[exercisesList.size()];
        exercisesArr = exercisesList.toArray(exercisesArr);

        cursor.close();
        db.close();
        return exercisesArr;
    }

    public String[][] getCommittedExercises() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor committedExercisesTableCursor = db.rawQuery("SELECT * from commited_exercises",null);
        int tableNumOfCols = committedExercisesTableCursor.getColumnCount();
        int tableNumOfRows = committedExercisesTableCursor.getCount();
        String[][] committedExercisesTable = new String[tableNumOfRows][tableNumOfCols];

        if (committedExercisesTableCursor.moveToFirst()) {
            while (!committedExercisesTableCursor.isAfterLast()) {
                for (int i = 0; i < tableNumOfCols; i++) {
                    committedExercisesTable[committedExercisesTableCursor.getPosition()][i] = committedExercisesTableCursor.getString(i);
                }
                committedExercisesTableCursor.moveToNext();
            }
        }

        committedExercisesTableCursor.close();
        return committedExercisesTable;
    }

//        TODO close cursor on getCommittedExercisesRows and getCommittedExercisesCols without causing assert
    public int getCommittedExercisesCols() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor committedExercisesTableCursor = db.rawQuery("SELECT * from commited_exercises",null);
        return committedExercisesTableCursor.getColumnCount();
    }

    public int getCommittedExercisesRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor committedExercisesTableCursor = db.rawQuery("SELECT * from commited_exercises",null);
        return committedExercisesTableCursor.getCount();
    }

    //add new exercise to data base
    public String commitExerciseToDB(String trainType, String exerciseType, String time, String distance){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TRAIN_TYPE, trainType);
        values.put(EXERCISE_TYPE, exerciseType);
        values.put(TIME, time);
        values.put(DISTANCE, distance);
        long newRowID = db.insert(TABLE_NAME3, null, values);
        return "" + newRowID;
    }

    //add new train option to data base
    public String addOptionTrainToDB(String valueToAdd) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TRAIN_TYPE, valueToAdd);
        long newRowID = db.insert(TABLE_NAME, null, values);
        return "" + newRowID;
    }

    //add new exercise option to data base
    public String addOptionExerciseToDB(String valueToAdd, String trainType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TRAIN_TYPE, trainType);
        values.put(EXERCISE_TYPE, valueToAdd);
        long newRowID = db.insert(TABLE_NAME2, null, values);
        return "" + newRowID;
    }

    //return number of rows in committed exercises table
    public String getExercisesCountFromDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_NAME3);
        db.close();
        return "" + count;
    }
}