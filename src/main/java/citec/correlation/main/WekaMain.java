/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.main;

import citec.correlation.core.wikipedia.Property;
import citec.correlation.core.wikipedia.DBpediaEntity;
import citec.correlation.core.weka.MakeArff;
import static citec.correlation.core.Constants.UNICODE;
import citec.correlation.core.json.DemocraticDataUnit;
import citec.correlation.core.yaml.ParseYaml;
import citec.correlation.main.EvaluationMain;
import citec.correlation.utils.StringWrap;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author elahi
 */
public class WekaMain {

    private static String dbpediaDir = "src/main/resources/dbpedia/";
    private static String democratic = "democratic/";
    private static String inputJsonFile = dbpediaDir + democratic + "results-100000000-1000-concretePO.json";
    private static String outputArff = dbpediaDir + democratic + "democratic.arff";
    private static String inputWordFile = dbpediaDir + democratic + "politicians_with_democratic.yml";
    //private static Map<String, Boolean> entityWordPresence = new HashMap<String, Boolean>();
    //private static Map<String, String> entityProperty = new HashMap<String, String>();
    private Map<String, List<String>> propertyList = new TreeMap<String, List<String>>();

    public static void main(String[] args) throws IOException, Exception {
        WekaMain trainingTable = new WekaMain();
        MakeArff convertToArff = trainingTable.createArffTrainingTable(inputJsonFile, inputWordFile, outputArff);
    }

    private MakeArff createArffTrainingTable(String democraticJSON, String democraticWordFile, String democraticArff) throws FileNotFoundException, IOException {
        Map<String, Boolean> entityWordPresence = getWordPresence(democraticWordFile);
        Map<String, List<String>> propertyEntities = getPropertyEntities(democraticJSON);
        Map<String, DBpediaEntity> entityTable = getEntityTable(propertyEntities,entityWordPresence);
        //ConvertToArff convertToArff = new MakeArff(entityTable, propertyList, democraticArff);

        /*InputStream inputStream = new FileInputStream(democraticJSON);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        Map<String, List<String>> propertyEntities = new HashMap<String, List<String>>();
        String jsonString = IOUtils.toString(inputStream, UNICODE);
        ArrayList<LinkedHashMap<String, Object>> list = mapper.readValue(jsonString, ArrayList.class);
       

        for (LinkedHashMap<String, Object> democraticDataUnit : list) {
            String propertyString = null;
            List<String> entities = new ArrayList<String>();
            for (String key : democraticDataUnit.keySet()) {
                if (key.contains("number_of_variables")) {
                    String value = (String) democraticDataUnit.get(key);
                }
                if (key.contains("triple_pattern")) {
                    propertyString = (String) democraticDataUnit.get(key);
                    //System.out.println("value:"+propertyString);
                }
                if (key.contains("entities")) {
                    LinkedHashMap<String, String> value = (LinkedHashMap<String, String>) democraticDataUnit.get(key);
                    entities = new ArrayList(value.keySet());
                    //System.out.println("entities:"+entities);
                }
            }
            propertyEntities.put(propertyString, entities);
            Property property = new Property(propertyString);
            String propertyAtt = property.getPredicate();
            String propertyValue = property.getObject();

            List<String> properties = new ArrayList<String>();
            if (propertyList.containsKey(propertyAtt)) {
                properties = propertyList.get(propertyAtt);
                properties.add(propertyValue);
                propertyList.put(propertyAtt, properties);

            } else {
                properties.add(propertyValue);
                propertyList.put(propertyAtt, properties);
            }

        }*/

 /*Map<String, DBpediaEntity> entityTable = new TreeMap<String, DBpediaEntity>();

        for (String propertyString : propertyEntities.keySet()) {
            List<String> entities = propertyEntities.get(propertyString);
            Property property = new Property(propertyString);
            for (String entity : entities) {
                Boolean wordFound = false;
                if (entityWordPresence.containsKey(entity)) {
                    wordFound = entityWordPresence.get(entity);
                }
                Map<String, String> properties = new HashMap<String, String>();
                if (entityTable.containsKey(entity)) {
                    DBpediaEntity dbpediaEntity = entityTable.get(entity);
                    properties = dbpediaEntity.getProperties();
                    properties.put(property.getPredicate(), property.getObject());
                    dbpediaEntity.setProperties(properties);
                    entityTable.put(entity, dbpediaEntity);

                } else {
                    properties.put(property.getPredicate(), property.getObject());
                    DBpediaEntity dbpediaEntity = new DBpediaEntity(entity, wordFound, properties);
                    entityTable.put(entity, dbpediaEntity);
                }
            }
        }*/
        return new MakeArff(entityTable, propertyList, democraticArff);
    }

    private Map<String, Boolean> getWordPresence(String democraticWordFile) throws IOException {
        ParseYaml parseYaml = new ParseYaml();
        return parseYaml.yamlDemocratic(democraticWordFile);
    }

    private Map<String, List<String>> getPropertyEntities(String democraticJSON ) throws FileNotFoundException, IOException {
        Map<String, List<String>> propertyEntities = new HashMap<String, List<String>>();
        InputStream inputStream = new FileInputStream(democraticJSON);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        String jsonString = IOUtils.toString(inputStream, UNICODE);
        ArrayList<LinkedHashMap<String, Object>> list = mapper.readValue(jsonString, ArrayList.class);

        for (LinkedHashMap<String, Object> democraticDataUnit : list) {
            String propertyString = null;
            List<String> entities = new ArrayList<String>();
            for (String key : democraticDataUnit.keySet()) {
                if (key.contains("number_of_variables")) {
                    String value = (String) democraticDataUnit.get(key);
                }
                if (key.contains("triple_pattern")) {
                    propertyString = (String) democraticDataUnit.get(key);
                    //System.out.println("value:"+propertyString);
                }
                if (key.contains("entities")) {
                    LinkedHashMap<String, String> value = (LinkedHashMap<String, String>) democraticDataUnit.get(key);
                    entities = new ArrayList(value.keySet());
                    //System.out.println("entities:"+entities);
                }
            }
            propertyEntities.put(propertyString, entities);
            Property property = new Property(propertyString);
            String propertyAtt = property.getPredicate();
            String propertyValue = property.getObject();

            List<String> properties = new ArrayList<String>();
            if (propertyList.containsKey(propertyAtt)) {
                properties = propertyList.get(propertyAtt);
                properties.add(propertyValue);
                propertyList.put(propertyAtt, properties);

            } else {
                properties.add(propertyValue);
                propertyList.put(propertyAtt, properties);
            }

        }
        return propertyEntities;
    }

    private Map<String, DBpediaEntity> getEntityTable(Map<String, List<String>> propertyEntities,Map<String, Boolean> entityWordPresence) {
        Map<String, DBpediaEntity> entityTable = new TreeMap<String, DBpediaEntity>();
      // System.out.println("test:"+entityWordPresence.keySet());
        for (String propertyString : propertyEntities.keySet()) {
            List<String> entities = propertyEntities.get(propertyString);
            Property property = new Property(propertyString);
            for (String entity : entities) {
                Boolean wordFound = false;
                if (entityWordPresence.containsKey(entity)) {
                    wordFound = entityWordPresence.get(entity);
                }
                Map<String, String> properties = new HashMap<String, String>();
                if (entityTable.containsKey(entity)) {
                    DBpediaEntity dbpediaEntity = entityTable.get(entity);
                    properties = dbpediaEntity.getProperties();
                    properties.put(property.getPredicate(), property.getObject());
                    dbpediaEntity.setProperties(properties);
                    entityTable.put(entity, dbpediaEntity);

                } else {
                    properties.put(property.getPredicate(), property.getObject());
                    DBpediaEntity dbpediaEntity = new DBpediaEntity(entity, wordFound, properties);
                    entityTable.put(entity, dbpediaEntity);
                }
            }
        }
        return entityTable;
    }

}
