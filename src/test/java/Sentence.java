
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import opennlp.tools.util.Span;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author elahi
 */
public class Sentence {

    private String[] sentence;
    private Span span;
    private Integer index;
    private String[]  contextWords;

    public Sentence(String[] sentence, Span span, Integer index,String nameEntity,Integer windowSize) {
        this.sentence = sentence;
        this.span = span;
        this.index = index;
        this.contextWords=this.findContextWords(nameEntity,sentence,windowSize);
    }

    public String[] getSentence() {
        return sentence;
    }

    public Span getSpan() {
        return span;
    }

    public Integer getIndex() {
        return index;
    }

    @Override
    public String toString() {
        List<String> containsThese = Arrays.asList(contextWords);
        return  "\n"+containsThese+ ", index=" + index + '}';
    }

    private String[] findContextWords(String nameEntity, String[] sentence, Integer windowSize) {
        List<String> words = new ArrayList<String>();
        Integer limit = this.span.getEnd() + windowSize;
        for (Integer index = this.span.getEnd(); index < limit; index++) {
            if (index < sentence.length) {
                words.add(sentence[index]);
            } else {
                break;
            }
        }
        return words.toArray(String[]::new);
    }

    public String[] getContextWords() {
        return contextWords;
    }

}
