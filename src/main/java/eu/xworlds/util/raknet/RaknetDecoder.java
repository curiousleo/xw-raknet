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

import java.lang.reflect.Constructor;
import java.net.InetSocketAddress;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import eu.xworlds.util.raknet.protocol.RaknetMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;

/**
 * Pipeline for incoming server connections.
 * 
 * <p>
 * This handler translates incoming datagram packages to RaknetMessage objects.
 * </p>
 * 
 * @author mepeisen
 */
class RaknetDecoder extends MessageToMessageDecoder<DatagramPacket>
{
    
    /** the well known messages */
    private final Map<Byte, Constructor<? extends RaknetMessage>> messages = new HashMap<>();
    
    /**
     * The handler for incoming connections.
     * 
     * @param serverListeners
     *            the server listeners.
     */
    public RaknetDecoder(RaknetServerListener[] serverListeners)
    {
        this.registerMessages(this.getDefaultMessages());
        for (final RaknetServerListener listener : serverListeners)
        {
            final Map<Byte, Class<? extends RaknetMessage>> messageClasses = listener.getMessageClasses();
            this.registerMessages(messageClasses);
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket msg, List<Object> out) throws Exception
    {
        final ByteBuf buf = msg.content();
        buf.order(ByteOrder.BIG_ENDIAN);
        final byte id = buf.readByte();
        final Constructor<? extends RaknetMessage> ctor = this.messages.get(id);
        if (ctor == null)
        {
            final RaknetMessage result = new InvalidRaknetMessage(buf, msg.sender(), msg.recipient());
            out.add(result);
        }
        else
        {
            try
            {
                final RaknetMessage result = ctor.newInstance(buf, msg.sender(), msg.recipient());
                out.add(result);
            }
            catch (Exception ex)
            {
                final RaknetMessage result = new InvalidRaknetMessage(buf, msg.sender(), msg.recipient(), ex);
                out.add(result);
            }
        }
    }
    
    /**
     * Register message classes.
     * @param classMap the message classes.
     */
    private void registerMessages(Map<Byte, Class<? extends RaknetMessage>> classMap)
    {
        if (classMap != null)
        {
            for (final Map.Entry<Byte, Class<? extends RaknetMessage>> entry : classMap.entrySet())
            {
                try
                {
                    final Constructor<? extends RaknetMessage> ctor = entry.getValue().getConstructor(ByteBuf.class, InetSocketAddress.class, InetSocketAddress.class);
                    this.messages.put(entry.getKey(), ctor);
                }
                catch (NoSuchMethodException ex)
                {
                    RaknetServer.LOGGER.log(Level.WARNING, "Invalid raknet message", ex); //$NON-NLS-1$
                }
            }
        }
    }
    
    /**
     * Creates the default messages
     * @return class map for standard protocol
     */
    private Map<Byte, Class<? extends RaknetMessage>> getDefaultMessages()
    {
        final Map<Byte, Class<? extends RaknetMessage>> result = new HashMap<>();
        // TODO
        return result;
    }
    
}
