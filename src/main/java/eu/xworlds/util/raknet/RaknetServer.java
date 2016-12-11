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

import java.lang.management.ManagementFactory;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * The server class for raknet servers.
 * 
 * <p>
 * To create an instance see class {@link RaknetServerBuilder}.
 * </p>
 * 
 * <p>
 * You can wait for the server to terminate by calling the loop method. If you decide to let the server do its work in background you need not do anything after building it.
 * </p>
 * 
 * <p>
 * A call to {@link #close()} will terminate the server asynchronous.
 * </p>
 * 
 * @author mepeisen
 */
public class RaknetServer
{
    
    /** the logger instance */
    static final Logger                                      LOGGER            = Logger.getLogger(RaknetServer.class.getName());
    
    // /**
    // * The addresses to bind to.
    // */
    // private InetSocketAddress[] addresses;
    
    /**
     * The server listeners.
     */
    RaknetServerListener[]                                   serverListeners;
    
    /**
     * the recv buffer size.
     */
    private int                                              recvBuffer;
    
    /**
     * the send buffer size.
     */
    private int                                              sendBuffer;
    
    /**
     * The sender nio worker group.
     */
    private NioEventLoopGroup                                senderGroup;
    
    /**
     * The receiver nio working group.
     */
    private NioEventLoopGroup                                receiverGroup;
    
    /**
     * The futures for binding.
     */
    final Collection<ChannelFuture>                          listenFutures     = new ConcurrentLinkedQueue<>();
    
    /**
     * The futures for closing.
     */
    final Collection<ChannelFuture>                          closeFutures      = new ConcurrentLinkedQueue<>();
    
    /**
     * The channels where network traffic is incomming.
     */
    final Map<InetSocketAddress, Channel>                    incommingChannels = new HashMap<>();
    
    /**
     * The closing flag
     */
    boolean                                                  isClosing         = false;
    
    /**
     * The trace flag.
     */
    boolean                                                  isTracing         = LOGGER.isLoggable(Level.FINEST);
    
    /** The time this server started */
    final long                                               startupTime       = ManagementFactory.getRuntimeMXBean().getUptime();
    
    /**
     * The session cache.
     */
    private final LoadingCache<InetSocketCon, RaknetSession> sessions;
    
    /**
     * The factories to create netty channel handlers
     */
    final RaknetPipelineFactory[]                            pipelineFactories;
    
    /**
     * the factories to register additional raknet message handlers
     */
    final RaknetHandlerFactory[]                             handlerFactories;
    
    /**
     * the factories to register additional raknet message classes
     */
    final RaknetMessageFactory[]                             messageFactories;
    
    /**
     * Hidden constructor.
     * 
     * @param addresses
     *            the addresses to bind to.
     * @param serverListeners
     *            the listeners.
     * @param recvBuffer
     *            the recv buffer size.
     * @param sendBuffer
     *            the send buffer size.
     * @param sGroup
     *            the sender nio group.
     * @param rGroup
     *            the receiver nio group.
     * @param sessionReadTimeout
     *            the raknet session read timeout in milliseconds
     * @param pFactories
     *            the factories to create additional raknet pipeline handlers
     * @param hFactories
     *            the factories to register additional raknet message handlers
     * @param mFactories
     *            the factories to register additional raknet message classes
     */
    RaknetServer(InetSocketAddress[] addresses, RaknetServerListener[] serverListeners, int recvBuffer, int sendBuffer, NioEventLoopGroup sGroup, NioEventLoopGroup rGroup, int sessionReadTimeout,
            RaknetPipelineFactory[] pFactories, RaknetMessageFactory[] mFactories, RaknetHandlerFactory[] hFactories)
    {
        // this.addresses = addresses;
        this.serverListeners = serverListeners;
        this.recvBuffer = recvBuffer;
        this.sendBuffer = sendBuffer;
        this.senderGroup = sGroup;
        this.receiverGroup = rGroup;
        
        this.pipelineFactories = pFactories;
        this.handlerFactories = hFactories;
        this.messageFactories = mFactories;
        
        this.sessions = CacheBuilder.newBuilder().maximumSize(1000). // TODO read from builder
                expireAfterAccess(sessionReadTimeout, TimeUnit.MILLISECONDS).removalListener(new SessionRemovalListener()).build(new SessionCacheLoader());
                
        final Bootstrap b = new Bootstrap();
        b.group(this.receiverGroup).channel(NioDatagramChannel.class).option(ChannelOption.SO_REUSEADDR, Boolean.TRUE).option(ChannelOption.SO_RCVBUF, Integer.valueOf(this.recvBuffer))
                .option(ChannelOption.SO_SNDBUF, Integer.valueOf(this.sendBuffer)).option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(this.recvBuffer))
                .handler(new RaknetInitializer());
                
        for (final InetSocketAddress address : addresses)
        {
            this.listenFutures.add(b.bind(address).addListener(new BindListener()));
        }
    }
    
    /**
     * Terminates the server.
     */
    public void close()
    {
        synchronized (this.incommingChannels)
        {
            this.isClosing = true;
            for (final Channel channel : this.incommingChannels.values())
            {
                channel.close();
            }
        }
    }
    
    /**
     * Waits till the server terminates.
     */
    public void loop()
    {
        // wait for successful bind if there are some listen futures.
        final Object[] listenArray = this.listenFutures.toArray();
        for (final Object listenFuture : listenArray)
        {
            ((ChannelFuture) listenFuture).syncUninterruptibly();
        }
        
        // wait for successful close that indicates the server was closed.
        final Object[] closeArray = this.closeFutures.toArray();
        for (final Object closeFuture : closeArray)
        {
            ((ChannelFuture) closeFuture).syncUninterruptibly();
        }
    }
    
    /**
     * Listens for bind connections.
     *
     */
    private final class BindListener implements GenericFutureListener<ChannelFuture>
    {
        
        /**
         * Constructor.
         */
        protected BindListener()
        {
            // empty
        }
        
        /**
         * @see io.netty.util.concurrent.GenericFutureListener#operationComplete(io.netty.util.concurrent.Future)
         */
        @Override
        public void operationComplete(ChannelFuture future) throws Exception
        {
            final Channel channel = future.channel();
            if (future.isSuccess())
            {
                RaknetServer.this.closeFutures.add(channel.closeFuture());
                channel.closeFuture().addListener(new CloseListener());
                RaknetServer.this.listenFutures.remove(future);
                synchronized (RaknetServer.this.incommingChannels)
                {
                    if (RaknetServer.this.isClosing)
                    {
                        channel.close();
                    }
                    else
                    {
                        RaknetServer.this.incommingChannels.put((InetSocketAddress) channel.localAddress(), channel);
                        for (final RaknetServerListener listener : RaknetServer.this.serverListeners)
                        {
                            listener.onBindSucceeded(channel);
                        }
                    }
                }
            }
            else
            {
                for (final RaknetServerListener listener : RaknetServer.this.serverListeners)
                {
                    listener.onBindFailed(channel);
                }
            }
        }
        
    }
    
    /**
     * Listens for closed connections.
     *
     */
    private final class CloseListener implements GenericFutureListener<ChannelFuture>
    {
        
        /**
         * Constructor.
         */
        protected CloseListener()
        {
            // empty
        }
        
        @Override
        public void operationComplete(ChannelFuture future) throws Exception
        {
            final Channel channel = future.channel();
            RaknetServer.this.closeFutures.remove(future);
            for (final RaknetServerListener listener : RaknetServer.this.serverListeners)
            {
                listener.onChannelClosed(channel);
            }
        }
        
    }
    
    /**
     * Initializer for the raknet channel.
     *
     */
    private final class RaknetInitializer extends ChannelInitializer<NioDatagramChannel>
    {
        
        /**
         * Constructor.
         */
        protected RaknetInitializer()
        {
            // empty
        }
        
        @Override
        protected void initChannel(NioDatagramChannel ch) throws Exception
        {
            final ChannelPipeline p = ch.pipeline();
            p.addLast(new ConnectionHandler(RaknetServer.this.serverListeners));
            p.addLast(new RaknetDecoder());
            if (RaknetServer.this.isTracing)
            {
                p.addLast(new RaknetTrace());
            }
            p.addLast(new RaknetHandler(RaknetServer.this, RaknetServer.this.handlerFactories));
            
            for (final RaknetPipelineFactory factory : RaknetServer.this.pipelineFactories)
            {
                final ChannelHandler[] handlers = factory.getHandlerPipeline(RaknetServer.this);
                if (handlers != null)
                {
                    for (final ChannelHandler handler : handlers)
                    {
                        p.addLast(handler);
                    }
                }
            }
        }
        
    }
    
    /**
     * Returns the racnet time in milliseconds.
     * 
     * @return racnet time in milliseconds
     */
    public long getRaknetTime()
    {
        return ManagementFactory.getRuntimeMXBean().getUptime() - this.startupTime;
    }
    
    /**
     * Helper class to watch for network timeouts/ inactive sessions
     */
    private final class SessionRemovalListener implements RemovalListener<InetSocketCon, RaknetSession>
    {
        
        /**
         * 
         */
        public SessionRemovalListener()
        {
            // empty
        }
        
        @Override
        public void onRemoval(RemovalNotification<InetSocketCon, RaknetSession> notification)
        {
            for (final RaknetServerListener listener : RaknetServer.this.serverListeners)
            {
                listener.onRemoval(notification.getValue());
            }
        }
        
    }
    
    /**
     * Gets the session for associated connection; creates it if needed.
     * 
     * @param from
     *            sender address
     * @param to
     *            receiver address
     * @return raknet session or {@code null} if the address is blacklisted
     * @throws IllegalStateException
     *             thrown if there was a problem accessing the cache
     */
    public RaknetSession getOrCreateSession(final InetSocketAddress from, final InetSocketAddress to)
    {
        try
        {
            return this.sessions.get(new InetSocketCon(from, to));
        }
        catch (ExecutionException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Gets the session for associated connection
     * 
     * @param from
     *            sender address
     * @param to
     *            receiver address
     * @return raknet session or {@code null} if it was not found
     */
    public RaknetSession getSessionIfPresent(final InetSocketAddress from, final InetSocketAddress to)
    {
        return this.sessions.getIfPresent(new InetSocketCon(from, to));
    }
    
    /**
     * Helper class to build new sessions on demand
     */
    private final class SessionCacheLoader extends CacheLoader<InetSocketCon, RaknetSession>
    {
        
        /**
         * 
         */
        public SessionCacheLoader()
        {
            // empty
        }
        
        @Override
        public RaknetSession load(InetSocketCon key) throws Exception
        {
            final RaknetSession session = new RaknetSessionImpl(key);
            for (final RaknetServerListener listener : RaknetServer.this.serverListeners)
            {
                if (!listener.onNewSession(session))
                {
                    return null;
                }
            }
            return session;
        }
        
    }
    
}
