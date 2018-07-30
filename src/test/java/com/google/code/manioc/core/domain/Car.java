package com.google.code.manioc.core.domain;

import com.google.code.manioc.core.ManiocFramework.Inject;
import com.google.code.manioc.core.ManiocFramework.ManagedBean;

@ManagedBean
public class Car
{
    @Inject
    private Engine engine;
    
    @Inject
    private Wheels wheels;
    
    @Inject
    private Seat seat;
    
    @Inject
    private Radio radio;
    
    @Inject
    private IColor color;

    public Engine getEngine()
    {
        return engine;
    }

    public Wheels getWheels()
    {
        return wheels;
    }
    
    public Seat getSeat() {
        return seat;
    }
    
    public Radio getRadio() {
        return radio;
    };
    
    public IColor getColor() {
        return color;
    }
    
}
