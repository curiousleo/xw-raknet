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
 * Message "ConnectedPing".
 * 
 * <p><b>The following docu is taken from procotol information</b>:</p>
 * 
 * <p>
 * Ping from a connected system.  Update timestamps (internal use only)
 * </p>
 * 
 * @author mepeisen
 */
public class ConnectedPing extends TargetedMessage
{
    
    /** the raknet message id */
    public static final byte ID = 0x00;
    
    /** the ping time code */
    private long time;
    
    /**
     * Constructor for incoming message.
     * @param buf message data
     * @param sender message sender.
     * @param receiver message receiver.
     */
    public ConnectedPing(ByteBuf buf, InetSocketAddress sender, InetSocketAddress receiver)
    {
        super(buf, sender, receiver);
    }

    /**
     * Constructor for outgoing message.
     * @param sender message sender.
     * @param receiver message receiver.
     */
    public ConnectedPing(InetSocketAddress sender, InetSocketAddress receiver)
    {
        super(sender, receiver);
    }

    /**
     * @return the time
     */
    public long getTime()
    {
        return this.time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(long time)
    {
        this.time = time;
    }

    @Override
    public byte getId()
    {
        return ID;
    }
    
    @Override
    public ByteBuf encode()
    {
        final ByteBuf result = Unpooled.buffer(1 + 8);
        result.order(ByteOrder.BIG_ENDIAN);
        result.writeByte(ID);
        result.writeLong(this.time);
        return result;
    }
    
    @Override
    protected void parseMessage(ByteBuf buf)
    {
        this.time = buf.readLong();
    }

    @Override
    public String toString()
    {
        return "ConnectedPing [time=" + this.time + "]";  //$NON-NLS-1$//$NON-NLS-2$
    }
    
}
