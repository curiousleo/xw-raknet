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
package eu.xworlds.util.raknet;

import eu.xworlds.util.raknet.protocol.TargetedMessage;

/**
 * @author mepeisen
 *
 */
public interface RaknetSession
{

    /**
     * Sends given message to client.
     * @param msg message
     */
    void send(TargetedMessage msg);

    /**
     * Returns the ping support for raknet sessions.
     * @return raknet session pings
     */
    RaknetSessionPings getPings();
    
    /**
     * Returns current connection state
     * @return connection state.
     */
    ConnectionState getConnectionState();
    
    /**
     * Returns the current connect mode
     * @return connect mode.
     */
    ConnectMode getConnectMode();
    
}
