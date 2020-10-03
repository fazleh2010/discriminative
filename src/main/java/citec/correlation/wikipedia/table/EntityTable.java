/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.wikipedia.table;

import citec.correlation.wikipedia.element.DBpediaEntity;
import citec.correlation.wikipedia.element.CurlSparqlQuery;
import citec.correlation.wikipedia.element.DBpediaProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author elahi
 */
public class EntityTable {

    private List<DBpediaEntity> dbpediaEntities = new ArrayList<DBpediaEntity>();
    private String tableName;
    private String inputFileName;
    
    public EntityTable(String inputFileName,String tableName,List<DBpediaEntity> dbpediaEntities) throws Exception {
        this.inputFileName=inputFileName;
        this.tableName=tableName;
        this.dbpediaEntities=dbpediaEntities;
        this.convertToJson(dbpediaEntities, tableName);
    }

    public EntityTable(String inputFileName,String dbpediaDir,String freqClass, String freqProp,LinkedHashSet<String> entities, String POS_TAGGER) throws Exception {
        this.inputFileName=inputFileName;
        this.tableName=dbpediaDir+freqClass + "_" + freqProp;
        this.setProperties(entities, POS_TAGGER,freqClass,freqProp);
        this.convertToJson(dbpediaEntities, tableName);
    }

    private void setProperties(LinkedHashSet<String> entities, String POS_TAGGER,String freqClass,String freqProperty) throws Exception {
        Integer index = 0,total=entities.size();
        
        for (String entityString : entities) {
            String entityUrl = DBpediaEntity.getEntityUrl(entityString);
            CurlSparqlQuery curlSparqlQuery = new CurlSparqlQuery(entityUrl,freqProperty);
            DBpediaEntity dbpediaEntity = new DBpediaEntity(inputFileName,freqClass,freqProperty,entityString, curlSparqlQuery.getProperties(), POS_TAGGER);
            dbpediaEntities.add(dbpediaEntity);
            //System.out.println("entityIndex:" + dbpediaEntity.getEntityIndex());
            //if (entityString.startsWith("A")||entityString.startsWith("a")) {
                if (!dbpediaEntity.getProperties().isEmpty()) {
                    String key = dbpediaEntity.getProperties().keySet().iterator().next();
                    System.out.println("entity:" + entityString+" property:" + key+" count"+index+ " total"+total);
                }

            //}
           

            index++;

            if (index >10) {
                break;
            }
        }
    }

    private void convertToJson(List<DBpediaEntity> dbpediaEntities, String filename) throws IOException {
        System.out.println("filename:"+filename);
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(Paths.get(filename+".json").toFile(), dbpediaEntities);

    }

    public void setDbpediaEntities(List<DBpediaEntity> dbpediaEntities) {
        this.dbpediaEntities = dbpediaEntities;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<DBpediaEntity> getDbpediaEntities() {
        return dbpediaEntities;
    }

    public String getTableName() {
        return tableName;
    }

    @Override
    public String toString() {
        return "{" + "tableName=" + tableName +  "," +"dbpediaEntities=" + dbpediaEntities +'}';
    }

}
