package com.example.myapplication;

import android.os.AsyncTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ImageSender extends AsyncTask<String, Integer, String> {


    private int port;
    private String ip;

    @Override
    protected String doInBackground(String... serverAdd) {
        String filePath = "Path to file";

        File sdFile = new File(filePath);
        Socket client;
        OutputStream outputStream;

        try {

            client = new Socket();
            client.bind(new InetSocketAddress(ip, port));



            outputStream = client.getOutputStream();
            byte[] buffer = new byte[1024];
            FileInputStream in = new FileInputStream(sdFile);
            int rBytes;
            while ((rBytes = in.read(buffer, 0, 1024)) != -1) {
                outputStream.write(buffer, 0, rBytes);
            }

            outputStream.flush();
            outputStream.close();
            client.close();


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
