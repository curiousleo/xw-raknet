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
 * Connection attempt result.
 * 
 * @author mepeisen
 */
public enum ConnectionAttemptResult
{
    
    /** */
    CONNECTION_ATTEMPT_STARTED,
    /** */
    INVALID_PARAMETER,
    /** */
    CANNOT_RESOLVE_DOMAIN_NAME,
    /** */
    ALREADY_CONNECTED_TO_ENDPOINT,
    /** */
    CONNECTION_ATTEMPT_ALREADY_IN_PROGRESS,
    /** */
    SECURITY_INITIALIZATION_FAILED
    
}
