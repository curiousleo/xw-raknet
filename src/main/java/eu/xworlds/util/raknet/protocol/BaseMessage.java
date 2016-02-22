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

import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Hex;

import io.netty.buffer.ByteBuf;

/**
 * Abstract base class that can be used for raknet messages.
 * 
 * <p>
 * Each message should have a toString for tracing network messages.
 * </p>
 * 
 * @author mepeisen
 *        
 */
public abstract class BaseMessage implements RaknetMessage
{
    
    /**
     * writes an unsigned byte to byte buf
     * 
     * @param target target byte buffer.
     * @param value the unsigned byte value.
     */
    protected void writeUnsignedByte(ByteBuf target, short value)
    {
        if (value < 0 || value > 0xff)
        {
            throw new IllegalArgumentException(value + " exceeds allowed size"); //$NON-NLS-1$
        }
        target.writeByte(value);
    }
    
    /**
     * writes an unsigned short to byte buf
     * 
     * @param target target byte buffer.
     * @param value the unsigned short value.
     */
    protected void writeUnsignedShort(ByteBuf target, int value)
    {
        if (value < 0 || value > 0xffff)
        {
            throw new IllegalArgumentException(value + " exceeds allowed size"); //$NON-NLS-1$
        }
        if (target.order() == ByteOrder.BIG_ENDIAN)
        {
            target.writeByte(value >> 8);
            target.writeByte(value);
        }
        else
        {
            target.writeByte(value);
            target.writeByte(value >> 8);
        }
    }
    
    /**
     * writes an unsigned medium to byte buf
     * 
     * @param target target byte buffer.
     * @param value the unsigned medium value.
     */
    protected void writeUnsignedMedium(ByteBuf target, int value)
    {
        if (value < 0 || value > 0xffffff)
        {
            throw new IllegalArgumentException(value + " exceeds allowed size"); //$NON-NLS-1$
        }
        if (target.order() == ByteOrder.BIG_ENDIAN)
        {
            target.writeByte(value >> 16);
            target.writeByte(value >> 8);
            target.writeByte(value);
        }
        else
        {
            target.writeByte(value);
            target.writeByte(value >> 8);
            target.writeByte(value >> 16);
        }
    }
    
    /**
     * Reads string from byte buf with usigned short length and utf8 encoding.
     * 
     * @param src source byte buffer
     * @return string
     */
    protected String readStringUtf8UShort(ByteBuf src)
    {
        final int length = src.readUnsignedShort();
        return new String(src.readBytes(length).array(), StandardCharsets.UTF_8);
    }
    
    /**
     * Writes string to byte buf with usigned short length and utf8 encoding.
     * 
     * @param target the target byte buffer
     * @param value string value
     */
    protected void writeString(ByteBuf target, String value)
    {
        final byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        this.writeUnsignedShort(target, bytes.length);
        target.writeBytes(bytes);
    }
    
    /**
     * Converts given byte array to hex (null-safe)
     * @param buf byte array
     * @return string representation
     */
    protected String tohex(byte[] buf)
    {
        if (buf == null)
        {
            return "null"; //$NON-NLS-1$
        }
        return Hex.encodeHexString(buf);
    }
    
}
