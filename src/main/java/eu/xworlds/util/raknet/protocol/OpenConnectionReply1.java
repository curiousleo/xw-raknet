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
 * Message "OpenConnectionReply1".
 * 
 * <p><b>The following docu is taken from procotol information</b>:</p>
 * 
 * <p>
 * S2C: Header(1), OfflineMesageID(16), server GUID(8), HasSecurity(1), Cookie(4, if HasSecurity)
 * public key (if do security is true), MTU(2). If public key fails on client, returns ID_PUBLIC_KEY_MISMATCH
 * </p>
 * 
 * @author mepeisen
 */
public class OpenConnectionReply1 extends TargetedMessage
{
    
    /** the raknet message id */
    public static final byte ID = 0x06;
    
    /** the magic */
    private byte[] magic;
    
    /** the server guid */
    private long serverGuid;
    
    /** true if the server has security */
    private boolean hasSecurity;
    
    /** the security cookie */
    private int securityCookie;
    
    /** the public key */
    private byte[] publicKey;
    
    /** the mtu size (unsigned short) */
    private int mtuSize;
    
    /**
     * Constructor for incoming message.
     * @param buf message data
     * @param sender message sender.
     * @param receiver message receiver.
     */
    public OpenConnectionReply1(ByteBuf buf, InetSocketAddress sender, InetSocketAddress receiver)
    {
        super(buf, sender, receiver);
    }

    /**
     * Constructor for outgoing message.
     * @param sender message sender.
     * @param receiver message receiver.
     */
    public OpenConnectionReply1(InetSocketAddress sender, InetSocketAddress receiver)
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
     * @return the serverGuid
     */
    public long getServerGuid()
    {
        return this.serverGuid;
    }

    /**
     * @param serverGuid the serverGuid to set
     */
    public void setServerGuid(long serverGuid)
    {
        this.serverGuid = serverGuid;
    }

    /**
     * @return the hasSecurity
     */
    public boolean isHasSecurity()
    {
        return this.hasSecurity;
    }

    /**
     * Sets the security flag to false
     */
    public void setNoSecurity()
    {
        this.hasSecurity = false;
    }
    
    /**
     * Sets the security flag to true and the given information
     * @param securityCookie the security cookie
     * @param publicKey the public key
     * @throws IllegalArgumentException thrown if the public key is not 64 bytes long
     */
    public void setSecurity(int securityCookie, byte[] publicKey)
    {
        if (this.publicKey == null || this.publicKey.length != EASYHANDSHAKE_PUBLIC_KEY_BYTES)
        {
            throw new IllegalArgumentException("publicKey"); //$NON-NLS-1$
        }
        this.hasSecurity = true;
        this.securityCookie = securityCookie;
        this.publicKey = publicKey;
    }

    /**
     * @return the securityCookie
     */
    public int getSecurityCookie()
    {
        return this.securityCookie;
    }

    /**
     * @return the publicKey
     */
    public byte[] getPublicKey()
    {
        return this.publicKey;
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

    @Override
    public ByteBuf encode()
    {
        int size = 1 + this.magic.length + 8 + 1 + 2;
        if (this.hasSecurity)
        {
            size += 4 + this.publicKey.length;
        }
        final ByteBuf result = Unpooled.buffer(size);
        result.order(ByteOrder.BIG_ENDIAN);
        result.writeByte(ID);
        result.writeBytes(this.magic);
        result.writeLong(this.serverGuid);
        result.writeBoolean(this.hasSecurity);
        if (this.hasSecurity)
        {
            result.writeInt(this.securityCookie);
            result.writeBytes(this.publicKey);
        }
        writeUnsignedShort(result, this.mtuSize);
        return result;
    }
    
    @Override
    protected void parseMessage(ByteBuf buf)
    {
        this.magic = new byte[16];
        buf.readBytes(this.magic);
        this.serverGuid = buf.readLong();
        this.hasSecurity = buf.readBoolean();
        if (this.hasSecurity)
        {
            this.securityCookie = buf.readInt();
            this.publicKey = new byte[EASYHANDSHAKE_PUBLIC_KEY_BYTES];
            buf.readBytes(this.publicKey);
        }
        this.mtuSize = buf.readUnsignedShort();
    }

    @Override
    public String toString()
    {
        return "OpenConnectionReply1 [magic=" + tohex(this.magic) + ", serverGuid=" + this.serverGuid + ", hasSecurity=" + String.valueOf(this.hasSecurity) + ", securityCookie=" + this.securityCookie  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                + ", publicKey=" + tohex(this.publicKey) + ", mtuSize=" + this.mtuSize + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
    
}
