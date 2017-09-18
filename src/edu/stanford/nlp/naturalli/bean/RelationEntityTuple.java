package edu.stanford.nlp.naturalli.bean;

public class RelationEntityTuple {
    private String sid;
    private String entity;
    private String slotValue;
    private int entityCharOffsetBegin;
    private int entityCharOffsetEnd;
    private int slotValueCharOffsetBegin;
    private int slotValueCharOffsetEnd;

    public RelationEntityTuple() {
    }

    public RelationEntityTuple(String entity, String slotValue, int entityCharOffsetBegin, int entityCharOffsetEnd, int slotValueCharOffsetBegin, int slotValueCharOffsetEnd) {
        this.entity = entity;
        this.slotValue = slotValue;
        this.entityCharOffsetBegin = entityCharOffsetBegin;
        this.entityCharOffsetEnd = entityCharOffsetEnd;
        this.slotValueCharOffsetBegin = slotValueCharOffsetBegin;
        this.slotValueCharOffsetEnd = slotValueCharOffsetEnd;
    }

    public RelationEntityTuple(String sid, String entity, String slotValue, int entityCharOffsetBegin, int entityCharOffsetEnd, int slotValueCharOffsetBegin, int slotValueCharOffsetEnd) {
        this.sid = sid;
        this.entity = entity;
        this.slotValue = slotValue;
        this.entityCharOffsetBegin = entityCharOffsetBegin;
        this.entityCharOffsetEnd = entityCharOffsetEnd;
        this.slotValueCharOffsetBegin = slotValueCharOffsetBegin;
        this.slotValueCharOffsetEnd = slotValueCharOffsetEnd;
    }

    public String getSid() {
        return sid;
    }

    public String getEntity() {
        return entity;
    }

    public String getSlotValue() {
        return slotValue;
    }

    public int getEntityCharOffsetBegin() {
        return entityCharOffsetBegin;
    }

    public int getEntityCharOffsetEnd() {
        return entityCharOffsetEnd;
    }

    public int getSlotValueCharOffsetBegin() {
        return slotValueCharOffsetBegin;
    }

    public int getSlotValueCharOffsetEnd() {
        return slotValueCharOffsetEnd;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public void setSlotValue(String slotValue) {
        this.slotValue = slotValue;
    }

    public void setEntityCharOffsetBegin(int entityCharOffsetBegin) {
        this.entityCharOffsetBegin = entityCharOffsetBegin;
    }

    public void setEntityCharOffsetEnd(int entityCharOffsetEnd) {
        this.entityCharOffsetEnd = entityCharOffsetEnd;
    }

    public void setSlotValueCharOffsetBegin(int slotValueCharOffsetBegin) {
        this.slotValueCharOffsetBegin = slotValueCharOffsetBegin;
    }

    public void setSlotValueCharOffsetEnd(int slotValueCharOffsetEnd) {
        this.slotValueCharOffsetEnd = slotValueCharOffsetEnd;
    }

    @Override
    public String toString() {
        return "RelationEntityTuple{" +
                "sid='" + sid + '\'' +
                ", entity='" + entity + '\'' +
                ", slotValue='" + slotValue + '\'' +
                ", entityCharOffsetBegin=" + entityCharOffsetBegin +
                ", entityCharOffsetEnd=" + entityCharOffsetEnd +
                ", slotValueCharOffsetBegin=" + slotValueCharOffsetBegin +
                ", slotValueCharOffsetEnd=" + slotValueCharOffsetEnd +
                '}';
    }
}
