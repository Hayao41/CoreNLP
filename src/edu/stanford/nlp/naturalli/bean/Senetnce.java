package edu.stanford.nlp.naturalli.bean;

public class Senetnce {
    private String sentence;
    private String sid;

    public Senetnce() {
    }

    public Senetnce(String sentence, String sid) {
        this.sentence = sentence;
        this.sid = sid;
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

    @Override
    public String toString() {
        return "Senetnce{" +
                "sentence='" + sentence + '\'' +
                ", sid=" + sid +
                '}';
    }
}
