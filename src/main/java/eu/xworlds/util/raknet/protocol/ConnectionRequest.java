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
 * Message "ConnectionRequest".
 * 
 * <p><b>The following docu is taken from procotol information</b>:</p>
 * 
 * <p>
 * C2S: Header(1), GUID(8), Timestamp, HasSecurity(1), Proof(32)
 * </p>
 * 
 * @author mepeisen
 */
public class ConnectionRequest extends TargetedMessage
{
    
    /** the raknet message id */
    public static final byte ID = 0x09;
    
    /** the client guid */
    private long clientGuid;
    
    /** the ping timestamp */
    private long time;
    
    /** true for security */
    private boolean doSecurity;
    
    /** the security proof (32 bytes) */
    private byte[] proof;
    
    /** true for identity */
    private boolean doIdentity;
    
    /** identity */
    private byte[] identity;
    
    /**
     * Constructor for incoming message.
     * @param buf message data
     * @param sender message sender.
     * @param receiver message receiver.
     */
    public ConnectionRequest(ByteBuf buf, InetSocketAddress sender, InetSocketAddress receiver)
    {
        super(buf, sender, receiver);
    }

    /**
     * Constructor for outgoing message.
     * @param sender message sender.
     * @param receiver message receiver.
     */
    public ConnectionRequest(InetSocketAddress sender, InetSocketAddress receiver)
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
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    protected void parseMessage(ByteBuf buf)
    {
        this.clientGuid = buf.readLong();
        this.time = buf.readLong();
        this.doSecurity = buf.readBoolean();
        if (this.doSecurity)
        {
            this.proof = new byte[32]; // TODO have a constant value of the size
            buf.readBytes(this.proof);
            this.doIdentity = buf.readBoolean();
            if (this.doIdentity)
            {
                this.identity = new byte[EASYHANDSHAKE_IDENTITY_BYTES];
                buf.readBytes(this.identity);
            }
        }
    }
    
}
