package edu.stanford.nlp.naturalli.bean;

import edu.stanford.nlp.util.CoreMap;

public class Senetnce {
    private String sentence;
    private String parse_tree;
    private String sid;
    private CoreMap coreMap;

    public Senetnce() {
    }

    public Senetnce(String sentence, String sid) {
        this.sentence = sentence;
        this.sid = sid;
    }

    public String getParse_tree() {
        return parse_tree;
    }

    public void setParse_tree(String parse_tree) {
        this.parse_tree = parse_tree;
    }

    public String getSentence() {
        return sentence;
    }

    public String getSid() {
        return sid;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public CoreMap getCoreMap() {
        return coreMap;
    }

    public void setCoreMap(CoreMap coreMap) {
        this.coreMap = coreMap;
    }

    @Override
    public String toString() {
        return "Senetnce{" +
                "sentence='" + sentence + '\'' +
                ", sid=" + sid +
                '}';
    }
}
