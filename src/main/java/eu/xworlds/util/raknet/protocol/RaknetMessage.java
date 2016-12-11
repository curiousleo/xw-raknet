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

import io.netty.buffer.ByteBuf;

import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;

public interface RaknetMessage {

    byte id();

    default void encodeInner(ByteBuf out) {
        // do nothing
    }

    /**
     * Decodes {@link ByteBuf} into {@link RaknetMessage}.
     *
     * @param in the Raknet message
     */
    static RaknetMessage decode(ByteBuf in) {
        in.order(ByteOrder.BIG_ENDIAN);
        final byte id = in.readByte();
        final RaknetMessageType messageType = RaknetMessageType.of(id);
        if (messageType == null) {
            return InvalidRaknetMessage.create(id, in);
        }
        return messageType.decodeInner(in);
    }
}
