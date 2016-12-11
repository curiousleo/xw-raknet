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

import static eu.xworlds.util.raknet.protocol.RaknetMessageType.PUBLIC_KEY_MISMATCH;

import io.netty.buffer.ByteBuf;

/**
 * <strong>Original documentation:</strong>
 *
 * <p>RakPeer - Same as ID_ADVERTISE_SYSTEM, but intended for internal use rather than being passed
 * to the user. Second byte indicates type. Used currently for NAT punchthrough for receiver port
 * advertisement. See ID_NAT_ADVERTISE_RECIPIENT_PORT.
 */
public class PublicKeyMismatch implements RaknetMessage {

    private static final PublicKeyMismatch INSTANCE = new PublicKeyMismatch();

    @Override
    public byte id() {
        return (byte) PUBLIC_KEY_MISMATCH.ordinal();
    }

    public static PublicKeyMismatch decodeBody(ByteBuf in) {
        return INSTANCE;
    }
}
