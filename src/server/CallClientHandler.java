package server;

import javax.sound.sampled.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketAddress;

public class CallClientHandler {

    Socket socket;
    InputStream is;
    DataOutputStream dos;
    SourceDataLine _speaker;
    boolean _running = true;
    Thread listenThread;
    public SocketAddress RemoteAddress;
    public int ErrorCount = 0;

    public CallClientHandler(Socket sock) throws IOException, LineUnavailableException {
        this.socket = sock;
        dos = new DataOutputStream(sock.getOutputStream());
        is = sock.getInputStream();
        RemoteAddress = sock.getRemoteSocketAddress();
        init();
        listenThread = new Thread(this::Start);
        listenThread.start();
    }

    public void Send(byte[] buffer, int offset, int count) throws IOException {
        dos.write(buffer, offset, count);
    }

//    private void init() throws LineUnavailableException {
//        //  specifying the audio format
//        AudioFormat _format = new AudioFormat(8000.F,// Sample Rate
//                16,     // Size of SampleBits
//                1,      // Number of Channels
//                true,   // Is Signed?
//                false   // Is Big Endian?
//        );
//
//        //  creating the DataLine Info for the speaker format
//        DataLine.Info speakerInfo = new DataLine.Info(SourceDataLine.class, _format);
//
//        //  getting the mixer for the speaker
//        _speaker = (SourceDataLine) AudioSystem.getLine(speakerInfo);
//        _speaker.open(_format);
//    }

    public void Start() {
        try {

            _speaker.start();

            byte[] data = new byte[8000];
            System.out.println("Waiting for data...");
            while (_running) {

                //  checking if the data is available to speak
                if (is.available() <= 0)
                    continue;   //  data not available so continue back to start of loop

                //  count of the data bytes read
                int readCount = is.read(data, 0, data.length);

                if(readCount>0){
                    _speaker.write(data, 0, readCount);
                }
            }
            //honestly.... the control never reaches here.
            _speaker.drain();
            _speaker.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
