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

enum RaknetMessageType {
    // See https://github.com/OculusVR/RakNet/blob/7fe6cdd0ee411a742d01cd020d4bd885f39e1cf1/Source/MessageIdentifiers.h#L51
    CONNECTED_PING(ConnectedPing::decodeInner),
    UNCONNECTED_PING(UnconnectedPing::decodeInner),
    UNCONNECTED_PING_OPEN_CONNECTIONS(UnconnectedPingOpenConnections::decodeInner),
    CONNECTED_PONG(ConnectedPong::decodeInner),
    DETECT_LOST_CONNECTIONS(DetectLostConnections::decodeInner),
    OPEN_CONNECTION_REQUEST_1(OpenConnectionRequest1::decodeInner),
    OPEN_CONNECTION_REPLY_1(OpenConnectionReply1::decodeInner),
    OPEN_CONNECTION_REQUEST_2(OpenConnectionRequest2::decodeInner),
    OPEN_CONNECTION_REPLY_2(OpenConnectionReply2::decodeInner),
    CONNECTION_REQUEST(ConnectionRequest::decodeInner),
    REMOTE_SYSTEM_REQUIRES_PUBLIC_KEY(RemoteSystemRequiresPublicKey::decodeInner),
    OUR_SYSTEM_REQUIRES_SECURITY(OurSystemRequiresSecurity::decodeInner),
    PUBLIC_KEY_MISMATCH(PublicKeyMismatch::decodeInner),
    OUT_OF_BAND_INTERNAL(OutOfBandInternal::decodeInner),
    SND_RECEIPT_ACKED(SndReceiptAcked::decodeInner),
    SND_RECEIPT_LOSS(SndReceiptLoss::decodeInner);

    private static final List<RaknetMessageType> RAKNET_MESSAGE_TYPES =
            Arrays.asList(RaknetMessageType.values());

    private final Function<ByteBuf, RaknetMessage> decoder;

    RaknetMessageType(Function<ByteBuf, RaknetMessage> decoder) {
        this.decoder = decoder;
    }

    static RaknetMessageType of(byte id) {
        return RAKNET_MESSAGE_TYPES.get(id);
    }

    RaknetMessage decodeInner(ByteBuf in) {
        return decoder.apply(in);
    }
}
