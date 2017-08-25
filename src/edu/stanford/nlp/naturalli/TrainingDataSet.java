package edu.stanford.nlp.naturalli;

import com.sun.javafx.scene.web.Debugger;
import edu.stanford.nlp.ie.machinereading.structure.Span;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.Pair;
import sun.rmi.runtime.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static edu.stanford.nlp.util.logging.Redwood.forceTrack;

public class TrainingDataSet {
    public static void main(String[] args) {
        try{
            BufferedReader reader = new BufferedReader(new FileReader("D:\\Git\\CoreNLP\\src\\edu\\stanford\\nlp\\AnnotatedFile\\annotated_sentences.csv"));
            reader.readLine();
            String line = reader.readLine();
            ArrayList<String> sentences = new ArrayList<>();
            forceTrack(line);
            String sent = "Alexandra of Denmark ( 1844 &ndash; 1925 ) was Queen Consort to Edward VII of the United Kingdom and thus Empress of India during her husband's reign .";
            //String sent = "Alexandra of Denmark was Queen Consort to Edward VII of the United Kingdom and thus Empress of India during her husband's reign .";
            //String sent ="Coleco Industries Inc.,a once high-flying toy maker whose stock peaked at $65 a share in the early 1980s,filed a Chapter 11 reorganization plan that provides just 1.125 cents a share for common stockholders.";
            sentences.add(sent);
            List<Pair<CoreMap, Collection<Pair<Span, Span>>>> trainingData = null;
            trainingData = RelationDataPreprocessing.getDataSet(sentences);
            System.out.println(trainingData.get(0).first.toShorterString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
