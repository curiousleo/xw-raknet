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

import static eu.xworlds.util.raknet.protocol.Constants.INT_SIZE;
import static eu.xworlds.util.raknet.protocol.Decoder.MessageType.SND_RECEIPT_ACKED;

import com.google.auto.value.AutoValue;
import eu.xworlds.util.raknet.buffer.ByteBufHelper;
import io.netty.buffer.ByteBuf;

/**
 * <strong>Original documentation:</strong>
 *
 * <p>If RakPeerInterface::Send() is called where PacketReliability contains
 * UNRELIABLE_WITH_ACK_RECEIPT, then on a later call to
 * RakPeerInterface::Receive() you will get ID_SND_RECEIPT_ACKED or ID_SND_RECEIPT_LOSS. The message
 * will be 5 bytes long, and bytes 1-4 inclusive will contain a number in native order containing a
 * number that identifies this message. This number will be returned by RakPeerInterface::Send() or
 * RakPeerInterface::SendList(). ID_SND_RECEIPT_LOSS means that an ack for the message did not
 * arrive (it may or may not have been delivered, probably not). On disconnect or shutdown, you will
 * not get ID_SND_RECEIPT_LOSS for unsent messages, you should consider those messages as all lost.
 */
@AutoValue
public abstract class SndReceiptAcked implements RaknetMessage {

    public abstract long serial();

    @Override
    public byte id() {
        return (byte) SND_RECEIPT_ACKED.ordinal();
    }

    @Override
    public void encodeBody(ByteBuf out) {
        ByteBufHelper.writeUnsignedInt(out, serial());
    }

    @Override
    public int size() {
        return INT_SIZE;
    }

    public static SndReceiptAcked decodeBody(ByteBuf in) {
        return new AutoValue_SndReceiptAcked(in.readUnsignedInt());
    }
}
