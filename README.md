Basic order book management.

Definition of 'Order Book' (from Investopedia.com):
An electronic list of buy and sell orders for a specific security or financial instrument, organized by price level. The order book lists the number of shares being bid or offered at each price point.

In other words, each price level (for simplicity let's think of it as an integer value) can be either bid (there are people willing to buy at this price), ask (people are willing to sell at this price) or spread (nobody is willing to buy or sell at this price).

Generally, order book looks like this (B - bid, S - spread, A - ask), size defines how many shares can be bought/sold at this price:

PRICE|SIZE|TYPE
===============
  100| 50 |A
   99|  0 |A - size is zero, but it is still ask price, because it is higher then best ask
   98| 30 |A - best ask (lowest non-zero ask price)
   97|  0 |S
   96|  0 |S
   95| 40 |B - best bid (highest non-zero bid price)
   94| 30 |B
   93| 15 |B
   92| 77 |B
 

Best bid is always lower then best ask. (for this task it's not important why it is so, but actually it's because as soon as best bid and ask prices cross, orders get executed).

INPUT FILE:

There is a text file with incremental updates in the following format:
u,<price>,<size>,bid - set bid size at <price> to <size>
u,<price>,<size>,ask - set ask size at <price> to <size>


queries in the following format:
q,best_bid - print best bid price and size
q,best_ask - print best ask price and size
q,size,<price> - print size at specified price (bid, ask or spread).

and orders in the following format:
o,buy,<size> - removes <size> shares out of asks, most cheap ones.
o,sell,<size> - removes <size> shares out of bids, most expansive ones.
This orders are usually called "market orders". In case of a buy order this is similar to going to a marketplace (assuming that you want to buy <size> similar items there, and that all instances have identical quality, so price is the only factor) - you buy <size> units at the cheapest price available.

Queries, orders, and updates are in arbitrary order. Each line in the file is either one of three and ends with newline character.

Example of input file:
u,9,1,bid
u,11,5,ask
q,best_bid
u,10,2,bid
q,best_bid
o,sell,1
q,size,10
Example of output file (for this input file):
9,1
10,2
1

It is possible to run application in two modes: interactive console (used by default) or reading data from file (useful for testing).
After the execution check created result file and log file.

NOTE: to run application in reading file mode add such arguments to command line: "-f <absolute path to file with orders>"
File content should be in the format described above

