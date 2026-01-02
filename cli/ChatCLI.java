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

    public ChatCLI(String username, MulticastClient client) {
        this.username = username;
        this.client = client;
    }

    @Override
    public void run() {
        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(System.in))) {

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

