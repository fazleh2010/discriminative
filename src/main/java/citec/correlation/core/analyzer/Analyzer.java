/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.core.analyzer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import citec.correlation.wikipedia.element.PropertyNotation;
import edu.stanford.nlp.util.Sets;
import java.util.Arrays;
import java.util.TreeMap;

/**
 *
 * @author elahi
 *
 */
public class Analyzer implements TextAnalyzer,PropertyNotation {

    @JsonIgnore
    private static String resources = "src/main/resources/";
    @JsonIgnore
    private static String stanfordModelFile = resources + "stanford-postagger-2015-12-09/models/english-left3words-distsim.tagger";
    @JsonIgnore
    private static MaxentTagger taggerModel = new MaxentTagger(stanfordModelFile);
    @JsonIgnore
    private Integer numberOfSentences = 0;
    
    @JsonIgnore 
    public static final Map<String,Set<String>> propertySelectedWords = new TreeMap<String,Set<String>>();


    //private List<HashMap<String, Set<String>>> sentences = new ArrayList<HashMap<String, Set<String>>>();
    
    private Set<String> words = new HashSet<String>();
    private Set<String> adjectives = new HashSet<String>();
    private Set<String> nouns = new HashSet<String>();

    private String text = null;
    //private final DBpediaAbstract dbpediaAbstract;

    public Analyzer(String property,String inputText, String analysisType, Integer numberOfSentences) throws Exception {
        this.initialize();
        
        this.numberOfSentences = numberOfSentences;
        this.text = inputText;
        //this.dbpediaAbstract = new DBpediaAbstract(inputText);
        if (analysisType.contains(POS_TAGGER)) {
            Reader inputString = new StringReader(inputText);
            BufferedReader reader = new BufferedReader(inputString);
            posTagger(property,reader);
        }
    }

    private void posTagger(String property,BufferedReader reader) throws Exception {
        taggerModel = new MaxentTagger(stanfordModelFile);
        Map<Integer, Map<String, Set<String>>> sentencePosTags = new HashMap<Integer, Map<String, Set<String>>>();
        Map<Integer, Set<String>> sentenceWords = new HashMap<Integer, Set<String>>();

        List<List<HasWord>> sentences = MaxentTagger.tokenizeText(reader);
        //List<List<HasWord>> sentences = MaxentTagger.tokenizeText(new BufferedReader(new FileReader(inputText)));
        Integer index = 0;
        for (List<HasWord> sentence : sentences) {
            index++;
            Set<String> wordsofSentence = new HashSet<String>();
            Map<String, Set<String>> posTaggers = new HashMap<String, Set<String>>();
            List<TaggedWord> tSentence = taggerModel.tagSentence(sentence);
            for (TaggedWord taggedWord : tSentence) {
                String word = taggedWord.word();
                if(isStopWord(word)){
                    continue;
                }
                if (taggedWord.tag().startsWith(TextAnalyzer.ADJECTIVE) || taggedWord.tag().startsWith(TextAnalyzer.NOUN)) {
                    posTaggers = this.populateValues(taggedWord.tag(), word, posTaggers);
                }
                wordsofSentence.add(word);
            }
            sentenceWords.put(index, wordsofSentence);
            sentencePosTags.put(index, posTaggers);
        }
        
        for (Integer number : sentenceWords.keySet()) {
            if (sentenceWords.get(number) != null) {
                Set<String> set = sentenceWords.get(number);
                words.addAll(set);
            }

            if (sentencePosTags.get(number).get(TextAnalyzer.NOUN) != null) {
                Set<String> set = sentencePosTags.get(number).get(TextAnalyzer.NOUN);
                nouns.addAll(set);
            }
            if (sentencePosTags.get(number).get(TextAnalyzer.ADJECTIVE) != null) {
                Set<String> set = sentencePosTags.get(number).get(TextAnalyzer.ADJECTIVE);
                adjectives.addAll(set);
            }

            number++;
            if (number == numberOfSentences) {
                break;
            }
            //System.out.println(sentenceInfo);
        }

        /*for (Integer number : sentenceWords.keySet()) {
            HashMap<String, Set<String>> sentenceInfo = new HashMap<String, Set<String>>();

            if (sentenceWords.get(number) != null) {
                Set<String> words = sentenceWords.get(number);
                sentenceInfo.put(WORD + "_" + number.toString(), words);
            }
            else
                 sentenceInfo.put(WORD + "_" + number.toString(), new HashSet<String>());
            if (sentencePosTags.get(number).get(TextAnalyzer.NOUN) != null) {
                Set<String> nouns = sentencePosTags.get(number).get(TextAnalyzer.NOUN);
                sentenceInfo.put(NOUN + "_" + number.toString(), nouns);
            }
            else
                sentenceInfo.put(NOUN + "_" + number.toString(), new HashSet<String>());
            if (sentencePosTags.get(number).get(TextAnalyzer.ADJECTIVE) != null) {
                Set<String> adjectives = sentencePosTags.get(number).get(TextAnalyzer.ADJECTIVE);
                sentenceInfo.put(ADJECTIVE + "_" + number.toString(), adjectives);
            }
            else
                 sentenceInfo.put(ADJECTIVE + "_" + number.toString(),new HashSet<String>());

            //sentenceInfo.put(NOUN + "_" + number.toString(), sentencePosTags.get(number).get(TextAnalyzer.NOUN));
            //sentenceInfo.put(ADJECTIVE + "_" + number.toString(), sentencePosTags.get(number).get(TextAnalyzer.ADJECTIVE));
            this.sentences.add(sentenceInfo);
            number++;
            if (number == numberOfSentences) {
                break;
            }
            //System.out.println(sentenceInfo);
        }*/
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

    public String getText() {
        return text;
    }

    /*public List<HashMap<String, Set<String>>> getSenetences() {
        return sentences;
    }*/

    /*public Map<String, Set<String>> getPosTaggers(Integer index) {
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
    }*/

 /*@Override
    public String toString() {
        String str="";
        for(Integer index=0;index<sentenceWords.size();index++){
               String text=dbpediaAbstract.getText()+"\n"
                      +this.sentencePosTags.get(index)+"\n"
                      +this.sentenceWords.get(index)+"\n";
               str+=text+"\n";
        }
       
                           
        return str;
    }*/

    private boolean isStopWord(String word) {
        word=word.trim().toLowerCase();
        if(ENGLISH_STOPWORDS.contains(word)){
            return true;
        }
        return false;
    }

    public Set<String> getWords() {
        return words;
    }

    public Set<String> getAdjectives() {
        return adjectives;
    }

    public Set<String> getNouns() {
        return nouns;
    }


    private void populate(Set<String> wordsOfSentence,String key, Map<String, Set<String>> hash) {
        if (hash.containsKey(key)) {
            Set<String> existingWords = hash.get(key);
            hash.put(key, Sets.union(existingWords, wordsOfSentence));
        } else {
            hash.put(key, wordsOfSentence);
        }
    }

    private void initialize() {
        propertySelectedWords.put(PropertyNotation.dbo_party, dbpPartyWords);
    }

}
