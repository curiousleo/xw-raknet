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
package eu.xworlds.util.raknet.protocol;

import java.net.InetSocketAddress;
import java.nio.ByteOrder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Message "DetectLostConnections".
 * 
 * <p><b>The following docu is taken from procotol information</b>:</p>
 * 
 * <p>
 * A reliable packet to detect lost connections (internal use only)
 * </p>
 * 
 * @author mepeisen
 */
public class DetectLostConnections extends TargetedMessage
{
    
    /** the raknet message id */
    public static final byte ID = 0x04;
    
    /**
     * Constructor for incoming message.
     * @param buf message data
     * @param sender message sender.
     * @param receiver message receiver.
     */
    public DetectLostConnections(ByteBuf buf, InetSocketAddress sender, InetSocketAddress receiver)
    {
        super(buf, sender, receiver);
    }

    /**
     * Constructor for outgoing message.
     * @param sender message sender.
     * @param receiver message receiver.
     */
    public DetectLostConnections(InetSocketAddress sender, InetSocketAddress receiver)
    {
        super(sender, receiver);
    }

    @Override
    public byte getId()
    {
        return ID;
    }
    
    @Override
    public ByteBuf encode()
    {
        final ByteBuf buf = Unpooled.buffer(1);
        buf.order(ByteOrder.BIG_ENDIAN);
        buf.writeByte(ID);
        return buf;
    }
    
    @Override
    protected void parseMessage(ByteBuf buf)
    {
        // no additional data
    }

    @Override
    public String toString()
    {
        return "DetectLostConnections []"; //$NON-NLS-1$
    }
    
}
