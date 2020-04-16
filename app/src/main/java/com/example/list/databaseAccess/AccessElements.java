package com.example.list.databaseAccess;

import android.content.Context;

import com.example.list.model.EasyList;
import com.example.list.model.ListElement;

import java.util.ArrayList;

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
                "customIndex = ?, description = ?, color = ? WHERE id = ?;";

        String[] selectionArgs = new String[6];
        selectionArgs[0] = element.getContent();
        if(element.isFinished())
            selectionArgs[1] = "1";
        else
            selectionArgs[1] = "0";
        selectionArgs[2] = String.valueOf(element.getCustomIndex());
        selectionArgs[3] = element.getDescription();
        selectionArgs[5] = String.valueOf(element.getColor());
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

        element.getParent().removeElement(element);
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
                int color = 0;

                if(!(cursor.getString(8) == null)){
                    color = Integer.parseInt(cursor.getString(8));
                }

                (new AccessTags(context)).setTags(new ListElement(id, null, content,
                        finished, customIndex, timeStamp, description, element, color));
            }while(cursor.moveToNext());
        }
        this.close();
    }

    public ArrayList<ListElement> search(EasyList list, String term) {
        term = "%" + term + "%";
        String selectQuery = "SELECT * FROM elements \n" +
                "LEFT JOIN element_tags ON elements.id = element_tags.element\n" +
                "LEFT JOIN tags ON element_tags.tag = tags.id\n" +
                "\n" +
                "WHERE \n" +
                "elements.parent = ? AND \n" +
                "(content LIKE ? OR \n" +
                "name LIKE ?);";
        String[] selectionArgs = new String[]{String.valueOf(list.getId()), term, term};

        ArrayList<ListElement> result = new ArrayList<>();
        this.open();
        this.cursor = db.rawQuery(selectQuery, selectionArgs);
        if(cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(0));
                String content = cursor.getString(1);
                EasyList listParent  = (new AccessLists(context)).getList(Integer.parseInt(cursor.getString(2)));
                int finished = Integer.parseInt(cursor.getString(3));
                int customIndex = Integer.parseInt(cursor.getString(4));
                String timeStamp = cursor.getString(5);
                String description = cursor.getString(6);
                int color = 0;

                if(!(cursor.getString(8) == null)){
                    color = Integer.parseInt(cursor.getString(8));
                }

                result.add(new ListElement(id, listParent, content, finished, customIndex, timeStamp, description, null, color));
            } while (cursor.moveToNext());

            this.close();
        }
        return result;
    }

    private ListElement getElement(int id) {
        String selectQuery = "SELECT * FROM elements WHERE id = ?;";
        String[] selectionArgs = new String[]{String.valueOf(id)};

        this.open();
        this.cursor = db.rawQuery(selectQuery, selectionArgs);
        ListElement result;
        if (cursor.moveToFirst()) {
            String content = cursor.getString(1);
            EasyList listParent = (new AccessLists(context)).getList(Integer.parseInt(cursor.getString(2)));
            int finished = Integer.parseInt(cursor.getString(3));
            int customIndex = Integer.parseInt(cursor.getString(4));
            String timeStamp = cursor.getString(5);
            String description = cursor.getString(6);
            ListElement elementParent = this.getElement(Integer.parseInt(cursor.getString(7)));
            int color = 0;

            if(!cursor.getString(8).equals("NULL")){
                color = Integer.parseInt(cursor.getString(8));
            }

            result = new ListElement(id, listParent, content, finished, customIndex, timeStamp, description, elementParent, color);
        } else {
            return null;
        }

        this.close();
        return result;
    }
}
