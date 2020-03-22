package com.example.list.model;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class Element implements Comparable{
    private int id;
    private List parent;
    private String content;
    private boolean finished;
    private int customIndex;
    private Date date;
    private String description;
    private ArrayList<String> tags;

    private static compareMode mode = compareMode.CUSTOM;

    public enum compareMode {ALPHABETICAL, CHRONOLOGICAL, CUSTOM}

    /**
     * Method meant to be used when initializing elements from database.
     *
     * @param id id as found in database.
     * @param parent parent in the database. Cannot be the same as id.
     * @param content content of the element to be displayed.
     * @param finished current state of the element. Either finished or not.
     * @param customIndex index used to replicate order defined by user.
     * @param date date the element was created.
     */
    public Element(int id, List parent, String content, int finished, int customIndex, String date,
                   String description) {
        this.id = id;
        this.parent = parent;
        this.content = content;
        this.customIndex = customIndex;
        this.finished = (finished == 1);
        this.description = "";
        this.tags = new ArrayList<>();

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
     * @param parent parent in the database. Cannot be the same as id.
     * @param content content of the element to be displayed.
     */
    public Element(List parent, String content) {
        this.parent = parent;
        this.content = content;
        this.customIndex = parent.getElements().size();
        this.finished = false;
        this.date = new Date();
        this.description = "";
        this.tags = new ArrayList<>();

        //Save to database.
    }

    @Override
    public int compareTo(@NonNull Object o) {
        Element e = (Element)o;

        switch(Element.mode){
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

    public static void setMode(compareMode mode) {
        Element.mode = mode;
    }

    public int getId(){
        return id;
    }

    public List getParent(){
        return parent;
    }

    public void setParent(List parent){
        this.parent = parent;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public int getCustomIndex() {
        return customIndex;
    }

    public void setCustomIndex(int customIndex) {
        this.customIndex = customIndex;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addTag(String tag){
        this.tags.add(tag);
    }

    public void removeTag(String tag){
        this.tags.remove(tag);
    }

    @NonNull
    @Override
    public String toString() {
        return "ListElement{" +
                "id=" + id +
                ", content='" + content + '\'' +
                '}';
    }
}