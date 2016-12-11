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

import com.google.auto.value.AutoValue;
import eu.xworlds.util.raknet.buffer.ByteBufHelper;
import io.netty.buffer.ByteBuf;

/**
 * <strong>Original documentation:</strong>
 *
 * <p>RakPeer - In a client/server environment, our connection request to the server has been
 * accepted.
 */
@AutoValue
public abstract class SndReceiptLoss implements RaknetMessage {

    public abstract long serial();

    @Override
    public byte id() {
        return (byte) RaknetMessageType.SND_RECEIPT_LOSS.ordinal();
    }

    @Override
    public void encodeInner(ByteBuf out) {
        ByteBufHelper.writeUnsignedInt(out, serial());
    }

    public static SndReceiptLoss decodeInner(ByteBuf in) {
        return new AutoValue_SndReceiptLoss(in.readUnsignedInt());
    }
}
