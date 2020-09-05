/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.core.wikipedia.table;

import citec.correlation.core.analyzer.TextAnalyzer;
import citec.correlation.core.wikipedia.DbpediaClass;
import citec.correlation.core.wikipedia.Property;
import citec.correlation.utils.FileFolderUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public void readTable() throws IOException, Exception {
        File[] list = FileFolderUtils.getFiles(dbpediaDir, ".json");
        for (File file : list) {
            ObjectMapper mapper = new ObjectMapper();
            List<DBpediaEntity> dbpediaEntitys = mapper.readValue(file, new TypeReference<List<DBpediaEntity>>() {
            });
            EntityTable entityTable = new EntityTable(inputFileName,file.getName(), dbpediaEntitys);
            entityTables.put(entityTable.getTableName(), entityTable);
        }
    }

    public void writingTable(DbpediaClass dbpediaClass, Set<String> checkProperties) throws Exception {
        for (String propertyString : dbpediaClass.getPropertyEntities().keySet()) {
            Property property = new Property(propertyString);
            Set<String> entities = dbpediaClass.getPropertyEntities().get(propertyString);
            if (checkProperties.contains(property.getPredicate())) {
                EntityTable entityTable = new EntityTable(inputFileName,dbpediaDir, dbpediaClass.getClassName(), property.getPredicate(), entities, TextAnalyzer.POS_TAGGER);
                entityTables.put(entityTable.getTableName(), entityTable);
            }
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
