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

import static eu.xworlds.util.raknet.protocol.RaknetMessageType.UNCONNECTED_PING_OPEN_CONNECTIONS;

import com.google.auto.value.AutoValue;
import eu.xworlds.util.raknet.buffer.ByteBufHelper;
import io.netty.buffer.ByteBuf;

/**
 * <strong>Original documentation:</strong>
 *
 * <p>Ping from an unconnected system. Only reply if we have open connections. Do not update
 * timestamps. (internal use only)
 */
@AutoValue
public abstract class UnconnectedPingOpenConnections implements RaknetMessage {

    public abstract long time();

    @SuppressWarnings("mutable")
    public abstract byte[] magic();

    @Override
    public byte id() {
        return (byte) UNCONNECTED_PING_OPEN_CONNECTIONS.ordinal();
    }

    @Override
    public void encodeInner(ByteBuf out) {
        ByteBufHelper.writeTime(out, time());
        out.writeBytes(magic());
    }

    /**
     * Decodes {@link ByteBuf} into {@link UnconnectedPingOpenConnections}.
     *
     * @param in the Raknet message (without leading byte)
     */
    public static UnconnectedPingOpenConnections decodeInner(ByteBuf in) {
        long time = ByteBufHelper.readTime(in);
        byte[] magic = new byte[16];
        in.readBytes(magic);
        return new AutoValue_UnconnectedPingOpenConnections(time, magic);
    }
}
