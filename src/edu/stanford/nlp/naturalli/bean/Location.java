package edu.stanford.nlp.naturalli.bean;

public class Location {
    private int lid;
    private String sid;
    private int start_left;
    private int start_right;
    private int end_left;
    private int end_right;

    public Location() {
    }

    public Location(int lid, String sid, int start_left, int start_right, int end_left, int end_right) {
        this.lid = lid;
        this.sid = sid;
        this.start_left = start_left;
        this.start_right = start_right;
        this.end_left = end_left;
        this.end_right = end_right;
    }

    public int getLid() {
        return lid;
    }

    public void setLid(int lid) {
        this.lid = lid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public int getStart_left() {
        return start_left;
    }

    public void setStart_left(int start_left) {
        this.start_left = start_left;
    }

    public int getStart_right() {
        return start_right;
    }

    public void setStart_right(int start_right) {
        this.start_right = start_right;
    }

    public int getEnd_left() {
        return end_left;
    }

    public void setEnd_left(int end_left) {
        this.end_left = end_left;
    }

    public int getEnd_right() {
        return end_right;
    }

    public void setEnd_right(int end_right) {
        this.end_right = end_right;
    }

    @Override
    public String toString() {
        return "Location{" +
                "lid=" + lid +
                ", sid=" + sid +
                ", start_left=" + start_left +
                ", start_right=" + start_right +
                ", end_left=" + end_left +
                ", end_right=" + end_right +
                '}';
    }
}
