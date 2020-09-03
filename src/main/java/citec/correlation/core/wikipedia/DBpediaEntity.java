/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.core.wikipedia;

import citec.correlation.core.analyzer.Analyzer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.HashMap;
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
    private final String entityIndex;
    private final String entityUrl;
    private final String entityString;
    private  String dboClass;
    private Map<String, List<String>> properties = new TreeMap<String, List<String>>();
    private List<HashMap<String,Set<String>>> senetences=new ArrayList<HashMap<String,Set<String>>>();
    private String text=null;
    @JsonIgnore
    private Boolean democraticWord;

    public DBpediaEntity(String dboClass,String entityString, Map<String, List<String>> properties, String POS_TAGGER) throws Exception {
        this.dboClass=dboClass;
        this.entityString = entityString;
        this.entityUrl = this.getEntityUrl(this.entityString);
        index = index + 1;
        this.entityIndex = PREFIX + (index);
        this.text=this.getText(properties);
        if (this.text != null) {
            senetences=new Analyzer(this.text, POS_TAGGER,5).getSenetences();
        }
        this.properties = properties;
        this.properties.remove("dbo:abstract");

    }

    public DBpediaEntity(String entityString, Analyzer textAnalyzer) {
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
    }

    public String getEntityString() {
        return entityString;
    }

    public Boolean getDemocraticWord() {
        return democraticWord;
    }

    public Map<String, List<String>> getProperties() {
        return properties;
    }

    public String getEntityIndex() {
        return entityIndex;
    }

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

    public String getEntityUrl() {
        return entityUrl;
    }

    public List<HashMap<String, Set<String>>> getSenetences() {
        return senetences;
    }

    public static String getEntityUrl(String entityString) {
        return "<http://dbpedia.org/resource/" + entityString + ">";
    }

    public String getText() {
        return text;
    }

    private String getText(Map<String, List<String>> properties) {
        return properties.get("dbo:abstract").iterator().next();
    }

    public String getDboClass() {
        return dboClass;
    }

}
