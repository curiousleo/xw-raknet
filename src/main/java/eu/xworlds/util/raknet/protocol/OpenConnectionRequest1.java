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

import org.apache.commons.codec.binary.Hex;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Message "OpenConnectionRequest1".
 * 
 * <p><b>The following docu is taken from procotol information</b>:</p>
 * 
 * <p>
 * C2S: Initial query: Header(1), OfflineMesageID(16), Protocol number(1), Pad(toMTU), sent with no fragment set.
 * If protocol fails on server, returns ID_INCOMPATIBLE_PROTOCOL_VERSION to client
 * </p>
 * 
 * @author mepeisen
 */
public class OpenConnectionRequest1 extends TargetedMessage
{
    
    /** the raknet message id */
    public static final byte ID = 0x05;
    
    /** the magic */
    private byte[] magic;
    
    /** the raknet protocol version */
    private byte procotolVersion;
    
    /** the mtu payload */
    private byte[] mtuPayload;
    
    /**
     * Constructor for incoming message.
     * @param buf message data
     * @param sender message sender.
     * @param receiver message receiver.
     */
    public OpenConnectionRequest1(ByteBuf buf, InetSocketAddress sender, InetSocketAddress receiver)
    {
        super(buf, sender, receiver);
    }

    /**
     * Constructor for outgoing message.
     * @param sender message sender.
     * @param receiver message receiver.
     */
    public OpenConnectionRequest1(InetSocketAddress sender, InetSocketAddress receiver)
    {
        super(sender, receiver);
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
     * @return the procotolVersion
     */
    public byte getProcotolVersion()
    {
        return this.procotolVersion;
    }

    /**
     * @param procotolVersion the procotolVersion to set
     */
    public void setProcotolVersion(byte procotolVersion)
    {
        this.procotolVersion = procotolVersion;
    }

    /**
     * @return the mtuPayload
     */
    public byte[] getMtuPayload()
    {
        return this.mtuPayload;
    }

    /**
     * @param mtuPayload the mtuPayload to set
     */
    public void setMtuPayload(byte[] mtuPayload)
    {
        this.mtuPayload = mtuPayload;
    }

    @Override
    public byte getId()
    {
        return ID;
    }
    
    @Override
    public ByteBuf encode()
    {
        final ByteBuf result = Unpooled.buffer(1 + 16 + this.magic.length + 1 + this.mtuPayload.length);
        result.order(ByteOrder.BIG_ENDIAN);
        result.writeByte(ID);
        result.writeBytes(this.magic);
        result.writeByte(this.procotolVersion);
        result.writeBytes(this.mtuPayload);
        return result;
    }
    
    @Override
    protected void parseMessage(ByteBuf buf)
    {
        this.magic = new byte[MAGIC_BYTES];
        buf.readBytes(this.magic);
        this.procotolVersion = buf.readByte();
        this.mtuPayload = buf.readBytes(buf.readableBytes()).array();
    }

    @Override
    public String toString()
    {
        return "OpenConnectionRequest1 [magic=" + tohex(this.magic) + ", procotolVersion=" + this.procotolVersion + ", mtuPayload=" + Hex.encodeHexString(this.mtuPayload) + "]";  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    }
    
}
