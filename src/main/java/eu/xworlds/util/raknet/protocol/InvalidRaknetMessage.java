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

import com.google.auto.value.AutoValue;
import io.netty.buffer.ByteBuf;

@AutoValue
public abstract class InvalidRaknetMessage implements RaknetMessage {
    @SuppressWarnings("mutable")
    public abstract byte[] payload();

    @Override
    public abstract byte id();

    @Override
    public void encodeInner(ByteBuf out) {
        throw new UnsupportedOperationException("Cannot encode invalid message");
    }

    /**
     * Creates {@link RaknetMessage} for invalid Raknet message.
     *
     * @param id the Raknet messsage id (leading byte of the message)
     * @param in the Raknet message (without leading byte)
     */
    public static InvalidRaknetMessage create(byte id, ByteBuf in) {
        int size = Math.min(1500, in.readableBytes());
        byte[] payload = new byte[size];
        in.readBytes(payload);
        return new AutoValue_InvalidRaknetMessage(payload, id);
    }
}
