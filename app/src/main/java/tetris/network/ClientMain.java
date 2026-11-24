package tetris.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientMain {
    public static void main(String[] args) {
        Socket socket = new Socket();

        try {
            socket.connect(
                new InetSocketAddress("127.0.0.1", 7777),
                3000
            );

            BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    socket.getInputStream(),
                    StandardCharsets.UTF_8
                )
            );

            String content = in.readLine();

            System.out.println("Server said: " + content);
        } catch (Exception e) {
            // TODO: handle exception
        }

    }
}
