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

import eu.xworlds.util.raknet.protocol.InvalidRaknetMessage;
import eu.xworlds.util.raknet.protocol.RaknetMessage;
import eu.xworlds.util.raknet.protocol.TargetedMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Pipeline to log every incoming data package
 * 
 * @author mepeisen
 */
class RaknetTrace extends SimpleChannelInboundHandler<TargetedMessage>
{

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TargetedMessage msg) throws Exception
    {
        RaknetMessage inner = msg.inner();
        if (inner instanceof InvalidRaknetMessage)
        {
            final InvalidRaknetMessage inv = (InvalidRaknetMessage) inner;
            //noinspection ThrowableResultOfMethodCallIgnored
            RaknetServer.LOGGER.finest("invalid/unknown raknet code from from " + msg.sender() + " to " + msg.receiver() + ": " + inv);
        }
        else if (inner instanceof TargetedMessage)
        {
            RaknetServer.LOGGER.finest("incoming data from " + msg.sender() + " to " + msg.receiver() + ": " + inner);   //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
        }
        else
        {
            RaknetServer.LOGGER.finest("Incoming data package " + msg); //$NON-NLS-1$
        }
        ctx.fireChannelRead(msg);
    }
    
}
