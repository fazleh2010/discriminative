/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.core.wikipedia;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author elahi
 */
public class DBpediaEntity {

    private final static String PREFIX="entity";
    private static Integer index=0;
    private final String entityString;
    private final String entityIndex;
    private final Boolean democraticWord;
    private  Map<String,String> properties=new HashMap<String,String>();
    //private List<Abstract> abstracts;

    public DBpediaEntity(String entityString, Boolean democraticWord,Map<String,String> properties) {
        this.entityString = entityString;
        index=index+1;
        this.entityIndex=PREFIX+(index);
        this.democraticWord = democraticWord;
        this.properties=properties;
    }

    public DBpediaEntity(String entityString) {
        String[] nGramSplit = entityString.split("");
        this.entityString = nGramSplit[0];
        this.democraticWord = null;
        this.properties = null;
        this.entityIndex=null;
        this.properties=null;
    }

    public DBpediaEntity(String entityString, boolean democraticWord) {
        this.entityString = entityString;
        this.democraticWord = democraticWord;
        this.properties = null;
        this.entityIndex=null;
        this.properties=null;
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
        String start=  entityString + " " + democraticWord ;
        String line="\n";
        for(String property:this.properties.keySet()){
           line+=property+ " "+properties.get(property)+"\n";
        }
        start+=line;
        return start;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties=properties;
    }

}
