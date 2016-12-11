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

import static eu.xworlds.util.raknet.protocol.RaknetMessageType.OPEN_CONNECTION_REQUEST_2;

import com.google.auto.value.AutoValue;
import eu.xworlds.util.raknet.buffer.ByteBufHelper;
import io.netty.buffer.ByteBuf;

/**
 * <strong>Original documentation:</strong>
 *
 * <p>S2C: Header(1), OfflineMesageID(16), server GUID(8), mtu(2), doSecurity(1 bit),
 * handshakeAnswer (if do security is true).
 */
@AutoValue
public abstract class OpenConnectionRequest2 implements RaknetMessage {

    @SuppressWarnings("mutable")
    public abstract byte[] magic();

    public abstract boolean useSecurity();

    public abstract int cookie();

    public abstract boolean clientWroteChallenge();

    @SuppressWarnings("mutable")
    public abstract byte[] clientChallenge();

    public abstract long bindingAddress();

    public abstract int mtuSize();

    public abstract long guid();

    @Override
    public byte id() {
        return (byte) OPEN_CONNECTION_REQUEST_2.ordinal();
    }

    @Override
    public void encodeInner(ByteBuf out) {
        out.writeBytes(magic());
        if (useSecurity()) {
            out.writeInt(cookie());
            out.writeBoolean(clientWroteChallenge());
            if (clientWroteChallenge()) {
                out.writeBytes(clientChallenge());
            }
        }
        ByteBufHelper.writeIpv4Address(out, bindingAddress());
        ByteBufHelper.writeUnsignedShort(out, mtuSize());
        ByteBufHelper.writeGuid(out, guid());
    }

    public static OpenConnectionRequest2 decodeInner(ByteBuf in) {
        throw new UnsupportedOperationException("todo");
    }
}
