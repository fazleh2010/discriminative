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
import java.util.Map;

/**
 *
 * @author elahi
 */
@JsonPropertyOrder({"tableName", "KB", "Word","probabilities"})
public class Result {
    
    @JsonProperty("tableName")
    private String tableName;
    @JsonProperty("KB")
    private String KB;
    @JsonProperty("Word")
    private String Word;
    @JsonProperty("probabilities")
    private Map<String, Double> probabilities;
    @JsonIgnore
    public static String conditional_probability = "conditional_probability" ;
    @JsonIgnore
    public static String KB_STR = "KB" ;
    @JsonIgnore
    public static String WORD_STR = "Word" ;
    @JsonIgnore
    public static String RESULT_DIR = "result" ;

    public Result(String outputDir, String tableName, String object, String word, Map<String, Double> probabilities) throws IOException {
        this.KB = object;
        this.Word = word;
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

}
