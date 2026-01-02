package chat.app;

import chat.cli.ChatCLI;
import chat.network.MulticastClient;
import chat.protocol.ChatMessage;

/**
 * Entry point for the LAN Chat Application.
 * <p>
 * Initializes networking, CLI, and manages
 * application lifecycle.
 */
public final class ChatApplication {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java chat.app.ChatApplication <username>");
            return;
        }

        String username = args[0];

        try {
            MulticastClient client = new MulticastClient();

            // Start listening for incoming messages
            client.start(message -> {

    // Ignore messages sent by this client
    if (message.getSender().equals(username)) {
        return;
    }

   	switch (message.getType()) {
        	case CHAT:
            		System.out.println(
                	"[" + message.getSender() + "] " + message.getContent()
           		 );
           	 	break;

        	case JOIN:
            		System.out.println(
                	"*** " + message.getSender() + " joined the chat ***"
            		);
            		break;

        	case LEAVE:
            		System.out.println(
                	"*** " + message.getSender() + " left the chat ***"
            		);
            		break;
    		}
	});


            // Send JOIN message
            client.send(ChatMessage.join(username));

            ChatCLI cli = new ChatCLI(username, client);
            Thread cliThread = new Thread(cli, "chat-cli");
            cliThread.start();

            // Shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    client.send(ChatMessage.leave(username));
                    cli.shutdown();
                    client.shutdown();
                } catch (Exception ignored) {}
            }));

            cliThread.join();

        } catch (Exception e) {
            System.err.println("Application error: " + e.getMessage());
        }
    }
}
 
