/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.wikipedia.element;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.javatuples.Pair;

/**
 *
 * @author elahi
 */
@JsonPropertyOrder({"tableName","NUMBER_OF_ENTITIES", "KB_WORD_FOUND","WORD_FOUND", "KB_FOUND","KB", "Word","probabilities"})
public class Result {
    
    @JsonIgnore
    private String tableName;
    //@JsonProperty("KB")
    @JsonIgnore
    private String KB;
    //@JsonProperty("Word")
    @JsonIgnore
    private String Word;
    
    @JsonProperty("probabilities")
    private LinkedHashMap<String, Double> probabilities=new LinkedHashMap<String, Double>();
    @JsonIgnore
    public static String conditional_probability = "probability" ;
    @JsonIgnore
    public static String KB_STR = "KB" ;
    @JsonIgnore
    public static String WORD_STR = "Word" ;
    @JsonIgnore
    public static String RESULT_DIR = "result" ;
    

    public Result(Pair<String,Double> object,Pair<String,Double> word) throws IOException {
        //this.KB = object;
        //this.Word = word;
        //this.tableName = tableName;

        this.probabilities.put(object.getValue0(), object.getValue1());
        this.probabilities.put(word.getValue0(), word.getValue1());
    }

    public String getTableName() {
        return tableName;
    }

    /*public String getKB() {
        return KB;
    }

    public String getWord() {
        return Word;
    }*/

    public Map<String, Double> getProbabilities() {
        return probabilities;
    }

    public static String getConditional_probability() {
        return conditional_probability;
    }

    @Override
    public String toString() {
        return "Result{" + "tableName=" + tableName + ", KB=" + KB + ", Word=" + Word + ", probabilities=" + probabilities + ", KB_WORD_FOUND=" + '}';
    }

   
}
