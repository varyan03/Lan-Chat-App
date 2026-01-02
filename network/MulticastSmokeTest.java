package chat.network;

import chat.protocol.ChatMessage;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Smoke test for validating UDP multicast networking
 * using Java assertions.
 *
 * This test verifies that a message sent by one
 * MulticastClient is received by another.
 */
public final class MulticastSmokeTest {

    public static void main(String[] args) throws Exception {

        MulticastClient receiver = new MulticastClient();
        MulticastClient sender = new MulticastClient();

        CountDownLatch latch = new CountDownLatch(1);

        receiver.start(message -> {
            if ("network-test".equals(message.getContent())) {
                latch.countDown();
            }
        });

        sender.send(ChatMessage.chat("tester", "network-test"));

        boolean received = latch.await(2, TimeUnit.SECONDS);

        sender.shutdown();
        receiver.shutdown();

        assert received : "Multicast message was not received";

        System.out.println("Multicast networking smoke test passed.");
    }
}

