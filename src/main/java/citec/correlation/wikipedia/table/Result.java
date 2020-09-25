/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.wikipedia.table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.javatuples.Pair;

/**
 *
 * @author elahi
 */
public class Result  implements Comparator<Result>{

    @JsonIgnore
    public static Integer PROBABILITY_WORD_GIVEN_OBJECT = 1;
    @JsonIgnore
    public static Integer PROBABILITY_OBJECT_GIVEN_WORD = 2;
    @JsonIgnore
    public  Double multiple= null;
    @JsonIgnore
    public  String multipleStr= null;
    

    @JsonProperty("probabilities")
    private LinkedHashMap<String, Double> probabilities = new LinkedHashMap<String, Double>();
    @JsonIgnore
    public static String conditional_probability = "probability";
    public static String RESULT_DIR = "result";
    
     public Result()  {
         
     }

    public Result(Pair<String, Double> object, Pair<String, Double> word) throws IOException {
        this.probabilities.put(object.getValue0(), this.format(object.getValue1()));
        this.probabilities.put(word.getValue0(), this.format(word.getValue1()));
        this.multiple=this.format(object.getValue1()*word.getValue1());
        this.multipleStr=String.format("%.12f", multiple);
    }

    public Map<String, Double> getProbabilities() {
        return probabilities;
    }

    public static String getConditional_probability() {
        return conditional_probability;
    }
    
    @Override
    public int compare(Result result1, Result result2) {
       return  Double.compare(result1.getMultiple(), result2.getMultiple());
    }

    @Override
    public String toString() {
        return "Result{" + "probabilities=" + probabilities + '}';
    }

    private Double format(double value) {
        return Double.parseDouble(new DecimalFormat("##.##########").format(value));
    }

    public  Double getMultiple() {
        return multiple;
    }

   

}