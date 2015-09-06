package com.example.yudiandrean.socioblood.databases;

/**
 * Created by yudiandrean on 5/9/15.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "cloud_contacts";

    // Login table name
    private static final String TABLE_LOGIN = "login";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static String KEY_UID = "uid";
    private static String KEY_USERNAME = "username";
    private static String KEY_FULLNAME = "fullname";

    private static String KEY_EMAIL = "email";
    private static String KEY_CREATED_AT = "created_at";
    private static String KEY_BLOOD_TYPE = "blood_type";
    private static String KEY_RHESUS = "rhesus";
    private static String KEY_GENDER = "gender";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_FULLNAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_USERNAME + " TEXT,"
                + KEY_UID + " TEXT,"
                + KEY_CREATED_AT + " TEXT,"
                + KEY_GENDER + " TEXT,"
                + KEY_BLOOD_TYPE + " TEXT,"
                + KEY_RHESUS + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        DATABASE_VERSION++;

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String fullname, String email, String username, String uid, String created_at, String gender, String blood_type, String rhesus) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FULLNAME, fullname); // Fullname
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_USERNAME, username); // UserName
        values.put(KEY_UID, uid); // userid
        values.put(KEY_CREATED_AT, created_at); // Created At
        values.put(KEY_GENDER, gender); // gender
        values.put(KEY_BLOOD_TYPE, blood_type); // Rhesus
        values.put(KEY_RHESUS, rhesus); // rhesus

        // Inserting Row
        db.insert(TABLE_LOGIN, null, values);
        db.close(); // Closing database connection
    }


    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String,String> user = new HashMap<>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            user.put("fullname", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("uname", cursor.getString(3));
            user.put("uid", cursor.getString(4));
            user.put("created_at", cursor.getString(5));
            user.put("gender", cursor.getString(6));
            user.put("blood_type", cursor.getString(7));
            user.put("rhesus", cursor.getString(8));

        }
        cursor.close();
        db.close();
        // return user
        return user;
    }






    /**
     * Getting user login status
     * return true if rows are there in table
     * */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        // return row count
        return rowCount;
    }


    /**
     * Re crate database
     * Delete all tables and create them again
     * */
    public void resetTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_LOGIN, null, null);
        db.close();
    }

}
