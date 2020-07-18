package auctionsniper;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import java.util.concurrent.ArrayBlockingQueue;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertNotNull;

/**
 * FakeAuctionServer is a substitute server that allows the test to check how the Auction Sniper
 * interacts with an auction using XMPP messages.
 *
 * It has three responsibilities:
 *     1) connect to the XMPP broker and accept a request to join the chat from the Sniper;
 *     2) receive chat messages from the Sniper or fail if no message arrives within some timeout;
 *     3) allow the test to send messages back to the Sniper.
 */
public class FakeAuctionServer {
    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    public static final String AUCTION_RESOURCE = "Auction";
    public static final String XMPP_HOSTNAME = "localhost";
    private static final String AUCTION_PASSWORD = "auction";

    private final String itemId;
    private final XMPPConnection connection;
    private Chat currentChat;
    private final SingleMessageListener messageListener = new SingleMessageListener();

    public FakeAuctionServer(String itemId) {
        this.itemId = itemId;
        this.connection = new XMPPConnection(XMPP_HOSTNAME);
    }

    /**
     * startSellingItem connects to the XMPP broker, using the item identifier to construct the login name;
     * then it registers a ChatManagerListener. Smack will call this listener with a Chat object that represents
     * the session when a Sniper connects in.
     *
     * @throws XMPPException
     */
    public void startSellingItem() throws XMPPException {
        connection.connect();
        connection.login(format(ITEM_ID_AS_LOGIN, itemId), AUCTION_PASSWORD, AUCTION_RESOURCE);
        connection.getChatManager().addChatListener(
                (chat, createdLocally) -> {
                    currentChat = chat;
                    chat.addMessageListener(messageListener);
                });
    }

    public String getItemId() {
        return itemId;
    }

    public void hasReceivedJoinRequestFromSniper() throws InterruptedException {
        messageListener.receivesAMessage();
    }

    public void announceClosed() throws XMPPException {
        currentChat.sendMessage(new Message());
    }

    public void stop() {
        connection.disconnect();
    }

    /**
     * SingleMessageListener waits for messages from the Sniper to arrive
     * and time out if they don't.
     */
    public class SingleMessageListener implements MessageListener {
        private final ArrayBlockingQueue<Message> messages = new ArrayBlockingQueue<>(1);

        public void processMessage(Chat chat, Message message) {
            messages.add(message);
        }

        public void receivesAMessage() throws InterruptedException {
            assertNotNull(messages.poll(5, SECONDS));
        }
    }
}
