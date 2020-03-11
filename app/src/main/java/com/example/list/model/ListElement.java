package com.example.list.model;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class ListElement implements Comparable{
    private int id;
    private int parentId;
    private String content;
    private boolean finished;
    private int customIndex;
    private Date date;
    private ArrayList<ListElement> elements = new ArrayList<>();

    private static int currentId;
    private static compareMode mode;

    private enum compareMode {ALPHABETICAL, CHRONOLOGICAL, CUSTOM};

    /**
     * Method meant to be used when initializing elements from database.
     *
     * @param id id as found in database.
     * @param parentId id of parent in the database. Cannot be the same as id.
     * @param content content of the element to be displayed.
     * @param finished current state of the element. Either finished or not.
     * @param customIndex index used to replicate order defined by user.
     * @param date date the element was created.
     */
    public ListElement(int id, int parentId, String content, int finished, int customIndex, String date) {
        if(id == parentId)
            throw new AssertionError("id (" + id + ") cannot be the same as parentID.");

        this.id = id;
        this.parentId = parentId;
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
     * @param parentId id of parent in the database. Cannot be the same as id.
     * @param content content of the element to be displayed.
     */
    public ListElement(int parentId, String content, int customIndex) {
        this.id = ++ListElement.currentId;
        this.parentId = parentId;
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

    public void addElement(ListElement element){
        this.elements.add(element);
    }

    public void removeElement(ListElement element){
        this.elements.remove(element);
    }

    public void insertElement(ListElement element, int newIndex){
        if(newIndex > this.elements.size()-1)
            throw new AssertionError("Index is too large for current list.");

        int currentIndex = elements.indexOf(element);
        Collections.rotate(this.elements.subList(newIndex, currentIndex), -1);

        for(int i = newIndex; i <= currentIndex; i++){
            this.elements.get(i).customIndex = i;
        }
    }

    public int getId() {
        return id;
    }

    public int getParentId() {
        return parentId;
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