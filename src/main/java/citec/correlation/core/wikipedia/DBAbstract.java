/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.core.wikipedia;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author elahi
 */
public class DBAbstract {

    private final String text;
    private final List<String> sentences;
    private Map<String, Boolean> wordExists=new HashMap<String, Boolean>();

    public DBAbstract(String text, List<String> sentences) {
        this.text = text;
        this.sentences = sentences;
    }

    public String getText() {
        return text;
    }

    public List<String> getSentences() {
        return sentences;
    }

    public Map<String, Boolean> getWordExists() {
        return wordExists;
    }

}
