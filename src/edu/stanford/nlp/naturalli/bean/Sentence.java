package edu.stanford.nlp.naturalli.bean;

import edu.stanford.nlp.util.CoreMap;

public class Sentence {
    private String sentence;
    private String parse_tree;
    private String sid;
    private CoreMap coreMap;
    private String dpTreeStr;

    public Sentence() {
    }

    public Sentence(String sentence, String sid) {
        this.sentence = sentence;
        this.sid = sid;
    }

    public String getDpTreeStr() {
        return dpTreeStr;
    }

    public void setDpTreeStr(String dpTreeStr) {
        this.dpTreeStr = dpTreeStr;
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
        return "Sentence{" +
                "sentence='" + sentence + '\'' +
                ", sid=" + sid +
                '}';
    }
}
