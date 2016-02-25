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

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

/**
 * Interface to watch for raknet server events.
 * 
 * @author mepeisen
 */
public interface RaknetServerListener
{

    /**
     * Invoked if the bind address succeeded.
     * @param channel the channel that was bound.
     */
    void onBindSucceeded(Channel channel);

    /**
     * Invoked if the bind address failed.
     * @param channel the channel that failed.
     */
    void onBindFailed(Channel channel);

    /**
     * Invoked once a channel was closed
     * @param channel the channel that was closed.
     */
    void onChannelClosed(Channel channel);

    /**
     * Checks if the incoming network traffic is allowed.
     * 
     * <p>Implement this method to realize ip-address-blacklists.</p>
     * 
     * @param ctx the channel context.
     * @param msg the incoming network packet
     * @return {@code true} if the network packet should be processed or {@code false} if it should be blocked silently.
     */
    boolean isBlocked(ChannelHandlerContext ctx, DatagramPacket msg);

    /**
     * Invoked once a session was removed due to inactivity.
     * @param value the session that was removed.
     */
    void onRemoval(RaknetSession value);

    /**
     * Invoked once a new session was created.
     * @param session the session that was created.
     * @return {@code true} to allow the new session and {@code false} to block the session creation.
     */
    boolean onNewSession(RaknetSession session);
    
}
