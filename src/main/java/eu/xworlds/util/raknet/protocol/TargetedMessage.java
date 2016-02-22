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
 * Abstract base class that can be used for raknet messages.
 * 
 * @author mepeisen
 *        
 */
public abstract class TargetedMessage extends BaseMessage
{
    
    /** the sender */
    private final InetSocketAddress sender;
    
    /** the receiver */
    private final InetSocketAddress receiver;
    
    /**
     * Constructor for outgoing messages.
     * @param sender message sender.
     * @param receiver message receiver.
     */
    public TargetedMessage(InetSocketAddress sender, InetSocketAddress receiver)
    {
        this.sender = sender;
        this.receiver = receiver;
    }
    
    /**
     * Constructor for messages with targets
     * @param buf incoming data
     * @param sender message sender
     * @param receiver message receiver
     */
    public TargetedMessage(ByteBuf buf, InetSocketAddress sender, InetSocketAddress receiver)
    {
        this.sender = sender;
        this.receiver = receiver;
        this.parseMessage(buf);
    }
    
    /**
     * Parses incoming message
     * @param buf parse message.
     */
    protected abstract void parseMessage(ByteBuf buf);
    
    /**
     * @return the sender
     */
    public InetSocketAddress getSender()
    {
        return this.sender;
    }

    /**
     * @return the receiver
     */
    public InetSocketAddress getReceiver()
    {
        return this.receiver;
    }
    
}
