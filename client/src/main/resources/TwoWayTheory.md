In constructing a two-way communication system a few things have to be kept in mind.
1) Both aspects of communication (sending and receiving) must be asynchronous from both one another AND the connected party.
2) In a similar manner to a Discord call, each party must have an identical interface for connecting to a call, no matter the order of initiation.
3) In opposition to 2), one party MUST act as a server, and the other MUST act as a client. The alternative is full of bugs and issues.
4) Each endpoint ought be identical at compile time.

In the spirit of fulfilling these four conditions, I propose a model that avoids the multi-connection race conditions of the original style AS WELL as the inflexible single-modal alternative to the former.
This new model allows either end of communication to initially act as though it is the client in the client-server relationship, attempting to connect its own Socket to the other's ServerSocket.
If the connection drops, our side assumes that it must be the first of the two endpoints to initiate communication, and so transforms into server mode, establishing a ServerSocket and blocking until the other side provides a connection for it to accept.
On the other side, now, the process is identical and symmetrical (fulfilling 4)). We first query our other, asking for a connection with a Socket, and, if we are met with a dropped connection, we become a server ourselves.
Now, an example:
Peter and Kate each have a copy of Swiss, and want to start a TwoWayConnection. They have not established which one of them is going to initiate the connection, *since such information ought not be necessary.*
Peter happens to start the connection, and his Swiss attempts to connect to Kate's. However, since she has not yet hit the call button, there is no server to accept Peter, so the connection drops, and Peter's Swiss transforms into a server.
Now Kate hits that button, and her own Swiss attempts to connect to Peter's. This time, since Peter's Swiss is a server now, the connection goes through, and wallah! We have two-way communication via a single socket.
