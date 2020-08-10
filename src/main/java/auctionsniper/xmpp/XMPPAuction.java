package auctionsniper.xmpp;

import auctionsniper.Auction;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;

/**
 * XMPPAuction is an Auction implementation based on the XMPP messaging infrastructure.
 */
public class XMPPAuction implements Auction {
    public static final String JOIN_COMMAND_FORMAT = "SOLVersion: 1.1; Event: JOIN;";
    public static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: PRICE; Price: %d;";

    private final Chat chat;

    public XMPPAuction(Chat chat) {
        this.chat = chat;
    }

    @Override
    public void bid(int amount) {
        sendMessage(String.format(BID_COMMAND_FORMAT, amount));
    }

    @Override
    public void join() {
        sendMessage(JOIN_COMMAND_FORMAT);
    }

    private void sendMessage(final String message) {
        try {
            chat.sendMessage(message);
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }
}