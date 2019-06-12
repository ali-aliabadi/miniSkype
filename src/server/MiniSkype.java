package server;

import java.io.IOException;

public class MiniSkype {

    public static void main(String[] args) throws IOException {
        new Server(8888).startServer();
    }

}
