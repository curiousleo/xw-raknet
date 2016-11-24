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

import eu.xworlds.util.raknet.protocol.RaknetMessage;

/**
 * @author mepeisen
 *
 */
class RaknetSessionImpl implements RaknetSession
{

    public RaknetSessionImpl(InetSocketCon key)
    {
        // TODO
        throw new UnsupportedOperationException("new");
    }

    @Override
    public void send(RaknetMessage msg) {
        // TODO
        throw new UnsupportedOperationException("send");
    }

    @Override
    public RaknetSessionPings getPings() {
        // TODO
        throw new UnsupportedOperationException("getPings");
    }

    @Override
    public ConnectionState getConnectionState() {
        // TODO
        throw new UnsupportedOperationException("getConnectionState");
    }

    @Override
    public ConnectMode getConnectMode() {
        // TODO
        throw new UnsupportedOperationException("getConnectMode");
    }
}
