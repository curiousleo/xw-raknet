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

import java.util.Map;

import eu.xworlds.util.raknet.protocol.RaknetMessage;
import eu.xworlds.util.raknet.protocol.TargetedMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
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
     * @param channel
     */
    void onBindSucceeded(Channel channel);

    /**
     * @param channel
     */
    void onBindFailed(Channel channel);

    /**
     * @param channel
     */
    void onChannelClosed(Channel channel);

    /**
     * @param raknetServer 
     * @return
     */
    ChannelHandler[] getHandlerPipeline(RaknetServer raknetServer);

    /**
     * @param ctx
     * @param msg
     * @return
     */
    boolean isBlocked(ChannelHandlerContext ctx, DatagramPacket msg);

    /**
     * @return
     */
    Map<Byte, Class<? extends RaknetMessage>> getMessageClasses();

    /**
     * @return
     */
    Map<Class<? extends TargetedMessage>, RaknetMessageHandler<? extends TargetedMessage>> getMessageHandlers();

    /**
     * @param value
     */
    void onRemoval(RaknetSession value);

    /**
     * @param session
     * @return
     */
    boolean onNewSession(RaknetSession session);
    
}
