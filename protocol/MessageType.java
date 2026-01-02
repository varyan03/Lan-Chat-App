package chat.protocol;

/**
 * Represents the semantic type of a chat message.
 * <p>
 * MessageType is used to distinguish between different
 * kinds of messages exchanged in the LAN chat system.
 * This enables lifecycle events (JOIN / LEAVE) and
 * extensibility for future message categories.
 */
public enum MessageType {

    /**
     * Indicates that a user has joined the chat session.
     */
    JOIN,

    /**
     * Represents a standard chat message sent by a user.
     */
    CHAT,

    /**
     * Indicates that a user has left the chat session.
     */
    LEAVE
}

