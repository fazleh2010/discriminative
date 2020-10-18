/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.wikipedia.main;

import citec.correlation.core.analyzer.Analyzer;
import static citec.correlation.core.analyzer.TextAnalyzer.POS_TAGGER;
import citec.correlation.wikipedia.qald.DataUnit;
import citec.correlation.wikipedia.qald.JsonReader;
import citec.correlation.wikipedia.qald.Question;
import citec.correlation.wikipedia.qald.QaldPosTagger;
import citec.correlation.wikipedia.qald.Unit;
import citec.correlation.wikipedia.utils.FileFolderUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author elahi
 */
public class QALDMain {

    private static String qald9Dir = "src/main/resources/qald9/data/";
    private static String testJson = "qald-9-test-multilingual.json";
    private static String trainingJson = "qald-9-train-multilingual.json";
    private static String english = "en";
    private static Map<String, Unit> posTags = new TreeMap<String, Unit>();

    public static void main(String[] args) throws IOException, Exception {
        List<String> posTags = new ArrayList<String>();
        posTags.add(Analyzer.ADJECTIVE);
        posTags.add(Analyzer.NOUN);
        posTags.add(Analyzer.VERB);

        File initialFile = new File(qald9Dir + trainingJson);
        List<QaldPosTagger> resultQald9s = parse(initialFile);
        for (String posTag : posTags) {
            makeFiles(qald9Dir, resultQald9s, posTag);
        }

    }

    private static List<QaldPosTagger> parse(File initialFile) throws FileNotFoundException, Exception {
        InputStream inputStream = new FileInputStream(initialFile);
        JsonReader jsonReader = new JsonReader(inputStream);
        Integer index = 0;
        List<QaldPosTagger> resultQald9s = new ArrayList<QaldPosTagger>();
        for (DataUnit dataUnit : jsonReader.getDataUnit()) {
            for (Question question : dataUnit.getQuestion()) {
                if (question.getLanguage().contains(english)) {
                    Analyzer analyzer = new Analyzer(null, question.getString(), POS_TAGGER, 5);
                    String sparql = dataUnit.getQuery().get("sparql");
                    QaldPosTagger resultQald9 = new QaldPosTagger(dataUnit.getId(), question.getString(), sparql, analyzer.getNouns(), analyzer.getAdjectives(), analyzer.getVerbs());
                    resultQald9s.add(resultQald9);
                    System.out.println("now processing:" + resultQald9.getId());
                }
            }
            index = index + 1;
            /*if (index == 20) {
                break;
            }*/
        }
        return resultQald9s;
    }

    private static void makeFiles(String qald9Dir, List<QaldPosTagger> resultQald9s, String postagType) throws Exception {
        Map<String, Unit> posTags = new TreeMap<String, Unit>();
        String fileName = qald9Dir + postagType + ".json";
        for (QaldPosTagger resultQald9 : resultQald9s) {
            Set<String> words = new HashSet<String>();
            if (postagType.contains(Analyzer.ADJECTIVE)) {
                words = resultQald9.getAdjectives();
            } else if (postagType.contains(Analyzer.NOUN)) {
                words = resultQald9.getNouns();
            } else if (postagType.contains(Analyzer.VERB)) {
                words = resultQald9.getVerbs();
            }
            for (String word : words) {
                word = word.toLowerCase();
                if (word.contains("8th")) {
                    continue;
                }

                if (posTags.containsKey(word)) {
                    Unit unit = posTags.get(word);
                    unit.setSparqls(resultQald9.getId(), resultQald9.getSparql());
                    unit.setQaldQuestionId(resultQald9.getId());
                    posTags.put(word, unit);
                } else {
                    //System.out.println(resultQald9.getId()+" "+resultQald9.getSparql());
                    Unit unit = new Unit(word, resultQald9.getId(), resultQald9.getSparql());
                    posTags.put(word, unit);
                }

            }
        }
        List<Unit> units = new ArrayList<Unit>();
        for (String word : posTags.keySet()) {
            Unit unit = posTags.get(word);
            units.add(unit);
        }
        FileFolderUtils.writeToJsonFile(units, fileName);
    }

    /*private static String format(String sparql) {
        sparql=sparql.replace("PREFIX", "\n"+"PREFIX");
        sparql=sparql.replace("SELECT", "\n"+"SELECT");
        sparql=sparql.replace("WHERE", "\n"+"WHERE");
        return sparql;
    }*/
}