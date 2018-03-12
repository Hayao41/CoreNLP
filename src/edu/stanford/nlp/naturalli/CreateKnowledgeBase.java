package edu.stanford.nlp.naturalli;

import com.csvreader.CsvReader;
import com.sun.org.apache.xml.internal.security.algorithms.implementations.IntegrityHmac;
import edu.stanford.nlp.naturalli.bean.RelationEntityTuple;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.swing.plaf.synth.SynthEditorPaneUI;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

/**
 * Simple script for creating knowledge base(relation entity tuple ie. r(e1,e2), thus (e1,e2) is the item for staying in kb)
 */
public class CreateKnowledgeBase {
    public static void main(String[] args) {
        try{
            Connection connection = RelationDataPreprocessing.conn2database();
            String url = "D:\\Git\\CoreNLP\\src\\edu\\stanford\\nlp\\AnnotatedFile\\annotated_sentences.csv";
            String url2 = "D:\\Git\\CoreNLP\\src\\edu\\stanford\\nlp\\AnnotatedFile\\annotated_sentences2.csv";
            String url3 = "D:\\Git\\CoreNLP\\src\\edu\\stanford\\nlp\\AnnotatedFile\\annotatedSentences.csv";
            String sql = "insert into knowledgebase(entity,slotValue,entityCharOffsetBegin,entityCharOffsetEnd,slotValueCharOffsetBegin,slotValueCharOffsetEnd)values(?,?,?,?,?,?)";
            BufferedReader reader = new BufferedReader(new FileReader(url3));
            CsvReader csvReader = new CsvReader(url3, ',', Charset.forName("UTF-8"));
            csvReader.readHeaders();
            int counter = 0;
            while (csvReader.readRecord()){
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                String[] values = csvReader.getValues();
                String sid = "S#" + counter++;
                String senetnce = values[4];
                String entity = values[5];
                String slotValue = values[8];
                senetnce = RelationDataPreprocessing.decodeHtml(senetnce);
                entity = RelationDataPreprocessing.decodeHtml(entity);
                slotValue = RelationDataPreprocessing.decodeHtml(slotValue);
                int entityCharOffsetBegin = senetnce.indexOf(entity);
                int entityCharOffsetEnd = entityCharOffsetBegin + entity.length();
                int slotValueCharOffsetBegin = senetnce.indexOf(slotValue);
                int slotValueCharOffsetEnd = slotValueCharOffsetBegin + slotValue.length();
                preparedStatement.setString(1, entity);
                preparedStatement.setString(2, slotValue);
                preparedStatement.setInt(3, entityCharOffsetBegin);
                preparedStatement.setInt(4, entityCharOffsetEnd);
                preparedStatement.setInt(5, slotValueCharOffsetBegin);
                preparedStatement.setInt(6, slotValueCharOffsetEnd);
                int rows = preparedStatement.executeUpdate();
                String t1 = senetnce.substring(entityCharOffsetBegin,entityCharOffsetEnd);
                String t2 = senetnce.substring(slotValueCharOffsetBegin,slotValueCharOffsetEnd);
                if(rows > 0){
                    System.out.println("S# " + sid + " insert " + rows + " relation tuples into knowledgebase" + "<" + t1 + "," + t2 + ">" +
                            "  [<" + entityCharOffsetBegin + " , " + entityCharOffsetEnd + "> ,<" + slotValueCharOffsetBegin+" , " + slotValueCharOffsetEnd + ">]");
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
