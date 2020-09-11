/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.wikipedia.element;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
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
    @JsonProperty("distribution")
    private List<Result> distributions = new ArrayList<Result>();

    public Results(String property,String object, List<Result> distributions) {
        this.property=property;
        this.KB = object;
        this.distributions = distributions;
        index = index + 1;
        this.objectIndex =  index.toString();

    }

    @Override
    public String toString() {
        return "Results{" + "objectIndex=" + objectIndex + ", property=" + property + ", KB=" + KB + ", distributions=" + distributions + '}';
    }

}
