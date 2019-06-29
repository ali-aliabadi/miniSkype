package client.controller;

import javax.sound.sampled.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class VoiceStream {

    DataInputStream dis = null;
    DataOutputStream dos = null;


    VoiceStream(DataInputStream in, DataOutputStream out){
        this.dis = in;
        this.dos = out;
        Thread receive = new Thread(this::getVoice);
        receive.start();
        Thread send = new Thread(this::sendVoice);
        send.start();
    }

    public void getVoice() {
        AudioFormat format = new AudioFormat(8000.F,16,1,true,false);
        DataLine.Info speakerInfo = new DataLine.Info(SourceDataLine.class, format);

        SourceDataLine speaker = null;
        try {
            speaker = (SourceDataLine) AudioSystem.getLine(speakerInfo);
            speaker.open(format);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        speaker.start();
        byte[] data = new byte[8000];
        while (true){
            try {
                if(dis.available() <= 0)
                    continue;
            } catch (IOException e) {
                e.printStackTrace();
            }
            int readCount = 0;
            try {
                readCount = dis.read(data,0,data.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(readCount);
            if(readCount>0){
                speaker.write(data,0,readCount);
            }
        }
    }

    public void sendVoice() {
        AudioFormat format = new AudioFormat(8000.F, 16, 1, true, false);
        TargetDataLine mic = null;
        try {
            mic = AudioSystem.getTargetDataLine(format);
            mic.open(format);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        System.out.println("Start recording");
        mic.start();

        byte[] bytes = new byte[(int) (mic.getFormat().getSampleRate()*0.4)];
        System.out.println(bytes);
        while (true) {
            int count = mic.read(bytes, 0, bytes.length);
            System.out.println(count);
            if(count>0){
                try {
                    dos.write(bytes,0,count);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
