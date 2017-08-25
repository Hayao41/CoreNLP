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
import edu.stanford.nlp.util.logging.Redwood;
import sun.rmi.runtime.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static edu.stanford.nlp.util.logging.Redwood.Util.endTrack;
import static edu.stanford.nlp.util.logging.Redwood.Util.log;
import static edu.stanford.nlp.util.logging.Redwood.finishThread;
import static edu.stanford.nlp.util.logging.Redwood.forceTrack;

/**
 * Simple script for pre-processing training data
 */
public class TrainingDataSet {
    private static List<String> loadAnnotaedSentences(String url) throws IOException{
        ArrayList<String> annotatedSentences = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(url));
        reader.readLine();
        String line = "";
        while ((line = reader.readLine()) != null){
            String segment = "";
            Pattern pattern = Pattern.compile("\"(.*?)\"");
            Matcher matcher = pattern.matcher(line);
            int counter = 0;
            for(int i = 0; i < 5; i++){
                matcher.find();
            }
            segment = matcher.group();
            segment = segment.substring(1,segment.length()-1);
            annotatedSentences.add(segment);
        }
        return annotatedSentences;
    }


    public static void main(String[] args) {
        try{
            forceTrack("Processing treebanks");
            List<String> annotatedSentences = null;
            //String url = "G:\\ideaprojects\\CoreNLP\\src\\edu\\stanford\\nlp\\AnnotatedFile\\annotated_sentences.csv";
            String url = "D:\\Git\\CoreNLP\\src\\edu\\stanford\\nlp\\AnnotatedFile\\annotated_sentences.csv";
            annotatedSentences = loadAnnotaedSentences(url);
            /*
            annotatedSentences = new ArrayList<>();
            annotatedSentences.add("Alexandra of Denmark ( 1844 &ndash; 1925 ) was Queen Consort to Edward VII of the United Kingdom and thus Empress of India during her husband's reign .");
            */
            List<Pair<CoreMap, Collection<Pair<Span, Span>>>> dataset = RelationDataPreprocessing.getDataSet(annotatedSentences);
            endTrack("Processing treebanks");

            forceTrack("Training");
            log("dataset size: " + dataset.size());
            ClauseSplitter.train(
                    dataset.stream(),
                    new File("D:\\Lib\\model\\temp\\clauseSearcher.ser.gz"),
                    new File("D:\\Lib\\model\\temp\\clauseSearcherData.tab.gz"));
            endTrack("Training");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
