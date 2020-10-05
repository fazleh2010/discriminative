/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.wikipedia.utils;

import citec.correlation.wikipedia.table.WordResult;
import citec.correlation.wikipedia.table.EntityResults;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author elahi
 */
public class FileFolderUtils {

    private static String folder = "src/main/resources/dbpedia/democratic/input.zip";

    private static String calculationProcess = 
            "This is the probability distribution of dbo:Politician_dbo:party_probability.json\n"
            + "\n"
            + "1. predict whether a certain word appears in abstract given the object of the property.\n"
            + "property=dbo:party  object=http://dbpedia.org/resource/Labor_Party_UK  word=british\n"
            + "P(british | http://dbpedia.org/resource/Labor_Party_UK)= P(british & http://dbpedia.org/resource/Labor_Party_UK) / P(http://dbpedia.org/resource/Labor_Party_UK)\n"
            + "where "
            + "P(british & http://dbpedia.org/resource/Labor_Party_UK)=the number of entities where the object is (http://dbpedia.org/resource/Labor_Party_UK) and the abstract contains the word british\n"
            + "P(http://dbpedia.org/resource/Labor_Party_UK): the number of entities where the object is http://dbpedia.org/resource/Labor_Party_UK\n"
            + "\n"
            + "2. predict the object of the property given certain word in abstract \n"
            + "P(http://dbpedia.org/resource/Labor_Party_UK | british)=P(british and http://dbpedia.org/resource/Labor_Party_UK)/P(british)\n"
            + "P(british): all entities in which the abstract contains the word british"
            +"\n"
            +"\n";

    public static void main(String a[]) {

        FileFolderUtils mfe = new FileFolderUtils();
        mfe.printFileList(folder);
    }

    public static void createDirectory(String location) throws IOException {
        Path location_path = Paths.get(location);
        /*if (Files.exists(location_path)) {
            Files.delete(location_path);
        }*/
        Files.createDirectories(location_path);

    }

    public static File[] getFiles(String fileDir, String ntriple) {
        File dir = new File(fileDir);
        FileFilter fileFilter = new WildcardFileFilter("*" + ntriple);
        File[] files = dir.listFiles(fileFilter);
        return files;

    }

    public static List<File> getFiles(String fileDir, String category, String extension) {
        String[] files = new File(fileDir).list();
        List<File> selectedFiles = new ArrayList<File>();
        for (String fileName : files) {
            if (fileName.contains(category) && fileName.contains(extension)) {
                selectedFiles.add(new File(fileDir + fileName));
            }
        }

        return selectedFiles;

    }

    public static List<String> getHash(String fileName) throws FileNotFoundException, IOException {
        List<String> lists = new ArrayList<String>();
        List<String> lines = new ArrayList<String>();
        BufferedReader reader;
        String line = "";
        try {
            reader = new BufferedReader(new FileReader(fileName));
            line = reader.readLine();
            while (line != null) {
                line = reader.readLine();

                if (line != null) {
                    line = line.replace("http", "\nhttp");
                    lines = IOUtils.readLines(new StringReader(line));
                    for (String value : lines) {
                        //System.out.println("test:" + value);
                        lists.add(value);
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lists;
    }

    public static List<String> getList(String fileName) throws FileNotFoundException, IOException {
        List<String> entities = new ArrayList<String>();

        BufferedReader reader;
        String line = "";
        try {
            reader = new BufferedReader(new FileReader(fileName));
            line = reader.readLine();
            while (line != null) {
                line = reader.readLine();
                if (line != null) {
                    String url = line.trim();
                    entities.add(url);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return entities;
    }

    public static List<String> getSortedList(String fileName, Integer thresold, Integer listSize) throws FileNotFoundException, IOException {
        List<String> words = new ArrayList<String>();
        List<String> finalWords = new ArrayList<String>();
        BufferedReader reader;
        String line = "";
        try {
            reader = new BufferedReader(new FileReader(fileName));
            line = reader.readLine();
            while (line != null) {
                line = reader.readLine();
                if (line != null) {
                    if (line.contains(" ")) {
                        //System.out.println(line);
                        String[] info = line.split(" ");
                        Integer count = Integer.parseInt(info[0].trim());
                        String word = info[1].trim();

                        if (count > thresold) {
                            //System.out.println(line);
                            words.add(word);
                        }
                    }

                }
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Integer size = 0;
        for (String word : words) {

            if (size == listSize) {
                break;
            } else {
                finalWords.add(word);
            }
            size = size + 1;
        }
        return finalWords;
    }

    public static void listToFiles(List<String> list, String fileName) {
        String str = "";
        Integer number = -1, index = 0;
        for (String element : list) {
            if (element.contains("http")) {
                index++;
                String line = element + "\n";
                str += line;
                if (index == number) {
                    break;
                }
            }

        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            System.out.println(str);
            writer.write(str);
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(FileFolderUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void stringToFiles(String str, String fileName) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(str);
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(FileFolderUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void writeToTextFile(List<EntityResults> entityResults, String entityDir, String tableName) {
        String filename = entityDir + "result/" + tableName.replaceAll(".json", "_probability.txt");
        if (entityResults.isEmpty()) {
            return;
        }

        String str = "";
            
        for (EntityResults entities : entityResults) {
            String entityLine = "id=" + entities.getObjectIndex() + "  " + "property=" + entities.getProperty() + "  " + "object=" + entities.getKB() + "  " + "NumberOfEntitiesFoundForObject=" + entities.getNumberOfEntitiesFoundInObject() + "\n";
            String wordSum = "";
            for (WordResult wordResults : entities.getDistributions()) {
                String multiply = "multiply=" + wordResults.getMultiple();
                String probabilty = "";
                for (String rule : wordResults.getProbabilities().keySet()) {
                    Double value = wordResults.getProbabilities().get(rule);
                    String line = rule + "=" + String.valueOf(value) + "  ";
                    probabilty += line;
                }
                String lift="Lift="+wordResults.getLift();
                //temporarily lift value made null, since we are not sure about the Lift calculation
                lift="";
                String wordline = wordResults.getWord() + "  " + multiply + "  " + probabilty + "  "+lift+"\n";
                wordSum += wordline;
            }
            entityLine = entityLine + wordSum + "\n";
            str += entityLine;
        }
        stringToFiles(str, filename);
    }

    public void printFileList(String filePath) {

        FileInputStream fis = null;
        ZipInputStream zipIs = null;
        ZipEntry zEntry = null;
        try {
            fis = new FileInputStream(filePath);
            zipIs = new ZipInputStream(new BufferedInputStream(fis));
            while ((zEntry = zipIs.getNextEntry()) != null) {
                System.out.println(zEntry.getName());
            }
            zipIs.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void writeToJsonFile(List<EntityResults> entityResults, String entityDir, String tableName) throws IOException {
        String filename = entityDir + "result/" + tableName.replaceAll(".json", "_probability.json");
        if (entityResults.isEmpty()) {
            return;
        }
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(Paths.get(filename).toFile(), entityResults);
    }

    /*public static void writeToTextFile(List<EntityResults> entityResults, String entityDir, String tableName) throws IOException {
        String filename = entityDir + "result/" + tableName.replaceAll(".json", "_probability.txt");
        if (entityResults.isEmpty()) {
            return;
        }

        String str = "";
            
        for (EntityResults entities : entityResults) {
            String entityLine = "id=" + entities.getObjectIndex() + "  " + "property=" + entities.getProperty() + "  " + "object=" + entities.getKB() + "  " + "NumberOfEntitiesFoundForObject=" + entities.getNumberOfEntitiesFoundInObject() + "\n";
            String wordSum = "";
            for (WordResult wordResults : entities.getDistributions()) {
                String multiply = "multiply=" + wordResults.getMultiple();
                String probabilty = "";
                for (String rule : wordResults.getProbabilities().keySet()) {
                    Double value = wordResults.getProbabilities().get(rule);
                    String line = rule + "=" + String.valueOf(value) + "  ";
                    probabilty += line;
                }
                String wordline = wordResults.getWord() + "  " + multiply + "  " + probabilty + "\n";
                wordSum += wordline;
            }
            entityLine = entityLine + wordSum + "\n";
            str += entityLine;
        }
        stringToFiles(str, filename);

    }*/

    public static String urlUnicodeToString(String url) throws Exception {
        URI uri = new URI(url);
        String urlStr = uri.getQuery();
        return urlStr;
    }

    public static String stringToUrlUnicode(String string) throws UnsupportedEncodingException {
        String encodedString = URLEncoder.encode(string, "UTF-8");
        return encodedString;
    }

}
