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
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 *
 * @author elahi
 */
public class InterestedWords {

    private List<String> interestedWords = new ArrayList<String>();
    private List<String> alphabeticSorted = new ArrayList<String>();
    private Integer numberOfEntitiesToLimitInFile = -1;
    //private Integer numberOfEntities = 10;
    private Integer listSize = -1;
    private String sortFile = null;
    private String outputDir = null;
    private String className = null;
    private static String INTERESTED_WORD_FILE = "a_interestedList.txt";
    private Tables tables = null;

    public InterestedWords(String className, Tables tables, String outputDir) {
        this.tables = tables;
        this.className = className;
        this.outputDir = outputDir;
        this.sortFile = tables.getEntityTableDir() + INTERESTED_WORD_FILE;
    }

    public void getWords(Integer numberOfEntities,Integer listSize) throws IOException {
        this.interestedWords = FileFolderUtils.getSortedList(sortFile, numberOfEntities, listSize);
        alphabeticSorted.addAll(interestedWords);
        Collections.sort(alphabeticSorted);
    }

    public void prepareWords() throws Exception {
        tables.readSplitTables(outputDir, className);
        String str = this.generateINterestingWords(tables.getAllDBpediaEntitys());
        FileFolderUtils.stringToFiles(str, sortFile);
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

    public List<String> getInterestedWords() {
        return interestedWords;
    }

    public String getSortFile() {
        return sortFile;
    }

}
