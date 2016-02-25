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
 * Message "ConnectedPong".
 * 
 * <p><b>The following docu is taken from procotol information</b>:</p>
 * 
 * <p>
 * Pong from a connected system.  Update timestamps (internal use only)
 * </p>
 * 
 * @author mepeisen
 */
public class ConnectedPong extends TargetedMessage
{
    
    /** the raknet message id */
    public static final byte ID = 0x03;
    
    /** the ping time */
    private long pingTime;
    
    /** the pong time */
    private long pongTime;
    
    /**
     * Constructor for incoming message.
     * @param buf message data
     * @param sender message sender.
     * @param receiver message receiver.
     */
    public ConnectedPong(ByteBuf buf, InetSocketAddress sender, InetSocketAddress receiver)
    {
        super(buf, sender, receiver);
    }

    /**
     * Constructor for outgoing message.
     * @param sender message sender.
     * @param receiver message receiver.
     */
    public ConnectedPong(InetSocketAddress sender, InetSocketAddress receiver)
    {
        super(sender, receiver);
    }

    @Override
    public byte getId()
    {
        return ID;
    }
    
    /**
     * @return the pingTime
     */
    public long getPingTime()
    {
        return this.pingTime;
    }

    /**
     * @param pingTime the pingTime to set
     */
    public void setPingTime(long pingTime)
    {
        this.pingTime = pingTime;
    }

    /**
     * @return the pongTime
     */
    public long getPongTime()
    {
        return this.pongTime;
    }

    /**
     * @param pongTime the pongTime to set
     */
    public void setPongTime(long pongTime)
    {
        this.pongTime = pongTime;
    }

    @Override
    public ByteBuf encode()
    {
        final ByteBuf result = Unpooled.buffer(1 + SIZE_TIME + SIZE_TIME);
        result.order(ByteOrder.BIG_ENDIAN);
        result.writeByte(ID);
        writeTime(result, this.pingTime);
        writeTime(result, this.pongTime);
        return result;
    }
    
    @Override
    protected void parseMessage(ByteBuf buf)
    {
        this.pingTime = readTime(buf);
        this.pongTime = readTime(buf);
    }

    @Override
    public String toString()
    {
        return "ConnectedPong [pingTime=" + this.pingTime + ", pongTime=" + this.pongTime + "]";  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
    }
    
}
