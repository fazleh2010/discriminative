
import java.io.InputStream;
import static java.lang.System.in;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.ngram.NGramModel;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.sentdetect.SentenceSampleStream;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Span;
import opennlp.tools.util.StringList;
import org.checkerframework.common.reflection.qual.GetMethod;
import org.junit.Assert;

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
        
        
        List<String> sentenceLines = new ArrayList<String>();
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
        }
         
    }

}
