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

public class ListElement implements Comparable<ListElement>, Parcelable {
    private int id;
    private final List parent;
    private String content;
    private boolean finished;
    private int customIndex;
    private  Date date;
    private String description;
    private final ListElement parentElement;
    private final ArrayList<ListElement> childrenElements;
    private String[] tags;

    private static compareMode mode = compareMode.CUSTOM;

    protected ListElement(Parcel in) {
        id = in.readInt();
        parent = in.readParcelable(List.class.getClassLoader());
        content = in.readString();
        finished = in.readByte() != 0;
        customIndex = in.readInt();
        description = in.readString();
        parentElement = in.readParcelable(ListElement.class.getClassLoader());
        childrenElements = in.createTypedArrayList(ListElement.CREATOR);
        tags = in.createStringArray();
    }

    public static final Creator<ListElement> CREATOR = new Creator<ListElement>() {
        @Override
        public ListElement createFromParcel(Parcel in) {
            return new ListElement(in);
        }

        @Override
        public ListElement[] newArray(int size) {
            return new ListElement[size];
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
        dest.writeStringArray(tags);
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
    public ListElement(int id, List parent, String content, int finished, int customIndex, String date,
                       String description, @Nullable ListElement parentElement) {
        this.id = id;
        this.parent = parent;
        this.content = content;
        this.customIndex = customIndex;
        this.finished = (finished == 1);
        this.description = description;
        this.parentElement = parentElement;
        this.childrenElements = new ArrayList<>();
        this.tags = new String[]{};

        if(parent != null)
            parent.addElement(this);
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
    public ListElement(List parent, String content, @Nullable ListElement parentElement) {
        this.parent = parent;
        this.content = content;
        this.customIndex = parent.getElements().size();
        this.finished = false;
        this.date = new Date();
        this.description = "";
        this.parentElement = parentElement;
        this.childrenElements = new ArrayList<>();
        this.tags = new String[]{};

        if(parent != null)
            parent.addElement(this);
        if(parentElement != null)
            parentElement.addChildElement(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListElement element = (ListElement) o;
        return id == element.id;
    }

    @Override
    public int compareTo(ListElement e) {
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

    public static void setMode(compareMode mode) {
        ListElement.mode = mode;
    }

    public int getId(){
        return id;
    }

    public List getParent(){
        return parent;
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

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public void addChildElement(ListElement child){
        this.childrenElements.add(child);
    }

    public void removeChildElement(ListElement child){
        this.childrenElements.remove(child);
    }

    public ArrayList<ListElement> getChildrenElements() {
        return childrenElements;
    }

    public ListElement getParentElement() {
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