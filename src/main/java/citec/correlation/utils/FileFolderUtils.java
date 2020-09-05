/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.utils;

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
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author elahi
 */
public class FileFolderUtils {
    
    private static String folder="src/main/resources/dbpedia/democratic/input.zip";

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

}
