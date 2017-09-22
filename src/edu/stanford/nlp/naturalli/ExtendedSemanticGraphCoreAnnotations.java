package edu.stanford.nlp.naturalli;

import edu.stanford.nlp.ling.CoreAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.trees.Tree;

public class ExtendedSemanticGraphCoreAnnotations extends SemanticGraphCoreAnnotations {
    public static class SenetceText implements CoreAnnotation<String> {
        @Override
        public Class<String> getType() {
            return String.class;
        }
    }

    public static class ParseTree implements CoreAnnotation<Tree> {
        @Override
        public Class<Tree> getType() {
            return Tree.class;
        }
    }
}
