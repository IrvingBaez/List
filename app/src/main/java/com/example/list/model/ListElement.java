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
    private ListElement parent;
    private String content;
    private boolean finished;
    private int customIndex;
    private Date date;
    private ArrayList<ListElement> elements = new ArrayList<>();
    private ArrayList<ListElement> finishedElements = new ArrayList<>();
    private ArrayList<ListElement> unfinishedElements = new ArrayList<>();


    private static int currentId = 0;
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
    public ListElement(int id, ListElement parent, String content, int finished, int customIndex, String date) {
        if(this == parent)
            return;

        this.id = id;
        this.parent = parent;
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
     * @param parent parent in the database. Cannot be the same as id.
     * @param content content of the element to be displayed.
     */
    public ListElement(ListElement parent, String content) {
        this.id = ++ListElement.currentId;
        this.parent = parent;
        this.content = content;
        this.customIndex = parent.elements.size();
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

    public void sortElements(){
        Collections.sort(this.elements);
    }

    public void divideElements(){
        for (ListElement e:this.elements) {
            if(e.finished)
                this.finishedElements.add(e);
            else
                this.unfinishedElements.add(e);
        }
    }

    public void addElement(ListElement element){
        element.parent = this;
        this.elements.add(element);
    }

    public void removeElement(ListElement element){
        this.elements.remove(element);
    }

    public void insertElement(ListElement element, int newIndex){
        if(newIndex > this.elements.size()-1)
            throw new AssertionError("Index is too large for current list.");

        int currentIndex = elements.indexOf(element);
        Collections.rotate(this.elements.subList(newIndex, currentIndex + 1), 1);

        for(int i = newIndex; i <= currentIndex; i++){
            this.elements.get(i).customIndex = i;
        }
    }

    public static void setMode(compareMode mode) {
        ListElement.mode = mode;
    }

    public int getId(){
        return id;
    }

    public ListElement getParent(){
        return parent;
    }

    public String getContent() {
        return content;
    }

    public ArrayList<ListElement> getElements() {
        return elements;
    }

    public ArrayList<ListElement> getFinishedElements() {
        return finishedElements;
    }

    public ArrayList<ListElement> getUnfinishedElements() {
        return unfinishedElements;
    }

    public boolean isEmpty(){
        return this.elements.isEmpty();
    }

    public boolean isFinished() {
        return finished;
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