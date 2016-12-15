package eu.xworlds.util.raknet;

import eu.xworlds.util.raknet.protocol.RaknetMessage;
import eu.xworlds.util.raknet.protocol.TargetedMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.nio.ByteOrder;
import java.util.List;

/**
 * Encodes Raknet messages into datagram packets.
 */
public class RaknetEncoder<T extends RaknetMessage>
        extends MessageToMessageEncoder<TargetedMessage<T>> {

    @Override
    protected void encode(ChannelHandlerContext ctx, TargetedMessage<T> msg, List<Object> out)
            throws Exception {
        final ByteBuf buf = ctx.alloc()
                .buffer(msg.size())
                .order(ByteOrder.BIG_ENDIAN);
        buf.writeByte(msg.inner().id());
        msg.inner().encodeBody(buf);

        out.add(new DatagramPacket(buf, msg.receiver(), msg.sender()));
    }
}
