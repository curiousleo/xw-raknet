package eu.xworlds.util.raknet.buffer;

import io.netty.buffer.ByteBuf;
import org.apache.commons.codec.binary.Hex;

import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class ByteBufHelper {
    /**
     * Write an unsigned byte to byte buf
     *
     * @param value the unsigned byte value.
     */
    public static void writeUnsignedByte(ByteBuf buf, short value) {
        if (value < 0 || value > 0xff) {
            throw new IllegalArgumentException(value + " exceeds allowed size"); //$NON-NLS-1$
        }
        buf.writeByte(value);
    }

    /**
     * Write an unsigned short to byte buf
     *
     * @param value the unsigned short value.
     */
    public static void writeUnsignedShort(ByteBuf buf, int value) {
        if (value < 0 || value > 0xffff) {
            throw new IllegalArgumentException(value + " exceeds allowed size"); //$NON-NLS-1$
        }
        if (buf.order() == ByteOrder.BIG_ENDIAN) {
            buf.writeByte(value >> 8);
            buf.writeByte(value);
        } else {
            buf.writeByte(value);
            buf.writeByte(value >> 8);
        }
    }

    /**
     * writes an unsigned int to byte buf
     *
     * @param value the unsigned short value.
     */
    public static void writeUnsignedInt(ByteBuf buf, long value) {
        if (value < 0 || value > 0xffffffL) {
            throw new IllegalArgumentException(value + " exceeds allowed size"); //$NON-NLS-1$
        }
        if (buf.order() == ByteOrder.BIG_ENDIAN) {
            buf.writeByte((int) value >> 32);
            buf.writeByte((int) value >> 16);
            buf.writeByte((int) value >> 8);
            buf.writeByte((int) value);
        } else {
            buf.writeByte((int) value);
            buf.writeByte((int) value >> 8);
            buf.writeByte((int) value >> 16);
            buf.writeByte((int) value >> 32);
        }
    }

    /**
     * Writes an unsigned medium to byte buf
     *
     * @param value the unsigned medium value.
     */
    public static void writeUnsignedMedium(ByteBuf buf, int value) {
        if (value < 0 || value > 0xffffff) {
            throw new IllegalArgumentException(value + " exceeds allowed size"); //$NON-NLS-1$
        }
        if (buf.order() == ByteOrder.BIG_ENDIAN) {
            buf.writeByte(value >> 16);
            buf.writeByte(value >> 8);
            buf.writeByte(value);
        } else {
            buf.writeByte(value);
            buf.writeByte(value >> 8);
            buf.writeByte(value >> 16);
        }
    }

    /**
     * Writes string with unsigned short length and utf8 encoding.
     *
     * @param value string value
     */
    public static void writeString(ByteBuf buf, String value) {
        final byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        writeUnsignedShort(buf, bytes.length);
        buf.writeBytes(bytes);
    }

    public static long readIPv4Address(ByteBuf buf) {
        return buf.readUnsignedInt();
    }

    public static void writeIpv4Address(ByteBuf buf, long address) {
        writeUnsignedInt(buf, address);
    }

    public static long readTime(ByteBuf buf) {
        return buf.readLong();
    }

    public static void writeTime(ByteBuf buf, long time) {
        buf.writeLong(time);
    }

    public static long readGuid(ByteBuf buf) {
        return buf.readLong();
    }

    public static void writeGuid(ByteBuf buf, long time) {
        buf.writeLong(time);
    }

}
