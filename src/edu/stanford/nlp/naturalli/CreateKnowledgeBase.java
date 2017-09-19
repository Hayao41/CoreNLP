package edu.stanford.nlp.naturalli;

import com.sun.org.apache.xml.internal.security.algorithms.implementations.IntegrityHmac;
import edu.stanford.nlp.naturalli.bean.RelationEntityTuple;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Simple script for creating knowledge base(relation entity tuple ie. r(e1,e2), thus (e1,e2) is the item for staying in kb)
 */
public class CreateKnowledgeBase {
    public static void main(String[] args) {
        try{
            Connection connection = RelationDataPreprocessing.conn2database();
            String url = "D:\\Git\\CoreNLP\\src\\edu\\stanford\\nlp\\AnnotatedFile\\annotated_sentences2.csv";
            String url2 = "G:\\ideaprojects\\CoreNLP\\src\\edu\\stanford\\nlp\\AnnotatedFile\\annotated_sentences.csv";
            String sql = "insert into knowledgebase(sid,entity,slotValue,entityCharOffsetBegin,entityCharOffsetEnd,slotValueCharOffsetBegin,slotValueCharOffsetEnd)values(?,?,?,?,?,?,?)";
            BufferedReader reader = new BufferedReader(new FileReader(url2));
            reader.readLine();
            String line = "";
            int counter = 0;
            while ((line = reader.readLine()) != null){
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
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
                preparedStatement.setString(1, sid);
                preparedStatement.setString(2, entity);
                preparedStatement.setString(3, slotValue);
                preparedStatement.setInt(4, entityCharOffsetBegin);
                preparedStatement.setInt(5, entityCharOffsetEnd);
                preparedStatement.setInt(6, slotValueCharOffsetBegin);
                preparedStatement.setInt(7, slotValueCharOffsetEnd);
                int rows = preparedStatement.executeUpdate();
                String t1 = values[4].substring(entityCharOffsetBegin,entityCharOffsetEnd);
                String t2 = values[4].substring(slotValueCharOffsetBegin,slotValueCharOffsetEnd);
                if(rows > 0){
                    System.out.println(sid + " insert " + rows + " relation tuples into knowledgebase" + "<" + t1 + "," + t2 + ">");
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
