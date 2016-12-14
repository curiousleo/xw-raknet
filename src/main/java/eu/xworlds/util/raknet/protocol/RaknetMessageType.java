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

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public enum RaknetMessageType {
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

    @FunctionalInterface
    interface Decoder {
        RaknetMessage apply(ByteBuf buf) throws DecodeException;
    }

    private final Decoder decoder;

    RaknetMessageType(Decoder decoder) {
        this.decoder = decoder;
    }

    public static RaknetMessageType of(byte id) {
        if (id < 0 || id >= RaknetMessageType.values().length) {
            return null;
        }
        return RaknetMessageType.values()[id];
    }

    public RaknetMessage decodeBody(ByteBuf in) throws DecodeException {
        return decoder.apply(in);
    }
}
