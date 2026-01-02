package chat.network;

import chat.protocol.ChatMessage;
import chat.protocol.MessageCodec;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Handles UDP multicast communication for the LAN chat application.
 * <p>
 * A MulticastClient can send messages to and receive messages from
 * a multicast group. Receiving is handled on a dedicated thread.
 */
public class MulticastClient {

    private final InetAddress group;
    private final int port;
    private final MulticastSocket socket;
    private final AtomicBoolean running = new AtomicBoolean(false);

    /**
     * Creates and initializes a multicast client.
     *
     * @throws IOException if the socket cannot be created or the group cannot be joined
     */
    public MulticastClient() throws IOException {
        this.group = InetAddress.getByName(MulticastConfig.MULTICAST_ADDRESS);
        this.port = MulticastConfig.PORT;
        this.socket = new MulticastSocket(port);

        socket.setTimeToLive(MulticastConfig.TTL);
        socket.joinGroup(group);
    }

    /**
     * Starts listening for incoming multicast messages.
     *
     * @param handler callback invoked for every received ChatMessage
     */
    public void start(MessageHandler handler) {
        running.set(true);

        Thread listenerThread = new Thread(() -> listen(handler), "multicast-listener");
        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    /**
     * Continuously listens for incoming UDP packets and dispatches
     * decoded messages to the handler.
     */
    private void listen(MessageHandler handler) {
        byte[] buffer = new byte[4096];

        while (running.get()) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                ChatMessage message =
                        MessageCodec.decode(packet.getData(), packet.getLength());

                handler.onMessage(message);

            } catch (IOException | IllegalArgumentException e) {
                if (running.get()) {
                    System.err.println("Network receive error: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Sends a ChatMessage to the multicast group.
     *
     * @param message the message to send
     * @throws IOException if sending fails
     */
    public void send(ChatMessage message) throws IOException {
        byte[] data = MessageCodec.encode(message);
        DatagramPacket packet =
                new DatagramPacket(data, data.length, group, port);

        socket.send(packet);
    }

    /**
     * Stops the multicast client and releases all resources.
     */
    public void shutdown() {
        running.set(false);

        try {
            socket.leaveGroup(group);
        } catch (IOException ignored) {
        }

        socket.close();
    }

    /**
     * Callback interface for handling received chat messages.
     */
    @FunctionalInterface
    public interface MessageHandler {

        /**
         * Invoked when a ChatMessage is received from the network.
         *
         * @param message the received chat message
         */
        void onMessage(ChatMessage message);
    }
}

