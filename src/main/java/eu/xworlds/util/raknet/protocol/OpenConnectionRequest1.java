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

import static eu.xworlds.util.raknet.protocol.Constants.BYTE_SIZE;
import static eu.xworlds.util.raknet.protocol.Constants.MAGIC;
import static eu.xworlds.util.raknet.protocol.Constants.MAGIC_SIZE;
import static eu.xworlds.util.raknet.protocol.RaknetMessageType.OPEN_CONNECTION_REQUEST_1;

import com.google.auto.value.AutoValue;
import eu.xworlds.util.raknet.buffer.ByteBufHelper;
import io.netty.buffer.ByteBuf;

/**
 * <strong>Original documentation:</strong>
 *
 * <p>S2C: Header(1), OfflineMessageID(16), server GUID(8), HasSecurity(1), Cookie(4,
 * if HasSecurity), public key (if do security is true), MTU(2). If public key fails on client,
 * returns ID_PUBLIC_KEY_MISMATCH
 */
@AutoValue
public abstract class OpenConnectionRequest1 implements RaknetMessage {

    public abstract byte protocolVersion();

    @SuppressWarnings("mutable")
    public abstract byte[] mtuPayload();

    @Override
    public byte id() {
        return (byte) OPEN_CONNECTION_REQUEST_1.ordinal();
    }

    @Override
    public int size() {
        return MAGIC_SIZE + BYTE_SIZE + mtuPayload().length;
    }

    @Override
    public void encodeBody(ByteBuf out) {
        out.writeBytes(MAGIC);
        out.writeByte(protocolVersion());
        out.writeBytes(mtuPayload());
    }

    /**
     * Decodes {@link ByteBuf} into {@link OpenConnectionRequest1}.
     *
     * @param in the Raknet message (without leading byte)
     */
    public static OpenConnectionRequest1 decodeBody(ByteBuf in) throws DecodeException {
        ByteBufHelper.checkOrSkipMagic(in, true);
        byte protocolVersion = in.readByte();
        byte[] mtuPayload = new byte[in.readableBytes()];
        in.readBytes(mtuPayload);
        return new AutoValue_OpenConnectionRequest1(protocolVersion, mtuPayload);
    }

    public static OpenConnectionRequest1 create(byte protocolVersion, byte[] mtuPayload) {
        return new AutoValue_OpenConnectionRequest1(protocolVersion, mtuPayload);
    }
}
