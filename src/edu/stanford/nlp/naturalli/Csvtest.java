package edu.stanford.nlp.naturalli;

import com.csvreader.CsvReader;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class Csvtest {
    public static void main(String[] args){
        ArrayList<String[]> csvFileValues = new ArrayList<>();
        String path = "D:\\Git\\CoreNLP\\src\\edu\\stanford\\nlp\\AnnotatedFile\\annotatedSentences.csv";
        try{
            CsvReader reader = new CsvReader(path, ',', Charset.forName("UTF-8"));

            reader.readHeaders();

            while (reader.readRecord()){
                csvFileValues.add(reader.getValues());
            }
            for(String[] temp : csvFileValues){
                System.out.println(temp[4]);
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
