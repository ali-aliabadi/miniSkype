package client;

import org.bson.Document;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Test {

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("localhost", 8888);
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

        ArrayList<Double> aa = new ArrayList<>();
        aa.add(1.6);
        aa.add(-134.3452);
        aa.add(745.743345);

        Document doc = new Document();
        doc.put("type", "login");
        doc.put("int test", 7);
        doc.put("array", aa);

        String result = doc.toJson();
        dos.writeUTF(result);
        dos.flush();

    }

}
