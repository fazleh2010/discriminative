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
public class Results {

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
    private List<Result> distributions = new ArrayList<Result>();

    public Results(String property, String object, List<Result> distributions) {
        this.property = property;
        this.KB = object;
        this.distributions = distributions;
        Collections.sort(this.distributions, new Result()); 
        Collections.reverse(this.distributions);
        index = index + 1;
        this.objectIndex = index.toString();
        /*for(Result result:this.distributions){
            System.out.println(result.toString());
            words.add(result.word);
        }*/

    }

   
    public List<Result> getDistributions() {
        return distributions;
    }

    @Override
    public String toString() {
        return "Results{" + "objectIndex=" + objectIndex + ", property=" + property + ", KB=" + KB + ", distributions=" + distributions + '}';
    }

}
