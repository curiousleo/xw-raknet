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

import java.net.InetSocketAddress;
import java.nio.ByteOrder;

@AutoValue
public abstract class TargetedMessage<T extends RaknetMessage> {

    public abstract InetSocketAddress sender();

    public abstract InetSocketAddress receiver();

    public abstract T inner();

    /**
     * Encodes {@link RaknetMessage} into {@link ByteBuf}.
     *
     * @param out the encoded Raknet message
     */
    public final void encode(ByteBuf out) {
        out.order(ByteOrder.BIG_ENDIAN);
        out.writeByte(inner().id());
        inner().encodeInner(out);
    }

    public static <T extends RaknetMessage> TargetedMessage create(InetSocketAddress sender,
            InetSocketAddress receiver, T inner) {
        return new AutoValue_TargetedMessage<T>(sender, receiver, inner);
    }
}
