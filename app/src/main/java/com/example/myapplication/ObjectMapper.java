package com.example.myapplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectMapper {

    public static byte[] serialize(FileContainer obj) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream os = null;
            os = new ObjectOutputStream(out);
            os.writeObject(obj);
            return out.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static FileContainer deserialize(byte[] data) {

        FileContainer fileContainer = null;
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            ObjectInputStream is = null;
            is = new ObjectInputStream(in);
            fileContainer = (FileContainer) is.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return fileContainer;

    }
}
