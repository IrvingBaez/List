package com.example.list.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class EasyList implements Comparable, Parcelable {
    private int id;
    private String title;
    private int customIndex;
    private Date date;
    private CompareMode compareMode;

    public enum CompareMode {ALPHABETICAL, CHRONOLOGICAL, CUSTOM}
    private ArrayList<ListElement> elements = new ArrayList<>();

    protected EasyList(Parcel in) {
        id = in.readInt();
        title = in.readString();
        customIndex = in.readInt();
        compareMode = CompareMode.valueOf(in.readString());
    }

    public static final Creator<EasyList> CREATOR = new Creator<EasyList>() {
        @Override
        public EasyList createFromParcel(Parcel in) {
            return new EasyList(in);
        }

        @Override
        public EasyList[] newArray(int size) {
            return new EasyList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeInt(customIndex);
        dest.writeString(this.compareMode.name());
    }

    /**
     * Meant to be used when loading lists from database.
     * @param id id as appears in database, used for identification.
     * @param title id as appears in database, used for display.
     * @param customIndex id as appears in database, used for custom sorting.
     * @param date date as appears in database, will be casted into a Date object.
     */
    public EasyList(int id, String title, int customIndex, String date, CompareMode compareMode) {
        this.id = id;
        this.title = title;
        this.customIndex = customIndex;
        this.compareMode = compareMode;

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
    public EasyList(String title, int customIndex) {
        this.title = title;
        this.customIndex = customIndex;
        this.compareMode = CompareMode.CHRONOLOGICAL;
        //Save to database.
    }

    @Override
    public int compareTo(Object o) {
        EasyList l = (EasyList)o;

        switch(this.compareMode){
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

    public void sortElements(){
        Collections.sort(this.elements);
    }

    public void addElement(ListElement element){
        int index = elements.indexOf(element);

        if(index == -1)
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

    public CompareMode getCompareMode() {
        return compareMode;
    }

    public void setCompareMode(CompareMode compareMode) {
        this.compareMode = compareMode;
    }

    public int getCustomIndex() {
        return customIndex;
    }

    public void setCustomIndex(int customIndex){
        this.customIndex = customIndex;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isEmpty(){
        return this.elements.isEmpty();
    }

    public ArrayList<ListElement> getElements() {
        return elements;
    }

    public int getFinishedCount(){
        int finished = 0;

        for(ListElement element : this.elements){
            if(element.isFinished())
                finished++;
        }

        return finished;
    }

    public void dropElements() {
        this.elements = new ArrayList<>();
    }
}
