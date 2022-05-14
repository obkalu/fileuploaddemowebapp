package com.obinna.example.fileuploaddemo.fileuploaddemowebapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "storage")
public class StorageConfigProperties {

    /**
     * Folder path location in the File system for storing uploaded files
     */
    private String location = "upload-dir";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
