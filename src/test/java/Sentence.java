
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
public class Sentence implements Constants {

    private String[] sentence;
    private Span span;
    private Integer index;
    private String[] contextWords;
    private List<String> stringToCheck=new ArrayList<String>();

    public Sentence(String[] sentence, Span span, Integer index, String nameEntity, Integer windowSize,Map<String,String> objectValuePairs) {
        this.sentence = sentence;
        this.span = span;
        this.index = index;
        this.contextWords = this.findContextWords(nameEntity, sentence, windowSize);
        this.format(nameEntity,this.contextWords,objectValuePairs);
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
       // List<String> containsThese = Arrays.asList(contextWords);
        return "\n" +this.stringToCheck;
    }

    private String[] findContextWords(String nameEntity, String[] sentence, Integer windowSize) {
        List<String> contextWords = new ArrayList<String>();
        Integer limit = this.span.getEnd() + windowSize;
        for (Integer index = this.span.getEnd(); index < limit; index++) {

            if (index < sentence.length) {
                if (!sentence[index].contains(OBJECT)) {
                    contextWords.add(sentence[index]);
                }
            } else {
                break;
            }
        }
        return contextWords.toArray(String[]::new);
    }

    public String[] getContextWords() {
        return contextWords;
    }

    private void format(String nameEntity, String[] contextWords, Map<String,String> objectValuePairs) {
        for(String objectIndex:objectValuePairs.keySet()){
            String value=objectValuePairs.get(objectIndex);
            String line="s("+nameEntity.trim()+")"+" "+"context"+Arrays.asList(contextWords)+" o'("+value+")";
            stringToCheck.add(line);
        }    
    }

    public List<String> getStringToCheck() {
        return stringToCheck;
    }
    
}
