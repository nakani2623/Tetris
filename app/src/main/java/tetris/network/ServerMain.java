package tetris.network;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ServerMain {
    static int playerCount = 0;
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket();
            server.bind(new InetSocketAddress(7777));
            while (true) {
                Socket conn = server.accept();
            
                if (playerCount < 2) {
                    welcome(conn);
                    playerCount++;
                } else if (playerCount >= 2) {
                    refuse(conn);
                }
            }


        } catch(IOException e) {
            System.out.println("Something went wrong while server trying to accept a connection");
        }

    }

    public static void welcome(Socket conn) throws IOException {
        OutputStreamWriter out = new OutputStreamWriter(
                conn.getOutputStream(),
                StandardCharsets.UTF_8
            );

            out.write("WELCOME!\n");
            out.flush();
        
    }

    public static void refuse(Socket socket) throws IOException {
        OutputStreamWriter out = new OutputStreamWriter(
                socket.getOutputStream(),
                StandardCharsets.UTF_8
            );

            out.write("ROOM FULL!\n");
            out.flush();
            socket.close();
    }
}
