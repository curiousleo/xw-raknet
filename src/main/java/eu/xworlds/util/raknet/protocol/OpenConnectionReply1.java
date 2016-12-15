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

import static eu.xworlds.util.raknet.protocol.Constants.BOOL_SIZE;
import static eu.xworlds.util.raknet.protocol.Constants.GUID_SIZE;
import static eu.xworlds.util.raknet.protocol.Constants.INT_SIZE;
import static eu.xworlds.util.raknet.protocol.Constants.MAGIC;
import static eu.xworlds.util.raknet.protocol.Constants.MAGIC_SIZE;
import static eu.xworlds.util.raknet.protocol.Constants.SHORT_SIZE;
import static eu.xworlds.util.raknet.protocol.Decoder.MessageType.OPEN_CONNECTION_REPLY_1;

import com.google.auto.value.AutoValue;
import eu.xworlds.util.raknet.buffer.ByteBufHelper;
import io.netty.buffer.ByteBuf;

/**
 * <strong>Original documentation:</strong>
 *
 * <p>C2S: Header(1), OfflineMesageID(16), Cookie(4, if HasSecurity is true on the server),
 * clientSupportsSecurity(1 bit), handshakeChallenge (if has security on both server and client),
 * remoteBindingAddress(6), MTU(2), client GUID(8)
 *
 * <p>Connection slot allocated if cookie is valid, server is not full, GUID and IP not already in
 * use.
 */
@AutoValue
public abstract class OpenConnectionReply1 implements RaknetMessage {

    private static final int PUBLIC_KEY_SIZE = 64;

    public abstract long serverGuid();

    public abstract boolean hasSecurity();

    public abstract int cookie();

    @SuppressWarnings("mutable")
    public abstract byte[] publicKey();

    public abstract int mtuSize();

    @Override
    public byte id() {
        return (byte) OPEN_CONNECTION_REPLY_1.ordinal();
    }

    @Override
    public int size() {
        int size = MAGIC_SIZE + GUID_SIZE + BOOL_SIZE;
        if (hasSecurity()) {
            size += INT_SIZE + PUBLIC_KEY_SIZE;
        }
        size += SHORT_SIZE;
        return size;
    }

    @Override
    public void encodeBody(ByteBuf out) {
        out.writeBytes(MAGIC);
        ByteBufHelper.writeGuid(out, serverGuid());
        out.writeBoolean(hasSecurity());
        if (hasSecurity()) {
            out.writeInt(cookie());
            out.writeBytes(publicKey());
        }
        ByteBufHelper.writeUnsignedShort(out, mtuSize());
    }

    /**
     * Decodes {@link ByteBuf} into {@link OpenConnectionReply1}.
     *
     * @param in the Raknet message (without leading byte)
     */
    public static OpenConnectionReply1 decodeBody(ByteBuf in) throws DecodeException {
        ByteBufHelper.checkOrSkipMagic(in, true);
        long serverGuid = ByteBufHelper.readGuid(in);
        boolean hasSecurity = in.readBoolean();
        int cookie = 0;
        byte[] publicKey = new byte[PUBLIC_KEY_SIZE];
        if (hasSecurity) {
            cookie = in.readInt();
            in.readBytes(publicKey);
        }
        int mtuSize = in.readUnsignedShort();
        return new AutoValue_OpenConnectionReply1(serverGuid, hasSecurity, cookie, publicKey,
                mtuSize);
    }
}
