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
import static eu.xworlds.util.raknet.protocol.RaknetMessageType.REMOTE_SYSTEM_REQUIRES_PUBLIC_KEY;

import com.google.auto.value.AutoValue;
import io.netty.buffer.ByteBuf;

/**
 * <strong>Original documentation:</strong>
 *
 * <p>RakPeer - We passed a public key to RakPeerInterface::Connect(), but the other system did not
 * have security turned on.
 */
@AutoValue
public abstract class RemoteSystemRequiresPublicKey implements RaknetMessage {

    public enum ErrorType {
        ServerPublicKeyMissing,
        ClientIdentityMissing,
        ClientIdentityInvalid,
    }

    public abstract ErrorType error();

    @Override
    public byte id() {
        return (byte) REMOTE_SYSTEM_REQUIRES_PUBLIC_KEY.ordinal();
    }

    @Override
    public int size() {
        return BYTE_SIZE;
    }

    @Override
    public void encodeBody(ByteBuf out) {
        out.writeByte(error().ordinal());
    }

    public static RemoteSystemRequiresPublicKey decodeBody(ByteBuf in) {
        ErrorType error = ErrorType.values()[in.readByte()];
        return new AutoValue_RemoteSystemRequiresPublicKey(error);
    }
}
