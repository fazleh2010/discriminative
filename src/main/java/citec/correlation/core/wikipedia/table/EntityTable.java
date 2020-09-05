/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.core.wikipedia.table;

import citec.correlation.core.sparql.CurlSparqlQuery;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author elahi
 */
public class EntityTable {

    private List<DBpediaEntity> dbpediaEntities = new ArrayList<DBpediaEntity>();
    private String tableName;
    
    public EntityTable(String tableName,List<DBpediaEntity> dbpediaEntities) throws Exception {
        this.tableName=tableName;
        this.dbpediaEntities=dbpediaEntities;
    }

    public EntityTable(String dbpediaDir,String freqClass, String freqProp,Set<String> keySet, String POS_TAGGER) throws Exception {
        this.tableName=dbpediaDir+freqClass + "_" + freqProp;
        this.setProperties(keySet, POS_TAGGER,freqClass);
        this.convertToJson(dbpediaEntities, tableName);
    }

    private void setProperties(Set<String> keySet, String POS_TAGGER,String freqClass) throws Exception {
        Integer index = 0;
        for (String entityString : keySet) {
            String entityUrl = DBpediaEntity.getEntityUrl(entityString);
            String sparqlQuery = CurlSparqlQuery.setSparqlQueryProperty(entityUrl);
            CurlSparqlQuery curlSparqlQuery = new CurlSparqlQuery(sparqlQuery);
            DBpediaEntity dbpediaEntity = new DBpediaEntity(freqClass,entityString, curlSparqlQuery.getProperties(), POS_TAGGER);
            dbpediaEntities.add(dbpediaEntity);
            System.out.println(dbpediaEntity.getEntityUrl());

            index++;

           /*if (index == 10) {
                break;
            }*/
        }
    }

    private void convertToJson(List<DBpediaEntity> dbpediaEntities, String filename) throws IOException {
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
