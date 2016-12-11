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

import static eu.xworlds.util.raknet.protocol.RaknetMessageType.OPEN_CONNECTION_REPLY_2;

import com.google.auto.value.AutoValue;
import eu.xworlds.util.raknet.buffer.ByteBufHelper;
import io.netty.buffer.ByteBuf;

/**
 * <strong>Original documentation:</strong>
 *
 * <p>C2S: Header(1), GUID(8), Timestamp, HasSecurity(1), Proof(32).
 */
@AutoValue
public abstract class OpenConnectionReply2 implements RaknetMessage {

    @SuppressWarnings("mutable")
    public abstract byte[] magic();

    public abstract long serverGuid();

    public abstract int port();

    public abstract int mtuSize();

    public abstract boolean doSecurity();

    @SuppressWarnings("mutable")
    public abstract byte[] securityAnswer();

    @Override
    public byte id() {
        return (byte) OPEN_CONNECTION_REPLY_2.ordinal();
    }

    @Override
    public void encodeInner(ByteBuf out) {
        out.writeBytes(magic());
        ByteBufHelper.writeGuid(out, serverGuid());
        ByteBufHelper.writeUnsignedShort(out, port());
        ByteBufHelper.writeUnsignedShort(out, mtuSize());
        out.writeBoolean(doSecurity());
        if (doSecurity()) {
            out.writeBytes(securityAnswer());
        }
    }

    /**
     * Decodes {@link ByteBuf} into {@link OpenConnectionReply2}.
     *
     * @param in the Raknet message (without leading byte)
     */
    public static OpenConnectionReply2 decodeInner(ByteBuf in) {
        byte[] magic = new byte[16];
        in.readBytes(magic);
        long serverGuid = ByteBufHelper.readGuid(in);
        int port = in.readUnsignedShort();
        int mtuSize = in.readUnsignedShort();
        boolean doSecurity = in.readBoolean();
        byte[] securityAnswer = new byte[128];
        if (doSecurity) {
            in.readBytes(securityAnswer);
        }
        return new AutoValue_OpenConnectionReply2(magic, serverGuid, port, mtuSize, doSecurity,
                securityAnswer);
    }
}
