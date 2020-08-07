/*
 * 
 */
package citec.correlation.core.sparql;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Mohammad Fazleh Elahi
 *
 * This is a interface contains constants of file type
 *
 */
public interface SparqlEndpoint {
    
    //http://scdemo.techfak.uni-bielefeld.de/termeallod/
    //https://github.com/kohlschutter/junixsocket/blob/master/junixsocket-mysql/src/main/java/org/newsclub/net/mysql/AFUNIXDatabaseSocketFactory.java
    //https://github.com/kohlschutter/junixsocket/blob/master/junixsocket-mysql/src/main/java/org/newsclub/net/mysql/AFUNIXDatabaseSocketFactoryCJ.java
    //jdbc:mariadb://localhost:3306/revmgt?localSocket=/var/run/mysqld/mysqld.sock
    
    public static final String tbx2rdf_iate_endpoint = "https://webtentacle1.techfak.uni-bielefeld.de/tbx2rdf_iate/sparql/";
    public static String tbx2rdf_iate__query = "select distinct ?Concept where {[] a ?Concept} LIMIT 100";

    public static final String endpoint_atc = "https://webtentacle1.techfak.uni-bielefeld.de/tbx2rdf_atc/sparql/";
    public static final String endpoint_intaglio ="https://webtentacle1.techfak.uni-bielefeld.de/tbx2rdf_intaglio/sparql/";
    public static final String endpoint_solar ="https://webtentacle1.techfak.uni-bielefeld.de/tbx2rdf_solarenergy/sparql/";
    public static final String endpoint_wastemanagement ="https://webtentacle1.techfak.uni-bielefeld.de/tbx2rdf_wastemanagement/sparql/";
    
    
    public static String tbx2rdf_atc_query = "PREFIX cc:    <http://creativecommons.org/ns#> \n"
            + "PREFIX void:  <http://rdfs.org/ns/void#> \n"
            + "PREFIX skos:  <http://www.w3.org/2004/02/skos/core#> \n"
            + "PREFIX rdfs:  <http://www.w3.org/2000/01/rdf-schema#> \n"
            + "PREFIX tbx:   <http://tbx2rdf.lider-project.eu/tbx#> \n"
            + "PREFIX decomp: <http://www.w3.org/ns/lemon/decomp#> \n"
            + "PREFIX dct:   <http://purl.org/dc/terms/> \n"
            + "PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
            + "PREFIX ontolex: <http://www.w3.org/ns/lemon/ontolex#> \n"
            + "PREFIX ldr:   <http://purl.oclc.org/NET/ldr/ns#> \n"
            + "PREFIX odrl:  <http://www.w3.org/ns/odrl/2/> \n"
            + "PREFIX dcat:  <http://www.w3.org/ns/dcat#> \n"
            + "PREFIX prov:  <http://www.w3.org/ns/prov#> \n"
            + "\n"
            + "SELECT ?s ?p ?o from <http://tbx2rdf.lider-project.eu/> WHERE { \n"
            + "    ?s ?p ?o .\n"
            + "    ?o ontolex:canonicalForm ?canform .\n"
            + "    ?canform ontolex:writtenRep ?rep .\n"
            + "    FILTER regex(str(?rep), \"ace\") .\n"
            + "} LIMIT 5\n"
            + "        ";

    public static final String dbpedia_endpoint = "http://dbpedia.org/sparql";
    public static String dbpedia_query = "select distinct ?Concept where {[] a ?Concept} LIMIT 100";
    public static String tbx2rdftest = "https://webtentacle1.techfak.uni-bielefeld.de/tbx2rdftest/sparql";

    /*public static String query_writtenRep = "PREFIX cc:    <http://creativecommons.org/ns#> \n"
            + "\n"
            + "PREFIX void:  <http://rdfs.org/ns/void#> \n"
            + "\n"
            + "PREFIX skos:  <http://www.w3.org/2004/02/skos/core#> \n"
            + "\n"
            + "PREFIX rdfs:  <http://www.w3.org/2000/01/rdf-schema#> \n"
            + "\n"
            + "PREFIX tbx:   <http://tbx2rdf.lider-project.eu/tbx#> \n"
            + "\n"
            + "PREFIX decomp: <http://www.w3.org/ns/lemon/decomp#> \n"
            + "\n"
            + "PREFIX dct:   <http://purl.org/dc/terms/> \n"
            + "\n"
            + "PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
            + "\n"
            + "PREFIX ontolex: <http://www.w3.org/ns/lemon/ontolex#> \n"
            + "\n"
            + "PREFIX ldr:   <http://purl.oclc.org/NET/ldr/ns#> \n"
            + "\n"
            + "PREFIX odrl:  <http://www.w3.org/ns/odrl/2/> \n"
            + "\n"
            + "PREFIX dcat:  <http://www.w3.org/ns/dcat#> \n"
            + "\n"
            + "PREFIX prov:  <http://www.w3.org/ns/prov#> \n"
            + "\n"
            + "SELECT ?s ?p ?o from <http://tbx2rdf.lider-project.eu/> WHERE { \n"
            + "\n"
            + "    ?s ?p ?o .\n"
            + "\n"
            + "    ?o ontolex:canonicalForm ?canform .\n"
            + "\n"
            + "    ?canform ontolex:writtenRep ?rep .\n"
            + "\n"
            + "    FILTER regex(str(?rep), \"ace\") .\n"
            + "\n"
            + "} LIMIT 5";*/
    public static String ontoLexPrefix="PREFIX cc:    <http://creativecommons.org/ns#> \n"
            + "\n"
            + "PREFIX void:  <http://rdfs.org/ns/void#> \n"
            + "\n"
            + "PREFIX skos:  <http://www.w3.org/2004/02/skos/core#> \n"
            + "\n"
            + "PREFIX rdfs:  <http://www.w3.org/2000/01/rdf-schema#> \n"
            + "\n"
            + "PREFIX tbx:   <http://tbx2rdf.lider-project.eu/tbx#> \n"
            + "\n"
            + "PREFIX decomp: <http://www.w3.org/ns/lemon/decomp#> \n"
            + "\n"
            + "PREFIX dct:   <http://purl.org/dc/terms/> \n"
            + "\n"
            + "PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
            + "\n"
            + "PREFIX ontolex: <http://www.w3.org/ns/lemon/ontolex#> \n"
            + "\n"
            + "PREFIX ldr:   <http://purl.oclc.org/NET/ldr/ns#> \n"
            + "\n"
            + "PREFIX odrl:  <http://www.w3.org/ns/odrl/2/> \n"
            + "\n"
            + "PREFIX dcat:  <http://www.w3.org/ns/dcat#> \n"
            + "\n"
            + "PREFIX prov:  <http://www.w3.org/ns/prov#> \n";
    
     public static String iate_query1 = ontoLexPrefix
            + "\n"
            + "SELECT ?s ?p ?o from <http://tbx2rdf.lider-project.eu/> WHERE { \n"
            + "\n"
            + "    ?s ?p ?o .\n"
            + "\n"
            + "    ?o ontolex:canonicalForm ?canform .\n"
            + "\n"
            + "    ?canform ontolex:writtenRep ?rep .\n"
            + "\n"
            + "    FILTER regex(str(?rep), \"ace\") .\n"
            + "\n"
            + "} LIMIT 5";
     
        public static String query_writtenRep = ontoLexPrefix
            + "\n"
            + "select  ?s ?o  where { ?s ontolex:writtenRep ?o}";
     
        public static String iate_query = ontoLexPrefix
            + "\n"
            + "select  ?s ?p ?o  where {  ?s ?p ?o . ?s ontolex:writtenRep ?o}";
       
       //https://lemon-model.net/sparql.php
        
          /* String tbx2rdf_atc_query = "PREFIX cc:<http://creativecommons.org/ns#> "
                + "PREFIX void: <http://rdfs.org/ns/void#> "
                + "PREFIX skos: <http://www.w3.org/2004/02/skos/core#> "
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
                + "PREFIX tbx: <http://tbx2rdf.lider-project.eu/tbx#> "
                + "PREFIX decomp: <http://www.w3.org/ns/lemon/decomp#> "
                + "PREFIX dct: <http://purl.org/dc/terms/> "
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                + "PREFIX ontolex: <http://www.w3.org/ns/lemon/ontolex#> "
                + "PREFIX ldr: <http://purl.oclc.org/NET/ldr/ns#> "
                + "PREFIX odrl: <http://www.w3.org/ns/odrl/2/> "
                + "PREFIX dcat: <http://www.w3.org/ns/dcat#> "
                + "PREFIX prov: <http://www.w3.org/ns/prov#> "
                + "SELECT ?s ?p ?o from <http://tbx2rdf.lider-project.eu/> WHERE { "
                + "?s ?p ?o ."
                + "?o ontolex:canonicalForm ?canform ."
                + "?canform ontolex:writtenRep ?rep ."
                + "}Limit 5";*/
        public static String ontolex_prefix = "PREFIX ontolex: <http://www.w3.org/ns/lemon/ontolex#>";
        public static String ontolex_owl_sameAs = "ontolex:owl:sameAs";
}
