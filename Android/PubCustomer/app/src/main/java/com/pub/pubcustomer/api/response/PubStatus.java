package com.pub.pubcustomer.api.response;

import java.util.List;

/**
 * Created by Fernando Santiago on 02/08/2016.
 */
public class PubStatus {

    private final long id;
    private final List<String> content;

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
