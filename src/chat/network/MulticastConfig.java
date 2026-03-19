package chat.network;

/**
 * Holds configuration constants for the multicast chat network.
 * <p>
 * Centralizing configuration avoids magic numbers and makes
 * the networking layer easier to tune and maintain.
 */
public final class MulticastConfig {

    /**
     * Multicast group address (must be in the range 224.0.0.0 – 239.255.255.255).
     */
    public static final String MULTICAST_ADDRESS = "230.0.0.1";

    /**
     * UDP port used for multicast communication.
     */
    public static final int PORT = 4446;

    /**
     * Time-to-live for multicast packets.
     * Restricts packets to the local network.
     */
    public static final int TTL = 1;

    private MulticastConfig() {
        // Prevent instantiation
    }
}

