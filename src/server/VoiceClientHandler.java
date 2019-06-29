package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class VoiceClientHandler {

    DataInputStream dis1;
    DataInputStream dis2;
    DataOutputStream dos1;
    DataOutputStream dos2;

    public VoiceClientHandler(Socket one, Socket two) {
        try {
            dis1 = new DataInputStream(one.getInputStream());
            dos1 = new DataOutputStream(one.getOutputStream());
            dis2 = new DataInputStream(two.getInputStream());
            dos2 = new DataOutputStream(two.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread t1 = new Thread(this::first);
        Thread t2 = new Thread(this::second);
    }

    void first() {
        byte[] data = new byte[8000];
        while (true) {
            try {
                if(dis1.available() <= 0)
                    continue;
            } catch (IOException e) {
                e.printStackTrace();
            }
            int readCount = 0;
            try {
                readCount = dis1.read(data,0,data.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(readCount);
            if(readCount>0){
                try {
                    dos2.write(data,0,readCount);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void second() {
        byte[] data = new byte[8000];
        while (true) {
            try {
                if(dis2.available() <= 0)
                    continue;
            } catch (IOException e) {
                e.printStackTrace();
            }
            int readCount = 0;
            try {
                readCount = dis2.read(data,0,data.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(readCount);
            if(readCount>0){
                try {
                    dos1.write(data,0,readCount);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
