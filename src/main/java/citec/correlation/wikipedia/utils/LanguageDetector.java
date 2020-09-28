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

    public static void main(String[] args) {
        LanguageIdentifier identifier = new LanguageIdentifier("Hello, this is javatpoint.");
        String language = identifier.getLanguage();
        System.out.println("Language code is : " + language);
    }

}
