package com.example.myapplication;

import java.io.Serializable;

public class FileContainer implements Serializable {

    private String filename;
    private Integer size;
    private byte[] data;

    public FileContainer(String filename, Integer size, byte[] data) {
        this.filename = filename;
        this.size = size;
        this.data = data;
    }

    public FileContainer() {
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
