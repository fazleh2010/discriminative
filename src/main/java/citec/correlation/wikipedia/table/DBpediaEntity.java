/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.wikipedia.table;

import citec.correlation.core.analyzer.Analyzer;
import citec.correlation.wikipedia.element.DBpediaProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author elahi
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DBpediaEntity {

    @JsonIgnore
    private static String PREFIX = "entity";
    private static Integer index = 0;
    @JsonProperty("entityIndex")
    private String entityIndex;
    @JsonProperty("entityUrl")
    private String entityUrl;
    @JsonIgnore
    private String entityString;
    @JsonProperty("inputFileName")
    private String inputFileName;
    @JsonProperty("dboClass")
    private String dboClass;
    @JsonProperty("properties")
    private Map<String, List<String>> properties = new TreeMap<String, List<String>>();
    @JsonProperty("interestingWords")
    private Set<String> interestingWords = new TreeSet<String>();
    /*@JsonProperty("senetences")
    private List<HashMap<String,Set<String>>> senetences=new ArrayList<HashMap<String,Set<String>>>();*/
    @JsonProperty("words")
    private Set<String> words = new  HashSet<String>();
    @JsonProperty("adjectives")
    private Set<String> adjectives = new  HashSet<String>();
    @JsonProperty("nouns")
    private Set<String> nouns = new  HashSet<String>();
    @JsonProperty("text")
    private String text = null;
    @JsonIgnore
    private Boolean democraticWord;

    //this constructor is for searilization of json string to a Java class
    public DBpediaEntity() {

    }

    public DBpediaEntity(String inputFileName, String dboClass, String dboProperty, String entityString, Map<String, List<String>> properties, String POS_TAGGER) throws Exception {
        this.inputFileName = inputFileName;
        this.dboClass = dboClass;
        this.entityString = entityString;
        this.entityUrl = this.getEntityUrl(this.entityString);
        index = index + 1;
        this.entityIndex = PREFIX + (index);
        this.text = this.getText(properties, DBpediaProperty.DBO_ABSTRACT);
        if (this.text != null) {
            Analyzer analyzer = new Analyzer(dboProperty, this.text, POS_TAGGER, 5);
            this.words = analyzer.getWords();
            this.nouns=analyzer.getNouns();
            this.adjectives=analyzer.getAdjectives();
            this.interestingWords = analyzer.getInterestingWords();
        }
        this.properties = properties;
        this.properties.remove(DBpediaProperty.DBO_ABSTRACT);

    }

    /*public DBpediaEntity(String entityString, Analyzer textAnalyzer) {
        this.entityString = entityString;
        this.entityUrl = this.getEntityUrl(this.entityString);
        index = index + 1;
        this.entityIndex = PREFIX + (index);

    }

    public DBpediaEntity(String entityString, Boolean democraticWord, Map<String, List<String>> properties) {
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
    }*/

 /*@Override
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
    }*/
    public void setProperties(Map<String, List<String>> properties) {
        this.properties = properties;
    }

    public static String getEntityUrl(String entityString) {
        return "<http://dbpedia.org/resource/" + entityString + ">";
    }

    private String getText(Map<String, List<String>> properties, String property) {
        try {
            return properties.get(property).iterator().next();
        } catch (NullPointerException e) {
            return "";
        }
    }

    public static String getPREFIX() {
        return PREFIX;
    }

    public Map<String, List<String>> getProperties() {
        return properties;
    }

    public static Integer getIndex() {
        return index;
    }

    public String getEntityIndex() {
        return entityIndex;
    }

    public String getEntityUrl() {
        return entityUrl;
    }

    public String getEntityString() {
        return entityString;
    }

    public String getDboClass() {
        return dboClass;
    }

    public Set<String> getWords() {
        return words;
    }

    public Set<String> getAdjectives() {
        return adjectives;
    }

    public Set<String> getNouns() {
        return nouns;
    }

  

    public String getText() {
        return text;
    }

    public Boolean getDemocraticWord() {
        return democraticWord;
    }

    public String getInputFileName() {
        return inputFileName;
    }

    public Set<String> getInterestingWords() {
        return interestingWords;
    }

    @Override
    public String toString() {
        return "{" + "entityUrl=" + entityUrl + ", dboClass=" + dboClass + ", properties=" + properties + '}';
    }

}
