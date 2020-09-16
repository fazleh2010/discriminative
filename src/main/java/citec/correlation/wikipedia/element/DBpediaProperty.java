/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.wikipedia.element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author elahi
 */
public class DBpediaProperty implements PropertyNotation {
    private String propertyString = null;
    private String subject = null;
    private String predicate = null;
    private String object = null;
    private static Map<String, List<String>> propertyList = new TreeMap<String, List<String>>();

    public DBpediaProperty(String propertyString) {
        this.propertyString = propertyString;
        if(propertyString.contains(" "))
          this.parsePropertyString(propertyString);
        else {
            this.subject = propertyString;
            this.predicate = propertyString;
            this.object = propertyString;
        }
    }

    public void parsePropertyString(String propertyString) {
        this.propertyString = propertyString;
        String[] words = propertyString.split(" ");
        this.subject = words[0];
        this.predicate = words[1];
        this.object = words[2];

    }

    public static void setPropertyList(Map<String, List<String>> propertyListGiven) {
        propertyList = propertyListGiven;
    }

    public String getPropertyString() {
        return propertyString;
    }

    public String getSubject() {
        return subject;
    }

    public String getPredicate() {
        return predicate;
    }

    public String getObject() {
        return object;
    }

    public List<String> getObjectList() {
        List<String> propertyValues = new ArrayList<String>();
        propertyValues.add(object);
        return propertyValues;
    }

    public static Map<String, List<String>> getPropertyList() {
        return propertyList;
    }

    @Override
    public String toString() {
        return subject + " " + predicate + " " + object;
    }

}
