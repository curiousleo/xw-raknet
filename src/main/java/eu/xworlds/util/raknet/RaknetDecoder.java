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

import eu.xworlds.util.raknet.protocol.ConnectedPing;
import eu.xworlds.util.raknet.protocol.ConnectedPong;
import eu.xworlds.util.raknet.protocol.ConnectionRequest;
import eu.xworlds.util.raknet.protocol.DetectLostConnections;
import eu.xworlds.util.raknet.protocol.InvalidRaknetMessage;
import eu.xworlds.util.raknet.protocol.OpenConnectionReply1;
import eu.xworlds.util.raknet.protocol.OpenConnectionReply2;
import eu.xworlds.util.raknet.protocol.OpenConnectionRequest1;
import eu.xworlds.util.raknet.protocol.OpenConnectionRequest2;
import eu.xworlds.util.raknet.protocol.OurSystemRequiresSecurity;
import eu.xworlds.util.raknet.protocol.OutOfBandInternal;
import eu.xworlds.util.raknet.protocol.PublicKeyMismatch;
import eu.xworlds.util.raknet.protocol.RaknetMessage;
import eu.xworlds.util.raknet.protocol.RemoteSystemRequiresPublicKey;
import eu.xworlds.util.raknet.protocol.SndReceiptAcked;
import eu.xworlds.util.raknet.protocol.SndReceiptLoss;
import eu.xworlds.util.raknet.protocol.UnconnectedPing;
import eu.xworlds.util.raknet.protocol.UnconnectedPingOpenConnections;
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
        final Constructor<? extends RaknetMessage> ctor = this.messages.get(Byte.valueOf(id));
        if (ctor == null)
        {
            final RaknetMessage result = new InvalidRaknetMessage(id, buf, msg.sender(), msg.recipient());
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
                final RaknetMessage result = new InvalidRaknetMessage(id, buf, msg.sender(), msg.recipient(), ex);
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
        result.put(Byte.valueOf(ConnectedPing.ID), ConnectedPing.class);
        result.put(Byte.valueOf(ConnectedPong.ID), ConnectedPong.class);
        result.put(Byte.valueOf(ConnectionRequest.ID), ConnectionRequest.class);
        result.put(Byte.valueOf(DetectLostConnections.ID), DetectLostConnections.class);
        result.put(Byte.valueOf(OpenConnectionReply1.ID), OpenConnectionReply1.class);
        result.put(Byte.valueOf(OpenConnectionReply2.ID), OpenConnectionReply2.class);
        result.put(Byte.valueOf(OpenConnectionRequest1.ID), OpenConnectionRequest1.class);
        result.put(Byte.valueOf(OpenConnectionRequest2.ID), OpenConnectionRequest2.class);
        result.put(Byte.valueOf(OurSystemRequiresSecurity.ID), OurSystemRequiresSecurity.class);
        result.put(Byte.valueOf(OutOfBandInternal.ID), OutOfBandInternal.class);
        result.put(Byte.valueOf(PublicKeyMismatch.ID), PublicKeyMismatch.class);
        result.put(Byte.valueOf(RemoteSystemRequiresPublicKey.ID), RemoteSystemRequiresPublicKey.class);
        result.put(Byte.valueOf(SndReceiptAcked.ID), SndReceiptAcked.class);
        result.put(Byte.valueOf(SndReceiptLoss.ID), SndReceiptLoss.class);
        result.put(Byte.valueOf(UnconnectedPing.ID), SndReceiptAcked.class);
        result.put(Byte.valueOf(UnconnectedPingOpenConnections.ID), UnconnectedPingOpenConnections.class);
        return result;
    }
    
}
