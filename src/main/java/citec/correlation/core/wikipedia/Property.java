/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.core.wikipedia;

/**
 *
 * @author elahi
 */
public class Property {

    private String propertyString = null;
    private String subject = null;
    private String predicate = null;
    private String object = null;

    public Property(String propertyString) {
        this.propertyString = propertyString; 
        String[] words = propertyString.split(" ");
        this.subject=words[0];
        this.predicate=words[1];
        this.object=words[2];
    }

    public String getPropertyString() {
        return propertyString;
    }

    public String getSubject() {
        return subject;
    }

    public String getPredicate() {
        return predicate;
    }

    public String getObject() {
        return object;
    }

    @Override
    public String toString() {
        return  subject + " " + predicate + " " + object;
    }

   
   

    
}
