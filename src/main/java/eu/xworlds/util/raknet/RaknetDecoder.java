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

package eu.xworlds.util.raknet;

import eu.xworlds.util.raknet.protocol.DecodeException;
import eu.xworlds.util.raknet.protocol.InvalidRaknetMessage;
import eu.xworlds.util.raknet.protocol.RaknetMessageType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.nio.ByteOrder;
import java.util.List;

/**
 * Decodes incoming datagram packets into Raknet messages.
 */
class RaknetDecoder extends MessageToMessageDecoder<DatagramPacket> {

    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket msg, List<Object> out)
            throws DecodeException {
        final ByteBuf buf = msg.content();
        buf.order(ByteOrder.BIG_ENDIAN);
        final byte id = buf.readByte();

        final RaknetMessageType messageType = RaknetMessageType.of(id);
        if (messageType == null) {
            out.add(InvalidRaknetMessage.create(id, buf));
            return;
        }
        out.add(messageType.decodeBody(buf));
    }
}
