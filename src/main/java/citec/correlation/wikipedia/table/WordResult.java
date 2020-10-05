/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.wikipedia.table;

import citec.correlation.wikipedia.element.Triple;
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
public class WordResult  implements Comparator<WordResult>{

    @JsonIgnore
    public static Integer PROBABILITY_WORD_GIVEN_OBJECT = 1;
    @JsonIgnore
    public static Integer PROBABILITY_OBJECT_GIVEN_WORD = 2;
    @JsonIgnore
    public  Double multiple= null;
    @JsonProperty("Word")
    public String word= null;
    @JsonProperty("multiply")
    public  Double multipleValue= null;
    @JsonProperty("probabilities")
    private LinkedHashMap<String, Double> probabilities = new LinkedHashMap<String, Double>();
    @JsonProperty("Lift")
    public  Double lift= null;
    @JsonIgnore
    public static String RESULT_DIR = "result";
    
     public WordResult()  {
         
     }

    public WordResult(Triple object, Triple  word,String wordString,String partOfSfSpeech) throws IOException {
        this.word=wordString+"-"+partOfSfSpeech;
        this.probabilities.put(object.getProbability_Str(), this.format(object.getProbability_value()));
        this.probabilities.put(word.getProbability_Str(), this.format(word.getProbability_value()));
        this.multiple=this.format(object.getProbability_value()*word.getProbability_value());
        this.lift= object.getKB_WORD_FOUND()/(object.getKB_OR_WORD()*word.getKB_OR_WORD());
        this.multipleValue=Double.parseDouble(String.format("%.12f", multiple)); 
    }

    public Map<String, Double> getProbabilities() {
        return probabilities;
    }

  
    @Override
    public int compare(WordResult result1, WordResult result2) {
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

    public String getWord() {
        return word;
    }

    public Double getLift() {
        return lift;
    }

}
