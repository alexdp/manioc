package com.google.code.manioc.core.domain;

import com.google.code.manioc.core.ManiocFramework;
import com.google.code.manioc.core.ManiocFramework.ManagedBean;
import com.google.code.manioc.core.ManiocFramework.PostConstruct;
import com.google.code.manioc.core.ManiocFramework.PreDestroy;

@ManagedBean
public class Engine
{
    private boolean isRunning = false;
    
    
    
    public boolean isRunning() {
        return isRunning;
    }

    @PostConstruct
    public void start() {
        isRunning = true;
    }
    
    @PreDestroy
    public void stop() {
        isRunning = false;
        System.out.println("Engine stopped before JVM shutdown");
    }
    
}
