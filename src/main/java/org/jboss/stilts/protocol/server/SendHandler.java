package org.jboss.stilts.protocol.server;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.stilts.StompMessage;
import org.jboss.stilts.spi.StompProvider;

public class SendHandler extends AbstractProviderHandler {

    public SendHandler(StompProvider server, ConnectionContext context) {
        super( server, context );
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        log.info( "SEND: " + e.getMessage() );
        if (e.getMessage() instanceof StompMessage) {
            log.info( "SEND: " + e.getMessage() + " via " + getContext()  );
            log.info( "SEND: " + e.getMessage() + " via " + getContext().getClientAgent()  );
            getContext().getClientAgent().send( (StompMessage) e.getMessage() );
        }
        super.messageReceived( ctx, e );
    }

}
