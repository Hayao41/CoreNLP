package edu.stanford.nlp.naturalli;

import com.sun.javafx.scene.web.Debugger;
import edu.stanford.nlp.ie.machinereading.structure.Span;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.Index;
import edu.stanford.nlp.util.Pair;
import edu.stanford.nlp.util.logging.Redwood;
import org.apache.commons.lang3.StringEscapeUtils;
import sun.nio.cs.ext.ExtendedCharsets;
import sun.rmi.runtime.Log;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static edu.stanford.nlp.util.logging.Redwood.Util.endTrack;
import static edu.stanford.nlp.util.logging.Redwood.Util.log;
import static edu.stanford.nlp.util.logging.Redwood.Util.runtimeException;
import static edu.stanford.nlp.util.logging.Redwood.finishThread;
import static edu.stanford.nlp.util.logging.Redwood.forceTrack;

/**
 * Simple script for pre-processing training data
 */
public class TrainingDataSet {
    public static void main(String[] args) {
        try{
            List<Pair<CoreMap, Collection<Pair<Span, Span>>>> dataset = null;
            Properties properties = new Properties();
            InputStream in = new FileInputStream(new File("D:\\Git\\CoreNLP\\src\\edu\\stanford\\nlp\\naturalli\\properties\\datasetproperties.properties"));
            //InputStream in = new FileInputStream(new File("G:\\ideaprojects\\CoreNLP\\src\\edu\\stanford\\nlp\\naturalli\\properties\\datasetproperties.properties"));

            properties.load(in);
            in.close();
            String dataSource = properties.getProperty("dataset");
            List<String> annotatedSentences = null;
            if(dataSource.equals("from database")){
                dataset = RelationDataPreprocessing.loadDataSetfromDataBase();
            }else{
                forceTrack("Processing treebanks");

                //String url = "G:\\ideaprojects\\CoreNLP\\src\\edu\\stanford\\nlp\\AnnotatedFile\\annotated_sentences.csv";
                //String url = "D:\\Git\\CoreNLP\\src\\edu\\stanford\\nlp\\AnnotatedFile\\annotated_sentences.csv";
                String url = "D:\\Git\\CoreNLP\\src\\edu\\stanford\\nlp\\AnnotatedFile\\annotated_sentences2.csv";
                //String url = "G:\\ideaprojects\\CoreNLP\\src\\edu\\stanford\\nlp\\AnnotatedFile\\annotated_sentences2.csv";
                annotatedSentences = RelationDataPreprocessing.loadAnnotaedSentences(url);
                ArrayList<Integer> temp = new ArrayList<>();
                for(String sentence : annotatedSentences){
                    temp.add(sentence.length());
                }
                temp.sort(Integer::compareTo);
                System.out.println(temp.get(temp.size()-1));

                dataset = RelationDataPreprocessing.getDataSet(annotatedSentences);

                endTrack("Processing treebanks");
            }

            test(dataset);

            if(properties.getProperty("trained").equals("false")){
                forceTrack("Training");
                log("dataset size: " + dataset.size());
                ClauseSplitter.train(
                        dataset.stream(),
                        new File("D:\\Lib\\model\\temp\\clauseSearcher.ser.gz"),
                        new File("D:\\Lib\\model\\temp\\clauseSearcherData.tab.gz"));
                endTrack("Training");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void test(List<Pair<CoreMap, Collection<Pair<Span, Span>>>> dataset){
        for(Pair<CoreMap, Collection<Pair<Span, Span>>> data : dataset){
            Pair<String, String> subobjPair = null;
            CoreMap sentence = data.first;
            Collection<Pair<Span, Span>> pairs = data.second;
            Pair<Span, Span> pair_span = null;
            for(Pair<Span, Span> temp : pairs){
                pair_span = temp;
                break;
            }
            SemanticGraph graph = RelationDataPreprocessing.parse2dependency(sentence.get(ExtendedSemanticGraphCoreAnnotations.ParseTree.class));
            subobjPair = RelationDataPreprocessing.getSubobjPair(graph, pair_span);
            System.out.println("ok");
        }
    }
}
