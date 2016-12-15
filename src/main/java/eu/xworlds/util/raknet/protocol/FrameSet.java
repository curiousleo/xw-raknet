package eu.xworlds.util.raknet.protocol;

import static eu.xworlds.util.raknet.protocol.Constants.UINT24LE_SIZE;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import eu.xworlds.util.raknet.buffer.ByteBufHelper;
import io.netty.buffer.ByteBuf;

import java.util.stream.Collectors;

// http://wiki.vg/Pocket_Edition_Protocol_Documentation#Frame_Set_Packet
@AutoValue
public abstract class FrameSet implements RaknetMessage {

    public static FrameSet decodeBody(byte id, ByteBuf in) {
        int frameSetIndex = in.readUnsignedMedium();
        ImmutableList.Builder<Frame> framesBuilder = ImmutableList.builder();
        while (in.readableBytes() > Frame.MINIMUM_SIZE) {
            framesBuilder.add(Frame.decodeFrame(in));
        }
        return new AutoValue_FrameSet(frameSetIndex, framesBuilder.build(), id);
    }

    public abstract int frameSetIndex();

    public abstract ImmutableList<Frame> frames();

    public abstract byte id();

    public boolean allReliable() {
        return frames().stream()
                .allMatch(frame -> frame.reliability().isReliable());
    }

    @Override
    public int size() {
        int totalFrameSize = frames().stream()
                .collect(Collectors.summingInt(Frame::frameSize));
        return UINT24LE_SIZE + totalFrameSize;
    }

    @Override
    public void encodeBody(ByteBuf out) {
        ByteBufHelper.writeUnsignedMedium(out, frameSetIndex());
        frames().forEach(frame -> frame.encodeFrame(out));
    }
}
