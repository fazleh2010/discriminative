/*
 * To change this license header, choose License Headers in Project PropertyConst.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.wikipedia.main;

import citec.correlation.core.analyzer.Analyzer;
import citec.correlation.wikipedia.table.Calculation;
import citec.correlation.wikipedia.element.DbpediaClass;
import citec.correlation.core.analyzer.TextAnalyzer;
import citec.correlation.wikipedia.element.DBpediaEntity;
import citec.correlation.core.weka.MakeArffTable;
import citec.correlation.wikipedia.table.Tables;
import citec.correlation.core.yaml.ParseYaml;
import citec.correlation.wikipedia.element.InterestedWords;
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
import citec.correlation.wikipedia.element.Triple;
import citec.correlation.wikipedia.evalution.Comparision;
import citec.correlation.wikipedia.evalution.LexiconUnit;
import citec.correlation.wikipedia.evalution.Qald;
import citec.correlation.wikipedia.qald.Unit;
import citec.correlation.wikipedia.table.WordResult;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author elahi
 */
public class TableMain implements PropertyNotation {
   //Horrible codes..just a play ground for all kinds of work
    private static String qald9Dir = "src/main/resources/qald9/data/";
    private static String testJson = "qald-9-test-multilingual.json";
    private static String trainingJson = "qald-9-train-multilingual.json";
    private static String dbpediaDir = "src/main/resources/dbpedia/";
    private static String dataDir = "data/";
    private static String entityTable = "entityTable/";
    private static String input = "input/";
    private static String output = "output/";
    //private static String inputJsonFile = dataDir + input + "results-100000000-1000-concretePO.json";
    private static String inputJsonFile = dataDir + input + "results-100000000-100-concretePO.json";

    //rivate static String inputWordFile = dbpediaDir + input + "politicians_with_democratic.yml";
    private static String allPoliticianFile = dbpediaDir + input + "persons.txt";
    //private static String outputArff = dbpediaDir + output + "democratic.arff";
    private static Set<String> freqClasses = new HashSet<String>();
    //private static String stanfordModelFile = dbpediaDir + "english-left3words-distsim.tagger";
    private static String write = "write";
    private static String proprtyGeneration = "proprtyGeneration";
    private static String interestingWord = "interestingWord";
    private static String calculation = "calculation";
    private static String meanReciprocal = "meanReciprocal";
    private static String qald = "qald";


    public static void main(String[] args) throws IOException, Exception {
        Set<String> posTags = new HashSet<String>();
        posTags.add(TextAnalyzer.NOUN);
        posTags.add(TextAnalyzer.ADJECTIVE);
        posTags.add(Analyzer.VERB);

        //QALDMain qaldMain=new QALDMain (posTags,qald9Dir,trainingJson);
        TableMain trainingTable = new TableMain();
        String type = meanReciprocal;
        Tables tables = null;
        String dbo_ClassName = PropertyNotation.dbo_Person;
            freqClasses.add(dbo_ClassName);
            String inputFile = allPoliticianFile;
            String fileType = DbpediaClass.ALL;
            String classDir = getClassDir(dbo_ClassName) + "/";
            String rawFiles = dbpediaDir + classDir + "rawFiles/";

        


        /*Integer numberOfEntitiesrmSelected=100;
            Integer wordFoundInNumberOfEntities=10;
            Integer TopNwords=100;
            Integer ObjectMinimumEntities=50;*/
        //parameter for actor
        Integer numberOfEntitiesrmSelected = 100;
        Integer wordFoundInNumberOfEntities = 10;
        Integer TopNwords = 100;
        Integer ObjectMinimumEntities = 60;

        Double wordGivenObjectThres = 0.2;
        Double objectGivenWordThres = 0.2;
        Integer topWordLimitToConsiderThres = 2;

        InterestedWords interestedWords = null;

        if (type.contains(write)) {
            
            DbpediaClass dbpediaClass = new DbpediaClass(dbo_ClassName, inputFile, TextAnalyzer.POS_TAGGER, fileType);
            makeClassDir(dbpediaDir + classDir);

            /*if (fileType.contains(DbpediaClass.FREQUENT_TRIPLE)) {
                trainingTable.write(inputFile, rawFiles, dbpediaClass, checkProperties);
            } else*/
            if (fileType.contains(DbpediaClass.ALL)) {
                trainingTable.write(inputFile, rawFiles, dbpediaClass, dbpediaClass.getPropertyEntities());
            }
        }
        if (type.contains(proprtyGeneration)) {
            //property generation
            tables = new Tables(new File(inputFile).getName(), rawFiles);
            tables.readSplitTables(rawFiles, dbo_ClassName);
            tables.writeTable(dbpediaDir + classDir + "tables/");
        }
        if (type.contains(calculation)) {
            String checkType = InterestedWords.PROPRTY_WISE;
            tables = new Tables(new File(inputFile).getName(), dbpediaDir + classDir + "tables/");
            interestedWords = new InterestedWords(dbo_ClassName, tables, dbpediaDir + classDir + "tables/");
            interestedWords.prepareWords(dbo_ClassName, checkType, numberOfEntitiesrmSelected);
            interestedWords.getWords(wordFoundInNumberOfEntities, TopNwords, checkType);
            tables = new Tables(new File(inputFile).getName(), dbpediaDir + classDir + "tables/");
            Calculation calculation = new Calculation(tables, dbo_ClassName, interestedWords,
                    numberOfEntitiesrmSelected, ObjectMinimumEntities,
                    dbpediaDir + output, qald9Dir, posTags,
                    wordGivenObjectThres, objectGivenWordThres, topWordLimitToConsiderThres);
            System.out.println("System execution ended!!!");

        }
        
        if (type.contains(qald)) {
            Qald qaldMain=new Qald (posTags,qald9Dir,trainingJson);
        }
        if (type.contains(meanReciprocal)) {
            String qaldFileName = qald9Dir + "JJ-qald9" + ".json";
            String conditionalFilename = qald9Dir + "lexicon-conditional-JJ" + ".json";
            Comparision comparision=new Comparision(qaldFileName,conditionalFilename);
        }

        //MakeArffTable makeTable = trainingTable.createArffTrainingTable(inputJsonFile, inputWordFile, outputArff);
    }

    private static String getClassDir(String dbo_Politician) {
        return dbo_Politician.split(":")[1];
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

    public static Boolean makeClassDir(String director) {
        try {
            Path path = Paths.get(director);
            Files.createDirectories(path);
            path = Paths.get(director + "rawFiles/");
            Files.createDirectories(path);
            path = Paths.get(director + "tables/" + "result/");
            Files.createDirectories(path);
            path = Paths.get(director + "tables/" + "selectedWords/");
            Files.createDirectories(path);
            return true;
        } catch (IOException e) {
            System.err.println("Failed to create directory!" + e.getMessage());
            return false;

        }
    }

}
