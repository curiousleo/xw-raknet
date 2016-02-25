/*
    This file is part of "xWorlds utilities".

    "xWorlds utilities" is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    "xWorlds utilities" is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with "nukkit xWorlds plugin". If not, see <http://www.gnu.org/licenses/>.

 */
package eu.xworlds.util.raknet.handler;

import java.util.Collection;

import eu.xworlds.util.raknet.RaknetMessageHandler;
import eu.xworlds.util.raknet.RaknetServer;
import eu.xworlds.util.raknet.RaknetSession;
import eu.xworlds.util.raknet.protocol.ConnectedPing;
import eu.xworlds.util.raknet.protocol.ConnectedPong;

/**
 * Handles incoming ping.
 * 
 * @author mepeisen
 */
public class ConnectedPingHandler implements RaknetMessageHandler<ConnectedPing>
{
 
    /** the raknet server */
    private final RaknetServer server;

    /**
     * Constructor
     * @param server the raknet server
     */
    public ConnectedPingHandler(RaknetServer server)
    {
        this.server = server;
    }

    @Override
    public void handle(ConnectedPing message, RaknetSession session, Collection<Object> out)
    {
        final ConnectedPong pong = new ConnectedPong(message.getReceiver(), message.getSender());
        pong.setPingTime(message.getTime());
        pong.setPongTime(this.server.getRaknetTime());
        session.send(pong);
    }
    
}
