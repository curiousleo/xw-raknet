/*
    This file is part of "xWorlds mcpe server".

    "xWorlds mcpe server" is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    "nukkit xWorlds plugin" is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with "xWorlds mcpe server". If not, see <http://www.gnu.org/licenses/>.

 */

package eu.xworlds.util.raknet;

import java.net.InetSocketAddress;

/**
 * Wrapper class for inet socket con.
 * 
 * @author mepeisen
 *
 */
class InetSocketCon
{
    /** sender */
    private final InetSocketAddress from;
    /** receiver */
    private final InetSocketAddress to;
    
    /** pre calculated hash code */
    private final int hash;

    @Override
    public int hashCode()
    {
        return this.hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        InetSocketCon other = (InetSocketCon) obj;
        if (this.from == null)
        {
            if (other.from != null)
                return false;
        } else if (!this.from.equals(other.from))
            return false;
        if (this.to == null)
        {
            if (other.to != null)
                return false;
        } else if (!this.to.equals(other.to))
            return false;
        return true;
    }

    /**
     * @param from sender
     * @param to receiver
     */
    public InetSocketCon(InetSocketAddress from, InetSocketAddress to)
    {
        this.from = from;
        this.to = to;
        
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.from == null) ? 0 : this.from.hashCode());
        result = prime * result + ((this.to == null) ? 0 : this.to.hashCode());
        this.hash = result;
    }

    /**
     * @return the from
     */
    public InetSocketAddress getFrom()
    {
        return this.from;
    }

    /**
     * @return the to
     */
    public InetSocketAddress getTo()
    {
        return this.to;
    }

    @Override
    public String toString()
    {
        return "InetSocketCon [from=" + this.from + ", to=" + this.to + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

}
