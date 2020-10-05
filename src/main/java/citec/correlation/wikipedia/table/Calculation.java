 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.wikipedia.table;

import citec.correlation.core.analyzer.TextAnalyzer;
import citec.correlation.wikipedia.utils.FileFolderUtils;
import citec.correlation.wikipedia.utils.SortUtils;
import citec.correlation.wikipedia.element.DBpediaEntity;
import citec.correlation.wikipedia.element.InterestedWords;
import citec.correlation.wikipedia.element.Triple;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import org.javatuples.Pair;

/**
 *
 * @author elahi
 */
public class Calculation implements TextAnalyzer {

    private InterestedWords interestedWords =null;
    private  Map<String, List<EntityResults>> tableResults = new HashMap<String, List<EntityResults> >();
    private Integer numberOfEntities = 200;
    private Integer numberOfEntitiesSelected=50;
    private Integer objectMinimumENtities=50;
   
    /*public Calculation(String property, String inputJsonFile, String outputDir) throws Exception {
        Tables tables = new Tables(new File(inputJsonFile).getName(), outputDir);
        tables.readTable(property);
        this.findInterestedWordsForEntities(tables.getAllDBpediaEntitys());
        this.calculation(tables,outputDir);
        System.out.println(tableResults);
        
    }*/

    public Calculation(Tables tables, String className, InterestedWords interestedWords,Integer numberOfEntitiesSelected,Integer objectMinimumENtities,String outputDir) throws IOException, Exception {
        this.numberOfEntitiesSelected=numberOfEntitiesSelected;
        this.objectMinimumENtities=objectMinimumENtities;
        this.interestedWords=interestedWords;
        this.calculation(tables,className,tables.getEntityTableDir());
    }
    
    private void calculation(Tables tables, String className,String outputDir) throws IOException, Exception {
        tables.readTable(className);
        Map<String, List<DBpediaEntity>> entityCategories = new HashMap<String, List<DBpediaEntity>>();
        for (String tableName : tables.getEntityTables().keySet()) {
            List<DBpediaEntity> dbpediaEntities = tables.getEntityTables().get(tableName).getDbpediaEntities();
            if(dbpediaEntities.size()<numberOfEntitiesSelected)
                     continue;
           
            String property = Tables.getProperty(tableName);
            String classNameAndProperty = Tables.getClassAndProperty(tableName);
            List<String> selectedWords=new ArrayList<String>();
            
            
            /*if (!tableName.contains("dbo:party")) {
                continue;
            }*/
            
            if(this.interestedWords.getPropertyInterestedWords().containsKey(classNameAndProperty)) {
                selectedWords=this.interestedWords.getPropertyInterestedWords().get(classNameAndProperty);
                //System.out.println("selectedWord:"+selectedWords.toString());
            }
            entityCategories = this.getObjectsOfproperties(property, dbpediaEntities);
            /*for(String key:entityCategories.keySet()){
                System.out.println(key+" :"+entityCategories.get(key));
            }*/

            //all KBs..........................
            List<EntityResults> kbResults = new ArrayList<EntityResults>();
            for (String objectOfProperty : entityCategories.keySet()) {
                List<WordResult> results = new ArrayList<WordResult>();
                List<DBpediaEntity> dbpediaEntitiesGroup = entityCategories.get(objectOfProperty);
                Integer numberOfEntitiesFoundInObject=dbpediaEntitiesGroup.size();
                if(dbpediaEntitiesGroup.size()<objectMinimumENtities)
                     continue;
                
                
                
              
                //System.out.println("KB:"+A);
                //all words
                for (String word : selectedWords) {
                    String partsOfSpeech=null;
                    if(this.interestedWords.getAdjectives().contains(word))
                       partsOfSpeech= TextAnalyzer.ADJECTIVE;
                    else if(this.interestedWords.getNouns().contains(word)){
                       partsOfSpeech= TextAnalyzer.NOUN; 
                    }
                   
                    //System.out.println("word:"+word);
                    WordResult result = null;
                    Triple pairWord = this.countConditionalProbabilities(tableName, dbpediaEntitiesGroup, property, objectOfProperty, word, WordResult.PROBABILITY_WORD_GIVEN_OBJECT);
                    Triple pairObject = this.countConditionalProbabilities(tableName, dbpediaEntities, property, objectOfProperty, word, WordResult.PROBABILITY_OBJECT_GIVEN_WORD);
                    if (pairWord != null && pairObject != null) {
                           Double wordCount=(Double)pairWord.getProbability_value();
                           Double objectCount=(Double)pairObject.getProbability_value();
                          
                           if ((wordCount*objectCount)>0.01&&!(wordCount==0&&objectCount==0)) {
                                result = new WordResult(pairWord, pairObject,word,partsOfSpeech);
                                results.add(result);   
                           }
                    }
                    //}
                }//all words end
                
                if (!results.isEmpty()) {
                    EntityResults kbResult = new EntityResults(property, objectOfProperty,numberOfEntitiesFoundInObject,results);
                    kbResults.add(kbResult);
                }
               

            }

            tableResults.put(tableName, kbResults);
            FileFolderUtils.writeToTextFile(kbResults, tables.getEntityTableDir(),tableName);
        }
    }

    private  Map<String, List<DBpediaEntity>> getObjectsOfproperties(String property,List<DBpediaEntity> dbpediaEntities) {
        Map<String, List<DBpediaEntity>> entityCategories = new HashMap<String, List<DBpediaEntity>>();
       
        LinkedHashSet<String> allObjects = new LinkedHashSet<String>();
        for (DBpediaEntity dbpediaEntity : dbpediaEntities) {
            if (!dbpediaEntity.getProperties().isEmpty()) {
                if(dbpediaEntity.getProperties().containsKey(property)){
                   LinkedHashSet<String> objects = new LinkedHashSet<String>(dbpediaEntity.getProperties().get(property));
                   allObjects.addAll(objects); 
                }
            }
        }

        for (DBpediaEntity DBpediaEntity : dbpediaEntities) {
            for (String key : DBpediaEntity.getProperties().keySet()) {
                if (!DBpediaEntity.getProperties().get(key).isEmpty()) {
                    String value = DBpediaEntity.getProperties().get(key).iterator().next();
                    if (allObjects.contains(value)) {
                        List<DBpediaEntity> list = new CopyOnWriteArrayList<DBpediaEntity>();
                        if (entityCategories.containsKey(value)) {
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
     return entityCategories;
    }
    

    private Triple countConditionalProbabilities(String tableName, List<DBpediaEntity> dbpediaEntities, String propertyName, String objectOfProperty, String word, Integer flag) throws IOException {
        Double KB_WORD_FOUND = 0.0, KB_FOUND = 0.0, WORD_FOUND = 0.0;
        Pair<String, Double> pair = null;
        Triple triple=null;   

        for (DBpediaEntity dbpediaEntity : dbpediaEntities) {
            String text = dbpediaEntity.getText();
            Boolean objectFlag = false, wordFlag = false;

            if (dbpediaEntity.getProperties().containsKey(propertyName)) {

                List<String> objects = dbpediaEntity.getProperties().get(propertyName);
                if (objects.contains(objectOfProperty)) {
                    KB_FOUND++;
                    objectFlag = true;
                }
            }
                      
            if (isWordContains(dbpediaEntity.getText(),word)){
                WORD_FOUND++;
                wordFlag = true;                 
            }

            if (objectFlag && wordFlag) {
                KB_WORD_FOUND++;
            }

        }

        objectOfProperty=objectOfProperty.replaceAll("http://dbpedia.org/resource/", "");
        //objectOfProperty="object[res:"+objectOfProperty+"]";
        objectOfProperty="object";
        String probability_object_word_str =  "P(" + objectOfProperty + "|" + word + ")";
        String probability_word_object_str = "P(" + word + "|" + objectOfProperty + ")";

        //if (WORD_FOUND > 10) {
        if (flag == WordResult.PROBABILITY_OBJECT_GIVEN_WORD) {
            Double probability_object_word = (KB_WORD_FOUND) / (WORD_FOUND);
            triple=new Triple(probability_object_word_str,probability_object_word,KB_WORD_FOUND,WORD_FOUND) ;
            //pair = new Pair<Triple, Double>(probability_object_word_str, probability_object_word);
            
        }
        else if (flag == WordResult.PROBABILITY_WORD_GIVEN_OBJECT) {
            Double probability_word_object = (KB_WORD_FOUND) / (KB_FOUND);
            //pair = new Pair<Triple, Double>(probability_word_object_str, probability_word_object);
            triple=new Triple(probability_word_object_str,probability_word_object,KB_WORD_FOUND,KB_FOUND) ;
        } 
        
        return triple;

    }
  
    private boolean isWordContains(String text,String B) {
       if (text.toLowerCase().toString().contains(B)) {
            return true;
        }
        return false;
    }


    private  void findInterestedWordsForEntities(Tables tables,String fileName) {
         Map<String, Integer> mostCommonWords = new HashMap<String, Integer>();
         Map<String, List<String>> tableTopwords = new HashMap<String, List<String>>();

        
        for (String tableName : tables.getEntityTables().keySet()) {
             List<DBpediaEntity> dbpediaEntities = tables.getEntityTables().get(tableName).getDbpediaEntities();     
             for(DBpediaEntity dbpediaEntity:dbpediaEntities){
                 for (String word:dbpediaEntity.getWords()){
                     word=word.toLowerCase().trim();
                     /*if(ENGLISH_STOPWORDS.contains(word)){
                         continue;
                     }*/
                      Integer count=0;
                      if(mostCommonWords.containsKey(word)){
                         count= mostCommonWords.get(word);
                         count=count+1;
                         mostCommonWords.put(word, count);
                      }else{
                         count=count+1;
                         mostCommonWords.put(word, count);
                      }     
                 }
                
             }
             String str = SortUtils.sort(mostCommonWords,100);
             System.out.println(str);
             FileFolderUtils.stringToFiles(str, tableName);
             //tableTopwords.put(tableName, topWords);
        }
        
    }
    
    private void findInterestedWordsForEntities(List<DBpediaEntity> dbpediaEntities,String fileName,Integer number) {
        Map<String, Integer> mostCommonWords = new HashMap<String, Integer>();
        for (DBpediaEntity dbpediaEntity : dbpediaEntities) {
            Set<String> adjectives=dbpediaEntity.getAdjectives();
            Set<String> list=dbpediaEntity.getNouns();
            list.addAll(adjectives);
            for (String word : list) {
                word = word.toLowerCase().trim();
                if(TextAnalyzer.ENGLISH_STOPWORDS.contains(word))
                    continue;
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
             String str = SortUtils.sort(mostCommonWords,number);
             FileFolderUtils.stringToFiles(str, fileName);

    }


   
}
