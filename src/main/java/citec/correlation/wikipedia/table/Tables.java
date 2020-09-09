/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.wikipedia.table;

import citec.correlation.core.analyzer.TextAnalyzer;
import citec.correlation.wikipedia.element.DbpediaClass;
import citec.correlation.wikipedia.element.DBpediaProperty;
import citec.correlation.utils.FileFolderUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author elahi
 */
public class Tables {
    private String inputFileName=null;
    private String dbpediaDir=null;
    private Map<String, EntityTable> entityTables = new HashMap<String, EntityTable>();

    public Tables(String inputFileName,String dbpediaDir) {
        this.inputFileName=inputFileName;
        this.dbpediaDir=dbpediaDir;
    }

    public void readTable(String fileName) throws IOException, Exception {
        List<File> list = FileFolderUtils.getFiles(dbpediaDir,fileName, ".json");
        //File[] list = FileFolderUtils.getFiles(dbpediaDir, ".json");
        for (File file : list) {
            //System.out.println("file..."+file.getName());
            ObjectMapper mapper = new ObjectMapper();
            List<DBpediaEntity> dbpediaEntitys = mapper.readValue(file, new TypeReference<List<DBpediaEntity>>() {
            });
            EntityTable entityTable = new EntityTable(inputFileName,file.getName(), dbpediaEntitys);
            entityTables.put(entityTable.getTableName(), entityTable);
        }
    }

    public void writingTable(DbpediaClass dbpediaClass, Set<String> checkProperties) throws Exception {
        Map<String, LinkedHashSet<String>> propertyEntities = new TreeMap<String, LinkedHashSet<String>>();
        for (String propertyString : dbpediaClass.getPropertyEntities().keySet()) {
            DBpediaProperty property = new DBpediaProperty(propertyString);
            LinkedHashSet<String> entities = dbpediaClass.getPropertyEntities().get(propertyString);
            String predicate = property.getPredicate();
            if (checkProperties.contains(predicate)) {
                if (propertyEntities.containsKey(predicate)) {
                    LinkedHashSet<String> existingEntities = propertyEntities.get(predicate);
                    existingEntities.addAll(entities);
                    propertyEntities.put(predicate, existingEntities);
                } else {
                    propertyEntities.put(predicate, entities);
                }
                
                //EntityTable entityTable = new EntityTable(inputFileName,dbpediaDir, dbpediaClass.getClassName(), property.getPredicate(), entities, TextAnalyzer.POS_TAGGER);
                //entityTables.put(entityTable.getTableName(), entityTable);
            }
        }
        
        for(String predicate:propertyEntities.keySet()){
           LinkedHashSet<String> entities = propertyEntities.get(predicate);
           EntityTable entityTable = new EntityTable(inputFileName,dbpediaDir, dbpediaClass.getClassName(), predicate, entities, TextAnalyzer.POS_TAGGER);
           //entityTables.put(entityTable.getTableName(), entityTable);
        }
    }

    public Map<String, EntityTable> getEntityTables() {
        return entityTables;
    }

    public void display() {
        for (String tableName : entityTables.keySet()) {
            System.out.println(tableName);
            System.out.println(entityTables.get(tableName));
        }
    }

}
