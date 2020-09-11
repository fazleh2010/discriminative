/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.utils;

import static citec.correlation.core.analyzer.TextAnalyzer.ENGLISH_STOPWORDS;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

/**
 *
 * @author elahi
 */
public class SortUtils {

    public static List<String> countWords(String texts) {
        Map<String, Integer> wordCounts = new HashMap<String, Integer>();
        StringTokenizer st = new StringTokenizer(texts);
        while (st.hasMoreTokens()) {
            String token = st.nextToken().trim().toLowerCase();
            if (isNotStopWord(token)) {
                if (wordCounts.containsKey(token)) {
                    Integer number = wordCounts.get(token) + 1;
                    wordCounts.put(token, number);
                } else {
                    wordCounts.put(token, 1);
                }
            }
        }
        return sort(wordCounts);
    }

    public static List<String> sort(Map<String, Integer> hm) {

        Set<Map.Entry<String, Integer>> set = hm.entrySet();
        List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(
                set);
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                    Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        Integer maxNumber = 100, index = 0;
        List<String> topWords = new ArrayList<String>();

        for (Map.Entry<String, Integer> entry : list) {
            //System.out.println(entry.getValue());
            //System.out.println(entry.getKey());
            index++;
            topWords.add(entry.getKey());
            if (index == maxNumber) {
                break;
            }

        }
        // System.out.println(topWords.toString());
        return topWords;
    }

    private static Boolean isNotStopWord(String token) {
        if (ENGLISH_STOPWORDS.contains(token)) {
            return false;
        }
        return true;
    }

}
