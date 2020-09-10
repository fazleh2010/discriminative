/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.wikipedia.calculation;

import citec.correlation.core.analyzer.TextAnalyzer;
import citec.correlation.utils.FileFolderUtils;
import citec.correlation.wikipedia.element.Result;
import citec.correlation.wikipedia.element.Results;
import citec.correlation.wikipedia.table.DBpediaEntity;
import citec.correlation.wikipedia.table.Tables;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.CopyOnWriteArrayList;
import org.javatuples.Pair;

/**
 *
 * @author elahi
 */
public class Calculation implements TextAnalyzer {

    private Map<String, List<Result>> kbResults = new HashMap<String, List<Result>>();
    private Map<String, List<DBpediaEntity>> entityCategories = new HashMap<String, List<DBpediaEntity>>();
    private Map<String, List<String>> entityTopTenWords = new HashMap<String, List<String>>();
    private Integer numberOfEntities = 857;
    private Integer PROBABILITY_WORD_GIVEN_OBJECT = 1;
    private Integer PROBABILITY_OBJECT_GIVEN_WORD = 2;

    public Calculation(String property, String inputJsonFile, String outputDir) throws Exception {
        Tables tables = new Tables(new File(inputJsonFile).getName(), outputDir);
        tables.readTable(property);
        for (String tableName : tables.getEntityTables().keySet()) {
            List<DBpediaEntity> dbpediaEntities = tables.getEntityTables().get(tableName).getDbpediaEntities();
            if (!dbpediaEntities.isEmpty()) {
                this.getObjectsOfproperties(dbpediaEntities, property);
                //all KBs..........................
                List<Results> kbResults = new ArrayList<Results>();
                for (String A : entityCategories.keySet()) {
                    List<Result> results = new ArrayList<Result>();
                    List<DBpediaEntity> dbpediaEntitiesGroup = entityCategories.get(A);
                    if (dbpediaEntitiesGroup.size() >= numberOfEntities) {
                        //all words
                        for (String word : dbpPartyWords) {
                            String B = word;
                            Result result=null;
                            Pair pairWord =null,pairObject=null;
                            //if (B.contains("democratic")) {

                            pairWord = this.countConditionalProbabilities(tableName, dbpediaEntitiesGroup, property, A, B, PROBABILITY_WORD_GIVEN_OBJECT);
                            //System.out.println(pairWord);

                            pairObject = this.countConditionalProbabilities(tableName, dbpediaEntities, property, A, B,PROBABILITY_OBJECT_GIVEN_WORD);
                            //System.out.println(pairObject);

                            //System.out.println(pair);
                            /*if (PROBABILITY_OBJECT_GIVEN_WORD) {
                             */
                            //Result result = this.countConditionalProbabilities(outputDir, tableName, dbpediaEntitiesGroup, property, A, B);
                            if (pairWord != null&&pairObject!=null) {
                                result=new Result(pairWord,pairObject);
                                results.add(result);   
                            }
                            //}
                        }//all words end
                        if (!results.isEmpty()) {
                            /*String keytoSort = Result.conditional_probability + "(" + Result.WORD_STR + "|" + Result.KB_STR + ")";
                            List<Result> resultsSorted = this.sortResutls(results, keytoSort);*/
                            Results kbResult = new Results(property, A, results);
                            kbResults.add(kbResult);
                        }

                    }//all KBs  end
                }
                FileFolderUtils.writeToJsonFile(kbResults, outputDir + File.separator + Result.RESULT_DIR + File.separator + tableName);

            }
        }
    }

    private Pair<String, Double> countConditionalProbabilities(String tableName, List<DBpediaEntity> dbpediaEntities, String propertyName, String A, String B, Integer flag) throws IOException {
        String texts = "";
        LinkedHashMap<String, Double> probabilities = new LinkedHashMap<String, Double>();
        Double KB_WORD_FOUND = 0.0, KB_FOUND = 0.0, WORD_FOUND = 0.0;
        Pair pair = null;

        for (DBpediaEntity dbpediaEntity : dbpediaEntities) {
            String text = dbpediaEntity.getText();
            texts += text + "\n";

            //Set<String> textWords=new HashSet<String>(this.countWords(text));
            Boolean objectFlag = false, wordFlag = false;

            if (dbpediaEntity.getProperties().containsKey(propertyName)) {

                List<String> objects = dbpediaEntity.getProperties().get(propertyName);
                if (objects.contains(A)) {
                    KB_FOUND++;
                    objectFlag = true;
                    //System.out.println("A"+A);
                }
            }

            if (dbpediaEntity.getInterestingWords().contains(B)) {
                WORD_FOUND++;
                wordFlag = true;
            }

            if (objectFlag && wordFlag) {
                KB_WORD_FOUND++;
            }

        }

        List<String> topWords = this.countWords(texts);
        entityTopTenWords.put(A, topWords);
        String probability_object_word_str = Result.conditional_probability + "(" + A + "|" + B + ")";
        String probability_word_object_str = Result.conditional_probability + "(" + B + "|" + A + ")";

        //if (WORD_FOUND > 10) {
        if (flag == PROBABILITY_OBJECT_GIVEN_WORD) {
            Double probability_object_word = (KB_WORD_FOUND) / (WORD_FOUND);
            pair = new Pair(probability_object_word_str, probability_object_word);
        }
        else if (flag == PROBABILITY_WORD_GIVEN_OBJECT) {
            Double probability_word_object = (KB_WORD_FOUND) / (KB_FOUND);

            /*if (probability_word_object < 0.24) {
                    return null;
                }*/
            pair = new Pair(probability_word_object_str, probability_word_object);
        } 

        return pair;

    }

    private void getObjectsOfproperties(List<DBpediaEntity> dbpediaEntities, String property) {
        LinkedHashSet<String> allObjects = new LinkedHashSet<String>();
        for (DBpediaEntity dbpediaEntity : dbpediaEntities) {
            if (!dbpediaEntity.getProperties().isEmpty()) {
                LinkedHashSet<String> objects = new LinkedHashSet<String>(dbpediaEntity.getProperties().get(property));
                allObjects.addAll(objects);
            }
        }

        for (DBpediaEntity DBpediaEntity : dbpediaEntities) {
            for (String key : DBpediaEntity.getProperties().keySet()) {
                if (!DBpediaEntity.getProperties().get(key).isEmpty()) {
                    String value = DBpediaEntity.getProperties().get(key).iterator().next();
                    if (allObjects.contains(value)) {
                        List<DBpediaEntity> list = new CopyOnWriteArrayList<DBpediaEntity>();
                        if (this.entityCategories.containsKey(value)) {
                            list = entityCategories.get(value);
                            list.add(DBpediaEntity);
                            entityCategories.put(value, list);
                        } else {
                            list.add(DBpediaEntity);
                            entityCategories.put(value, list);
                        }

                    }
                }

            }
        }

    }

    private List<String> countWords(String texts) {
        Map<String, Integer> wordCounts = new HashMap<String, Integer>();
        StringTokenizer st = new StringTokenizer(texts);
        while (st.hasMoreTokens()) {
            String token = st.nextToken().trim().toLowerCase();
            if (this.isNotStopWord(token)) {
                if (wordCounts.containsKey(token)) {
                    Integer number = wordCounts.get(token) + 1;
                    wordCounts.put(token, number);
                } else {
                    wordCounts.put(token, 1);
                }
            }
        }
        return sort(wordCounts);
    }

    private List<String> sort(Map<String, Integer> hm) {

        Set<Entry<String, Integer>> set = hm.entrySet();
        List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(
                set);
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                    Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        Integer maxNumber = 20, index = 0;
        List<String> topWords = new ArrayList<String>();

        for (Entry<String, Integer> entry : list) {
            //System.out.println(entry.getValue());
            //System.out.println(entry.getKey());
            index++;
            topWords.add(entry.getKey());
            if (index == maxNumber) {
                break;
            }

        }
        // System.out.println(topWords.toString());
        return topWords;
    }

    private Boolean isNotStopWord(String token) {
        if (ENGLISH_STOPWORDS.contains(token)) {
            return false;
        }
        return true;
    }

    private boolean isWordContains(String B, Set<String> textWords) {
        if (textWords.contains(B)) {
            return true;
        }
        return false;
    }

    /*if(isWordContains(B,textWords)) {
                System.out.println(B);
                System.out.println( textWords);
                WORD_FOUND++;
                wordFlag = true;  
            }*/
 /*for(String key:allIObjects) {
                    //System.out.println(key);
                 Result result=this.countConditionalProbabilities(outputDir,tableName, dbpediaEntities, propertyName, A, B);
                }*/
 /*Result result=this.countConditionalProbabilities(outputDir,tableName, dbpediaEntities, propertyName, A, B);
                this.results.add(result);
                FileFolderUtils.writeToJsonFile(this.results, outputDir + File.separator + Result.RESULT_DIR+ File.separator + tableName);
     */
    private List<Result> sortResutls(List<Result> results, String key) {
        List<Result> sortedResults = new ArrayList<Result>();
        List<Double> sorted = new ArrayList<Double>();
        Map<Double, Result> testHash = new HashMap<Double, Result>();
        for (Result result : results) {
            Double value = result.getProbabilities().get(key);
            testHash.put(value, result);
        }
        sorted = new ArrayList<Double>(testHash.keySet());
        Collections.sort(sorted, Collections.reverseOrder());
        for (Double value : sorted) {
            Result result = testHash.get(value);
            sortedResults.add(result);
        }
        //Collections.sort(sorted);
        return sortedResults;
    }
}
