
package com.gaiya.easybuy.model;


import java.io.Serializable;

/**
 * Created by dengt on 15-5-22.
 */
public class PushModel implements Serializable {
    private String type;
    private String url;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
