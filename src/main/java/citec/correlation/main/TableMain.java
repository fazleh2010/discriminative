/*
 * To change this license header, choose License Headers in Project PropertyConst.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.main;

import citec.correlation.core.wikipedia.PropertyConst;
import citec.correlation.core.wikipedia.DbpediaClass;
import citec.correlation.core.wikipedia.EntityTable;
import citec.correlation.core.analyzer.TextAnalyzer;
import citec.correlation.core.mysql.MySQLAccess;
import citec.correlation.core.wikipedia.Property;
import citec.correlation.core.wikipedia.DBpediaEntity;
import citec.correlation.core.weka.MakeArffTable;
import citec.correlation.core.yaml.ParseYaml;
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
public class TableMain implements PropertyConst {

    private static String dbpediaDir = "src/main/resources/dbpedia/";
    private static String input = "democratic/input/";
    private static String output = "democratic/output/";
    private static String inputJsonFile = dbpediaDir + input + "results-100000000-1000-concretePO.json";
    private static String inputWordFile = dbpediaDir + input + "politicians_with_democratic.yml";
    private static String outputArff = dbpediaDir + output + "democratic.arff";
    private static Set<String> freqClasses = new HashSet<String>();
    private static String stanfordModelFile = dbpediaDir + "english-left3words-distsim.tagger";

    public static void main(String[] args) throws IOException, Exception {
        TableMain trainingTable = new TableMain();
        MakeArffTable makeTable = trainingTable.createArffTrainingTable(inputJsonFile, inputWordFile, outputArff);
    }

    private void writeInTable(Set<EntityTable> entityTables) throws Exception {
        //MySQLAccess mySQLAccess=new MySQLAccess();
    }

    private MakeArffTable createArffTrainingTable(String entitiesPropertyFile, String wordPresenseFile, String democraticArff) throws FileNotFoundException, IOException, Exception {
        freqClasses.add("dbo:Politician");
        DbpediaClass dbpediaClass = new DbpediaClass("dbo:Politician", entitiesPropertyFile, TextAnalyzer.POS_TAGGER);
        Set<EntityTable> entityTables = new TreeSet<EntityTable>();
        for (String propertyString : dbpediaClass.getPropertyEntities().keySet()) {
            Property property = new Property(propertyString);
            Set<String> entities = dbpediaClass.getPropertyEntities().get(propertyString);
            String checkProperty = DBO_PARTY;
            if (property.getPredicate().contains(checkProperty)) {
                //System.out.println("..."+property.getPredicate());
                EntityTable entityTable = new EntityTable(dbpediaDir, dbpediaClass.getClassName(), checkProperty, entities, TextAnalyzer.POS_TAGGER);
                //entityTables.add(entityTable);

            }

        }
        
        //http://dbpedia.org/page/Akshay_Kumar
        
        //MySQLAccess mySQLAccess=new MySQLAccess();
        return null;
    }

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
                Map<String, List<String>> properties = new HashMap<String, List<String>>();
                if (entityTable.containsKey(entity)) {
                    DBpediaEntity dbpediaEntity = entityTable.get(entity);
                    properties = dbpediaEntity.getProperties();
                    //old codes.........
                    //properties.put(property.getPredicate(), property.getObject());
                    properties.put(property.getPredicate(), property.getObjectList());
                    dbpediaEntity.setProperties(properties);
                    entityTable.put(entity, dbpediaEntity);

                } else {
                    // old codes.............
                    //properties.put(property.getPredicate(), property.getObject());
                    properties.put(property.getPredicate(), property.getObjectList());
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
        for (DBpediaEntity dbpediaEntity : dbpediaEntities) {
            System.out.println(dbpediaEntity);
        }
    }

}
