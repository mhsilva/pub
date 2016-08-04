package com.pub.pubcustomer.api.response;

/**
 * Created by Fernando Santiago on 03/08/2016.
 */
import java.util.List;

public class PubStatusApi {
    private final long id;
    private final List<String> content;

    public PubStatusApi(long id, List<String> content) {
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