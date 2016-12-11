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

import static eu.xworlds.util.raknet.protocol.RaknetMessageType.OUT_OF_BAND_INTERNAL;

import com.google.auto.value.AutoValue;
import eu.xworlds.util.raknet.buffer.ByteBufHelper;
import io.netty.buffer.ByteBuf;

/**
 * <strong>Original documentation:</strong>
 *
 * <p>If RakPeerInterface::Send() is called where PacketReliability contains _WITH_ACK_RECEIPT, then
 * on a later call to RakPeerInterface::Receive() you will get ID_SND_RECEIPT_ACKED or
 * ID_SND_RECEIPT_LOSS. The message will be 5 bytes long, and bytes 1-4 inclusive will contain a
 * number in native order containing a number that identifies this message. This number will be
 * returned by RakPeerInterface::Send() or RakPeerInterface::SendList(). ID_SND_RECEIPT_ACKED means
 * that the message arrived.
 */
@AutoValue
public abstract class OutOfBandInternal implements RaknetMessage {

    public abstract long guid();

    @SuppressWarnings("mutable")
    public abstract byte[] magic();

    @SuppressWarnings("mutable")
    public abstract byte[] oobData();

    @Override
    public byte id() {
        return (byte) OUT_OF_BAND_INTERNAL.ordinal();
    }

    @Override
    public void encodeInner(ByteBuf out) {
        ByteBufHelper.writeGuid(out, guid());
        out.writeBytes(magic());
        out.writeBytes(oobData());
    }

    /**
     * Decodes {@link ByteBuf} into {@link OutOfBandInternal}.
     *
     * @param in the Raknet message (without leading byte)
     */
    public static OutOfBandInternal decodeInner(ByteBuf in) {
        long guid = ByteBufHelper.readGuid(in);
        byte[] magic = new byte[16];
        in.readBytes(magic);
        byte[] oobData = new byte[in.readableBytes()];
        in.readBytes(oobData);
        return new AutoValue_OutOfBandInternal(guid, magic, oobData);
    }
}
