/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.core.sparql;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
//import org.apache.commons.io.filefilter.WildcardFileFilter;

/**
 *
 * @author elahi
 */
public class FileUrlUtils implements SparqlEndpoint{
    
        //curl --request POST 'http://localhost:8890/sparql/?' --header 'Accept-Encoding: gzip' --data 'format=json' --data-urlencode 'query=CONSTRUCT { ?s ?p ?o } WHERE { ?s ?p ?o }LIMIT 5' --output 'filename.gz'
        //important links https://gist.github.com/maulikkamdar/5999514
    
    public static void main(String[] args) throws Exception {
        
          String tbx2rdf_atc_query = "PREFIX cc:<http://creativecommons.org/ns#> "
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
            + "}Limit 5";
          
          //curl "http://edan.si.edu/saam/sparql?query=SELECT%20*%20WHERE%20%7B%3Fs%20%3Fp%20%3Fo%7D%20LIMIT%208"

         String testUrl= "http://edan.si.edu/saam/sparql?query=SELECT%20*%20WHERE%20%7B%3Fs%20%3Fp%20%3Fo%7D%20LIMIT%208";
         
         String resultString=FileUrlUtils.urlUnicodeToString(testUrl);
         System.out.println("uniCodeToString: "+resultString);
         
         String string= "SELECT * WHERE {?s ?p ?o} LIMIT 8";
         String resultUnicode=FileUrlUtils.stringToUrlUnicode(iate_query1);
         System.out.println("curl "+endpoint_atc+"?query="+resultUnicode);
         
         String command ="curl "+endpoint_atc+"?query="+resultUnicode;
         Process process = Runtime.getRuntime().exec(command);
         
         try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			StringBuilder builder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
				builder.append(System.getProperty("line.separator"));
			}
			String result = builder.toString();
                        System.out.println("result String:");
			System.out.print(result);

		} catch (IOException e) {
			System.out.print("error");
			e.printStackTrace();
		}
         
         
       
         
         
       
   
        
       // ResultSet first_results = getResult(endpoint_atc, tbx2rdf_atc_query);
        //ResultSet sec_results = getResult(dbpedia_endpoint, dbpedia_query);
        //ResultSet first_results = getResult(tbx2rdftest, tbx2rdf_iate__query);
        
        
        
        
        

    }

    /*public static List<File> writeFile(TreeMap<String, TreeMap<String, List<TermInfo>>> langSortedTerms, String path) throws IOException {
        List<File> files = new ArrayList<File>();
        for (String language : langSortedTerms.keySet()) {
            String str = "";
            TreeMap<String, List<TermInfo>> alphabetPairTerms = langSortedTerms.get(language);
            Integer pairIndex = 0;
            for (String pair : alphabetPairTerms.keySet()) {
                String fileName = path + "_" + language + "_" + pair + ".txt";
                List<TermInfo> terms = alphabetPairTerms.get(pair);
                str = "";
                pairIndex++;
                Integer termIndex = 0;
                for (TermDetailNew term : terms) {
                    String pairNote = null;

                    if (language.contains("en")) {
                        pairNote = pair;
                    } else {
                        pairNote = pairIndex.toString();
                    }

                    String line = term.getTermString() + " = " + term.getTermUrl();
                    str += line + "\n";
                }
                stringToFile_ApendIf_Exists(str, fileName);
            }

        }
        return files;
    }*/
   
    
    /*public static File[] getFiles(String fileDir, String ntriple) throws Exception {

        File dir = new File(fileDir);
        FileFilter fileFilter = new WildcardFileFilter("*" + ntriple);
        File[] files = dir.listFiles(fileFilter);
        return files;

    }*/
    
    public static String urlUnicodeToString(String url) throws Exception {
        URI uri = new URI(url);
        String urlStr = uri.getQuery();
        return urlStr;
    }

    public static String stringToUrlUnicode(String string) throws UnsupportedEncodingException {
        String encodedString = URLEncoder.encode(string, "UTF-8");
        return encodedString;
    }

}
