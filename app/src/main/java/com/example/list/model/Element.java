package com.example.list.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Element implements Comparable<Element>, Parcelable {
    private int id;
    private List parent;
    private String content;
    private boolean finished;
    private int customIndex;
    private Date date;
    private String description;
    private Element parentElement;
    private final ArrayList<Element> childrenElements;
    private final ArrayList<String> tags;

    private static compareMode mode = compareMode.CUSTOM;

    protected Element(Parcel in) {
        id = in.readInt();
        parent = in.readParcelable(List.class.getClassLoader());
        content = in.readString();
        finished = in.readByte() != 0;
        customIndex = in.readInt();
        description = in.readString();
        parentElement = in.readParcelable(Element.class.getClassLoader());
        childrenElements = in.createTypedArrayList(Element.CREATOR);
        tags = in.createStringArrayList();
    }

    public static final Creator<Element> CREATOR = new Creator<Element>() {
        @Override
        public Element createFromParcel(Parcel in) {
            return new Element(in);
        }

        @Override
        public Element[] newArray(int size) {
            return new Element[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeParcelable(parent, flags);
        dest.writeString(content);
        dest.writeByte((byte) (finished ? 1 : 0));
        dest.writeInt(customIndex);
        dest.writeString(description);
        dest.writeParcelable(parentElement, flags);
        dest.writeTypedList(childrenElements);
        dest.writeStringList(tags);
    }

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
                   String description,@Nullable Element parentElement) {
        this.id = id;
        this.parent = parent;
        this.content = content;
        this.customIndex = customIndex;
        this.finished = (finished == 1);
        this.description = description;
        this.parentElement = parentElement;
        this.childrenElements = new ArrayList<>();
        this.tags = new ArrayList<>();
        if(parentElement != null)
            parentElement.addChildElement(this);

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
    public Element(List parent, String content,@Nullable Element parentElement) {
        this.parent = parent;
        this.content = content;
        this.customIndex = parent.getElements().size();
        this.finished = false;
        this.date = new Date();
        this.description = "";
        this.parentElement = parentElement;
        this.childrenElements = new ArrayList<>();
        this.tags = new ArrayList<>();

        if(parentElement != null)
            parentElement.addChildElement(this);

        //Save to database.
    }

    @Override
    public int compareTo(Element e) {
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

    public void addChildElement(Element child){
        this.childrenElements.add(child);
    }

    public void removeChildElement(Element child){
        this.childrenElements.remove(child);
    }

    public Element getParentElement() {
        return parentElement;
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