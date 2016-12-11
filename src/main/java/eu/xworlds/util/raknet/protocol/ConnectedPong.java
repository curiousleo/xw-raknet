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
import static eu.xworlds.util.raknet.protocol.RaknetMessageType.CONNECTED_PONG;

import com.google.auto.value.AutoValue;
import eu.xworlds.util.raknet.buffer.ByteBufHelper;
import io.netty.buffer.ByteBuf;

/**
 * <strong>Original documentation:</strong>
 *
 * <p>Ping from a connected system. Update timestamps (internal use only)
 */
@AutoValue
public abstract class ConnectedPong implements RaknetMessage {

    public abstract long ping();

    public abstract long pong();

    @Override
    public byte id() {
        return (byte) CONNECTED_PONG.ordinal();
    }

    @Override
    public void encodeBody(ByteBuf out) {
        ByteBufHelper.writeTime(out, ping());
        ByteBufHelper.writeTime(out, pong());
    }

    @Override
    public int size() {
        return TIME_SIZE + TIME_SIZE;
    }

    /**
     * Decodes {@link ByteBuf} into {@link ConnectedPong}.
     *
     * @param in the Raknet message (without leading byte)
     */
    public static ConnectedPong decodeBody(ByteBuf in) {
        long pingTime = ByteBufHelper.readTime(in);
        long pongTime = ByteBufHelper.readTime(in);
        return new AutoValue_ConnectedPong(pingTime, pongTime);
    }

    public static ConnectedPong create(long ping, long pong) {
        return new AutoValue_ConnectedPong(ping, pong);
    }
}
