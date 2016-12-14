package eu.xworlds.util.raknet.protocol;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public final class Constants {
    static final int BYTE_SIZE = 1;
    static final int BOOL_SIZE = 1;
    static final int SHORT_SIZE = 2;
    static final int UINT24LE_SIZE = 3;
    static final int INT_SIZE = 4;
    static final int LONG_SIZE = 8;
    public static final int MAGIC_SIZE = 16;
    static final int ADDRESS_SIZE = 7;
    static final int GUID_SIZE = 8;
    static final int TIME_SIZE = 8;

    public static final byte[] MAGIC;

    static {
        try {
            MAGIC = Hex.decodeHex("00ffff00fefefefefdfdfdfd12345678".toCharArray());
        } catch (DecoderException e) {
            throw new RuntimeException(e);
        }
    }

    private Constants() {}
}
