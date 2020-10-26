
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author elahi
 */
public class NamedEntity {

    private Map<String, Sentence> namedEntities = new HashMap<String, Sentence>();
    private Map<String, String> objectValuePairs = new HashMap<String, String>();
    private static TokenNameFinderModel model;
    private static String nameEntityDir = "src/main/resources/nameEntiry/";
    private static String PERSON = "person";
    private List<String> nameEntitiesForSentence = new ArrayList<String>();

    static {
        String modelFile = nameEntityDir + "en-ner-person.bin";
        try {
            InputStream inputStream = new FileInputStream(modelFile);
            model = new TokenNameFinderModel(inputStream);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NamedEntity.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(NamedEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public NamedEntity(String tokens[], Integer no, Integer windowSize, Map<String, String> objectValuePairs) throws Exception {
        String[] sentences = new String[tokens.length];
        Integer index = 0;
        for (String token : tokens) {
            sentences[index++] = token.trim();
        }

        Span nameSpans[] = nameEntity(sentences);
        for (Span span : nameSpans) {
            String nameEntity = this.setNamedEntity(sentences, span.getStart(), span.getEnd());
            if (span.getType().contains(PERSON)) {
                Sentence sentenceInfo = new Sentence(sentences, span, no, nameEntity, windowSize, objectValuePairs);
                namedEntities.put(nameEntity, sentenceInfo);
                nameEntitiesForSentence.add(nameEntity);
            }

        }

    }

    private String setNamedEntity(String[] sentence, Integer startIndex, Integer endIndex) {
        String str = "";
        for (Integer index = startIndex; index < endIndex; index++) {
            String line = sentence[index] + " ";
            str += line;
        }
        return str;
    }

    private Span[] nameEntity(String[] sentence) throws Exception {
        NameFinderME nameFinder = new NameFinderME(model);
        return nameFinder.find(sentence);
    }

    public Sentence getNamedEntities(String nameEntity) {
        return namedEntities.get(nameEntity);
    }

    public List<String> getNameEntitiesForSentence() {
        return nameEntitiesForSentence;
    }

}
