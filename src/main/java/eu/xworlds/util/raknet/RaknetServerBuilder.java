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

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import io.netty.channel.nio.NioEventLoopGroup;

/**
 * A builder to create common raknet servers.
 * 
 * <p>
 * Use this builder and the methods to specify the behaviour of the raknet server. At a minimum you should call the following methods:
 * </p>
 * <ul>
 * <li>{@link #addInterface(InetSocketAddress)} or {@link #addAllInterfaces(int, boolean, boolean)}</li>
 * </ul>
 * 
 * <p>
 * After configuring your server simply call {@link #build()} to create the server object. It will asynchronous bind to given interfaces.
 * </p>
 * 
 * <p>
 * You can optimize the behaviour and tune performance by setting your own sender and worker thread group or by setting the network buffer sizes.
 * </p>
 * 
 * @author mepeisen
 */
public class RaknetServerBuilder
{
    
    /**
     * The network addresses to be used.
     */
    private final List<InetSocketAddress>    interfaces         = new ArrayList<>();
    
    /**
     * the raknet server listeners.
     */
    private final List<RaknetServerListener> listeners          = new ArrayList<>();
    
    /**
     * The nio sender group
     */
    private NioEventLoopGroup                senderGroup;
    
    /**
     * The nio receiver group
     */
    private NioEventLoopGroup                receiverGroup;
    
    /**
     * The network recv buffer
     */
    private int                              recvBuffer         = 52 * 1024;        // 52k = BSD 4.3 limit
    
    /**
     * The network send buffer
     */
    private int                              sendBuffer         = 52 * 1024;        // 52k = BSD 4.3 limit
    
    /**
     * the session read timeout
     */
    private int                              sessionReadTimeout = 15 * 1000;        // within at least 15 seconds there should be a ping
    
    /**
     * Builds the server and starts connecting/binding in background.
     * 
     * @return the raknet server connecting in background.
     *         
     * @throws IllegalStateException
     *             thrown if there was no configured network interface.
     */
    public RaknetServer build()
    {
        if (this.interfaces.size() == 0)
        {
            throw new IllegalStateException("No interfaces available"); //$NON-NLS-1$
        }
        
        final InetSocketAddress[] addresses = this.interfaces.toArray(new InetSocketAddress[this.interfaces.size()]);
        final RaknetServerListener[] serverListeners = this.listeners.toArray(new RaknetServerListener[this.listeners.size()]);
        
        final NioEventLoopGroup sGroup = this.senderGroup == null ? new NioEventLoopGroup() : this.senderGroup;
        final NioEventLoopGroup rGroup = this.receiverGroup == null ? new NioEventLoopGroup() : this.receiverGroup;
        
        final RaknetServer result = new RaknetServer(addresses, serverListeners, this.recvBuffer, this.sendBuffer, sGroup, rGroup, this.sessionReadTimeout);
        return result;
    }
    
    /**
     * Sets the session read timeout in milliseconds
     * 
     * @param milliseconds
     *            amount of milliseconds a session will be alive if there is no network traffic
     * @return this builder
     */
    public RaknetServerBuilder setSessionReadTimeout(int milliseconds)
    {
        this.sessionReadTimeout = milliseconds;
        return this;
    }
    
    /**
     * Adds given interface to listen for incoming traffic
     * 
     * @param address
     *            ip address
     * @return this builder
     */
    public RaknetServerBuilder addInterface(InetSocketAddress address)
    {
        if (address == null)
        {
            throw new NullPointerException("address must not be null"); //$NON-NLS-1$
        }
        this.interfaces.add(address);
        return this;
    }
    
    /**
     * Adds all addresses fetched from local interfaces
     * 
     * @param port
     *            ip port to be used
     * @param ipv4
     *            {@code true} for ipv4 support
     * @param ipv6
     *            {@code true} for ipv6 support
     * @return this builder
     * @throws SocketException
     *             thrown if there was a problem querying the network interfaces.
     */
    public RaknetServerBuilder addAllInterfaces(int port, boolean ipv4, boolean ipv6) throws SocketException
    {
        for (final Enumeration<NetworkInterface> enNics = NetworkInterface.getNetworkInterfaces(); enNics.hasMoreElements();)
        {
            final NetworkInterface intf = enNics.nextElement();
            for (final Enumeration<InetAddress> enIps = intf.getInetAddresses(); enIps.hasMoreElements();)
            {
                final InetSocketAddress localAddress = new InetSocketAddress(enIps.nextElement(), port);
                if (localAddress.getAddress().getAddress().length == 4 && ipv4)
                {
                    // IPv4
                    this.interfaces.add(localAddress);
                }
                else if (localAddress.getAddress().getAddress().length == 16 && ipv6)
                {
                    // IPv6
                    this.interfaces.add(localAddress);
                }
                else
                {
                    // Logging
                    RaknetServer.LOGGER.warning("Unsupported network address found: " + localAddress); //$NON-NLS-1$
                }
            }
        }
        
        return this;
    }
    
    /**
     * Adds a listener for watching server events.
     * 
     * @param listener
     *            the listener to be added.
     * @return this builder
     */
    public RaknetServerBuilder addListener(RaknetServerListener listener)
    {
        this.listeners.add(listener);
        return this;
    }
    
    /**
     * Sets the network sender group to be used.
     * 
     * <p>
     * The sender group is responsible for sending network traffic to clients.
     * </p>
     * 
     * @param group
     *            sender thread group
     * @return this builder.
     */
    public RaknetServerBuilder setSenderGroup(NioEventLoopGroup group)
    {
        this.senderGroup = group;
        return this;
    }
    
    /**
     * Sets the network receiver group to be used.
     * 
     * <p>
     * The receiver group is responsible for receiving and parsing network traffic from clients.
     * </p>
     * 
     * @param group
     *            receiver thread group
     * @return this builder.
     */
    public RaknetServerBuilder setReceiverGroup(NioEventLoopGroup group)
    {
        this.receiverGroup = group;
        return this;
    }
    
    /**
     * Sets the SO_RCVBUF and SO_SNDBUF.
     * 
     * <p>
     * See description of network architecture for more details. A high value can minimize packet losses because a higher buffer means more traffic is cached before further packages are dropped.
     * However the maximum value is highly hardware and operating system specific. You may choose values of 32k up to 256k.
     * </p>
     * 
     * @param recvBuffer
     *            the recv buffer in size; minimum value is 10*1024
     * @param sendBuffer
     *            the send buffer in size; minimum value is 10*1024
     * @return this builder.
     */
    public RaknetServerBuilder setBufferSizes(int recvBuffer, int sendBuffer)
    {
        if (recvBuffer < 10 * 1024)
        {
            throw new IllegalArgumentException("recvBuffer must at least be 10*1024"); //$NON-NLS-1$
        }
        if (sendBuffer < 10 * 1024)
        {
            throw new IllegalArgumentException("sendBuffer must at least be 10*1024"); //$NON-NLS-1$
        }
        this.recvBuffer = recvBuffer;
        this.sendBuffer = sendBuffer;
        return this;
    }
    
}
