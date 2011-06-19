package org.jboss.stilts.stomplet.helpers;

import org.jboss.stilts.StompException;
import org.jboss.stilts.stomplet.Stomplet;
import org.jboss.stilts.stomplet.StompletConfig;

public abstract class AbstractStomplet implements Stomplet {
    
    @Override
    public void initialize(StompletConfig config) throws StompException {
        this.config = config;
        initialize();
    }
    
    public void initialize() throws StompException {
        // override me in your subclass.
    }
    
    @Override
    public void destroy() throws StompException {
    }
    
    public StompletConfig getStompletConfig() {
        return this.config;
    }
    
    private StompletConfig config;
}