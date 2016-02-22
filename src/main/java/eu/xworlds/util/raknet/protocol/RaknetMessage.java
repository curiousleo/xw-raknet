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

/**
 * Base interface for raknet messages.
 * 
 * @author mepeisen
 */
public interface RaknetMessage
{
    
    /** raknet offline data id or the "MAGIC" */
    byte[] OFFLINE_MESSAGE_DATA_ID = {
            0x00,(byte)0xFF,(byte)0xFF,0x00,
            (byte)0xFE,(byte)0xFE,(byte)0xFE,(byte)0xFE,
            (byte)0xFD,(byte)0xFD,(byte)0xFD,(byte)0xFD,
            0x12,0x34,0x56,0x78};
    
    /** constant for easy handshake */
    int EASYHANDSHAKE_BITS = 256;
    /** constant for easy handshake */
    int EASYHANDSHAKE_BYTES = EASYHANDSHAKE_BITS / 8;
    
    /** constant for easy handshake */
    int EASYHANDSHAKE_PUBLIC_KEY_BYTES = EASYHANDSHAKE_BYTES * 2;
    /** constant for easy handshake */
    int EASYHANDSHAKE_PRIVATE_KEY_BYTES = EASYHANDSHAKE_BYTES;
    /** constant for easy handshake */
    int EASYHANDSHAKE_CHALLENGE_BYTES = EASYHANDSHAKE_BYTES * 2; // Packet # 1 in handshake, sent to server
    /** constant for easy handshake */
    int EASYHANDSHAKE_ANSWER_BYTES = EASYHANDSHAKE_BYTES * 4; // Packet # 2 in handshake, sent to client
    /** constant for easy handshake */
    int EASYHANDSHAKE_PROOF_BYTES = EASYHANDSHAKE_BYTES; // Packet # 3 in handshake, sent to server
    /** constant for easy handshake */
    int EASYHANDSHAKE_IDENTITY_BYTES = EASYHANDSHAKE_BYTES * 5; // [optional] Packet # 3 in handshake, sent to server, proves identity of client also
    
    /**
     * gets the raknet message id.
     * @return Raknet message id.
     */
    byte getId();
        
    /**
     * Encodes this messge to byte buf
     * 
     * @return encoded messge
     */
    ByteBuf encode();
    
}
