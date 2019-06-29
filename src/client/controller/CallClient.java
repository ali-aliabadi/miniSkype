package client.controller;

import javax.sound.sampled.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class CallClient {

    public static int PORT = 9000;

    SourceDataLine _speaker;
    TargetDataLine _mic;
    InputStream _streamIn;
    DataOutputStream _streamOut;
    Socket _server;
    String _serverName = "127.0.0.1";
    boolean _running = true;

    public CallClient(String serverName) throws IOException, LineUnavailableException {
        this._serverName = serverName;
        init();
    }

    private void init() throws LineUnavailableException{
        //  specifying the audio format
        AudioFormat _format = new AudioFormat(8000.F,// Sample Rate
                16,     // Size of SampleBits
                1,      // Number of Channels
                true,   // Is Signed?
                false   // Is Big Endian?
        );

        //  creating the DataLine Info for the speaker format
        DataLine.Info speakerInfo = new DataLine.Info(SourceDataLine.class, _format);

        //  getting the mixer for the speaker
        _speaker = (SourceDataLine) AudioSystem.getLine(speakerInfo);
        _speaker.open(_format);
    }

    public void Start() {
        try {
            System.out.println("Connecting to server @" + _serverName + ":" + PORT);

            //  creating the socket and connect to the server
            _server = new Socket(_serverName, PORT);
            System.out.println("Connected to: " + _server.getRemoteSocketAddress());

            //  gettting the server stream
            _streamIn = _server.getInputStream();
            _streamOut = new DataOutputStream(_server.getOutputStream());

            _speaker.start();

            byte[] data = new byte[8000];
            System.out.println("Waiting for data...");
            while (_running) {

                //  checking if the data is available to speak
                if (_streamIn.available() <= 0)
                    continue;   //  data not available so continue back to start of loop

                //  count of the data bytes read
                int readCount = _streamIn.read(data, 0, data.length);

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

    public void Send(byte[] buffer, int offset, int count) throws IOException {
        _streamOut.write(buffer, offset, count);
    }

    private void initMic() throws LineUnavailableException {
        //  specifying the audio format
        AudioFormat _format = new AudioFormat(8000.F,// Sample Rate
                16,     // Size of SampleBits
                1,      // Number of Channels
                true,   // Is Signed?
                false   // Is Big Endian?
        );

        //  getting the source line i.e, mic
        _mic = AudioSystem.getTargetDataLine(_format);

        //  causes the line to acquire any required system resources and become operational
        _mic.open(_format);
    }

    public void Run() {
        try {
            System.out.println("Mic open.");
            byte _buffer[] = new byte[(int) (_mic.getFormat().getSampleRate() * 0.4)];
            _mic.start();
            while (_running) {
                // returns the length of data copied in buffer
                int count = _mic.read(_buffer, 0, _buffer.length);

                //if data is available
                if (count > 0) {
                    Send(_buffer, 0, count);
                }
            }
            //  honestly.... program never reaches here
            //  drain() causes the mixer's remaining data to get delivered to the target data line's buffer
            _mic.drain();
            _mic.close();
        } catch (Exception e) {
            System.out.println("Error!!!");
            e.printStackTrace();
        }
    }
}
