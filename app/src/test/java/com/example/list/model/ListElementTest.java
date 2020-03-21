package com.example.list.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class ListElementTest {

    @Test
    public void compareToAlphabetical() {
        ListElement a = new ListElement(1,null,"A",0,0, "2020-01-01 01:01:01");
        ListElement b = new ListElement(2,null,"B",0,1, "2020-02-01 01:01:01");

        ListElement.setMode(ListElement.compareMode.ALPHABETICAL);
        int expected = -1;
        int result = a.compareTo(b);
        assertEquals(expected,result);
    }

    @Test
    public void compareToChronological() {
        ListElement a = new ListElement(1,null,"A",0,0, "2020-01-01 01:01:01");
        ListElement b = new ListElement(2,null,"B",0,1, "2020-02-01 01:01:01");

        ListElement.setMode(ListElement.compareMode.CHRONOLOGICAL);
        int expected = -1;
        int result = a.compareTo(b);
        assertEquals(expected,result);
    }

    @Test
    public void compareToCustom() {
        ListElement a = new ListElement(1,null,"A",0,0, "2020-01-01 01:01:01");
        ListElement b = new ListElement(2,null,"B",0,1, "2020-02-01 01:01:01");

        ListElement.setMode(ListElement.compareMode.CUSTOM);
        int expected = -1;
        int result = a.compareTo(b);
        assertEquals(expected,result);
    }

    @Test
    public void addElementToList() {
        ListElement a = new ListElement(1,null,"A",0,0, "2020-01-01 01:01:01");
        ListElement b = new ListElement(2,null,"B",0,1, "2020-02-01 01:01:01");
        a.addElement(b);

        int expected = 1;
        int result = a.getElements().size();
        assertEquals(expected, result);
    }

    @Test
    public void addElementUpdateParent() {
        ListElement a = new ListElement(1,null,"A",0,0, "2020-01-01 01:01:01");
        ListElement b = new ListElement(2,null,"B",0,1, "2020-02-01 01:01:01");
        a.addElement(b);

        int expected = 1;
        int result = b.getParent().getElements().size();
        assertEquals(expected, result);
    }

    @Test
    public void removeElement() {
        ListElement a = new ListElement(1,null,"A",0,0, "2020-01-01 01:01:01");
        ListElement b = new ListElement(2,null,"B",0,1, "2020-02-01 01:01:01");
        a.addElement(b);
        a.removeElement(b);

        int expected = 0;
        int result = a.getElements().size();
        assertEquals(expected, result);
    }

    @Test
    public void insertElement() {
        ListElement a = new ListElement(1,null,"A",0,0, "2020-01-01 01:01:01");
        ListElement b = new ListElement(2,null,"B",0,1, "2020-02-01 01:01:01");
        ListElement c = new ListElement(3,null,"C",0,2, "2020-03-01 01:01:01");
        ListElement d = new ListElement(4,null,"D",0,3, "2020-04-01 01:01:01");
        ListElement e = new ListElement(5,null,"E",0,4, "2020-05-01 01:01:01");
        a.addElement(b);
        a.addElement(c);
        a.addElement(d);
        a.addElement(e);
        a.insertElement(e,1);

        ArrayList<ListElement> expected = new ArrayList<>(Arrays.asList(b, e, c, d));
        ArrayList<ListElement> result = a.getElements();
        assertArrayEquals(expected.toArray(), result.toArray());
    }
}