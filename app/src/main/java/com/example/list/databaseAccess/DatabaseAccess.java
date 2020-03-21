package com.example.list.databaseAccess;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.list.model.ListElement;

import java.util.ArrayList;

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

    public ArrayList<ListElement> getFirstChildren() {
        ArrayList<ListElement> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM Elements WHERE parent IS NULL;";

        try {
            this.open();
            Cursor cursor = db.rawQuery(selectQuery, null);
            try {
                if (cursor.moveToFirst()) {
                    do {
                        int id = Integer.parseInt(cursor.getString(0));
                        String content = cursor.getString(2);
                        int finished = Integer.parseInt(cursor.getString(3));
                        int customIndex = Integer.parseInt(cursor.getString(4));
                        String timeStamp = cursor.getString(5);

                        ListElement obj = new ListElement(id, null, content, finished, customIndex, timeStamp);
                        list.add(obj);
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    cursor.close();
                } catch (Exception ignore) {

                }
            }
        } finally {
            try {
                db.close();
            } catch (Exception ignore) {

            }
        }
        return list;
    }

    public void setChildren(ListElement parent) {
        String selectQuery = "SELECT * FROM Elements WHERE parent = " + parent.getId();

        try {
            this.open();
            Cursor cursor = db.rawQuery(selectQuery, null);
            try {
                if (cursor.moveToFirst()) {
                    do {
                        int id = Integer.parseInt(cursor.getString(0));
                        String content = cursor.getString(2);
                        int finished = Integer.parseInt(cursor.getString(3));
                        int customIndex = Integer.parseInt(cursor.getString(4));
                        String timeStamp = cursor.getString(5);

                        ListElement element = new ListElement(id, parent, content, finished, customIndex, timeStamp);
                        parent.addElement(element);
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    cursor.close();
                } catch (Exception ignore) {

                }
            }
        } finally {
            try {
                db.close();
            } catch (Exception ignore) {

            }
        }
    }
}