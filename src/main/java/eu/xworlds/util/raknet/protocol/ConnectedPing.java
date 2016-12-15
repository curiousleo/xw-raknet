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

import static eu.xworlds.util.raknet.protocol.Constants.TIME_SIZE;
import static eu.xworlds.util.raknet.protocol.Decoder.MessageType.CONNECTED_PING;

import com.google.auto.value.AutoValue;
import eu.xworlds.util.raknet.buffer.ByteBufHelper;
import io.netty.buffer.ByteBuf;

/**
 * <strong>Original documentation:</strong>
 *
 * <p>Ping from a connected system. Update timestamps (internal use only)
 */
@AutoValue
public abstract class ConnectedPing implements RaknetMessage {

    public abstract long time();

    @Override
    public byte id() {
        return (byte) CONNECTED_PING.ordinal();
    }

    @Override
    public int size() {
        return TIME_SIZE;
    }

    @Override
    public void encodeBody(ByteBuf out) {
        ByteBufHelper.writeTime(out, time());
    }

    public static ConnectedPing decodeBody(ByteBuf in) {
        return new AutoValue_ConnectedPing(ByteBufHelper.readTime(in));
    }
}
