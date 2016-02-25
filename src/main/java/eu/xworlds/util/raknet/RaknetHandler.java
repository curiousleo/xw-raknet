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
package eu.xworlds.util.raknet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.xworlds.util.raknet.protocol.TargetedMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

/**
 * Pipeline for incoming server connections.
 * 
 * @author mepeisen
 */
class RaknetHandler extends MessageToMessageDecoder<TargetedMessage>
{
    
    /** the well known raknet message handler */
    private final Map<Class<? extends TargetedMessage>, RaknetMessageHandler<? extends TargetedMessage>> handlers = new HashMap<>();
    
    /** the raknet server */
    private final RaknetServer                                                                           server;
    
    /**
     * The handler for incoming connections.
     * 
     * @param server
     *            the raknet server
     * @param serverListeners
     *            the server listeners.
     */
    public RaknetHandler(RaknetServer server, RaknetServerListener[] serverListeners)
    {
        this.server = server;
        this.handlers.putAll(this.getDefaulHandlers());
        for (final RaknetServerListener listener : serverListeners)
        {
            final Map<Class<? extends TargetedMessage>, RaknetMessageHandler<? extends TargetedMessage>> messageClasses = listener.getMessageHandlers();
            this.handlers.putAll(messageClasses);
        }
    }
    
    /**
     * Returns the default message handlers.
     * 
     * @return map with default message handlers for managing the protocol.
     */
    private Map<Class<? extends TargetedMessage>, RaknetMessageHandler<? extends TargetedMessage>> getDefaulHandlers()
    {
        final Map<Class<? extends TargetedMessage>, RaknetMessageHandler<? extends TargetedMessage>> result = new HashMap<>();
        // TODO Auto-generated method stub
        return result;
    }
    
    @Override
    protected void decode(ChannelHandlerContext ctx, TargetedMessage msg, List<Object> out) throws Exception
    {
        final RaknetMessageHandler<? extends TargetedMessage> handler = this.handlers.get(msg.getClass());
        if (handler == null)
        {
            // re-add message
            out.add(msg);
        }
        else
        {
            // handle message
            this.decode(handler, msg, out);
        }
    }
    
    /**
     * @param handler
     *            the handler
     * @param msg
     *            the incoming message
     * @param out
     *            the outgoing message
     */
    @SuppressWarnings("unchecked")
    private <T extends TargetedMessage> void decode(RaknetMessageHandler<T> handler, TargetedMessage msg, List<Object> out)
    {
        final RaknetSession session = this.server.getOrCreateSession(msg.getSender(), msg.getReceiver());
        handler.handle((T) msg, session, out);
    }
    
}
