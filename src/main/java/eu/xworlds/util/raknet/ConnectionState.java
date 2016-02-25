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

/**
 * The connection state. The docu is taken from original RakNet sources.
 * 
 * @author mepeisen
 */
public enum ConnectionState
{
    /** Connect() was called, but the process hasn't started yet */
    IS_PENDING,
    /** Processing the connection attempt */
    IS_CONNECTING,
    /** Is connected and able to communicate */
    IS_CONNECTED,
    /** Was connected, but will disconnect as soon as the remaining messages are delivered */
    IS_DISCONNECTING,
    /** A connection attempt failed and will be aborted */
    IS_SILENTLY_DISCONNECTING,
    /** No longer connected */
    IS_DISCONNECTED,
    /** Was never connected, or else was disconnected long enough ago that the entry has been discarded */
    IS_NOT_CONNECTED
    
}