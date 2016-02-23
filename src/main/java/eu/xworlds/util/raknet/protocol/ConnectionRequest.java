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
    
    /**
     * @return the clientGuid
     */
    public long getClientGuid()
    {
        return this.clientGuid;
    }

    /**
     * @param clientGuid the clientGuid to set
     */
    public void setClientGuid(long clientGuid)
    {
        this.clientGuid = clientGuid;
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

    /**
     * @return the doSecurity
     */
    public boolean isDoSecurity()
    {
        return this.doSecurity;
    }

    /**
     * @return the proof
     */
    public byte[] getProof()
    {
        return this.proof;
    }

    /**
     * @return the doIdentity
     */
    public boolean isDoIdentity()
    {
        return this.doIdentity;
    }

    /**
     * @return the identity
     */
    public byte[] getIdentity()
    {
        return this.identity;
    }
    
    /**
     * Sets doSecurity to false
     */
    public void setNoSecurity()
    {
        this.doSecurity = false;
    }
    
    /**
     * Sets security to true with given proof
     * @param proof security proof
     * @throws IllegalArgumentException thrown if proof is invalid
     */
    public void setSecurity(byte[] proof)
    {
        if (proof == null || proof.length != 32)
        {
            throw new IllegalArgumentException("Invalid proof"); //$NON-NLS-1$
        }
        this.doSecurity = true;
        this.proof = proof;
        this.doIdentity = false;
    }
    
    /**
     * Sets security to true with given proof and identity
     * @param proof security proof
     * @param ident security identity
     * @throws IllegalArgumentException thrown if proof or identity are invalid
     */
    public void setSecurity(byte[] proof, byte[] ident)
    {
        if (proof == null || proof.length != 32)
        {
            throw new IllegalArgumentException("Invalid proof"); //$NON-NLS-1$
        }
        if (ident == null || ident.length != EASYHANDSHAKE_IDENTITY_BYTES)
        {
            throw new IllegalArgumentException("Invalid ident"); //$NON-NLS-1$
        }
        this.doSecurity = true;
        this.proof = proof;
        this.doIdentity = true;
        this.identity = ident;
    }

    @Override
    public ByteBuf encode()
    {
        int size = 1 + 8 + 8 + 1;
        if (this.doSecurity)
        {
            size += 32 + 1;
            if (this.doIdentity)
            {
                size += EASYHANDSHAKE_IDENTITY_BYTES;
            }
        }
        final ByteBuf buf = Unpooled.buffer(size);
        buf.order(ByteOrder.BIG_ENDIAN);
        buf.writeByte(ID);
        buf.writeLong(this.clientGuid);
        buf.writeLong(this.time);
        buf.writeBoolean(this.doSecurity);
        if (this.doSecurity)
        {
            buf.writeBytes(this.proof);
            buf.writeBoolean(this.doIdentity);
            if (this.doIdentity)
            {
                buf.writeBytes(this.identity);
            }
        }
        return buf;
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

    @Override
    public String toString()
    {
        return "ConnectionRequest [clientGuid=" + this.clientGuid + ", time=" + this.time + ", doSecurity=" + String.valueOf(this.doSecurity) + ", proof=" + tohex(this.proof) + ", doIdentity="   //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$//$NON-NLS-5$
                + String.valueOf(this.doIdentity) + ", identity=" + tohex(this.identity) + "]"; //$NON-NLS-1$ //$NON-NLS-2$
    }
    
}
