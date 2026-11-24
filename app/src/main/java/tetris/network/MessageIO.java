package tetris.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ProtocolException;
import java.nio.charset.StandardCharsets;

public final class MessageIO {
    private static final int MAX_MESSAGE_BYTES = 64 * 1024;

    private final DataInputStream in;
    private final DataOutputStream out;

    public MessageIO(DataInputStream in, DataOutputStream out) {
        this.in = in;
        this.out = out;
    }

    public synchronized void send(String message) throws IOException {
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);

        if (bytes.length > MAX_MESSAGE_BYTES) {
            throw new ProtocolException("Message is too large");
        }

        out.writeInt(bytes.length);
        out.write(bytes);
        out.flush();
    }

    public String receive() throws IOException {
        int length = in.readInt();

        if (length < 0 || length > MAX_MESSAGE_BYTES) {
            throw new ProtocolException(
                "Invalid message length: " + length
            );
        }

        byte[] bytes = new byte[length];
        in.readFully(bytes);

        return new String(bytes, StandardCharsets.UTF_8);
    }
}