/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.wikipedia.element;

/**
 *
 * @author elahi
 */
public class Triple {

    private final String probability_Str;
    private final Double probability_value;
    private final Double KB_WORD_FOUND;
    private final Double KB_OR_WORD;

    public Triple(String probability_Str, Double probability_value,Double KB_WORD_FOUND, Double KB_OR_WORD ) {
     this.probability_Str=probability_Str;
     this.probability_value=probability_value;
     this.KB_WORD_FOUND=KB_WORD_FOUND;
     this.KB_OR_WORD=KB_OR_WORD;
    }

    public String getProbability_Str() {
        return probability_Str;
    }

    public Double getProbability_value() {
        return probability_value;
    }

    public Double getKB_WORD_FOUND() {
        return KB_WORD_FOUND;
    }

    public Double getKB_OR_WORD() {
        return KB_OR_WORD;
    }
  
   
}
