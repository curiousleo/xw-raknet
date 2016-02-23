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

import java.net.InetSocketAddress;
import java.nio.ByteOrder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Message "RemoteSystemRequiresPublicKey".
 * 
 * <p><b>The following docu is taken from procotol information</b>:</p>
 * 
 * <p>
 * RakPeer - Remote system requires secure connections, pass a public key to RakPeerInterface::Connect()
 * </p>
 * 
 * @author mepeisen
 */
public class RemoteSystemRequiresPublicKey extends TargetedMessage
{
    
    /** the raknet message id */
    public static final byte ID = 0x0A;
    
    /**
     * Possible error types
     */
    public enum ErrorType
    {
        /** ConnectionReply2 package did not send handshake but we need security */
        ServerPublicKeyMissing,
        /** Client did not sent a public key during connect */
        ClientIdentityMissing,
        /** Client sent a public key but the ident was invalid */
        ClientIdentityInvalid
    }
    
    /** the error type */
    private ErrorType error;
    
    /**
     * @return the error
     */
    public ErrorType getError()
    {
        return this.error;
    }

    /**
     * @param error the error to set
     */
    public void setError(ErrorType error)
    {
        this.error = error;
    }

    /**
     * Constructor for incoming message.
     * @param buf message data
     * @param sender message sender.
     * @param receiver message receiver.
     */
    public RemoteSystemRequiresPublicKey(ByteBuf buf, InetSocketAddress sender, InetSocketAddress receiver)
    {
        super(buf, sender, receiver);
    }

    /**
     * Constructor for outgoing message.
     * @param sender message sender.
     * @param receiver message receiver.
     */
    public RemoteSystemRequiresPublicKey(InetSocketAddress sender, InetSocketAddress receiver)
    {
        super(sender, receiver);
    }

    @Override
    public byte getId()
    {
        return ID;
    }
    
    @Override
    public ByteBuf encode()
    {
        final ByteBuf buf = Unpooled.buffer(1 + 1);
        buf.order(ByteOrder.BIG_ENDIAN);
        buf.writeByte(ID);
        buf.writeByte(this.error.ordinal());
        return buf;
    }
    
    @Override
    protected void parseMessage(ByteBuf buf)
    {
        this.error = ErrorType.values()[buf.readByte()];
    }

    @Override
    public String toString()
    {
        return "RemoteSystemRequiresPublicKey [error=" + this.error + "]"; //$NON-NLS-1$ //$NON-NLS-2$
    }
    
}
