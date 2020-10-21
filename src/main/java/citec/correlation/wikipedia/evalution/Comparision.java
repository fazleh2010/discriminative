/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.wikipedia.evalution;

import citec.correlation.wikipedia.qald.Unit;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**
 *
 * @author elahi
 */
public class Comparision {

    public Comparision(String qaldFileName, String methodFileName) throws IOException {
        List<LexiconUnit> lexiconUnit = getLexicon(methodFileName);
        List<Unit> units = getQald(qaldFileName);

        for (LexiconUnit LexiconUnit : lexiconUnit) {
            System.out.println(LexiconUnit);
        }
        for (Unit unit : units) {
            System.out.println(unit);
        }

    }

    private List<LexiconUnit> getLexicon(String methodFileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<LexiconUnit> lexiconUnit = mapper.readValue(Paths.get(methodFileName).toFile(), new TypeReference<List<LexiconUnit>>() {
        });
        return lexiconUnit;
    }

    private List<Unit> getQald(String qaldFileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
         List<Unit> lexiconUnit = mapper.readValue(Paths.get(qaldFileName).toFile(), new TypeReference<List<Unit>>() {
        });

        return lexiconUnit;
    }

}
