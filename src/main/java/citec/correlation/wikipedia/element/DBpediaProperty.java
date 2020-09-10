/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.wikipedia.element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author elahi
 */
public class DBpediaProperty implements PropertyNotation {
    
   /* public static final String DBP_SHORT_DESCRIPTION = "dbp:shortDescription";
    public static final String DC_DESCRIPTION = "dc:description";
    public static final String DBO_PARTY = "dbo:party";
    public static final String DBO_COUNTRY = "dbo:country";
    public static final String RDF_TYPE = "rdf:type";
    public static String DBO_ABSTRACT = "dbo:abstract";
    public static String DBO_CLASS_POLITICIAN ="dbo:Politician";*/

    private String propertyString = null;
    private String subject = null;
    private String predicate = null;
    private String object = null;
    private static Map<String, List<String>> propertyList = new TreeMap<String, List<String>>();
    public static Map<String, String> prefix_definitions = new TreeMap<String, String>();
    public static Map<String, Set<String>> propertyIncluded = new TreeMap<String, Set<String>>();


    public static Map<String, String> prefixesIncluded = new HashMap<String, String>();
    public static Set<String> prefixesExcluded = new HashSet<String>();

    static {
        prefixesIncluded.put("http://dbpedia.org/ontology/", "dbo:");
        prefixesIncluded.put("http://dbpedia.org/property/", "dbp:");
        prefixesIncluded.put("http://dbpedia.org/resource", "dbr:");
        prefixesIncluded.put("https://dublincore.org/specifications/dublin-core/dcmi-terms/#description", "dct:");
        
    }

    static {
        prefix_definitions.put("http://dbpedia.org/ontology/", "dbo");
        prefix_definitions.put("http://dbpedia.org/property/", "dbp");
        prefix_definitions.put("http://dbpedia.org/resource/", "dbr");
        prefix_definitions.put("http://yago-knowledge.org/resource/", "yago");
        prefix_definitions.put("http://www.w3.org/2004/02/skos/core#", "skos");
        prefix_definitions.put("http://xmlns.com/foaf/0.1/", "foaf");
        prefix_definitions.put("http://www.w3.org/ns/prov#", "prov");
        prefix_definitions.put("http://www.w3.org/2002/07/owl#", "owl");
    }
    
    static {
        Set<String> included = new HashSet<String>();
        included.add(DBpediaProperty.DBO_ABSTRACT);
        included.add(DBpediaProperty.DBO_PARTY);
        propertyIncluded.put(DBpediaProperty.DBO_PARTY, included);
        included = new HashSet<String>();
        included.add(DBpediaProperty.DBO_ABSTRACT);
        included.add(DBpediaProperty.DBO_COUNTRY);
        propertyIncluded.put(DBpediaProperty.DBO_COUNTRY, included);

    }

    public DBpediaProperty(String propertyString) {
        this.propertyString = propertyString;
        String[] words = propertyString.split(" ");
        this.subject = words[0];
        this.predicate = words[1];
        this.object = words[2];
    }

    public void parsePropertyString(String propertyString) {
        this.propertyString = propertyString;
        String[] words = propertyString.split(" ");
        this.subject = words[0];
        this.predicate = words[1];
        this.object = words[2];

    }

    public static void setPropertyList(Map<String, List<String>> propertyListGiven) {
        propertyList = propertyListGiven;
    }

    public String getPropertyString() {
        return propertyString;
    }

    public String getSubject() {
        return subject;
    }

    public String getPredicate() {
        return predicate;
    }

    public String getObject() {
        return object;
    }

    public List<String> getObjectList() {
        List<String> propertyValues = new ArrayList<String>();
        propertyValues.add(object);
        return propertyValues;
    }

    public static Map<String, List<String>> getPropertyList() {
        return propertyList;
    }

    @Override
    public String toString() {
        return subject + " " + predicate + " " + object;
    }

}