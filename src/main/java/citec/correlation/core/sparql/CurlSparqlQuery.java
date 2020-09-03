/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.core.sparql;


import citec.correlation.core.wikipedia.Property;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author elahi
 * //https://stackoverflow.com/questions/13356534/how-to-read-xml-child-node-values-by-using-a-loop-in-java
 * //https://howtodoinjava.com/xml/read-xml-dom-parser-example/
 */
public class CurlSparqlQuery {

    private static String endpoint = "https://dbpedia.org/sparql";
    //private String text = null;
    private Map<String,List<String>> properties= new TreeMap<String,List<String>> ();
    public Map<String, String> prefixesIncluded = new HashMap<String, String>();
    public Set<String> prefixesExcluded = new HashSet<String>();


    public CurlSparqlQuery(String sparqlQuery) {
        prefixesIncluded.put("http://dbpedia.org/ontology/", "dbo:");
        prefixesIncluded.put("http://dbpedia.org/property/", "dbp:");
        prefixesExcluded.add("dbo:abstract");
        String resultSparql = executeSparqlQuery(sparqlQuery);
        parseResult(resultSparql);
    }

    private String executeSparqlQuery(String query) {
        String result = null, resultUnicode = null, command = null;
        Process process = null;
        try {
            resultUnicode = FileUrlUtils.stringToUrlUnicode(query);
            command = "curl " + endpoint + "?query=" + resultUnicode;
            process = Runtime.getRuntime().exec(command);
            //System.out.print(command);
        } catch (Exception ex) {
            Logger.getLogger(CurlSparqlQuery.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("error in unicode in sparql query!" + ex.getMessage());
            ex.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }
            result = builder.toString();
            // System.out.println("result String:");
            //System.out.print(result);
            //convertToXML(result);

        } catch (IOException ex) {
            Logger.getLogger(CurlSparqlQuery.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("error in reading sparql query!" + ex.getMessage());
            ex.printStackTrace();
        }
        return result;
    }

    public void parseResult(String xmlStr) {
        Document doc = convertStringToXMLDocument(xmlStr);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            this.parseResult(builder, xmlStr);
        } catch (Exception ex) {
            Logger.getLogger(CurlSparqlQuery.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("error in parsing sparql in XML!" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private Document convertStringToXMLDocument(String xmlString) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void parseResult(DocumentBuilder builder, String xmlStr) throws SAXException, IOException, DOMException, Exception {
        Document document = builder.parse(new InputSource(new StringReader(
                xmlStr)));
        NodeList results = document.getElementsByTagName("results");

        for (int i = 0; i < results.getLength(); i++) {
            NodeList childList = results.item(i).getChildNodes();
            for (int j = 0; j < childList.getLength(); j++) {
                Node childNode = childList.item(j);
                if ("result".equals(childNode.getNodeName())) {
                    String string = childList.item(j).getTextContent().trim();
                    String[] infos = string.split("\n");
                    //List<String> wordList = Arrays.asList(infos);
                    String propertyAttibute = null, predicate = null, value = null;
                    //for (String http : wordList) {
                        if (this.istProperty(string)) {
                            propertyAttibute = infos[0];
                            propertyAttibute =isSelectedProperties(propertyAttibute);
                            if(propertyAttibute!=null){
                                value=infos[1].trim();
                                //value = value.replaceAll("\\s+","").trim();
                                //System.out.println(string);
                                List<String> propertyValue=new ArrayList<String>();
                                propertyValue.add(value);
                                properties.put(propertyAttibute, propertyValue); 
                            }
                           
                        } 
                       
                    //}
                }
            }
        }
    }

    /*private void parseResult(DocumentBuilder builder, String xmlStr) throws SAXException, IOException, DOMException, Exception {
        Document document = builder.parse(new InputSource(new StringReader(
                xmlStr)));
        NodeList results = document.getElementsByTagName("results");

        for (int i = 0; i < results.getLength(); i++) {
            NodeList childList = results.item(i).getChildNodes();
            for (int j = 0; j < childList.getLength(); j++) {
                Node childNode = childList.item(j);
                if ("result".equals(childNode.getNodeName())) {
                    String string = childList.item(j).getTextContent().trim();
                    this.text = string;
                }
            }
        }
    }*/
   

    private boolean istProperty(String string) {
        boolean flag=false;
        if (string.contains("http://dbpedia.org/ontology/")
                || string.contains("http://dbpedia.org/property/")
                || string.contains("http://dbpedia.org/resource/")) {
            flag= true;
        }
        if (string.contains("http://dbpedia.org/ontology/wiki")){
            flag= false;
        }

        return flag;
    }

    public Map<String, List<String>> getProperties() {
        return properties;
    }
    
    private static String setSparqlText(String entityUrl) {
        return "select str(?text) as ?text\n"
                + "    {\n"
                + "    " + entityUrl + "dbo:abstract  ?text \n"
                + "    FILTER (lang(?text) = 'en')\n"
                + "    }";
    }

    public static String setSparqlQueryProperty(String entityUrl) {
        return "select  ?p ?o\n"
                + "    {\n"
                + "    " + entityUrl + " ?p   ?o\n"
                + "    }";

    }

    private String isSelectedProperties(String property) {
        for (String propType : prefixesIncluded.keySet()) {
            if (property.contains(propType)) {
                String lastString = getLastString(property, '/');
                property = property.replace(property, prefixesIncluded.get(propType)) + lastString;
                return property;
                //if(!prefixesExcluded.toString().contains(property))
               /* if (!property.contains("dbo:abstract")) {
                   return property;
                 }*/
            }
        }
        return null;
    }

    public String getLastString(String subject, Character symbol) {
        int index = subject.lastIndexOf(symbol);
        if (index < 1) {
            return null;
        }
        return subject = subject.substring(index + 1);
    }

}
