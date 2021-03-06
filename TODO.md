# Auction Sniper

The initial plan for Auction Sniper.

We start with a "walking skeleton" that will cut the minimum path through Swing, XMPP, and our application.  
Each subsequent step adds a single element of complexity to the existing application.

Sequence of features to build:
- put together the core infrastructure;
- add bidding to the basic connectivity;
- distinguish who sent the winning bid;
- start to fill out the user interface;
- support bidding for multiple items;
- implement input via the user interface;
- add more intelligence in the Sniper algorithm.

### Backlog

- [ ] Stop bidding at stop price  
- [ ] Translator - invalid message from Auction  
- [ ] Translator - incorrect message version  
- [ ] Auction - handle XMPPException on send  

### In Progress

- [ ] Add new items through the GUI  

### Done

- [x] Single item - join, lose without bidding  
- [x] Single item - join, bid and lose  
- [x] Single item - join, bid and win  
- [x] Single item - show price details  
- [x] Multiple items  
