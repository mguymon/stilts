package org.jboss.stilts.server;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.transaction.TransactionManager;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.socket.ServerSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.util.VirtualExecutorService;
import org.jboss.stilts.base.DefaultServerEnvironment;
import org.jboss.stilts.logging.Logger;
import org.jboss.stilts.logging.LoggerManager;
import org.jboss.stilts.logging.SimpleLoggerManager;
import org.jboss.stilts.protocol.StompPipelineFactory;
import org.jboss.stilts.spi.StompProvider;
import org.jboss.stilts.spi.StompServerEnvironment;

public class BasicStompServer {
    
    public static final int DEFAULT_PORT = 8675;

    public BasicStompServer() {
        this( DEFAULT_PORT );
    }
    
    /**
     * Construct with a port.
     * 
     * @param port The listen port to bind to.
     */
    public BasicStompServer(int port) {
        this.port = port;
    }

    /**
     * Retrieve the bind port.
     * 
     * @return The bind port.
     */
    public int getPort() {
        return this.port;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    public Executor getExecutor() {
        return this.executor;
    }
    
    public void setLoggerManager(LoggerManager loggerManager) {
        this.loggerManager = loggerManager;
    }
    
    public LoggerManager getLoggerManager() {
        return this.loggerManager;
    }
    
    public void setStompProvider(StompProvider stompProvider) {
        this.stompProvider = stompProvider;
    }
    
    public  StompProvider getStompProvider() throws Exception {
        return this.stompProvider;
    }

    protected StompServerEnvironment getServerEnvironment() {
        DefaultServerEnvironment env = new DefaultServerEnvironment();
        env.setTransactionManager( this.transactionManager );
        return env;
    }
    
    /**
     * Start this server.
     * 
     */
    public void start() throws Exception {
        if ( this.loggerManager == null ) {
            this.loggerManager = SimpleLoggerManager.DEFAULT_INSTANCE;
        }
        
        this.log = this.loggerManager.getLogger( "server" );
        
        if (this.executor == null) {
            this.executor = Executors.newFixedThreadPool( 2 );
        }
        
        ServerBootstrap bootstrap = createServerBootstrap();
        this.channel = bootstrap.bind( new InetSocketAddress( this.port ) );
    }

    protected ServerBootstrap createServerBootstrap() throws Exception {
        ServerBootstrap bootstrap = new ServerBootstrap( createChannelFactory() );
        bootstrap.setOption( "reuseAddress", true );
        
        bootstrap.setPipelineFactory( new StompPipelineFactory( getStompProvider(), this.loggerManager ) );
        return bootstrap;
    }
    
    protected ServerSocketChannelFactory createChannelFactory() {
        VirtualExecutorService bossExecutor = new VirtualExecutorService( this.executor );
        VirtualExecutorService workerExecutor = new VirtualExecutorService( this.executor );
        return new NioServerSocketChannelFactory( bossExecutor, workerExecutor );
    }

    /**
     * Stop this server.
     * @throws Exception 
     */
    public void stop() throws Exception {
        this.channel.close();
        this.channel = null;
    }

    private int port;

    private StompProvider stompProvider;
    private TransactionManager transactionManager;
    private LoggerManager loggerManager;
    private Logger log;
    private Executor executor;
    private Channel channel;

}