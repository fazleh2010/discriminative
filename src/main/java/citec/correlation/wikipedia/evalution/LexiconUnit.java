/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.wikipedia.evalution;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.LinkedHashMap;
import java.util.List;
import org.javatuples.Pair;

/**
 *
 * @author elahi
 */
public class LexiconUnit {
    @JsonProperty("Word")
    private String word;
    @JsonIgnore
    private String partsOfSpeech;
    @JsonProperty("rank")
    private LinkedHashMap<Integer, List<String> > entityInfos = new LinkedHashMap<Integer, List<String>>();

    public LexiconUnit(String word,String  partsOfSpeech,LinkedHashMap<Integer, List<String> > entityInfos) {
        this.partsOfSpeech=partsOfSpeech;
        this.word = word;
        this.entityInfos = entityInfos;
    }

    public String getWord() {
        return word;
    }

    public LinkedHashMap<Integer, List<String> > getEntityInfos() {
        return entityInfos;
    }

}
