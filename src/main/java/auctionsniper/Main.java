package auctionsniper;

import auctionsniper.ui.MainWindow;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import javax.swing.*;

public class Main {
    private static final int ARG_HOSTNAME = 1;
    private static final int ARG_USERNAME = 2;
    private static final int ARG_PASSWORD = 3;
    private static final int ARG_ITEM_ID = 4;

    public static final String AUCTION_RESOURCE = "Auction";
    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    public static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;

    public static final String STATUS_JOINING = "Joining";
    public static final String STATUS_LOST = "Lost";
    public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";

    private MainWindow ui;

    public Main() throws Exception {
        startUserInterface();
    }

    private void startUserInterface() throws Exception {
        SwingUtilities.invokeAndWait(() -> ui = new MainWindow());
    }

    public static void main(String... args) throws Exception {
        Main main = new Main();

        XMPPConnection connection = connectTo(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]);
        Chat chat = connection.getChatManager().createChat(auctionId(args[ARG_ITEM_ID], connection),
                (ch, message) -> {
                    // nothing yet
                });
        chat.sendMessage(new Message());
    }

    private static XMPPConnection connectTo(String hostname, String username, String password) throws XMPPException {
        XMPPConnection connection = new XMPPConnection(hostname);
        connection.connect();
        connection.login(username, password, AUCTION_RESOURCE);

        return connection;
    }

    private static String auctionId(String itemId, XMPPConnection connection) {
        return String.format(AUCTION_ID_FORMAT, itemId, connection.getServiceName());
    }
}