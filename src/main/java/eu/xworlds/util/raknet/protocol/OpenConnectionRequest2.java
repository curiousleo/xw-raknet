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

import io.netty.buffer.ByteBuf;

/**
 * Message "OpenConnectionRequest2".
 * 
 * <p><b>The following docu is taken from procotol information</b>:</p>
 * 
 * <p>
 * C2S: Header(1), OfflineMesageID(16), Cookie(4, if HasSecurity is true on the server), clientSupportsSecurity(1 bit),
 * handshakeChallenge (if has security on both server and client), remoteBindingAddress(6), MTU(2), client GUID(8)
 * Connection slot allocated if cookie is valid, server is not full, GUID and IP not already in use.
 * </p>
 * 
 * @author mepeisen
 */
public class OpenConnectionRequest2 extends TargetedMessage
{
    
    /** the raknet message id */
    public static final byte ID = 0x07;
    
    /**
     * Constructor for incoming message.
     * @param buf message data
     * @param sender message sender.
     * @param receiver message receiver.
     */
    public OpenConnectionRequest2(ByteBuf buf, InetSocketAddress sender, InetSocketAddress receiver)
    {
        super(buf, sender, receiver);
    }

    /**
     * Constructor for outgoing message.
     * @param sender message sender.
     * @param receiver message receiver.
     */
    public OpenConnectionRequest2(InetSocketAddress sender, InetSocketAddress receiver)
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
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    protected void parseMessage(ByteBuf buf)
    {
        // TODO Auto-generated method stub
        
    }
    
}
