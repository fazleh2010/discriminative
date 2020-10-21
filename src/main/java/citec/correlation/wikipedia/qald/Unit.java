/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.wikipedia.qald;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author elahi
 */
public class Unit {

    @JsonProperty("word")
    private String word;
    @JsonProperty("Qald_id")
    private List<String> qaldQuestionId = new ArrayList<String>();
    @JsonProperty("Sparql")
    private LinkedHashMap<String, String> sparqls = new LinkedHashMap<String, String>();
    @JsonIgnore
    private static String Sparql_ = "Sparql_";

    public Unit() {

    }

    public Unit(String word, String qaldQuestionId, String sparql) {
        this.qaldQuestionId.add(qaldQuestionId);
        this.sparqls.put(Sparql_ + qaldQuestionId.toString(), sparql);
        this.word = word;
    }

    public List<String> getQaldQuestionId() {
        return qaldQuestionId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setSparqls(String qaldQuestionId, String sparql) {
        this.sparqls.put(Sparql_ + qaldQuestionId.toString(), sparql);
    }

    public void setQaldQuestionId(String qaldQuestionId) {
        this.qaldQuestionId.add(qaldQuestionId);
    }

    public LinkedHashMap<String, String> getSparqls() {
        return sparqls;
    }

    @Override
    public String toString() {
        return "Unit{" + "word=" + word + ", qaldQuestionId=" + qaldQuestionId + ", sparqls=" + sparqls + '}';
    }

}
