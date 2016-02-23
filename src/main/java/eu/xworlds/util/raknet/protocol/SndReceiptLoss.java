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
 * Message "SndReceiptLoss".
 * 
 * <p><b>The following docu is taken from procotol information</b>:</p>
 * 
 * <p>
 * If RakPeerInterface::Send() is called where PacketReliability contains UNRELIABLE_WITH_ACK_RECEIPT, then on a later call to
 * RakPeerInterface::Receive() you will get ID_SND_RECEIPT_ACKED or ID_SND_RECEIPT_LOSS. The message will be 5 bytes long,
 * and bytes 1-4 inclusive will contain a number in native order containing a number that identifies this message. This number
 * will be returned by RakPeerInterface::Send() or RakPeerInterface::SendList(). ID_SND_RECEIPT_LOSS means that an ack for the
 * message did not arrive (it may or may not have been delivered, probably not). On disconnect or shutdown, you will not get
 * ID_SND_RECEIPT_LOSS for unsent messages, you should consider those messages as all lost.
 * </p>
 * 
 * @author mepeisen
 */
public class SndReceiptLoss extends TargetedMessage
{
    
    /** the raknet message id */
    public static final byte ID = 0x0F;
    
    /** the serial */
    private long serial;
    
    /**
     * Constructor for incoming message.
     * @param buf message data
     * @param sender message sender.
     * @param receiver message receiver.
     */
    public SndReceiptLoss(ByteBuf buf, InetSocketAddress sender, InetSocketAddress receiver)
    {
        super(buf, sender, receiver);
    }

    /**
     * Constructor for outgoing message.
     * @param sender message sender.
     * @param receiver message receiver.
     */
    public SndReceiptLoss(InetSocketAddress sender, InetSocketAddress receiver)
    {
        super(sender, receiver);
    }

    @Override
    public byte getId()
    {
        return ID;
    }
    
    /**
     * @return the serial
     */
    public long getSerial()
    {
        return this.serial;
    }

    /**
     * @param serial the serial to set
     */
    public void setSerial(long serial)
    {
        this.serial = serial;
    }

    @Override
    public ByteBuf encode()
    {
        final ByteBuf buf = Unpooled.buffer(1 + 4);
        buf.order(ByteOrder.BIG_ENDIAN);
        buf.writeByte(ID);
        writeUnsignedInt(buf, this.serial);
        return buf;
    }
    
    @Override
    protected void parseMessage(ByteBuf buf)
    {
        this.serial = buf.readUnsignedInt();
    }

    @Override
    public String toString()
    {
        return "SndReceiptLoss [serial=" + this.serial + "]"; //$NON-NLS-1$ //$NON-NLS-2$
    }
    
}
