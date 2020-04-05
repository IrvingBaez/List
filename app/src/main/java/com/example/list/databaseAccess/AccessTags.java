package com.example.list.databaseAccess;

import android.content.Context;

import com.example.list.model.ListElement;
import com.example.list.model.EasyList;

import java.util.ArrayList;

public class AccessTags extends DatabaseAccess{

    public AccessTags(Context context) {
        super(context);
    }

    public String[] getListTags(EasyList list){
        String selectQuery = "SELECT name FROM lists JOIN tags ON lists.id = tags.list " +
                "WHERE lists.id = ?;";
        String[] selectionArgs = {String.valueOf(list.getId())};

        this.open();
        this.cursor = db.rawQuery(selectQuery, selectionArgs);
        ArrayList<String> tags = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                tags.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        this.close();

        String[] t = new String[tags.size()];
        for(int i = 0; i < tags.size(); i++){
            t[i] = tags.get(i);
        }
        return t;
    }

    public void insertTag(ListElement element, String[] tags) {
        //Erasing tags from element
        String selectQuery = "DELETE FROM element_tags WHERE element = ?;";
        String[] selectionArgs = new String[] {String.valueOf(element.getId())};
        this.open();
        db.execSQL(selectQuery, selectionArgs);

        for(String tag : tags) {
            //Check if tag exist.
            selectQuery = "SELECT id FROM tags WHERE name = ? AND list = ?";
            selectionArgs = new String[]{tag, String.valueOf(element.getParent().getId())};
            this.open();
            cursor = db.rawQuery(selectQuery, selectionArgs);
            int tagId = -1;
            if (cursor.moveToFirst()) {
                tagId = Integer.parseInt(cursor.getString(0));
            }


            if (tagId == -1) {
                //add to table tags.
                selectQuery = "INSERT INTO tags (list, name) VALUES (?, ?);";
                selectionArgs = new String[]{String.valueOf(element.getParent().getId()), tag};
                this.open();
                db.execSQL(selectQuery, selectionArgs);

                cursor = db.rawQuery("SELECT max(id) FROM tags;", null);
                cursor.moveToFirst();
                tagId = Integer.parseInt(cursor.getString(0));
            }


            //Add tag to table element_tags
            selectQuery = "INSERT INTO element_tags (element, tag) VALUES (?, ?);";
            selectionArgs = new String[]{String.valueOf(element.getId()), String.valueOf(tagId)};
            this.open();
            db.execSQL(selectQuery, selectionArgs);
            setTags(element);
        }
        this.close();
    }

    public void insertTag(EasyList list, String tag) {
        //Check if tag exist.
        String selectQuery = "SELECT id FROM tags WHERE name = ? AND list = ?";
        String[] selectionArgs = new String[]{tag, String.valueOf(list.getId())};
        this.open();
        cursor = db.rawQuery(selectQuery, selectionArgs);

        int tagId = -1;
        if (cursor.moveToFirst()) {
            tagId = Integer.parseInt(cursor.getString(0));
        }


        if (tagId == -1) {
            //add to table tags.
            selectQuery = "INSERT INTO tags (list, name) VALUES (?, ?);";
            selectionArgs = new String[]{String.valueOf(list.getId()), tag};
            this.open();
            db.execSQL(selectQuery, selectionArgs);
        }

        this.close();
    }

    private String[] getElementTagsFromDB(ListElement element){
        String selectQuery = "SELECT name FROM element_tags JOIN tags ON element_tags.tag = " +
                "tags.id WHERE element = ?;";
        String[] selectionArgs = new String[] {String.valueOf(element.getId())};

        this.open();
        cursor = db.rawQuery(selectQuery, selectionArgs);
        ArrayList<String> tagList = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                tagList.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }

        String[] tagArray = new String[tagList.size()];
        for (int i = 0; i < tagList.size(); i++){
            tagArray[i] = tagList.get(i);
        }
        return tagArray;
    }

    void setTags(ListElement element){
        element.setTags(getElementTagsFromDB(element));
    }

    public void deleteTag(EasyList list, String tagName){
        this.open();
        String selectQuery = "SELECT id FROM tags WHERE list = ? AND name = ?;";
        String[] selectionArgs = new String[2];

        selectionArgs[0] = String.valueOf(list.getId());
        selectionArgs[1] = tagName;

        cursor = db.rawQuery(selectQuery, selectionArgs);

        if(!cursor.moveToFirst())
            return;

        selectQuery = "DELETE FROM element_tags WHERE tag = ?";
        selectionArgs = new String[]{cursor.getString(0)};
        db.execSQL(selectQuery, selectionArgs);

        selectQuery = "DELETE FROM tags WHERE id = ?";
        db.execSQL(selectQuery, selectionArgs);

        this.close();
    }
}