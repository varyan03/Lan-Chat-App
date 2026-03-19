package chat.cli;

import chat.network.MulticastClient;
import chat.protocol.ChatMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Command-line interface for the LAN chat application.
 * <p>
 * Responsible for reading user input and sending chat
 * messages through the multicast client.
 */
public final class ChatCLI implements Runnable {

    private final String username;
    private final MulticastClient client;
    private volatile boolean running = true;

    /**
     * Constructs a ChatCLI instance.
     *
     * @param username the username of the local client.
     * @param client   the multicast client used to broadcast messages.
     */
    public ChatCLI(String username, MulticastClient client) {
        this.username = username;
        this.client = client;
    }

    /**
     * Continuously reads user input from standard input and sends it
     * as chat messages via the network client until stopped.
     * <p>
     * Special commands:
     * - {@code /exit}: stops the CLI and triggers application shutdown.
     */
    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {

            while (running) {
                String line = reader.readLine();

                if (line == null) {
                    continue;
                }

                if ("/exit".equalsIgnoreCase(line.trim())) {
                    shutdown();
                    break;
                }

                client.send(ChatMessage.chat(username, line));
            }

        } catch (IOException e) {
            System.err.println("CLI error: " + e.getMessage());
        }
    }

    /**
     * Stops the CLI loop.
     */
    public void shutdown() {
        running = false;
    }
}
