package com.example.list.databaseAccess;

import android.content.Context;
import android.widget.LinearLayout;

import com.example.list.model.ListElement;

public class AccessElements extends DatabaseAccess{
    public AccessElements(Context context) {
        super(context);
    }

    public void insertElement (ListElement element){
        if((element.getParent() != null) == (element.getParentElement() != null))
            return;

        String selectQuery = "INSERT INTO elements (content, parent, state, customIndex, " +
                "description, parentElement) VALUES (?, ?, ?, ?, ?, ?);";

        String[] selectionArgs = new String[6];

        selectionArgs[0] = element.getContent();

        if(element.getParent() != null) selectionArgs[1] = String.valueOf(element.getParent().getId());
        else selectionArgs[1] = "NULL";

        if(element.isFinished()) selectionArgs[2] = "1";
        else selectionArgs[2] = "0";

        selectionArgs[3] = String.valueOf(element.getCustomIndex());

        selectionArgs[4] = element.getDescription();

        if(element.getParentElement() != null) selectionArgs[5] = String.valueOf(element.getParentElement().getId());
        else selectionArgs[5] = "NULL";

        this.open();
        db.execSQL(selectQuery, selectionArgs);
        this.close();
    }

    public void updateElement (ListElement element) {
        //The following attributes are never to be changed: id, parent, timestamp, parentElement.
        String selectQuery = "UPDATE elements SET content = ?, state = ?, " +
                "customIndex = ?, description = ? WHERE id = ?;";

        String[] selectionArgs = new String[5];
        selectionArgs[0] = element.getContent();
        if(element.isFinished())
            selectionArgs[1] = "1";
        else
            selectionArgs[1] = "0";
        selectionArgs[2] = String.valueOf(element.getCustomIndex());
        selectionArgs[3] = element.getDescription();
        selectionArgs[4] = String.valueOf(element.getId());

        this.open();
        db.execSQL(selectQuery, selectionArgs);
        this.close();
    }

    public void deleteElement (ListElement element){
        String selectQuery = "DELETE FROM elements WHERE id = ?;";
        String[] selectionArgs = new String[]{String.valueOf(element.getId())};

        this.open();
        db.execSQL(selectQuery, selectionArgs);
        this.close();
    }

    public void setChildrenToElement(ListElement element){
        String selectQuery = "SELECT * FROM elements WHERE parentElement = ?;";
        String[] selectionArgs = {String.valueOf(element.getId())};

        this.open();
        this.cursor = db.rawQuery(selectQuery, selectionArgs);
        if(cursor.moveToFirst()){
            do{
                int id = Integer.parseInt(cursor.getString(0));
                String content = cursor.getString(1);
                int finished = Integer.parseInt(cursor.getString(3));
                int customIndex = Integer.parseInt(cursor.getString(4));
                String timeStamp = cursor.getString(5);
                String description = cursor.getString(6);

                (new AccessTags(context)).setTags(new ListElement(id, null, content,
                        finished, customIndex, timeStamp, description, element));
            }while(cursor.moveToNext());
        }
        this.close();
    }
}
