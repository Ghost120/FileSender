package com.example.myapplication.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.FileContainer;
import com.example.myapplication.ObjectMapper;
import com.example.myapplication.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import androidx.appcompat.app.AppCompatActivity;

public class Client extends AppCompatActivity {

    private ImageView imageView;
    private TextView ipAddrView;
    private EditText ipFrom;
    private EditText portFrom;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        imageView = findViewById(R.id.image);
        ipAddrView = findViewById(R.id.ipaddress);
        btn = findViewById(R.id.connect);
        ipFrom = findViewById(R.id.from_ipaddress);
        portFrom = findViewById(R.id.from_port);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClientRxThread clientRxThread =
                        new ClientRxThread(
                                ipFrom.getText().toString(),
                                Integer.parseInt(portFrom.getText().toString()));

                clientRxThread.start();
            }
        });

        ipAddrView.setText("your ip: " + getIPAddress(true));
    }

    private class ClientRxThread extends Thread {
        String dstAddress;
        int dstPort;

        ClientRxThread(String address, int port) {
            dstAddress = address;
            dstPort = port;
        }

        @Override
        public void run() {
            Socket socket = null;

            try {
                socket = new Socket(dstAddress, dstPort);

                InputStream is = socket.getInputStream();
                writeStreamToFile(is);
                socket.close();

                Client.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(Client.this,
                                "Finished",
                                Toast.LENGTH_LONG).show();
                    }
                });

            } catch (IOException e) {

                e.printStackTrace();

                final String eMsg = "Something wrong: " + e.getMessage();
                Client.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(Client.this,
                                eMsg,
                                Toast.LENGTH_LONG).show();
                    }
                });

            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {
            System.out.println("hjkl");
        }
        return "";
    }

    public String convert(InputStream inputStream) {
        try (Scanner scanner = new Scanner(inputStream, "UTF8")) {
            return scanner.useDelimiter("\\A").next();
        }
    }


    void writeStreamToFile(InputStream input) {
        try {

            byte[] buffer = new byte[4 * 1024];
            input.read(buffer);
            FileContainer fileContainer = ObjectMapper.deserialize(buffer);
            File file = new File(Environment.getExternalStorageDirectory() + "/Download/" + fileContainer.getFilename());

            try (OutputStream output = new FileOutputStream(file)) {
                output.write(fileContainer.getData());
                output.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
