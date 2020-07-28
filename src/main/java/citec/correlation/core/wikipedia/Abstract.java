/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.core.wikipedia;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author elahi
 */
public class Abstract {

    private String text = null;
    private List<String> sentences = new ArrayList<String>();

    public Abstract(String text, List<String> sentences) {
        this.text = text;
        this.sentences = sentences;
    }

    public String getText() {
        return text;
    }

    public List<String> getSentences() {
        return sentences;
    }

}
