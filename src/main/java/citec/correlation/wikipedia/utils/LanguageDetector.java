/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.wikipedia.utils;

import org.apache.tika.language.LanguageIdentifier;

/**
 *
 * @author elahi
 */
public class LanguageDetector {
    private static String english="en";

    public static Boolean isEnglish(String text) {
        LanguageIdentifier identifier = new LanguageIdentifier(text);
        String language = identifier.getLanguage();
        if(language.contains(english))
            return true;
        return false;
    }
    
    public static void main (String []args){
        System.out.println(isEnglish("Anthony Aston (died 1731) was an English actor and dramatist"));
    }

}
