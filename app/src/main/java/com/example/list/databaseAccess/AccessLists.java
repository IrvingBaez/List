package com.example.list.databaseAccess;

import android.content.Context;

import com.example.list.model.ListElement;
import com.example.list.model.List;

import java.util.ArrayList;

public class AccessLists extends DatabaseAccess{
    public AccessLists(Context context) {
        super(context);
    }

    public ArrayList<List> getLists() {
        String selectQuery = "SELECT * FROM lists;";

        this.open();
        this.cursor = db.rawQuery(selectQuery, null);
        ArrayList<List> list = new ArrayList<>();
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

        this.close();
        return list;
    }

    public void setListChildren(List parent) {
        parent.dropElements();

        String selectQuery = "SELECT * FROM elements WHERE parent = ?;";
        String[] selectionArgs = {String.valueOf(parent.getId())};

        this.open();
        this.cursor = db.rawQuery(selectQuery, selectionArgs);
        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(0));
                String content = cursor.getString(1);
                int finished = Integer.parseInt(cursor.getString(3));
                int customIndex = Integer.parseInt(cursor.getString(4));
                String timeStamp = cursor.getString(5);
                String description = cursor.getString(6);

                (new AccessTags(context)).setTags(new ListElement(id, parent, content,
                        finished, customIndex, timeStamp, description, null));
            } while (cursor.moveToNext());
        }
        this.close();
    }

    public void insertList(List newList, int customIndex) {
        this.open();
        String selectQuery = "INSERT INTO lists (title, customIndex) VALUES (?, ?);";
        String[] selectionAgs = new String[]{newList.getTitle(), String.valueOf(customIndex)};
        db.execSQL(selectQuery, selectionAgs);

        this.close();
    }

    public void deleteList(List list){
        String selectQuery = "DELETE FROM lists WHERE id = ?;";
        String[] selectionArgs = new String[]{String.valueOf(list.getId())};

        this.open();
        db.execSQL(selectQuery, selectionArgs);
        this.close();
    }
}