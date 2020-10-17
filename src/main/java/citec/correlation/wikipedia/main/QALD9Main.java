/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.wikipedia.main;

import citec.correlation.core.analyzer.Analyzer;
import static citec.correlation.core.analyzer.TextAnalyzer.POS_TAGGER;
import citec.correlation.wikipedia.main.TableMain;
import citec.correlation.wikipedia.qald.DataUnit;
import citec.correlation.wikipedia.qald.JsonReader;
import citec.correlation.wikipedia.qald.Question;
import citec.correlation.wikipedia.qald.ResultQald9;
import citec.correlation.wikipedia.qald.Unit;
import citec.correlation.wikipedia.table.Tables;
import citec.correlation.wikipedia.utils.FileFolderUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author elahi
 */
public class QALD9Main {

    private static String qald9Dir = "src/main/resources/qald9/data/";
    private static String testJson = "qald-9-test-multilingual.json";
    private static String trainingJson = "qald-9-train-multilingual.json";
    private static String english = "en";
    private static Map<String, Unit> posTags = new TreeMap<String, Unit>();

    public static void main(String[] args) throws IOException, Exception {
        File initialFile = new File(qald9Dir + trainingJson);
        List<ResultQald9> resultQald9s = parse(initialFile);
        makeFiles(qald9Dir, resultQald9s, Analyzer.ADJECTIVE);

    }

    private static List<ResultQald9> parse(File initialFile) throws FileNotFoundException, Exception {
        InputStream inputStream = new FileInputStream(initialFile);
        JsonReader jsonReader = new JsonReader(inputStream);
        Integer index=0;
        List<ResultQald9> resultQald9s = new ArrayList<ResultQald9>();
        for (DataUnit dataUnit : jsonReader.getDataUnit()) {
            for (Question question : dataUnit.getQuestion()) {
                if (question.getLanguage().contains(english)) {
                    Analyzer analyzer = new Analyzer(null, question.getString(), POS_TAGGER, 5);
                    ResultQald9 resultQald9 = new ResultQald9(dataUnit.getId(), question.getString(), dataUnit.getQuery().get("sparql"), analyzer.getNouns(), analyzer.getAdjectives());
                    resultQald9s.add(resultQald9);
                    System.out.println("now processing:"+dataUnit.getId());
                }
            }
            index=index+1;
            //if(index==20)
            //   break;
        }
        return resultQald9s;
    }

    private static void makeFiles(String qald9Dir, List<ResultQald9> resultQald9s, String postagType) throws Exception {
        String fileName = qald9Dir + postagType + ".json";
        for (ResultQald9 resultQald9 : resultQald9s) {
            for (String word : resultQald9.getAdjectives()) {
                word = word.toLowerCase();
                if (posTags.containsKey(word)) {
                    Unit unit = posTags.get(word);
                    unit.setSparqls(resultQald9.getId(),resultQald9.getSparql());
                    unit.setQaldQuestionId(resultQald9.getId());
                    posTags.put(word, unit);
                } else {
                    System.out.println(resultQald9.getId()+" "+resultQald9.getSparql());
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
        FileFolderUtils.writeToJsonFile(units,fileName);
    }
    

}
