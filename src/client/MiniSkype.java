package client;

import java.io.IOException;

import static java.lang.System.exit;

public class MiniSkype {

    public static void main(String[] args) {

        try {
            new Client("localhost", 8000);
        } catch (IOException e) {
            System.err.println("error in connecting to server");
            exit(0);
        }

    }

}
