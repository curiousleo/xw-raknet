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
 * Message "OpenConnectionRequest2".
 * 
 * <p><b>The following docu is taken from procotol information</b>:</p>
 * 
 * <p>
 * C2S: Header(1), OfflineMesageID(16), Cookie(4, if HasSecurity is true on the server), clientSupportsSecurity(1 bit),
 * handshakeChallenge (if has security on both server and client), remoteBindingAddress(6), MTU(2), client GUID(8)
 * Connection slot allocated if cookie is valid, server is not full, GUID and IP not already in use.
 * </p>
 * 
 * @author mepeisen
 */
public class OpenConnectionRequest2 extends TargetedMessage
{
    
    /** the raknet message id */
    public static final byte ID = 0x07;
    
    /** the magic */
    private byte[] magic;
    
    /** true if the client uses security */
    private boolean useSecurity;
    
    /** the cookie */
    private int cookie;
    
    /** true if the client write the challenge */
    private boolean clientWroteChallenge;
    
    /** the client challenge */
    private byte[] clientChallenge;
    
    /** the binding address */
    private long bindingAddress;
    
    /** the mtu size */
    private int mtuSize;
    
    /** the guid */
    private long guid;
    
    /**
     * Constructor for incoming message.
     * @param buf message data
     * @param sender message sender.
     * @param receiver message receiver.
     */
    public OpenConnectionRequest2(ByteBuf buf, InetSocketAddress sender, InetSocketAddress receiver)
    {
        super(buf, sender, receiver);
    }

    /**
     * Constructor for outgoing message.
     * @param sender message sender.
     * @param receiver message receiver.
     */
    public OpenConnectionRequest2(InetSocketAddress sender, InetSocketAddress receiver)
    {
        super(sender, receiver);
    }

    @Override
    public byte getId()
    {
        return ID;
    }
    
    /**
     * @return the magic
     */
    public byte[] getMagic()
    {
        return this.magic;
    }

    /**
     * @param magic the magic to set
     */
    public void setMagic(byte[] magic)
    {
        this.magic = magic;
    }

    /**
     * @return the useSecurity
     */
    public boolean isUseSecurity()
    {
        return this.useSecurity;
    }

    /**
     * Sets security flag to false
     */
    public void setNoSecurity()
    {
        this.useSecurity = false;
    }

    /**
     * @return the cookie
     */
    public int getCookie()
    {
        return this.cookie;
    }

    /**
     * Sets security flag to true and sets the cookie
     * @param cookie the cookie to set
     */
    public void setCookie(int cookie)
    {
        this.useSecurity = true;
        this.clientWroteChallenge = false;
        this.cookie = cookie;
    }

    /**
     * @return the clientWroteChallenge
     */
    public boolean isClientWroteChallenge()
    {
        return this.clientWroteChallenge;
    }

    /**
     * @return the clientChallenge
     */
    public byte[] getClientChallenge()
    {
        return this.clientChallenge;
    }

    /**
     * Sets security flag to true and sets the cookie and clientChallenge
     * @param cookie the cookie to set
     * @param clientChallenge the clientChallenge to set
     * @throws IllegalArgumentException thrown if client challenge is of wrong size
     */
    public void setClientChallenge(int cookie, byte[] clientChallenge)
    {
        if (clientChallenge == null || clientChallenge.length != EASYHANDSHAKE_CHALLENGE_BYTES)
        {
            throw new IllegalArgumentException("clientChallenge"); //$NON-NLS-1$
        }
        this.useSecurity = true;
        this.clientWroteChallenge = true;
        this.cookie = cookie;
        this.clientChallenge = clientChallenge;
    }

    /**
     * @return the bindingAddress
     */
    public long getBindingAddress()
    {
        return this.bindingAddress;
    }

    /**
     * @param bindingAddress the bindingAddress to set
     */
    public void setBindingAddress(long bindingAddress)
    {
        this.bindingAddress = bindingAddress;
    }

    /**
     * @return the mtuSize
     */
    public int getMtuSize()
    {
        return this.mtuSize;
    }

    /**
     * @param mtuSize the mtuSize to set
     */
    public void setMtuSize(int mtuSize)
    {
        this.mtuSize = mtuSize;
    }

    /**
     * @return the guid
     */
    public long getGuid()
    {
        return this.guid;
    }

    /**
     * @param guid the guid to set
     */
    public void setGuid(long guid)
    {
        this.guid = guid;
    }

    @Override
    public ByteBuf encode()
    {
        int size = 1 + this.magic.length + 4 + 2 + 8;
        if (this.useSecurity)
        {
            size += 4 + 1;
            if (this.clientWroteChallenge)
            {
                size += this.clientChallenge.length;
            }
        }
        final ByteBuf buf = Unpooled.buffer(size);
        buf.order(ByteOrder.BIG_ENDIAN);
        buf.writeByte(ID);
        buf.writeBytes(this.magic);
        if (this.useSecurity)
        {
            buf.writeInt(this.cookie);
            buf.writeBoolean(this.clientWroteChallenge);
            if (this.clientWroteChallenge)
            {
                buf.writeBytes(this.clientChallenge);
            }
        }
        writeUnsignedInt(buf, this.bindingAddress);
        writeUnsignedShort(buf, this.mtuSize);
        buf.writeLong(this.guid);
        return buf;
    }
    
    @Override
    protected void parseMessage(ByteBuf buf)
    {
        this.magic = new byte[MAGIC_BYTES];
        buf.readBytes(this.magic);
        // TODO how to get the security flag?
        if (this.useSecurity)
        {
            this.cookie = buf.readInt();
            this.clientWroteChallenge = buf.readBoolean();
            if (this.clientWroteChallenge)
            {
                this.clientChallenge = new byte[EASYHANDSHAKE_CHALLENGE_BYTES];
                buf.readBytes(this.clientChallenge);
            }
        }
        this.bindingAddress = buf.readUnsignedInt();
        this.mtuSize = buf.readUnsignedShort();
        this.guid = buf.readLong();
    }

    @Override
    public String toString()
    {
        return "OpenConnectionRequest2 [magic=" + tohex(this.magic) + ", useSecurity=" + String.valueOf(this.useSecurity) + ", cookie=" + this.cookie + ", clientWroteChallenge=" + String.valueOf(this.clientWroteChallenge) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                + ", clientChallenge=" + tohex(this.clientChallenge) + ", bindingAddress=" + this.bindingAddress + ", mtuSize=" + this.mtuSize + ", guid=" + this.guid + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
    }
    
}
