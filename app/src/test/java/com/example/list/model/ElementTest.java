package com.example.list.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class ElementTest {

    @Test
    public void compareToAlphabetical() {
        Element a = new Element(1,null,"A",0,0, "2020-01-01 01:01:01");
        Element b = new Element(2,null,"B",0,1, "2020-02-01 01:01:01");

        Element.setMode(Element.compareMode.ALPHABETICAL);
        int expected = -1;
        int result = a.compareTo(b);
        assertEquals(expected,result);
    }

    @Test
    public void compareToChronological() {
        Element a = new Element(1,null,"A",0,0, "2020-01-01 01:01:01");
        Element b = new Element(2,null,"B",0,1, "2020-02-01 01:01:01");

        Element.setMode(Element.compareMode.CHRONOLOGICAL);
        int expected = -1;
        int result = a.compareTo(b);
        assertEquals(expected,result);
    }

    @Test
    public void compareToCustom() {
        Element a = new Element(1,null,"A",0,0, "2020-01-01 01:01:01");
        Element b = new Element(2,null,"B",0,1, "2020-02-01 01:01:01");

        Element.setMode(Element.compareMode.CUSTOM);
        int expected = -1;
        int result = a.compareTo(b);
        assertEquals(expected,result);
    }

    @Test
    public void addElementToList() {
        Element a = new Element(1,null,"A",0,0, "2020-01-01 01:01:01");
        Element b = new Element(2,null,"B",0,1, "2020-02-01 01:01:01");
        a.addElement(b);

        int expected = 1;
        int result = a.getElements().size();
        assertEquals(expected, result);
    }

    @Test
    public void addElementUpdateParent() {
        Element a = new Element(1,null,"A",0,0, "2020-01-01 01:01:01");
        Element b = new Element(2,null,"B",0,1, "2020-02-01 01:01:01");
        a.addElement(b);

        int expected = 1;
        int result = b.getParent().getElements().size();
        assertEquals(expected, result);
    }

    @Test
    public void removeElement() {
        Element a = new Element(1,null,"A",0,0, "2020-01-01 01:01:01");
        Element b = new Element(2,null,"B",0,1, "2020-02-01 01:01:01");
        a.addElement(b);
        a.removeElement(b);

        int expected = 0;
        int result = a.getElements().size();
        assertEquals(expected, result);
    }

    @Test
    public void insertElement() {
        Element a = new Element(1,null,"A",0,0, "2020-01-01 01:01:01");
        Element b = new Element(2,null,"B",0,1, "2020-02-01 01:01:01");
        Element c = new Element(3,null,"C",0,2, "2020-03-01 01:01:01");
        Element d = new Element(4,null,"D",0,3, "2020-04-01 01:01:01");
        Element e = new Element(5,null,"E",0,4, "2020-05-01 01:01:01");
        a.addElement(b);
        a.addElement(c);
        a.addElement(d);
        a.addElement(e);
        a.insertElement(e,1);

        ArrayList<Element> expected = new ArrayList<>(Arrays.asList(b, e, c, d));
        ArrayList<Element> result = a.getElements();
        assertArrayEquals(expected.toArray(), result.toArray());
    }
}