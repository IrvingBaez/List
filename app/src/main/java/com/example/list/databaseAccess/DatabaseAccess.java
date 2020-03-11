package com.example.list.databaseAccess;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseAccess {
    private DatabaseOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;
    Cursor c = null;

    private DatabaseAccess(Context context){
        this.openHelper = new DatabaseOpenHelper(context);
    }

    public static DatabaseAccess getInstance(Context context){
        if(instance == null)
            instance = new DatabaseAccess(context);

        return instance;
    }

    public void open(){
        this.db = openHelper.getWritableDatabase();
    }

    public void close(){
        if(this.db != null)
            this.db.close();
    }

    public String getContent(String id){
        c = db.rawQuery("Select content from Elements where id = " + id, new String[]{});
        StringBuffer buffer = new StringBuffer();

        while(c.moveToNext()){
            String content = c.getString(0);
            buffer.append(""+content);
        }

        return buffer.toString();
    }
}