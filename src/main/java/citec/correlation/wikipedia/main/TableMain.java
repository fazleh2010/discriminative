/*
 * To change this license header, choose License Headers in Project PropertyConst.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.wikipedia.main;

import citec.correlation.wikipedia.table.Calculation;
import citec.correlation.wikipedia.element.DbpediaClass;
import citec.correlation.core.analyzer.TextAnalyzer;
import citec.correlation.core.mysql.MySQLAccess;
import citec.correlation.wikipedia.element.DBpediaEntity;
import citec.correlation.core.weka.MakeArffTable;
import citec.correlation.wikipedia.element.DBpediaProperty;
import citec.correlation.wikipedia.table.EntityTable;
import citec.correlation.wikipedia.table.Tables;
import citec.correlation.core.yaml.ParseYaml;
import citec.correlation.wikipedia.utils.FileFolderUtils;
import citec.correlation.wikipedia.element.InterestedWords;
import static citec.correlation.wikipedia.element.InterestedWords.PROPRTY_WISE;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import citec.correlation.wikipedia.element.PropertyNotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author elahi
 */
public class TableMain implements PropertyNotation {

    private static String dbpediaDir = "src/main/resources/dbpedia/";
    private static String dataDir = "data/";
    private static String entityTable = "entityTable/";
    private static String input = "input/";
    private static String output = "output/";
    //private static String inputJsonFile = dataDir + input + "results-100000000-1000-concretePO.json";
    private static String inputJsonFile = dataDir + input + "results-100000000-100-concretePO.json";

    private static String inputWordFile = dbpediaDir + input + "politicians_with_democratic.yml";
    private static String allPoliticianFile = dataDir + input + "politicians.txt";
    private static String outputArff = dbpediaDir + output + "democratic.arff";
    private static Set<String> freqClasses = new HashSet<String>();
    private static String stanfordModelFile = dbpediaDir + "english-left3words-distsim.tagger";
    private static String write = "write";
    private static String read = "read";

    public static void main(String[] args) throws IOException, Exception {
        TableMain trainingTable = new TableMain();
        String type=read;
        Tables tables =null;
        
        Set<String> checkProperties = new HashSet<String>();
        //checkProperties.add(PropertyNotation.DBP_SHORT_DESCRIPTION);
        //checkProperties.add(PropertyNotation.DBO_COUNTRY);
         //checkProperties.add(PropertyNotation.DBO_PARTY);
        //checkProperties.add(PropertyNotation.dbo_party);
        
        //checkProperties.add(DBO_COUNTRY);
        //checkProperties.add(DC_DESCRIPTION);

        freqClasses.add(dbo_Politician);
        String inputFile=allPoliticianFile;
        String fileType=DbpediaClass.ALL_POLITICIANS;
        DbpediaClass dbpediaClass = new DbpediaClass(dbo_Politician, inputFile, TextAnalyzer.POS_TAGGER,fileType);
        String outputDir=dbpediaDir+output+entityTable;
      
        if (type.contains(write)) {
            if (fileType.contains(DbpediaClass.FREQUENT_TRIPLE)) {
                trainingTable.write(inputFile, outputDir, dbpediaClass, checkProperties);
            } else if (fileType.contains(DbpediaClass.ALL_POLITICIANS)) {
                //generate alphabetic files
                trainingTable.write(inputFile, outputDir, dbpediaClass, dbpediaClass.getPropertyEntities());
            }
            
            //property generation
            tables = new Tables(new File(inputFile).getName(), outputDir);
            String dir=dbpediaDir + output;
            tables.readSplitTables(dbpediaDir + output,dbo_Politician);
            tables.writeTable(dir + entityTable);
        }

        if (type.contains(read)) {
            tables = new Tables(new File(inputFile).getName(), outputDir);
            InterestedWords interestedWords=new InterestedWords(dbo_Politician, tables,dbpediaDir+output);
            String checkType=InterestedWords.PROPRTY_WISE;
            Integer numberOfEntitiesrmSelected=200;
            interestedWords.prepareWords(dbo_Politician,checkType,numberOfEntitiesrmSelected);
            Integer wordFoundInNumberOfEntities=10;
            Integer TopNwords=100;
            interestedWords.getWords(wordFoundInNumberOfEntities,TopNwords,checkType);
            
            Integer ObjectMinimumEntities=100;
            Calculation calculation = new Calculation(tables,dbo_Politician,interestedWords,numberOfEntitiesrmSelected,ObjectMinimumEntities,dbpediaDir+output);
            System.out.println("System execution ended!!!");
            
            /*String property=PropertyNotation.dbo_party;
            Calculation calculation = new Calculation(property,inputJsonFile,outputDir);*/
        }
        
        //trainingTable.moveDirectory(dbpediaDir+output+entityTable,dataDir+entityTable);

        //tables.display();
        //tables.readTable();
        //tables.display();
        /*String A="http://dbpedia.org/resource/Democratic_Party_(United_States)";
        String B="democratic";
        Calculation calculation=new Calculation(outputDir,DBO_PARTY,A,B,tables);
        System.out.println(calculation);*/
        
        //MakeArffTable makeTable = trainingTable.createArffTrainingTable(inputJsonFile, inputWordFile, outputArff);
    }

    private MakeArffTable createArffTrainingTable(String entitiesPropertyFile, String wordPresenseFile, String democraticArff) throws FileNotFoundException, IOException, Exception {
        return null;
    }

    private Map<String, Boolean> checkWordPresence(String democraticWordFile) throws IOException {
        ParseYaml parseYaml = new ParseYaml();
        return parseYaml.yamlDemocratic(democraticWordFile);
    }

    /*private Map<String, DBpediaEntity> getEntityTable(Map<String, List<String>> propertyEntities, Map<String, Boolean> entityWordPresence) {
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
    }*/

    private void createTable(Map<String, List<DBpediaEntity>> propertyEntities) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void display(List<DBpediaEntity> dbpediaEntities) {
        for (DBpediaEntity dbpediaEntity : dbpediaEntities) {
            System.out.println(dbpediaEntity);
        }
    }

    /*private Map<String,EntityTable>  writingTable(DbpediaClass dbpediaClass, Set<String> checkProperties) throws Exception {
        Map<String,EntityTable> entityTables = new HashMap<String,EntityTable>();
        for (String propertyString : dbpediaClass.getPropertyEntities().keySet()) {
            Property property = new Property(propertyString);
            Set<String> entities = dbpediaClass.getPropertyEntities().get(propertyString);
            if (checkProperties.contains(property.getPredicate())) {
                EntityTable entityTable = new EntityTable(dbpediaDir, dbpediaClass.getClassName(), property.getPredicate(), entities, TextAnalyzer.POS_TAGGER);
                entityTables.put(entityTable.getTableName(), entityTable);
            }
        }
        return entityTables;
    }

    private Map<String, EntityTable> readTable(String fileName, String json) throws IOException, Exception {
        Map<String, EntityTable> entityTables = new HashMap<String, EntityTable>();
        File[] list = FileFolderUtils.getFiles(fileName,json);
        for (File file : list) {
            ObjectMapper mapper = new ObjectMapper();
            List<DBpediaEntity> dbpediaEntitys = mapper.readValue(file, new TypeReference<List<DBpediaEntity>>() {
            });
            EntityTable entityTable = new EntityTable(file.getName(), dbpediaEntitys);
            entityTables.put(entityTable.getTableName(), entityTable);
        }
        return entityTables;
    }*/

  
    private void write(String inputJsonFile, String outputDir, DbpediaClass dbpediaClass, Map<String, LinkedHashSet<String>> propertyEntities) {
        Tables tables = new Tables(new File(inputJsonFile).getName(), outputDir);
        try {
            tables.writingTable(dbpediaClass, propertyEntities);
        } catch (Exception ex) {
            Logger.getLogger(TableMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void write(String inputJsonFile, String outputDir, DbpediaClass dbpediaClass, Set<String> checkProperties) {
        Tables tables = new Tables(new File(inputJsonFile).getName(), outputDir);
        try {
            tables.writingTable(dbpediaClass, checkProperties);
        } catch (Exception ex) {
            Logger.getLogger(TableMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void moveDirectory(String source, String destination) throws IOException {
        FileUtils.deleteDirectory(new File(destination));
        FileUtils.moveDirectory(new File(source), new File(destination));
    }

}
