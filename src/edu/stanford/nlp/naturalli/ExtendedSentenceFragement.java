package edu.stanford.nlp.naturalli;

import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;

public class ExtendedSentenceFragement extends SentenceFragment{
    private IndexedWord root;

    public ExtendedSentenceFragement(SemanticGraph tree, boolean assumedTruth, boolean copy) {
        super(tree, assumedTruth, copy);
    }

    public ExtendedSentenceFragement(SemanticGraph tree, boolean assumedTruth, boolean copy, IndexedWord root) {
        super(tree, assumedTruth, copy);
        this.root = root;
    }

    public IndexedWord getRoot() {
        return root;
    }

    public void setRoot(IndexedWord root) {
        this.root = root;
    }
}
