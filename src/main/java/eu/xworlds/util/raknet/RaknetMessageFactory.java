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

import java.util.Map;

import eu.xworlds.util.raknet.protocol.RaknetMessage;

/**
 * A factory interface to register additional raknet messages during server startup.
 * 
 * @author mepeisen
 */
public interface RaknetMessageFactory
{
    
    /**
     * Return the message classes, mapped by identification byte.
     * 
     * <p>
     * Each class must have a constructor with 3 arguments: {@code ByteBuf buf, InetSocketAddress sender, InetSocketAddress receiver}
     * </p>
     * 
     * @return messge class map
     */
    Map<Byte, Class<? extends RaknetMessage>> getMessageClasses();
    
}
