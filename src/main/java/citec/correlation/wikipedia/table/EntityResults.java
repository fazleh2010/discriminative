/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.wikipedia.table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author elahi
 */
public class EntityResults {

    @JsonIgnore
    private static String PREFIX = "OBJECT";
    @JsonIgnore
    private static Integer index = 0;

    @JsonProperty("objectIndex")
    private String objectIndex;
    @JsonProperty("property")
    private String property;
    @JsonProperty("object")
    private String KB;
    //@JsonProperty("ListOfWords")
    //private List<String> words;
    @JsonProperty("detail")
    private List<WordResult> distributions = new ArrayList<WordResult>();
    @JsonProperty("numberOfEntitiesFoundInObject")
    private Integer numberOfEntitiesFoundInObject;

    public EntityResults(String property, String object, Integer numberOfEntitiesFoundInObject, List<WordResult> distributions) {
        this.property = property;
        this.KB = object;
        this.numberOfEntitiesFoundInObject = numberOfEntitiesFoundInObject;
        this.distributions = distributions;
        Collections.sort(this.distributions, new WordResult());
        Collections.reverse(this.distributions);
        index = index + 1;
        this.objectIndex = index.toString();
        /*for(Result result:this.distributions){
            System.out.println(result.toString());
            words.add(result.word);
        }*/

    }

    public static String getPREFIX() {
        return PREFIX;
    }

    public static Integer getIndex() {
        return index;
    }

    public String getObjectIndex() {
        return objectIndex;
    }

    public String getProperty() {
        return property;
    }

    public String getKB() {
        return KB;
    }

    public List<WordResult> getDistributions() {
        return distributions;
    }

    public Integer getNumberOfEntitiesFoundInObject() {
        return numberOfEntitiesFoundInObject;
    }

    @Override
    public String toString() {
        return "Results{" + "objectIndex=" + objectIndex + ", property=" + property + ", KB=" + KB + ", distributions=" + distributions + '}';
    }

}