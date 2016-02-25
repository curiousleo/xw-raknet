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
 * Message "UnconnectedPingOpenConnections".
 * 
 * <p><b>The following docu is taken from procotol information</b>:</p>
 * 
 * <p>
 * Ping from an unconnected system.  Only reply if we have open connections. Do not update timestamps. (internal use only)
 * </p>
 * 
 * @author mepeisen
 */
public class UnconnectedPingOpenConnections extends TargetedMessage
{
    
    /** the raknet message id */
    public static final byte ID = 0x02;
    
    /** the ping timestamp */
    private long time;
    
    /** the magic */
    private byte[] magic;
    
    /**
     * Constructor for incoming message.
     * @param buf message data
     * @param sender message sender.
     * @param receiver message receiver.
     */
    public UnconnectedPingOpenConnections(ByteBuf buf, InetSocketAddress sender, InetSocketAddress receiver)
    {
        super(buf, sender, receiver);
    }

    /**
     * Constructor for outgoing message.
     * @param sender message sender.
     * @param receiver message receiver.
     */
    public UnconnectedPingOpenConnections(InetSocketAddress sender, InetSocketAddress receiver)
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

        final ByteBuf result = Unpooled.buffer(1 + SIZE_TIME + this.magic.length);
        result.order(ByteOrder.BIG_ENDIAN);
        result.writeByte(ID);
        writeTime(result, this.time);
        result.writeBytes(this.magic);
        return result;
    }
    
    @Override
    protected void parseMessage(ByteBuf buf)
    {
        this.time = readTime(buf);
        this.magic = new byte[MAGIC_BYTES];
        buf.readBytes(this.magic.length);
    }

    @Override
    public String toString()
    {
        return "UnconnectedPingOpenConnections [time=" + this.time + ", magic=" + tohex(this.magic) + "]";   //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
    }
    
}
