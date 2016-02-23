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
 * Message "OpenConnectionReply2".
 * 
 * <p>
 * <b>The following docu is taken from procotol information</b>:
 * </p>
 * 
 * <p>
 * S2C: Header(1), OfflineMesageID(16), server GUID(8), mtu(2), doSecurity(1 bit), handshakeAnswer (if do security is true)
 * </p>
 * 
 * @author mepeisen
 */
public class OpenConnectionReply2 extends TargetedMessage
{
    
    /** the raknet message id */
    public static final byte ID = 0x08;
    
    /** the magic */
    private byte[]           magic;
    
    /** the server id */
    private long             serverGuid;
    
    /** the server port */
    private int              port;
    
    /** the mtu size */
    private int              mtuSize;
    
    /** do security flag */
    private boolean          doSecurity;
    
    /** the security answer */
    private byte[]           securityAnswer;
    
    /**
     * Constructor for incoming message.
     * 
     * @param buf
     *            message data
     * @param sender
     *            message sender.
     * @param receiver
     *            message receiver.
     */
    public OpenConnectionReply2(ByteBuf buf, InetSocketAddress sender, InetSocketAddress receiver)
    {
        super(buf, sender, receiver);
    }
    
    /**
     * Constructor for outgoing message.
     * 
     * @param sender
     *            message sender.
     * @param receiver
     *            message receiver.
     */
    public OpenConnectionReply2(InetSocketAddress sender, InetSocketAddress receiver)
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
     * @param magic
     *            the magic to set
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
     * @param serverGuid
     *            the serverGuid to set
     */
    public void setServerGuid(long serverGuid)
    {
        this.serverGuid = serverGuid;
    }
    
    /**
     * @return the port
     */
    public int getPort()
    {
        return this.port;
    }
    
    /**
     * @param port
     *            the port to set
     */
    public void setPort(int port)
    {
        this.port = port;
    }
    
    /**
     * @return the mtuSize
     */
    public int getMtuSize()
    {
        return this.mtuSize;
    }
    
    /**
     * @param mtuSize
     *            the mtuSize to set
     */
    public void setMtuSize(int mtuSize)
    {
        this.mtuSize = mtuSize;
    }
    
    /**
     * @return the doSecurity
     */
    public boolean isDoSecurity()
    {
        return this.doSecurity;
    }
    
    /**
     * Sets the security flag to false
     */
    public void setDoSecurityFalse()
    {
        this.doSecurity = false;
    }
    
    /**
     * Sets the security flag to true and sets the given answer
     * 
     * @param answer
     *            the security answer
     * @throws IllegalArgumentException
     *             thrown if the answer key is not 128 bytes long
     */
    public void setDoSecurity(byte[] answer)
    {
        if (answer == null || answer.length != EASYHANDSHAKE_ANSWER_BYTES)
        {
            throw new IllegalArgumentException("answer"); //$NON-NLS-1$
        }
        this.doSecurity = true;
    }
    
    /**
     * @return the securityAnswer
     */
    public byte[] getSecurityAnswer()
    {
        return this.securityAnswer;
    }
    
    @Override
    public byte getId()
    {
        return ID;
    }
    
    @Override
    public ByteBuf encode()
    {
        int size = 1 + this.magic.length + 8 + 2 + 2 + 1;
        if (this.doSecurity)
        {
            size += this.securityAnswer.length;
        }
        final ByteBuf buf = Unpooled.buffer(size);
        buf.order(ByteOrder.BIG_ENDIAN);
        buf.writeByte(ID);
        buf.writeBytes(this.magic);
        writeUnsignedShort(buf, this.port);
        writeUnsignedShort(buf, this.mtuSize);
        buf.writeBoolean(this.doSecurity);
        if (this.doSecurity)
        {
            buf.writeBytes(this.securityAnswer);
        }
        return buf;
    }
    
    @Override
    protected void parseMessage(ByteBuf buf)
    {
        this.magic = new byte[MAGIC_BYTES];
        buf.readBytes(this.magic);
        this.serverGuid = buf.readLong();
        this.port = buf.readUnsignedShort();
        this.mtuSize = buf.readUnsignedShort();
        this.doSecurity = buf.readBoolean();
        if (this.doSecurity)
        {
            this.securityAnswer = new byte[EASYHANDSHAKE_ANSWER_BYTES];
            buf.readBytes(this.securityAnswer);
        }
    }

    @Override
    public String toString()
    {
        return "OpenConnectionReply2 [magic=" + tohex(this.magic) + ", serverGuid=" + this.serverGuid + ", port=" + this.port + ", mtuSize=" + this.mtuSize + ", doSecurity=" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                + String.valueOf(this.doSecurity) + ", securityAnswer=" + tohex(this.securityAnswer) + "]"; //$NON-NLS-1$ //$NON-NLS-2$
    }
    
}
