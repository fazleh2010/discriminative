/*
 * To change this license header, choose License Headers in Project PropertyConst.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.main;

import citec.correlation.core.mysql.MySQLAccess;
import citec.correlation.core.wikipedia.Property;
import citec.correlation.core.wikipedia.DBpediaEntity;
import citec.correlation.core.weka.MakeArffTable;
import citec.correlation.core.yaml.ParseYaml;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
/**
 *
 * @author elahi
 */
public class WekaMain implements PropertyConst {

    private static String dbpediaDir = "src/main/resources/dbpedia/";
    private static String input = "democratic/input/";
    private static String output = "democratic/output/";
    private static String inputJsonFile = dbpediaDir + input + "results-100000000-1000-concretePO.json";
    private static String inputWordFile = dbpediaDir + input + "politicians_with_democratic.yml";
    private static String outputArff = dbpediaDir + output + "democratic.arff";
    private static Set<String> freqClasses = new HashSet<String>();
    private static String stanfordModelFile = dbpediaDir + "english-left3words-distsim.tagger";
    
    private void writeInTable(Set<EntityTable> entityTables) throws Exception {
        MySQLAccess mySQLAccess=new MySQLAccess();
    }

    public static void main(String[] args) throws IOException, Exception {
        WekaMain trainingTable = new WekaMain();
        MakeArffTable makeTable = trainingTable.createArffTrainingTable(inputJsonFile, inputWordFile, outputArff);
    }

    private MakeArffTable createArffTrainingTable(String entitiesPropertyFile, String wordPresenseFile, String democraticArff) throws FileNotFoundException, IOException, Exception {
        
        
        freqClasses.add("dbo:Politician");
        DbpediaClass dbpediaClass = new DbpediaClass("dbo:Politician", entitiesPropertyFile, TextAnalyzer.POS_TAGGER);
        Set<EntityTable> entityTables = new TreeSet<EntityTable>();
        for (String propertyString : dbpediaClass.getPropertyEntities().keySet()) {
            Property property = new Property(propertyString);
            Set<String> entities = dbpediaClass.getPropertyEntities().get(propertyString);
            if (property.getPredicate().contains(DBO_PARTY)) {
                EntityTable entityTable = new EntityTable(dbpediaClass.getClassName() + "_" + DBO_PARTY, entities, TextAnalyzer.POS_TAGGER);
                entityTables.add(entityTable);
            }
        }
         
        //Set<EntityTable> entityTables=new HashSet<EntityTable>();
        //this.writeInTable(entityTables);

       

        
        // Map<String, Boolean> entityWordPresence = checkWordPresence(wordPresenseFile);
        
       
        
          /*for (String propertyString : DbpediaClass.getPropertyEntities().keySet()) {
               String tableName=DbpediaClass.getClassName()+"_"+new Property(propertyString).getPredicate();
               this.createTable(DbpediaClass.getPropertyEntities());
          }*/
        
        /*Map<String, Boolean> entityWordPresence = checkWordPresence(wordPresenseFile);
        for (String propertyString : DbpediaClass.getPropertyEntities().keySet()) {
            Property property = new Property(propertyString);
            if (property.getPredicate().contains(DBO_PARTY)) {
                List<String> entities = DbpediaClass.getPropertyEntities().get(propertyString);
                Map<String, List<String>> propertyEntities = new HashMap<String, List<String>>();
                propertyEntities.put(propertyString, entities);
                Map<String, DBpediaEntity> entityTable = getEntityTable(propertyEntities, entityWordPresence);
                for (String key : entityTable.keySet()) {
                    DBpediaEntity dbpediaEntity = entityTable.get(key);
                    CurlSparqlQuery curlSparqlQuery = new CurlSparqlQuery(dbpediaEntity.getEntityUrl());
                    if (curlSparqlQuery.getText() != null) {
                        Analyzer analyzer = new Analyzer(curlSparqlQuery.getText(), TextAnalyzer.POS_TAGGER);
                        System.out.println(analyzer);
                    }

                }
            }
        }*/

        /*for(String entity:entityTable.keySet()){
            DBpediaEntity dbpediaEntity=entityTable.get(entity);
            System.out.println("DBpedia entity:"+dbpediaEntity);
        }*/
        //return new MakeArffTable(entityTable, propertyList, democraticArff);
        return null;
    }
    
     /*private void setProperties(Set<String> keySet, String POS_TAGGER) throws Exception {
        List<DBpediaEntity> dbpediaEntities = new ArrayList<DBpediaEntity>();
        for (String entityString : keySet) {
            String entityUrl = DBpediaEntity.getEntityUrl(entityString);
            String sparqlQuery = CurlSparqlQuery.setSparqlQueryProperty(entityUrl);
            CurlSparqlQuery curlSparqlQuery = new CurlSparqlQuery(sparqlQuery);
            DBpediaEntity dbpediaEntity = new DBpediaEntity(entityString, curlSparqlQuery.getProperties(), POS_TAGGER);
            dbpediaEntities.add(dbpediaEntity);
            break;
        }
    }*/
    
    private Map<String, Boolean> checkWordPresence(String democraticWordFile) throws IOException {
        ParseYaml parseYaml = new ParseYaml();
        return parseYaml.yamlDemocratic(democraticWordFile);
    }

    private Map<String, DBpediaEntity> getEntityTable(Map<String, List<String>> propertyEntities, Map<String, Boolean> entityWordPresence) {
        Map<String, DBpediaEntity> entityTable = new TreeMap<String, DBpediaEntity>();
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

    private void createTable(Map<String, List<DBpediaEntity>> propertyEntities) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void display(List<DBpediaEntity> dbpediaEntities) {
        for(DBpediaEntity dbpediaEntity:dbpediaEntities){
            System.out.println(dbpediaEntity);
        }
    }

    

}
