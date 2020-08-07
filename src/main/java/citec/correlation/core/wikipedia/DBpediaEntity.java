/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.core.wikipedia;

import citec.correlation.main.Analyzer;
import citec.correlation.main.TextAnalyzer;
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
public class DBpediaEntity {

    private final static String PREFIX = "entity";
    private static Integer index = 0;
    private final String entityString;
    private final String entityUrl;
    private final String entityIndex;
    private Boolean democraticWord;
    private Map<String, String> properties = new TreeMap<String, String>();
    private Analyzer textAnalyzer = null;

    public DBpediaEntity(String entityString, Map<String, String> properties, String POS_TAGGER) throws Exception {
        this.entityString = entityString;
        this.entityUrl = this.getEntityUrl(this.entityString);
        index = index + 1;
        this.entityIndex = PREFIX + (index);
        if(this.getText(properties)!=null)
          this.textAnalyzer = new Analyzer(this.getText(properties), POS_TAGGER);
        this.properties = properties;

    }

    public DBpediaEntity(String entityString, Analyzer textAnalyzer) {
        this.entityString = entityString;
        this.entityUrl = this.getEntityUrl(this.entityString);
        index = index + 1;
        this.entityIndex = PREFIX + (index);
        this.textAnalyzer = textAnalyzer;

    }

    public DBpediaEntity(String entityString, Boolean democraticWord, Map<String, String> properties) {
        this.entityString = entityString;
        index = index + 1;
        this.entityIndex = PREFIX + (index);
        this.democraticWord = democraticWord;
        this.properties = properties;
        this.entityUrl = this.getEntityUrl(this.entityString);
    }

    public DBpediaEntity(String entityString) {
        String[] nGramSplit = entityString.split("");
        this.entityString = nGramSplit[0];
        this.democraticWord = null;
        this.properties = null;
        this.entityIndex = null;
        this.properties = null;
        this.entityUrl = this.getEntityUrl(this.entityString);
    }

    public DBpediaEntity(String entityString, boolean democraticWord) {
        this.entityString = entityString;
        this.democraticWord = democraticWord;
        this.properties = null;
        this.entityIndex = null;
        this.properties = null;
        this.entityUrl = this.getEntityUrl(this.entityString);
    }

    public String getEntityString() {
        return entityString;
    }

    public Boolean getDemocraticWord() {
        return democraticWord;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public String getEntityIndex() {
        return entityIndex;
    }

    @Override
    public String toString() {
        String start = entityString + " " + democraticWord + "\n";
        String line="";
        if(this.textAnalyzer!=null) {
          line = this.textAnalyzer.toString() + "\n";  
        }
        for (String property : this.properties.keySet()) {
            line += property + " " + properties.get(property) + "\n";
        }
        start += line;
        return start;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public String getEntityUrl() {
        return entityUrl;
    }

    public Analyzer getTextAnalyzer() {
        return textAnalyzer;
    }

    public static String getEntityUrl(String entityString) {
        return "<http://dbpedia.org/resource/" + entityString + ">";
    }

    private String getText(Map<String, String> properties) {
        return properties.get("http://dbpedia.org/ontology/abstract");
    }

}
