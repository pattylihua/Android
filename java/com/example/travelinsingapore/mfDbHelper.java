package com.example.travelinsingapore;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class mfDbHelper extends SQLiteOpenHelper {


    private final Context context;
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase sqLiteDatabase;

    mfDbHelper(Context context) {
        super(context, MyFavorites.Entry.TABLE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        final String SQL_CREATE_TABLE = "CREATE TABLE "
                + MyFavorites.Entry.TABLE_NAME
                + "("
                + MyFavorites.Entry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MyFavorites.Entry.COL_NAME + " TEXT NOT NULL, "
                + MyFavorites.Entry.COL_ADRESS + " TEXT NOT NULL"
                + ");";
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS "
                + MyFavorites.Entry.TABLE_NAME;
        sqLiteDatabase.execSQL(SQL_DELETE_TABLE);
        onCreate(sqLiteDatabase);
    }
}