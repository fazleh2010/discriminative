/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.wikipedia.calculation;

import citec.correlation.utils.FileFolderUtils;
import citec.correlation.wikipedia.element.Result;
import citec.correlation.wikipedia.table.DBpediaEntity;
import citec.correlation.wikipedia.table.Tables;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author elahi
 */
public class Calculation {

    private List<Result> results = new ArrayList<Result>();

    public Calculation(String inputJsonFile, String outputDir, String propertyName, String object, String word) throws Exception {
        String A = object;
        String B = word;
        Tables tables = new Tables(new File(inputJsonFile).getName(), outputDir);
        tables.readTable(propertyName);
        for (String tableName : tables.getEntityTables().keySet()) {
            List<DBpediaEntity> dbpediaEntities = tables.getEntityTables().get(tableName).getDbpediaEntities();
            if (!dbpediaEntities.isEmpty()) {
                Result result=this.countConditionalProbabilities(outputDir,tableName, dbpediaEntities, propertyName, A, B);
                this.results.add(result);
                FileFolderUtils.writeToJsonFile(this.results, outputDir + File.separator + Result.RESULT_DIR+ File.separator + tableName);
            } else {
                throw new Exception("No entities in table to calculate conditional probability!");
            }
        }
    }

    private Result countConditionalProbabilities(String outputDir,String tableName, List<DBpediaEntity> dbpediaEntities, String propertyName, String A, String B) throws IOException {
        Map<String, Double> probabilities = new TreeMap<String, Double>();
        Double object_word_count = 0.0, object_count = 0.0, word_count = 0.0;
        for (DBpediaEntity dbpediaEntity : dbpediaEntities) {
            Boolean objectFlag = false, wordFlag = false;

            if (dbpediaEntity.getProperties().containsKey(propertyName)) {

                List<String> objects = dbpediaEntity.getProperties().get(propertyName);
                if (objects.contains(A)) {
                    object_count++;
                    objectFlag = true;
                    //System.out.println("A"+A);
                }
            }
            if (dbpediaEntity.getInterestingWords().contains(B)) {
                word_count++;
                wordFlag = true;
                //System.out.println("B"+B);
            }

            if (objectFlag && wordFlag) {
                object_word_count++;
            }

        }

        Double probability_object_word = (object_word_count) / (word_count);
        Double probability_word_object = (object_word_count) / (object_count);

        String probability_object_word_str = Result.conditional_probability + "(" + Result.KB_STR + "|" + Result.WORD_STR + ")";
        String probability_word_object_str =Result.conditional_probability + "(" + Result.WORD_STR + "|" + Result.KB_STR  + ")";

        probabilities.put(probability_object_word_str, probability_object_word);
        probabilities.put(probability_word_object_str, probability_word_object);

        return new Result(outputDir,tableName, A, B, probabilities);
        
    }
}
