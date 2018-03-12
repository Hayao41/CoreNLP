package edu.stanford.nlp.naturalli;

import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.PropertiesUtils;

import java.util.Collection;
import java.util.List;
import java.util.Properties;

/**
 * A demo illustrating how to call the OpenIE system programmatically.
 * You can call this code with:
 *
 * <pre>
 *   java -mx1g -cp stanford-openie.jar:stanford-openie-models.jar edu.stanford.nlp.naturalli.OpenIEDemo
 * </pre>
 *
 */
public class OpenIEDemo {

  private OpenIEDemo() {} // static main

  public static void main(String[] args) throws Exception {
    // Create the Stanford CoreNLP pipeline
    Properties props = PropertiesUtils.asProperties(
            "annotators", "tokenize,ssplit,pos,lemma,depparse,natlog,openie"
    );
    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

    // Annotate an example document.
    String text;
    if (args.length > 0) {
      text = IOUtils.slurpFile(args[0]);
    } else {
//      text = "Obama was born in Hawaii, he is our president.";
      text = "David Henshaw ( April 2 , 1791 – November 11 , 1852 ) is the 14th United States Secretary of the Navy .";
      //text = "Born in a small town,she took the midnight train going anywhere.";
      //text = "Born in Honolulu,Obama is a US Citizen.";
      //text = "Microsoft's co-founder Bill Gates spoke in New York city";
      //text = "Alexandra of Denmark (1844-1925) was Queen Consort to Edward VII of the United Kingdom and thus Empress of India during her husband's reign.";
      //text = "Bill's bike is Penn";
      //text = "Google's Hinton outlines new AI advance that requires less data";
//      text = "Born in a small town, she took a midnight train very hard.";
//      text = "White fox jumped away the dog.";
//      text = "There are also many community choirs and bands including the Imogen Children's Chorale , Queensland Philharmonic Chorale , the Brisbane Chorale , the Queensland University Musical Society ( QUMS ) , the Queensland Choir.Queensland Conservatorium Griffith University , Queensland University Musical Society ( QUMS ) , CIP , Queensland Musical Society , Brisbane Philharmonic Orchestra , Queensland Wind and Brass , St Lucia Orchestra , Brisbane Symphonic Band , Brisbane Municipal Concert Band , Brisbane Arts Theatre , Brisbane Excelsior Brass Band , Queensland Youth Orchestras , Brisbane Regional Youth Orchestra , Queensland Wind Orchestra , Centenary Theatre Group , Villanova Players , Ignatians Musical Society , Queensland Musical Theatre , Savoyards Musical Comedy Society and Springboard Theatre Company .";
    }
    Annotation doc = new Annotation(text);
    pipeline.annotate(doc);

    // Loop over sentences in the document
    int sentNo = 0;
    for (CoreMap sentence : doc.get(CoreAnnotations.SentencesAnnotation.class)) {
      System.out.println("Sentence #" + ++sentNo + ": " + sentence.get(CoreAnnotations.TextAnnotation.class));

      // Print SemanticGraph
//      System.out.println(sentence.get(SemanticGraphCoreAnnotations.EnhancedDependenciesAnnotation.class).toString(SemanticGraph.OutputFormat.LIST));
      System.out.println(sentence.get(SemanticGraphCoreAnnotations.EnhancedDependenciesAnnotation.class).toString(CoreLabel.OutputFormat.VALUE_TAG_INDEX));


      // Get the OpenIE triples for the sentence
      Collection<RelationTriple> triples = sentence.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class);

      // Print the triples
      for (RelationTriple triple : triples) {
        System.out.println(triple.confidence + "\t" +
            triple.subjectLemmaGloss() + "\t" +
            triple.relationLemmaGloss() + "\t" +
            triple.objectLemmaGloss());
      }

      // Alternately, to only run e.g., the clause splitter:
      List<SentenceFragment> clauses = new OpenIE(props).clausesInSentence(sentence);
      for (SentenceFragment clause : clauses) {
        System.out.println(clause.parseTree.toString(SemanticGraph.OutputFormat.LIST));
      }
      System.out.println();
    }
  }
}
