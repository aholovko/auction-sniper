package auctionsniper.ui;

import auctionsniper.SniperSnapshot;

import javax.swing.table.AbstractTableModel;

import static auctionsniper.Main.STATUS_JOINING;

/**
 * SnipersTableModel accepts updates from the Sniper and provides
 * a representation of those values to a JTable.
 */
public class SnipersTableModel extends AbstractTableModel {
    private final static SniperSnapshot STARTING_UP = new SniperSnapshot("", 0, 0);

    private String statusText = STATUS_JOINING;
    private SniperSnapshot sniperSnapshot = STARTING_UP;

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount() {
        return Column.values().length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        // we're not keen on using switch, as it's not object-oriented,
        // so we'll keep an eye on this
        switch (Column.at(columnIndex)) {
            case ITEM_IDENTIFIER:
                return sniperSnapshot.itemId;
            case LAST_PRICE:
                return sniperSnapshot.lastPrice;
            case LAST_BID:
                return sniperSnapshot.lastBid;
            case SNIPER_STATE:
                return statusText;
            default:
                throw new IllegalArgumentException("No column at " + columnIndex);
        }
    }

    public void setStatusText(String newStatusText) {
        statusText = newStatusText;
        fireTableRowsUpdated(0, 0);
    }

    public void sniperStatusChanged(SniperSnapshot newSniperSnapshot, String newStatusText) {
        sniperSnapshot = newSniperSnapshot;
        statusText = newStatusText;
        fireTableRowsUpdated(0, 0);
    }
}
