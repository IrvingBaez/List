package com.example.list.model;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ListElement implements Comparable{
    private int id;
    private int parentID;
    private String content;
    private boolean finished;
    private int customIndex;
    private Date date;

    private static int currentId;
    private static compareMode mode;

    private enum compareMode {ALPHABETICAL, CHRONOLOGICAL, CUSTOM};

    /**
     * Method meant to be used when initializing elements from database.
     *
     * @param id id as found in database.
     * @param parentID id of parent in the database. Cannot be the same as id.
     * @param content content of the element to be displayed.
     * @param finished current state of the element. Either finished or not.
     * @param customIndex index used to replicate order defined by user.
     * @param date date the element was created.
     */
    public ListElement(int id, int parentID, String content, int finished, int customIndex, String date) {
        if(id == parentID)
            throw new AssertionError("id cannot be the same as parentID");

        this.id = id;
        this.parentID = parentID;
        this.content = content;
        this.customIndex = customIndex;
        this.finished = (finished == 1);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
        try {
            this.date = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method meant to be used to create elements on execution time.
     *
     * @param parentID id of parent in the database. Cannot be the same as id.
     * @param content content of the element to be displayed.
     * @param customIndex index used to replicate order defined by user.
     */
    public ListElement(int parentID, String content, int customIndex) {
        this.id = ++ListElement.currentId;
        this.parentID = parentID;
        this.content = content;
        this.customIndex = customIndex;
        this.finished = false;
        this.date = new Date();
    }

    @Override
    public int compareTo(@NonNull Object o) {
        ListElement e = (ListElement)o;

        switch(ListElement.mode){
            case ALPHABETICAL:
                return this.content.compareTo(e.content);
            case CHRONOLOGICAL:
                return this.date.compareTo(e.date);
            case CUSTOM:
                if(this.customIndex < e.customIndex)
                    return -1;
                if(this.customIndex > e.customIndex)
                    return 1;
        }
        return 0;
    }

    public int getId() {
        return id;
    }

    public int getParentID() {
        return parentID;
    }

    public String getContent() {
        return content;
    }

    public boolean isFinished() {
        return finished;
    }

    public int getCustomIndex() {
        return customIndex;
    }

    public Date getDate() {
        return date;
    }

    public static void setMode(compareMode mode) {
        ListElement.mode = mode;
    }
}