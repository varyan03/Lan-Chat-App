package chat.protocol;

import java.time.Instant;
import java.util.Objects;

/**
 * Immutable data transfer object representing a single
 * chat message exchanged over the network.
 * <p>
 * A ChatMessage contains all metadata required for
 * interpretation and display:
 * <ul>
 *   <li>Message type</li>
 *   <li>Sender identity</li>
 *   <li>Creation timestamp</li>
 *   <li>Optional textual content</li>
 * </ul>
 *
 * <p>
 * This class is thread-safe and suitable for concurrent use.
 */
public final class ChatMessage {

    private final MessageType type;
    private final String sender;
    private final long timestamp;
    private final String content;

    /**
     * Constructs a new ChatMessage instance.
     *
     * @param type      the type of the message (JOIN, CHAT, LEAVE)
     * @param sender    the username of the sender
     * @param timestamp the creation time in epoch milliseconds
     * @param content   the textual content of the message
     *                  (may be empty but never {@code null})
     * @throws NullPointerException if type or sender is {@code null}
     */
    public ChatMessage(MessageType type, String sender, long timestamp, String content) {
        this.type = Objects.requireNonNull(type, "type must not be null");
        this.sender = Objects.requireNonNull(sender, "sender must not be null");
        this.timestamp = timestamp;
        this.content = content == null ? "" : content;
    }

    /**
     * Returns the semantic type of this message.
     *
     * @return the message type
     */
    public MessageType getType() {
        return type;
    }

    /**
     * Returns the sender's username.
     *
     * @return the sender identifier
     */
    public String getSender() {
        return sender;
    }

    /**
     * Returns the creation timestamp of this message.
     *
     * @return timestamp in epoch milliseconds
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Returns the textual content of this message.
     *
     * @return message content (never {@code null})
     */
    public String getContent() {
        return content;
    }

    /**
     * Creates a JOIN message for the given sender.
     *
     * @param sender the username joining the chat
     * @return a new JOIN ChatMessage
     */
    public static ChatMessage join(String sender) {
        return new ChatMessage(
                MessageType.JOIN,
                sender,
                Instant.now().toEpochMilli(),
                ""
        );
    }

    /**
     * Creates a LEAVE message for the given sender.
     *
     * @param sender the username leaving the chat
     * @return a new LEAVE ChatMessage
     */
    public static ChatMessage leave(String sender) {
        return new ChatMessage(
                MessageType.LEAVE,
                sender,
                Instant.now().toEpochMilli(),
                ""
        );
    }

    /**
     * Creates a CHAT message with textual content.
     *
     * @param sender  the username sending the message
     * @param content the chat message content
     * @return a new CHAT ChatMessage
     */
    public static ChatMessage chat(String sender, String content) {
        return new ChatMessage(
                MessageType.CHAT,
                sender,
                Instant.now().toEpochMilli(),
                content
        );
    }
}

