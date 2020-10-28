
import citec.correlation.wikipedia.element.CurlSparqlQuery;
import citec.correlation.wikipedia.utils.FileFolderUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import opennlp.tools.tokenize.WhitespaceTokenizer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author elahi
 */
public class Test implements Constants{

    private static String nameEntityDir = "src/main/resources/nameEntiry/";

  

    public static void main(String args[]) throws Exception {
        sentenceContext();
       
    }

    private static void sentenceContext() throws Exception {
        WhitespaceTokenizer tokenizer = WhitespaceTokenizer.INSTANCE;
        Map<Integer,NamedEntity> sentenceNameEntities=new TreeMap<Integer,NamedEntity>();
        String sentenceLine1 = "Donald John Trump (born June 14, 1946) is an American businessman, author, television producer, politician, and the Republican Party nominee for President of the United States in the 2016 election";
        String sentenceLine2 = "He is the chairman and president of The Trump Organization, which is the principal holding company for his real estate ventures and other business interests.";
        String sentenceLine3 = "During his career, Trump has built office towers, hotels, casinos, golf courses, an urban development project in Manhattan, and other branded facilities worldwide.";
        String sentenceLine4 ="Donald John Trump attended Fordham University";
        String university="Fordham University";
        System.out.println(sentenceLine4);
        sentenceLine4=sentenceLine4.replace(" ", "%20");
        System.out.println(sentenceLine4);

        //String command="curl -X GET https://api.dbpedia-spotlight.org/en/annotate?"+"text="+"Donald%20John%20Trump%20attended%20Fordham%20University"+" -H accept: application/json";
        String command="curl -X GET https://api.dbpedia-spotlight.org/en/annotate?"+"text="+sentenceLine4+" -H accept: application/json";
        
        
        CurlSparqlQuery CurlSparqlQuery=new CurlSparqlQuery(command);
        //command="curl -X POST https://postman-echo.com/post --data foo1=bar1&foo2=bar2";
        
        //String result=executeSparqlQuery(command);
          //      System.out.println(result);

        
       /* List<String> sentenceLines = new ArrayList<String>();
        sentenceLines.add(sentenceLine1);
        sentenceLines.add(sentenceLine2);
        sentenceLines.add(sentenceLine3);
        sentenceLines.add(sentenceLine4);
        Integer windowSize=5;
        
        Integer index=0;
        for (String sentenceLine : sentenceLines) {
            index=index+1;
            Map<String,String> objectValuePairs=new  TreeMap<String,String>();
            sentenceLine=sentenceLine.replace(university, OBJECT+"_"+index.toString());
            objectValuePairs.put(OBJECT+"_"+index.toString(), university);
            String tokens[] = tokenizer.tokenize(sentenceLine);
            NamedEntity namedEntity = new NamedEntity(tokens, index,windowSize,objectValuePairs);
            sentenceNameEntities.put(index, namedEntity);
        }
        
        for(Integer no:sentenceNameEntities.keySet()){
            NamedEntity namedEntity=sentenceNameEntities.get(no);
            for (String namenEntitiesOfSentence:namedEntity.getNameEntitiesForSentence()){
                 Sentence sentence=namedEntity.getNamedEntities(namenEntitiesOfSentence);
                 for(String pattern:sentence.getStringToCheck()){
                     System.out.println(pattern);
                 }
            }
        }*/
         
    }
    
   
}
