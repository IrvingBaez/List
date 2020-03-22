package com.example.list.databaseAccess;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.list.model.Element;
import com.example.list.model.List;

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

    public ArrayList<List> getLists() {
        ArrayList<List> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM lists";

        try {
            this.open();
            Cursor cursor = db.rawQuery(selectQuery, null);
            try {
                if (cursor.moveToFirst()) {
                    do {
                        int id = Integer.parseInt(cursor.getString(0));
                        String title = cursor.getString(1);
                        int customIndex = Integer.parseInt(cursor.getString(2));
                        String timeStamp = cursor.getString(3);

                        List l = new List(id, title, customIndex, timeStamp);
                        list.add(l);
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

    public void setChildren(List parent) {
        String selectQuery = "SELECT * FROM elements WHERE parent = " + parent.getId();

        try {
            this.open();
            Cursor cursor = db.rawQuery(selectQuery, null);
            try {
                if (cursor.moveToFirst()) {
                    do {
                        int id = Integer.parseInt(cursor.getString(0));
                        String content = cursor.getString(1);
                        int finished = Integer.parseInt(cursor.getString(3));
                        int customIndex = Integer.parseInt(cursor.getString(4));
                        String timeStamp = cursor.getString(5);
                        String description = cursor.getString(6);

                        Element element = new Element(id, parent, content, finished, customIndex,
                                timeStamp, description);
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

    public void updateElement (Element e) {
        String selectQuery = "UPDATE elements SET content = ?, state = ?, " +
                "customIndex = ?, description = ? WHERE id = ?;";
        String[] selectionArgs = new String[5];

        selectionArgs[0] = e.getContent();
        if(e.isFinished())
            selectionArgs[1] = "1";
        else
            selectionArgs[1] = "0";
        selectionArgs[2] = String.valueOf(e.getCustomIndex());
        selectionArgs[3] = e.getDescription();
        selectionArgs[4] = String.valueOf(e.getId());

        try {
            this.open();
            db.execSQL(selectQuery, selectionArgs);
        } finally {
            try {
                db.close();
            } catch (Exception ignore) {

            }
        }
    }

    public void insertElement (Element e){
        String selectQuery = "INSERT INTO elements (content, parent, state, customIndex, description)\n" +
                "VALUES (?, ?, ?, ?, ?);";
        String[] selectionArgs = new String[5];

        selectionArgs[0] = e.getContent();
        selectionArgs[1] = String.valueOf(e.getParent().getId());
        if(e.isFinished())
            selectionArgs[2] = "1";
        else
            selectionArgs[2] = "0";
        selectionArgs[3] = String.valueOf(e.getCustomIndex());
        selectionArgs[4] = e.getDescription();

        try {
            this.open();
            db.execSQL(selectQuery, selectionArgs);
        } finally {
            try {
                db.close();
            } catch (Exception ignore) {

            }
        }
    }
}