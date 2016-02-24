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

import io.netty.buffer.ByteBuf;

/**
 * Some invalid raknet message.
 * 
 * @author mepeisen
 */
public class InvalidRaknetMessage extends TargetedMessage
{
    
    /** the first 1500 bytes */
    private byte[] payload;
    
    /** the raknet message id */
    private byte id;
    
    /** the exception during encoding */
    private Exception ex;
    
    /**
     * Constructor for incoming message.
     * @param id the message id
     * @param buf message data
     * @param sender message sender.
     * @param receiver message receiver.
     */
    public InvalidRaknetMessage(byte id, ByteBuf buf, InetSocketAddress sender, InetSocketAddress receiver)
    {
        super(buf, sender, receiver);
        this.id = id;
    }
    
    /**
     * Constructor for incoming message.
     * @param id the message id
     * @param buf message data
     * @param sender message sender.
     * @param receiver message receiver.
     * @param ex exception
     */
    public InvalidRaknetMessage(byte id, ByteBuf buf, InetSocketAddress sender, InetSocketAddress receiver, Exception ex)
    {
        super(buf, sender, receiver);
        this.ex = ex;
        this.id = id;
    }

    @Override
    public byte getId()
    {
        return this.id;
    }
    
    /**
     * @return the payload
     */
    public byte[] getPayload()
    {
        return this.payload;
    }

    /**
     * @return the ex
     */
    public Exception getEx()
    {
        return this.ex;
    }

    @Override
    public ByteBuf encode()
    {
        throw new IllegalStateException("Not allowed"); //$NON-NLS-1$
    }
    
    @Override
    protected void parseMessage(ByteBuf buf)
    {
        this.payload = buf.readBytes(Math.min(1500, buf.readableBytes())).array();
    }

    @Override
    public String toString()
    {
        return "InvalidRaknetMessage [payload=" + tohex(this.payload) + ", id=" + this.id + ", ex=" + this.ex + "]";  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    }
    
}
