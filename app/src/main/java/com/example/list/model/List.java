package com.example.list.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class List implements Comparable {
    private int id;
    private String title;
    private int customIndex;
    private Date date;

    private ArrayList<Element> elements = new ArrayList<>();
    private ArrayList<Element> finishedElements = new ArrayList<>();
    private ArrayList<Element> unfinishedElements = new ArrayList<>();

    private static compareMode mode = List.compareMode.CUSTOM;

    public enum compareMode {ALPHABETICAL, CHRONOLOGICAL, CUSTOM}

    /**
     * Meant to be used when loading lists from database.
     * @param id id as appears in database, used for identification.
     * @param title id as appears in database, used for display.
     * @param customIndex id as appears in database, used for custom sorting.
     * @param date date as appears in database, will be casted into a Date object.
     */
    public List(int id, String title, int customIndex, String date) {
        this.id = id;
        this.title = title;
        this.customIndex = customIndex;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
        try {
            this.date = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Used to create Lists and add the to the data base.
     * @param title title of the new list.
     */
    public List(String title, int customIndex) {
        this.title = title;
        this.customIndex = customIndex;
        //Save to database.
    }

    @Override
    public int compareTo(Object o) {
        List l = (List)o;

        switch(List.mode){
            case ALPHABETICAL:
                return this.title.compareTo(l.title);
            case CHRONOLOGICAL:
                return this.date.compareTo(l.date);
            case CUSTOM:
                if(this.customIndex < l.customIndex)
                    return -1;
                if(this.customIndex > l.customIndex)
                    return 1;
        }
        return 0;
    }

    public void divideElements(){
        for (Element e:this.elements) {
            if(e.isFinished())
                this.finishedElements.add(e);
            else
                this.unfinishedElements.add(e);
        }
    }

    public void sortElements(){
        Collections.sort(this.elements);
        Collections.sort(this.finishedElements);
        Collections.sort(this.unfinishedElements);
    }

    public void addElement(Element element){
        this.elements.add(element);
    }

    public void removeElement(Element element){
        this.elements.remove(element);
    }

    public void insertElement(Element element, int newIndex){
        if(newIndex > this.elements.size()-1)
            throw new AssertionError("Index is too large for current list.");

        int currentIndex = elements.indexOf(element);
        Collections.rotate(this.elements.subList(newIndex, currentIndex + 1), 1);

        for(int i = newIndex; i <= currentIndex; i++){
            this.elements.get(i).setCustomIndex(i);
        }

        sortElements();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isEmpty(){
        return this.elements.isEmpty();
    }

    public ArrayList<Element> getElements() {
        return elements;
    }

    public ArrayList<Element> getFinishedElements() {
        return finishedElements;
    }

    public ArrayList<Element> getUnfinishedElements() {
        return unfinishedElements;
    }
}
