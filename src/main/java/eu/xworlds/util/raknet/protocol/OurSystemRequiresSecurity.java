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

import static eu.xworlds.util.raknet.protocol.Decoder.MessageType.OUR_SYSTEM_REQUIRES_SECURITY;

import io.netty.buffer.ByteBuf;

/**
 * <strong>Original documentation:</strong>
 *
 * <p>RakPeer - Wrong public key passed to RakPeerInterface::Connect().
 */
public class OurSystemRequiresSecurity implements RaknetMessage {

    private static final OurSystemRequiresSecurity INSTANCE = new OurSystemRequiresSecurity();

    @Override
    public byte id() {
        return (byte) OUR_SYSTEM_REQUIRES_SECURITY.ordinal();
    }

    public static OurSystemRequiresSecurity decodeBody(ByteBuf in) {
        return INSTANCE;
    }
}
