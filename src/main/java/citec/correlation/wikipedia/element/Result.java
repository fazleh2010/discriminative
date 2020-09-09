/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.wikipedia.element;

import citec.correlation.utils.FileFolderUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author elahi
 */
@JsonPropertyOrder({"tableName","NUMBER_OF_ENTITIES", "KB_WORD_FOUND","WORD_FOUND", "KB_FOUND","KB", "Word","probabilities"})
public class Result {
    
    @JsonIgnore
    private String tableName;
    @JsonProperty("KB")
    private String KB;
    @JsonProperty("Word")
    private String Word;
    
    @JsonIgnore
    //@JsonProperty("KB_WORD_FOUND")
    private Double KB_WORD_FOUND=0.0;
    @JsonIgnore
    //@JsonProperty("WORD_FOUND")
    private Double WORD_FOUND=0.0;
    
    //@JsonProperty("KB_FOUND")
    @JsonIgnore
    private Double KB_FOUND=0.0;
    //@JsonProperty("NUMBER_OF_ENTITIES")
    @JsonIgnore
    private Integer NUMBER_OF_ENTITIES=0;
    @JsonProperty("probabilities")
    private LinkedHashMap<String, Double> probabilities;
    @JsonIgnore
    public static String conditional_probability = "conditional_probability" ;
    @JsonIgnore
    public static String KB_STR = "KB" ;
    @JsonIgnore
    public static String WORD_STR = "Word" ;
    @JsonIgnore
    public static String RESULT_DIR = "result" ;
    

    public Result(String outputDir, String tableName, String object, String word, LinkedHashMap<String, Double> probabilities,
             Integer NUMBER_OF_ENTITIES,Double KB_WORD_FOUND,Double WORD_FOUND,Double KB_FOUND) throws IOException {
        this.KB = object;
        this.Word = word;
        this.NUMBER_OF_ENTITIES=NUMBER_OF_ENTITIES;
        this.KB_FOUND=KB_FOUND;
        this.WORD_FOUND=WORD_FOUND;
        this.KB_WORD_FOUND=KB_WORD_FOUND;
        this.tableName = tableName;
        this.probabilities = probabilities;
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
        return "Result{" + "tableName=" + tableName + ", KB=" + KB + ", Word=" + Word + ", probabilities=" + probabilities + ", KB_WORD_FOUND=" + KB_WORD_FOUND + ", WORD_FOUND=" + WORD_FOUND + ", KB_FOUND=" + KB_FOUND + ", NUMBER_OF_ENTITIES=" + NUMBER_OF_ENTITIES + '}';
    }

   
}
