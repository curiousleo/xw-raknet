package eu.xworlds.util.raknet.protocol;

import static eu.xworlds.util.raknet.protocol.Constants.BYTE_SIZE;
import static eu.xworlds.util.raknet.protocol.Constants.INT_SIZE;
import static eu.xworlds.util.raknet.protocol.Constants.SHORT_SIZE;
import static eu.xworlds.util.raknet.protocol.Constants.UINT24LE_SIZE;

import com.google.auto.value.AutoValue;
import eu.xworlds.util.raknet.buffer.ByteBufHelper;
import io.netty.buffer.ByteBuf;

@AutoValue
public abstract class Frame {

    public static final int MINIMUM_SIZE = 3;

    public static Frame decodeFrame(ByteBuf in) {
        final byte flags = in.readByte();
        final Reliability reliability = Reliability.of((byte) ((flags & 0b11100000) >> 5));
        final boolean fragmented = (flags & (byte) 0b00010000) > 0;
        final int bodyLengthBits = in.readUnsignedShort();
        int reliableFrameIndex = 0;
        if (reliability.isReliable()) {
            reliableFrameIndex = in.readUnsignedMedium();
        }
        int sequencedFrameIndex = 0;
        if (reliability.isSequenced()) {
            sequencedFrameIndex = in.readUnsignedMedium();
        }
        int orderedFrameIndex = 0;
        byte orderChannel = 0;
        if (reliability.isOrdered()) {
            orderedFrameIndex = in.readUnsignedMedium();
            orderChannel = in.readByte();
        }
        int compoundSize = 0;
        int compoundId = 0;
        int compoundIndex = 0;
        if (fragmented) {
            compoundSize = in.readInt();
            compoundId = in.readUnsignedShort();
            compoundIndex = in.readInt();
        }
        final int bodyLength = (int) Math.ceil(bodyLengthBits / 8.0);
        final byte[] body = new byte[bodyLength];
        in.readBytes(body);
        return new AutoValue_Frame(reliability, fragmented, bodyLengthBits, reliableFrameIndex,
                sequencedFrameIndex, orderedFrameIndex, orderChannel, compoundSize, compoundId,
                compoundIndex, body);
    }

    public abstract Reliability reliability();

    public abstract boolean fragmented();

    public abstract int bodyLengthBits();

    public abstract int reliableFrameIndex();

    public abstract int sequencedFrameIndex();

    public abstract int orderedFrameIndex();

    public abstract byte orderChannel();

    public abstract int compoundSize();

    public abstract int compoundId();

    public abstract int compoundIndex();

    @SuppressWarnings("mutable")
    public abstract byte[] body();

    public int frameSize() {
        int size = UINT24LE_SIZE + BYTE_SIZE + SHORT_SIZE;
        if (reliability().isReliable()) {
            size += UINT24LE_SIZE;
        }
        if (reliability().isSequenced()) {
            size += UINT24LE_SIZE;
        }
        if (reliability().isOrdered()) {
            size += UINT24LE_SIZE + BYTE_SIZE;
        }
        if (fragmented()) {
            size += INT_SIZE + SHORT_SIZE + INT_SIZE;
        }
        size += Math.ceil(bodyLengthBits() / 8.0);
        return size;
    }

    public void encodeFrame(ByteBuf out) {
        byte flags = (byte) ((reliability().asByte() << 5) | (fragmented() ? 0b00010000 : 0));
        out.writeByte(flags);
        ByteBufHelper.writeUnsignedShort(out, bodyLengthBits());
        if (reliability().isReliable()) {
            ByteBufHelper.writeUnsignedMedium(out, reliableFrameIndex());
        }
        if (reliability().isSequenced()) {
            ByteBufHelper.writeUnsignedMedium(out, sequencedFrameIndex());
        }
        if (reliability().isOrdered()) {
            ByteBufHelper.writeUnsignedMedium(out, orderedFrameIndex());
            out.writeByte(orderChannel());
        }
        if (fragmented()) {
            out.writeInt(compoundSize());
            ByteBufHelper.writeUnsignedShort(out, compoundId());
            out.writeInt(compoundIndex());
        }
        out.writeBytes(body());
    }

    public int bodySize() {
        return (int) Math.ceil(bodyLengthBits() / 8.0);
    }
}