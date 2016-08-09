package com.pub.pubcustomer.entity;

/**
 * Created by Fernando Santiago on 03/08/2016.
 */
import java.io.Serializable;
import java.util.List;

public class PubStatus implements Serializable {

    private  long id;
    private List<String> content;

    public PubStatus() {
    }

    public PubStatus(long id, List<String> content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public List<String> getContent() {
        return content;
    }
}