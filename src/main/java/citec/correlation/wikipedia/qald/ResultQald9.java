/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.wikipedia.qald;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Set;

/**
 *
 * @author elahi
 */
public class ResultQald9 {
    private String id;
    private String question;
    private String sparql;
    private Set<String> adjectives;
    private Set<String> nouns;

    public ResultQald9(String id, String question, String sparql, Set<String> nouns, Set<String> adjectives) {
        this.id = id;
        this.question = question;
        this.sparql = sparql;
        this.nouns = nouns;
        this.adjectives = adjectives;
    }


    public String getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getSparql() {
        return sparql;
    }

    public Set<String> getAdjectives() {
        return adjectives;
    }

    public Set<String> getNouns() {
        return nouns;
    }

    @Override
    public String toString() {
        return "ResultQald9{" + "id=" + id + ", question=" + question + ", sparql=" + sparql + ", adjectives=" + adjectives + ", nouns=" + nouns + '}';
    }
}
