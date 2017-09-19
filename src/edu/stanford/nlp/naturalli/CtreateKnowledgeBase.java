package edu.stanford.nlp.naturalli;

import com.sun.org.apache.xml.internal.security.algorithms.implementations.IntegrityHmac;
import edu.stanford.nlp.naturalli.bean.RelationEntityTuple;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Simple script for creating knowledge base(relation entity tuple ie. r(e1,e2), thus (e1,e2) is the item for staying in kb)
 */
public class CtreateKnowledgeBase {
    public static void main(String[] args) {
        try{
            String url = "D:\\Git\\CoreNLP\\src\\edu\\stanford\\nlp\\AnnotatedFile\\annotated_sentences2.csv";
            String sql = "";
            BufferedReader reader = new BufferedReader(new FileReader(url));
            reader.readLine();
            String line = "";
            int counter = 0;
            while ((line = reader.readLine()) != null){
                String[] values = line.split(",");
                for(int index = 0; index < values.length; index++){
                    values[index] = values[index].substring(1, values[index].length()-1);
                }
                String sid = "S#" + counter++;
                String entity = values[5];
                String slotValue = values[8];
                int entityCharOffsetBegin = Integer.parseInt(values[6]);
                int entityCharOffsetEnd = Integer.parseInt(values[7]);
                int slotValueCharOffsetBegin = Integer.parseInt(values[9]);
                int slotValueCharOffsetEnd = Integer.parseInt(values[10]);


        }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
