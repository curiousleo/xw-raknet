package eu.xworlds.util.raknet.protocol;

import io.netty.buffer.ByteBuf;

@FunctionalInterface
public interface Decoder {
    RaknetMessage decode(ByteBuf buf) throws DecodeException;

    /**
     * Creates a decoder for the given message id.
     * @param id the Raknet message id (first byte of the message)
     * @return a decoder for the message id, or {@code null} if the message id is unknown
     */
    static Decoder of(byte id) {
        if (id >= 0 && id < MessageType.values().length) {
            return MessageType.values()[id].decoder;
        }
        if (id >= (byte) 0x80 && id <= (byte) 0x8d) {
            return (ByteBuf in) -> FrameSet.decodeBody(id, in);
        }
        return null;
    }

    enum MessageType {
        // See https://github.com/OculusVR/RakNet/blob/7fe6cdd0ee411a742d01cd020d4bd885f39e1cf1/Source/MessageIdentifiers.h#L51
        CONNECTED_PING(ConnectedPing::decodeBody),
        UNCONNECTED_PING(UnconnectedPing::decodeBody),
        UNCONNECTED_PING_OPEN_CONNECTIONS(UnconnectedPingOpenConnections::decodeBody),
        CONNECTED_PONG(ConnectedPong::decodeBody),
        DETECT_LOST_CONNECTIONS(DetectLostConnections::decodeBody),
        OPEN_CONNECTION_REQUEST_1(OpenConnectionRequest1::decodeBody),
        OPEN_CONNECTION_REPLY_1(OpenConnectionReply1::decodeBody),
        OPEN_CONNECTION_REQUEST_2(OpenConnectionRequest2::decodeBody),
        OPEN_CONNECTION_REPLY_2(OpenConnectionReply2::decodeBody),
        CONNECTION_REQUEST(ConnectionRequest::decodeBody),
        REMOTE_SYSTEM_REQUIRES_PUBLIC_KEY(RemoteSystemRequiresPublicKey::decodeBody),
        OUR_SYSTEM_REQUIRES_SECURITY(OurSystemRequiresSecurity::decodeBody),
        PUBLIC_KEY_MISMATCH(PublicKeyMismatch::decodeBody),
        OUT_OF_BAND_INTERNAL(OutOfBandInternal::decodeBody),
        SND_RECEIPT_ACKED(SndReceiptAcked::decodeBody),
        SND_RECEIPT_LOSS(SndReceiptLoss::decodeBody);

        private final Decoder decoder;

        MessageType(Decoder decoder) {
            this.decoder = decoder;
        }

        public RaknetMessage decodeBody(ByteBuf in) throws DecodeException {
            return decoder.decode(in);
        }
    }
}
