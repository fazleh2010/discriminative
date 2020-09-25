/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.utils;

import citec.correlation.core.analyzer.TextAnalyzer;
import citec.correlation.wikipedia.element.DBpediaEntity;
import citec.correlation.wikipedia.table.Tables;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author elahi
 */
public class InterestedWords {

    private Map<String, List<String>> interestedWords = new HashMap<String, List<String>>();
    private List<String> alphabeticSorted = new ArrayList<String>();
    private Integer numberOfEntitiesToLimitInFile = -1;
    //private Integer numberOfEntities = 10;
    private Integer listSize = -1;
    private List<String> sortFiles = new ArrayList<String>();
    private String outputDir = null;
    private String className = null;
    private Tables tables = null;
    public static String ALL_WORDS = "all";
    public static String PROPRTY_WISE = "PROPRTY_WISE";
    private static String FILE_NOTATION = "_interWords";
    //private static String INTERESTED_WORD_FILE = ALL_WORDS + ".txt";
    private Set<String> properties = new HashSet<String>();

    public InterestedWords(String className, Tables tables, String outputDir) {
        this.tables = tables;
        this.className = className;
        this.outputDir = outputDir;
        //this.sortFile = tables.getEntityTableDir() + "result/";
    }

    /*public void getWords(Integer numberOfEntities, Integer listSize,String type) throws IOException {
         if (type.contains(ALL_WORDS)) {
            List<String> words = FileFolderUtils.getSortedList(sortFile +  ALL_WORDS+".txt", numberOfEntities, listSize);
            alphabeticSorted.addAll(words);
            Collections.sort(alphabeticSorted);
            interestedWords.put(ALL_WORDS, alphabeticSorted);
        }

    }*/
    public void prepareWords(String className, String type) throws Exception {

        String str = null;
        tables.readSplitTables(outputDir, className);
        String outputLocation=tables.getEntityTableDir()+"result/";
        this.findAllProperties();
        if (type.contains(ALL_WORDS)) {
            str = this.prepareForAllProperties(tables.getAllDBpediaEntitys());
            String sortFile = outputLocation +type+FILE_NOTATION+ ".txt";
            FileFolderUtils.stringToFiles(str, sortFile);
            sortFiles.add(sortFile);
        } else if(type.contains(PROPRTY_WISE)) {
            for (String property : properties) {
                str = this.prepareForAllProperties(tables.getAllDBpediaEntitys(), property);
                String sortFile = outputLocation+ className+"_"+property +FILE_NOTATION+ ".txt";
                FileFolderUtils.stringToFiles(str, sortFile);
                this.sortFiles.add(sortFile);
            }
        }

    }

    private String prepareForAllProperties(List<DBpediaEntity> dbpediaEntities) {
        Map<String, Integer> mostCommonWords = new HashMap<String, Integer>();
        for (DBpediaEntity dbpediaEntity : dbpediaEntities) {
            Set<String> adjectives = dbpediaEntity.getAdjectives();
            Set<String> list = dbpediaEntity.getNouns();
            list.addAll(adjectives);
            for (String word : list) {
                word = word.toLowerCase().trim();
                if (TextAnalyzer.ENGLISH_STOPWORDS.contains(word)) {
                    continue;
                }
                //System.out.println("word"+word);
                Integer count = 0;
                if (mostCommonWords.containsKey(word)) {
                    count = mostCommonWords.get(word);
                    count = count + 1;
                    mostCommonWords.put(word, count);
                } else {
                    count = count + 1;
                    mostCommonWords.put(word, count);
                }
            }
        }
        return SortUtils.sort(mostCommonWords, numberOfEntitiesToLimitInFile);
    }
    
    private String prepareForAllProperties(List<DBpediaEntity> dbpediaEntities, String property) {
        Map<String, Integer> mostCommonWords = new HashMap<String, Integer>();

        for (DBpediaEntity dbpediaEntity : dbpediaEntities) {
            if (!dbpediaEntity.getProperties().containsKey(property)) {
                continue;
            }

            Set<String> adjectives = dbpediaEntity.getAdjectives();
            Set<String> list = dbpediaEntity.getNouns();
            list.addAll(adjectives);
            for (String word : list) {
                word = word.toLowerCase().trim();
                if (TextAnalyzer.ENGLISH_STOPWORDS.contains(word)) {
                    continue;
                }
                //System.out.println("word"+word);
                Integer count = 0;
                if (mostCommonWords.containsKey(word)) {
                    count = mostCommonWords.get(word);
                    count = count + 1;
                    mostCommonWords.put(word, count);
                } else {
                    count = count + 1;
                    mostCommonWords.put(word, count);
                }
            }

        }
        return SortUtils.sort(mostCommonWords, numberOfEntitiesToLimitInFile);
    }

    private void findAllProperties() {
        for (DBpediaEntity dbpediaEntity : tables.getAllDBpediaEntitys()) {
            properties.addAll(dbpediaEntity.getProperties().keySet());
        }
    }

    private String generateINterestingWords(List<DBpediaEntity> dbpediaEntities) {
        Map<String, Integer> mostCommonWords = new HashMap<String, Integer>();
        for (DBpediaEntity dbpediaEntity : dbpediaEntities) {
            Set<String> adjectives = dbpediaEntity.getAdjectives();
            Set<String> list = dbpediaEntity.getNouns();
            list.addAll(adjectives);
            for (String word : list) {
                word = word.toLowerCase().trim();
                if (TextAnalyzer.ENGLISH_STOPWORDS.contains(word)) {
                    continue;
                }
                //System.out.println("word"+word);
                Integer count = 0;
                if (mostCommonWords.containsKey(word)) {
                    count = mostCommonWords.get(word);
                    count = count + 1;
                    mostCommonWords.put(word, count);
                } else {
                    count = count + 1;
                    mostCommonWords.put(word, count);
                }
            }

        }
        return SortUtils.sort(mostCommonWords, numberOfEntitiesToLimitInFile);
    }

    public List<String> getAlphabeticSorted() {
        return alphabeticSorted;
    }

    public Map<String, List<String>> getInterestedWords() {
        return interestedWords;
    }

    public List<String> getSortFiles() {
        return sortFiles;
    }

}
