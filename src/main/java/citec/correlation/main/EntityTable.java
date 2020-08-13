/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.main;

import citec.correlation.core.sparql.CurlSparqlQuery;
import citec.correlation.core.wikipedia.DBpediaEntity;
import citec.correlation.core.wikipedia.Property;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author elahi
 */
public class EntityTable {

    private List<DBpediaEntity> dbpediaEntities = new ArrayList<DBpediaEntity>();
    private String tableName=null;
    public EntityTable(String tableName,Set<String> keySet, String POS_TAGGER) throws Exception {
        this.tableName=tableName;
        this.setProperties(keySet, POS_TAGGER);
    }

    private void setProperties(Set<String> keySet, String POS_TAGGER) throws Exception {
        Integer index=0;
        for (String entityString : keySet) {
            String entityUrl = DBpediaEntity.getEntityUrl(entityString);
            String sparqlQuery = CurlSparqlQuery.setSparqlQueryProperty(entityUrl);
            CurlSparqlQuery curlSparqlQuery = new CurlSparqlQuery(sparqlQuery);
            DBpediaEntity dbpediaEntity = new DBpediaEntity(entityString, curlSparqlQuery.getProperties(), POS_TAGGER);
            dbpediaEntities.add(dbpediaEntity);
            System.out.println(dbpediaEntity);
            //entittyTable.put(entityString, dbpediaEntity);
            index++;
            //if(index==10)
             //   break;
        }
    }

    /*private void setAnalysis(Set<String> keySet, String POS_TAGGER) throws Exception {
        List<DBpediaEntity> dbpediaEntities = new ArrayList<DBpediaEntity>();
        for (String entityString : keySet) {
            String entityUrl = DBpediaEntity.getEntityUrl(entityString);
            String sparqlQuery = this.setSparqlText(entityUrl);
            CurlSparqlQuery curlSparqlQuery = new CurlSparqlQuery(sparqlQuery);
            if (curlSparqlQuery.getText() != null) {
                Analyzer textAnalyzer = new Analyzer(curlSparqlQuery.getText(), POS_TAGGER);
                DBpediaEntity dbpediaEntity = new DBpediaEntity(entityString, textAnalyzer);
                dbpediaEntities.add(dbpediaEntity);
                entittyTable.put(entityString, textAnalyzer);
                System.out.println(dbpediaEntity);
            }
            break;
        }
    }
   

    private String setSparqlText(String entityUrl) {
        return "select str(?text) as ?text\n"
                + "    {\n"
                + "    " + entityUrl + "dbo:abstract  ?text \n"
                + "    FILTER (lang(?text) = 'en')\n"
                + "    }";
    }

    private String setSparqlQueryProperty(String entityUrl) {
        return "select  ?p ?o\n"
                + "    {\n"
                + "    " + entityUrl + " ?p   ?o\n"
                + "    }";

    }*/

    public List<DBpediaEntity> getDbpediaEntities() {
        return dbpediaEntities;
    }

    public String getTableName() {
        return tableName;
    }   

}
