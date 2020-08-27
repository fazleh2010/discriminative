/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.core.wikipedia;

import static citec.correlation.core.Constants.UNICODE;
import citec.correlation.core.sparql.CurlSparqlQuery;
import citec.correlation.core.wikipedia.DBpediaEntity;
import citec.correlation.core.wikipedia.Property;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author elahi
 */
public class DbpediaClass {

    private String className = null;
    private Map<String, Set<String>> propertyEntities = new HashMap<String, Set<String>>();
    private Set<String> allEntities = new TreeSet<String>();

    public DbpediaClass(String className, String entitiesPropertyFile, String POS_TAGGER) throws IOException, Exception {
        this.className = className;
        setPropertyEntities(entitiesPropertyFile);
    }

    private void setPropertyEntities(String democraticJSON) throws FileNotFoundException, IOException, Exception {
        Map<String, List<String>> propertyList = new TreeMap<String, List<String>>();
        InputStream inputStream = new FileInputStream(democraticJSON);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        String jsonString = IOUtils.toString(inputStream, UNICODE);
        ArrayList<LinkedHashMap<String, Object>> list = mapper.readValue(jsonString, ArrayList.class);

        for (LinkedHashMap<String, Object> democraticDataUnit : list) {
            String propertyString = null;
            Set<String> entities = new TreeSet<String>();
            for (String key : democraticDataUnit.keySet()) {
                if (key.contains("number_of_variables")) {
                    //String value = (String) democraticDataUnit.get(key);
                }
                if (key.contains("triple_pattern")) {
                    propertyString = (String) democraticDataUnit.get(key);
                    //System.out.println("value:" + propertyString);
                }
                if (key.contains("entities")) {
                    LinkedHashMap<String, String> value = (LinkedHashMap<String, String>) democraticDataUnit.get(key);
                    entities = new TreeSet(value.keySet());
                    //entities = this.setDBpediaEntities(value.keySet(), POS_TAGGER);
                    //System.out.println("entities:" + entities);
                }
            }

            Property property = new Property(propertyString);
            String propertyAtt = property.getPredicate();
            String propertyValue = property.getObject();
            propertyValue = propertyValue.replace("\"", "");
            propertyEntities.put(propertyString, entities);
            allEntities.addAll(entities);

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
        Property.setPropertyList(propertyList);

    }

    /*private List<DBpediaEntity> setDBpediaEntities(Set<String> keySet, String POS_TAGGER) throws Exception {
        List<DBpediaEntity> dbpediaEntities = new ArrayList<DBpediaEntity>();
        for (String entityString : keySet) {
            String entityUrl =DBpediaEntity. getEntityUrl(entityString);
            CurlSparqlQuery curlSparqlQuery = new CurlSparqlQuery(entityUrl);
            if (curlSparqlQuery.getText() != null) {
                Analyzer textAnalyzer = new Analyzer(curlSparqlQuery.getText(), POS_TAGGER);
                DBpediaEntity dbpediaEntity = new DBpediaEntity(entityString, textAnalyzer);
                dbpediaEntities.add(dbpediaEntity);
                System.out.println(dbpediaEntity);
            }

        }
        return dbpediaEntities;
    }*/

    public String getClassName() {
        return className;
    }

    public Map<String, Set<String>> getPropertyEntities() {
        return propertyEntities;
    }

    public Set<String> getAllEntities() {
        return allEntities;
    }

}
