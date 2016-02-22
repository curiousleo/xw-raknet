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

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

/**
 * Pipeline for incoming server connections.
 * 
 * <p>
 * This handler asks the server listeners if the incoming traffic should be blocked. This can be used to establish some kind of spam protection.
 * </p>
 * 
 * @author mepeisen
 */
class ConnectionHandler extends SimpleChannelInboundHandler<DatagramPacket>
{
    
    /** the raknet server listeners. */
    private RaknetServerListener[] listeners;
    
    /**
     * The handler for incoming connections.
     * 
     * @param serverListeners
     *            the server listeners.
     */
    public ConnectionHandler(RaknetServerListener[] serverListeners)
    {
        this.listeners = serverListeners;
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception
    {
        for (final RaknetServerListener listener : this.listeners)
        {
            if (listener.isBlocked(ctx, msg))
            {
                ctx.fireChannelReadComplete();
            }
            else
            {
                ctx.fireChannelRead(msg);
            }
        }
    }
    
}
