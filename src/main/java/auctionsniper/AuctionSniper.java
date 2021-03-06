package auctionsniper;

/**
 * AuctionSniper contains our bidding policy.
 */
public class AuctionSniper implements AuctionEventListener {
    private final Auction auction;
    private final SniperListener listener;
    private SniperSnapshot snapshot;

    public AuctionSniper(Auction auction, SniperListener listener, String itemId) {
        this.auction = auction;
        this.listener = listener;
        this.snapshot = SniperSnapshot.joining(itemId);
    }

    @Override
    public void auctionClosed() {
        snapshot = snapshot.closed();
        notifyChange();
    }

    @Override
    public void currentPrice(int price, int increment, PriceSource priceSource) {
        switch (priceSource) {
            case FromSniper:
                snapshot = snapshot.winning(price);
                break;
            case FromOtherBidder:
                int bid = price + increment;
                auction.bid(bid);
                snapshot = snapshot.bidding(price, bid);
                break;
        }
        notifyChange();
    }

    private void notifyChange() {
        listener.sniperStateChanged(snapshot);
    }
}
