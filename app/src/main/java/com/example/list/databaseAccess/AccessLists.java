package com.example.list.databaseAccess;

import android.content.Context;

import com.example.list.model.ListElement;
import com.example.list.model.EasyList;

import java.util.ArrayList;

public class AccessLists extends DatabaseAccess{
    public AccessLists(Context context) {
        super(context);
    }

    public ArrayList<EasyList> getLists() {
        String selectQuery = "SELECT * FROM lists;";

        this.open();
        this.cursor = db.rawQuery(selectQuery, null);
        ArrayList<EasyList> listList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(0));
                String title = cursor.getString(1);
                int customIndex = Integer.parseInt(cursor.getString(2));
                String timeStamp = cursor.getString(3);

                EasyList.CompareMode compareMode;
                switch (Integer.parseInt(cursor.getString(4))){
                    case 0:
                        compareMode = EasyList.CompareMode.ALPHABETICAL;
                        break;
                    case 1:
                        compareMode = EasyList.CompareMode.CHRONOLOGICAL;
                        break;
                    case 2:
                        compareMode = EasyList.CompareMode.CUSTOM;
                        break;
                    default:
                        compareMode = EasyList.CompareMode.CHRONOLOGICAL;;
                }

                EasyList l = new EasyList(id, title, customIndex, timeStamp, compareMode);
                listList.add(l);
            } while (cursor.moveToNext());
        }

        this.close();
        return listList;
    }

    public EasyList getList(int id) {
        String selectQuery = "SELECT * FROM lists WHERE id = ?;";
        String[] selectionArgs = new String[]{String.valueOf(id)};

        this.open();
        this.cursor = db.rawQuery(selectQuery, selectionArgs);

        EasyList result;
        if (cursor.moveToFirst()) {
            String title = cursor.getString(1);
            int customIndex = Integer.parseInt(cursor.getString(2));
            String timeStamp = cursor.getString(3);

            EasyList.CompareMode compareMode;
            switch (Integer.parseInt(cursor.getString(4))) {
                case 0:
                    compareMode = EasyList.CompareMode.ALPHABETICAL;
                    break;
                case 1:
                    compareMode = EasyList.CompareMode.CHRONOLOGICAL;
                    break;
                case 2:
                    compareMode = EasyList.CompareMode.CUSTOM;
                    break;
                default:
                    compareMode = EasyList.CompareMode.CHRONOLOGICAL;
                    ;
            }

            result = new EasyList(id, title, customIndex, timeStamp, compareMode);
        }else {
            return null;
        }

        this.close();
        return result;
    }

    public void setListChildren(EasyList parent) {
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
                int color = 0;

                if(!(cursor.getString(8) == null)){
                    color = Integer.parseInt(cursor.getString(8));
                }

                (new AccessTags(context)).setTags(new ListElement(id, parent, content,
                        finished, customIndex, timeStamp, description, null, color));
            } while (cursor.moveToNext());
        }
        this.close();
    }

    public void insertList(EasyList newList, int customIndex) {
        this.open();
        String selectQuery = "INSERT INTO lists (title, customIndex, sortMode) VALUES (?, ?, ?);";

        String[] selectionAgs = new String[3];
        selectionAgs[0] = newList.getTitle();
        selectionAgs[1] = String.valueOf(customIndex);
        selectionAgs[2] = String.valueOf(newList.getCompareMode().ordinal());

        db.execSQL(selectQuery, selectionAgs);

        this.close();
    }

    public void updateList(EasyList list){
        String selectQuery = "UPDATE lists SET title = ?, customIndex = ?, " +
                "sortMode = ? WHERE id = ?;";

        String[] selectionArgs = new String[4];
        selectionArgs[0] = list.getTitle();
        selectionArgs[1] = String.valueOf(list.getCustomIndex());
        selectionArgs[2] = String.valueOf(list.getCompareMode().ordinal());
        selectionArgs[3] = String.valueOf(list.getId());

        this.open();
        db.execSQL(selectQuery, selectionArgs);
        this.close();
    }

    public void updateInstance(EasyList list){
        EasyList updated = this.getList(list.getId());

        list.setTitle(updated.getTitle());
        list.setCustomIndex(updated.getCustomIndex());
        list.setCompareMode(updated.getCompareMode());
        setListChildren(list);
    }

    public void deleteList(EasyList list){
        String selectQuery = "DELETE FROM lists WHERE id = ?;";
        String[] selectionArgs = new String[]{String.valueOf(list.getId())};

        this.open();
        db.execSQL(selectQuery, selectionArgs);
        this.close();
    }
}