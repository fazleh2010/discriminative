
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
    private String sentenceLine;
    private List<String> stringToCheck = new ArrayList<String>();
    private EntityInfo entityInfo=null;

    public Sentence(String sentenceLine,String[] sentence, Span span, Integer index, String nameEntity, Integer windowSize, EntityInfo entityInfo) {
        this.entityInfo=entityInfo;
        this.sentenceLine=sentenceLine;
        this.sentence = sentence;
        this.span = span;
        this.index = index;
        this.contextWords = this.findContextWords(nameEntity, sentence, windowSize);
        this.format(nameEntity, this.contextWords, entityInfo);
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
        return "\n" + this.stringToCheck;
    }

    private String[] findContextWords(String nameEntity, String[] sentence, Integer windowSize) {
        List<String> contextWords = new ArrayList<String>();
        Integer limit = this.span.getEnd() + windowSize;
        Integer limitIndex = 0;

        for (Integer index = this.span.getEnd(); index < limit; index++) {

            if (index < sentence.length) {
                if (sentence[index].contains(OBJECT)) {
                    break;
                } else {
                    contextWords.add(sentence[index]);
                }
            } else {
                break;
            }
        }
        return contextWords.toArray(String[]::new);
    }

    /*private String[] findContextWords(String nameEntity, String[] sentence, Integer windowSize) {
        List<String> contextWords = new ArrayList<String>();
        Integer limit = this.span.getEnd() + windowSize;
        for (Integer index = this.span.getEnd(); index < limit; index++) {

            if (index < sentence.length) {
                System.out.println("object check:"+sentence[index]);
                if (!sentence[index].contains(OBJECT)) {
                    contextWords.add(sentence[index]);
                }
            } else {
                break;
            }
        }
        System.out.println("contextWord:"+contextWords.toString());
        return contextWords.toArray(String[]::new);
    }*/
    public String[] getContextWords() {
        return contextWords;
    }

    private void format(String nameEntity, String[] contextWords, EntityInfo entityInfo) {
        Map<String, String> objectValuePairs = entityInfo.getObjectValuePairs();
        for (String objectIndex : objectValuePairs.keySet()) {
            String value = objectValuePairs.get(objectIndex);
            String line = "s(" + nameEntity.trim() + ")" + " " + "context" + Arrays.asList(contextWords) + " o'(" + value + ")";
            stringToCheck.add(line);
        }
    }

    public List<String> getStringToCheck() {
        return stringToCheck;
    }

    public String getSentenceLine() {
        return sentenceLine;
    }
    
    public String getSubject() {
        return this.entityInfo.getSubject();
    }

    public String getSubjectLink() {
       return this.entityInfo.getSubjectLink();
    }

    public Map<String, String> getObjects() {
        return this.entityInfo.getObjects();
    }

    public Map<String, String> getObjectValuePairs() {
        return this.entityInfo.getObjectValuePairs();
    }

}
