package chat.protocol;

import java.nio.charset.StandardCharsets;

/**
 * Utility class responsible for converting ChatMessage
 * objects to and from their wire-level representation.
 * <p>
 * The wire format is a delimiter-based UTF-8 encoded string:
 *
 * <pre>
 * TYPE|SENDER|TIMESTAMP|CONTENT
 * </pre>
 *
 * <p>
 * This codec is stateless, thread-safe, and does not rely
 * on external libraries.
 */
public final class MessageCodec {

    /**
     * Regular expression used to split incoming messages.
     */
    private static final String SPLIT_DELIMITER = "\\|";

    /**
     * Delimiter used when encoding messages.
     */
    private static final String JOIN_DELIMITER = "|";

    /**
     * Private constructor to prevent instantiation.
     */
    private MessageCodec() {}

    /**
     * Encodes a ChatMessage into a UTF-8 byte array suitable
     * for transmission over UDP.
     *
     * @param message the ChatMessage to encode
     * @return byte array containing the encoded message
     */
    public static byte[] encode(ChatMessage message) {
        String payload =
                message.getType() + JOIN_DELIMITER +
                message.getSender() + JOIN_DELIMITER +
                message.getTimestamp() + JOIN_DELIMITER +
                message.getContent();

        return payload.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Decodes a ChatMessage from a received UDP packet.
     *
     * @param data   the raw byte buffer
     * @param length the number of valid bytes in the buffer
     * @return decoded ChatMessage instance
     * @throws IllegalArgumentException if the message format is invalid
     */
    public static ChatMessage decode(byte[] data, int length) {
        String raw = new String(data, 0, length, StandardCharsets.UTF_8);
        String[] parts = raw.split(SPLIT_DELIMITER, 4);

        if (parts.length < 4) {
            throw new IllegalArgumentException("Malformed chat message");
        }

        MessageType type = MessageType.valueOf(parts[0]);
        String sender = parts[1];
        long timestamp = Long.parseLong(parts[2]);
        String content = parts[3];

        return new ChatMessage(type, sender, timestamp, content);
    }
}

