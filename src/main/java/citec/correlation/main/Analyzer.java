/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.main;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author elahi
 *
 */
public class Analyzer implements TextAnalyzer {

    private static String resources = "src/main/resources/";
    private static String stanfordModelFile = resources + "stanford-postagger-2015-12-09/models/english-left3words-distsim.tagger";
    private static MaxentTagger taggerModel = new MaxentTagger(stanfordModelFile);
    private Map<Integer,Map<String, Set<String>>> sentencePosTags = new HashMap<Integer,Map<String, Set<String>>>();
    private Map<Integer,Set<String>> sentenceWords = new  HashMap<Integer,Set<String>>();
    private final DBpediaAbstract dbpediaAbstract;

    public Analyzer(String inputText, String analysisType) throws Exception {
        this.dbpediaAbstract = new DBpediaAbstract(inputText);
        if (analysisType.contains(POS_TAGGER)) {
            Reader inputString = new StringReader(inputText);
            BufferedReader reader = new BufferedReader(inputString);
            posTagger(reader);
        }
    }

    private void posTagger(BufferedReader reader) throws Exception {
        taggerModel = new MaxentTagger(stanfordModelFile);
        List<List<HasWord>> sentences = MaxentTagger.tokenizeText(reader);
        //List<List<HasWord>> sentences = MaxentTagger.tokenizeText(new BufferedReader(new FileReader(inputText)));
        Integer index=0;
        for (List<HasWord> sentence : sentences) {
            index++;
            Set<String> words=new HashSet<String>();
            Map<String, Set<String>> posTaggers=new  HashMap<String, Set<String>>();
            List<TaggedWord> tSentence = taggerModel.tagSentence(sentence);
            for (TaggedWord taggedWord : tSentence) {
                String word = taggedWord.word();
                //String key = null;
                if (taggedWord.tag().startsWith(TextAnalyzer.ADJECTIVE) || taggedWord.tag().startsWith(TextAnalyzer.NOUN)) {
                    posTaggers = this.populateValues(taggedWord.tag(), word, posTaggers);
                }
                words.add(word);
            }
            sentenceWords.put(index, words);
            sentencePosTags.put(index, posTaggers);
        }
    }

    private Map<String, Set<String>> populateValues(String key, String value, Map<String, Set<String>> posTaggers) {
        Set<String> words = new HashSet<String>();
        if (posTaggers.containsKey(key)) {
            words = posTaggers.get(key);
        }
        words.add(value);
        posTaggers.put(key, words);

        return posTaggers;
    }

    public Map<String, Set<String>> getPosTaggers(Integer index) {
        return sentencePosTags.get(index);
    }

    public Set<String> getAdjectives(Integer index) {
        return sentencePosTags.get(index).get(TextAnalyzer.ADJECTIVE);
    }

    public Set<String> getNoun(Integer index) {
        return sentencePosTags.get(index).get(TextAnalyzer.NOUN);
    }

    public Set<String> getWords(Integer index) {
        return sentenceWords.get(index);
    }

    public String getDbpediaAbstract() {
        return dbpediaAbstract.getText();
    }

    @Override
    public String toString() {
        String str="";
        for(Integer index=0;index<sentenceWords.size();index++){
               String text=dbpediaAbstract.getText()+"\n"
                      +this.sentencePosTags.get(index)+"\n"
                      +this.sentenceWords.get(index)+"\n";
               str+=text+"\n";
        }
       
                           
        return str;
    }

}
