/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.core.wikipedia;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author elahi
 */
public class Property {

    private String propertyString = null;
    private String subject = null;
    private String predicate = null;
    private String object = null;
    private static Map<String, List<String>> propertyList = new TreeMap<String, List<String>>();
    public static Map<String, String> prefix_definitions = new TreeMap<String, String>();

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

    /*http://psink.de/scio/: scio
    http://purl.org/dc/elements/1.1/: dc
    http://schema.org/: schema
    http://scio/data/: sciodata
    http://wikiba.se/ontology#: wikibase
    http://www.example.org/: ex
    http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#: odp
    http://www.w3.org/1999/02/22-rdf-syntax-ns#: rdf
    http://www.w3.org/2000/01/rdf-schema#: rdfs
    http://www.w3.org/2001/XMLSchema#: xsd
    http://www.w3.org/2002/07/owl#: owl*/
    public Property(String propertyString) {
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
    
    public  List<String> getObjectList() {
        List<String> propertyValues=new ArrayList<String>();
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
