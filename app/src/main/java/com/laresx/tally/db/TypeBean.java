package com.laresx.tally.db;
//identify accurate spending or income type

public class TypeBean {
    int id;
    String typename;
    int imageId; //name of unselected image
    int simageId; //name of selected image
    int kind; //income: 1, spending: 0

    public TypeBean(int id, String type, int imageId, int simageId, int kind) {
        this.id = id;
        this.typename = type;
        this.imageId = imageId;
        this.simageId = simageId;
        this.kind = kind;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String type) {
        this.typename = type;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getSimageId() {
        return simageId;
    }

    public void setSimageId(int simageId) {
        this.simageId = simageId;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }
}
