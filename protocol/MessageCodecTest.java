package chat.protocol;

/**
 * Manual test harness for validating the MessageCodec
 * and ChatMessage round-trip behavior.
 */
public final class MessageCodecTest {

    public static void main(String[] args) {
        testJoinMessage();
        testChatMessage();
        testLeaveMessage();

        System.out.println("All protocol tests passed successfully.");
    }

    private static void testJoinMessage() {
        ChatMessage original = ChatMessage.join("alice");

        ChatMessage decoded = roundTrip(original);

        assertEquals(original.getType(), decoded.getType(), "JOIN type");
        assertEquals(original.getSender(), decoded.getSender(), "JOIN sender");
    }

    private static void testChatMessage() {
        ChatMessage original = ChatMessage.chat("bob", "Hello LAN chat");

        ChatMessage decoded = roundTrip(original);

        assertEquals(original.getType(), decoded.getType(), "CHAT type");
        assertEquals(original.getSender(), decoded.getSender(), "CHAT sender");
        assertEquals(original.getContent(), decoded.getContent(), "CHAT content");
    }

    private static void testLeaveMessage() {
        ChatMessage original = ChatMessage.leave("carol");

        ChatMessage decoded = roundTrip(original);

        assertEquals(original.getType(), decoded.getType(), "LEAVE type");
        assertEquals(original.getSender(), decoded.getSender(), "LEAVE sender");
    }

    private static ChatMessage roundTrip(ChatMessage message) {
        byte[] encoded = MessageCodec.encode(message);
        return MessageCodec.decode(encoded, encoded.length);
    }

    private static void assertEquals(Object expected, Object actual, String label) {
        if (!expected.equals(actual)) {
            throw new AssertionError(
                    label + " mismatch: expected=" + expected + ", actual=" + actual
            );
        }
    }
}

