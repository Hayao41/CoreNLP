package edu.stanford.nlp.parser.nndep.demo;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.util.logging.Redwood;

import java.io.StringReader;
import java.util.List;

/**
 * Demonstrates how to first use the tagger, then use the NN dependency
 * parser. Note that the parser will not work on untagged text.
 *
 * @author Jon Gauthier
 */
public class DependencyParserDemo  {

  /** A logger for this class */
  private static Redwood.RedwoodChannels log = Redwood.channels(DependencyParserDemo.class);

  public static void main(String[] args) {
    String modelPath = DependencyParser.DEFAULT_MODEL;
    String taggerPath = "edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger";

    for (int argIndex = 0; argIndex < args.length; ) {
      switch (args[argIndex]) {
        case "-tagger":
          taggerPath = args[argIndex + 1];
          argIndex += 2;
          break;
        case "-model":
          modelPath = args[argIndex + 1];
          argIndex += 2;
          break;
        default:
          throw new RuntimeException("Unknown argument " + args[argIndex]);
      }
    }

//    String text = "I can almost always tell when movies use fake dinosaurs.";
//    String text = "Alexandra of Denmark ( 1844 – 1925 ) was Queen Consort to Edward VII of the United Kingdom and thus Empress of India during her husband's reign .";
    String text = "Conversely , two of the most famous rock musicians from Ontario , Avril Lavigne and Alanis Morissette , are Franco-Ontarian by the second definition but not by the first , since they were born to Franco-Ontarian parents but currently work and live predominantly using the English language ( both currently have residences in Los Angeles ) .";
    MaxentTagger tagger = new MaxentTagger(taggerPath);
    DependencyParser parser = DependencyParser.loadFromModelFile(modelPath);

    DocumentPreprocessor tokenizer = new DocumentPreprocessor(new StringReader(text));
    for (List<HasWord> sentence : tokenizer) {
      List<TaggedWord> tagged = tagger.tagSentence(sentence);
      GrammaticalStructure gs = parser.predict(tagged);
      SemanticGraph graph = new SemanticGraph(gs.typedDependencies());
      System.out.println(graph.toString(CoreLabel.OutputFormat.VALUE_TAG_INDEX));

      // Print typed dependencies
      log.info(gs);
    }
  }

}
