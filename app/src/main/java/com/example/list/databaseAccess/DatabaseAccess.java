package com.example.list.databaseAccess;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

abstract class DatabaseAccess {
    Context context;
    SQLiteDatabase db;
    Cursor cursor = null;

    private static final String DB_NAME = "Lists";
    private static final int DB_VERSION = 1;

    DatabaseAccess(Context context){
        this.context = context;
    }

    void open(){
        SQLiteAssetHelper openHelper = new SQLiteAssetHelper(context, DB_NAME, null, DB_VERSION);
        this.db = openHelper.getWritableDatabase();
    }

    void close(){
        try{
            this.cursor.close();
            this.db.close();
        }catch(Exception ignore){

        }
    }
}