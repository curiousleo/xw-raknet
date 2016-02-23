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
 * Message "OutOfBandInternal".
 * 
 * <p><b>The following docu is taken from procotol information</b>:</p>
 * 
 * <p>
 * RakPeer - Same as ID_ADVERTISE_SYSTEM, but intended for internal use rather than being passed to the user.
 * Second byte indicates type. Used currently for NAT punchthrough for receiver port advertisement. See ID_NAT_ADVERTISE_RECIPIENT_PORT
 * </p>
 * 
 * @author mepeisen
 */
public class OutOfBandInternal extends TargetedMessage
{
    
    /** the raknet message id */
    public static final byte ID = 0x0D;
    
    /** the guid */
    private long guid;
    
    /** the magic */
    private byte[] magic;
    
    /** the out of bands extra data */
    private byte[] oobData;
    
    /**
     * Constructor for incoming message.
     * @param buf message data
     * @param sender message sender.
     * @param receiver message receiver.
     */
    public OutOfBandInternal(ByteBuf buf, InetSocketAddress sender, InetSocketAddress receiver)
    {
        super(buf, sender, receiver);
    }

    /**
     * Constructor for outgoing message.
     * @param sender message sender.
     * @param receiver message receiver.
     */
    public OutOfBandInternal(InetSocketAddress sender, InetSocketAddress receiver)
    {
        super(sender, receiver);
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
     * @return the oobData
     */
    public byte[] getOobData()
    {
        return this.oobData;
    }

    /**
     * @param oobData the oobData to set
     */
    public void setOobData(byte[] oobData)
    {
        this.oobData = oobData;
    }

    @Override
    public byte getId()
    {
        return ID;
    }
    
    @Override
    public ByteBuf encode()
    {
        int size = 1 + 8 + this.magic.length;
        if (this.oobData != null)
        {
            size += this.oobData.length;
        }
        final ByteBuf buf = Unpooled.buffer(size);
        buf.order(ByteOrder.BIG_ENDIAN);
        buf.writeByte(ID);
        buf.writeLong(this.guid);
        buf.writeBytes(this.magic);
        if (this.oobData != null)
        {
            buf.writeBytes(this.oobData);
        }
        return buf;
    }
    
    @Override
    protected void parseMessage(ByteBuf buf)
    {
        this.guid = buf.readLong();
        this.magic = new byte[MAGIC_BYTES];
        buf.readBytes(this.magic);
        if (buf.readableBytes() > 0)
        {
            this.oobData = buf.readBytes(buf.readableBytes()).array();
        }
    }

    @Override
    public String toString()
    {
        return "OutOfBandInternal [guid=" + this.guid + ", magic=" + tohex(this.magic) + ", oobData=" + tohex(this.oobData) + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    }
    
}
