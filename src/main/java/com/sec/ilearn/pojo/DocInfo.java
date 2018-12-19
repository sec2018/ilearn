package com.sec.ilearn.pojo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DocInfo {

    @Value(value="${doc.path}")
    private String path;

    public String getPath() {
        return path;
    }
}
