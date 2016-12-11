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

import static eu.xworlds.util.raknet.protocol.RaknetMessageType.CONNECTION_REQUEST;

import com.google.auto.value.AutoValue;
import eu.xworlds.util.raknet.buffer.ByteBufHelper;
import io.netty.buffer.ByteBuf;

/**
 * <strong>Original documentation:</strong>
 *
 * <p>RakPeer - Remote system requires secure connections, pass a public key to
 * RakPeerInterface::Connect().
 */
@AutoValue
public abstract class ConnectionRequest implements RaknetMessage {

    public abstract long clientGuid();

    public abstract long time();

    public abstract boolean doSecurity();

    @SuppressWarnings("mutable")
    public abstract byte[] proof();

    public abstract boolean doIdentity();

    @SuppressWarnings("mutable")
    public abstract byte[] identity();

    @Override
    public byte id() {
        return (byte) CONNECTION_REQUEST.ordinal();
    }

    @Override
    public void encodeInner(ByteBuf out) {
        ByteBufHelper.writeGuid(out, clientGuid());
        ByteBufHelper.writeTime(out, time());
        out.writeBoolean(doSecurity());
        if (doSecurity()) {
            out.writeBytes(proof());
            out.writeBoolean(doIdentity());
            if (doIdentity()) {
                out.writeBytes(identity());
            }
        }
    }

    /**
     * Decodes {@link ByteBuf} into {@link ConnectionRequest}.
     *
     * @param in the Raknet message (without leading byte)
     */
    public static ConnectionRequest decodeInner(ByteBuf in) {
        long clientGuid = ByteBufHelper.readGuid(in);
        long time = ByteBufHelper.readTime(in);
        boolean doSecurity = in.readBoolean();
        byte[] proof = new byte[32];
        boolean doIdentity = false;
        byte[] identity = new byte[160];
        if (doSecurity) {
            in.readBytes(proof);
            doIdentity = in.readBoolean();
            if (doIdentity) {
                in.readBytes(identity);
            }
        }
        return new AutoValue_ConnectionRequest(clientGuid, time, doSecurity, proof, doIdentity,
                identity);
    }
}
