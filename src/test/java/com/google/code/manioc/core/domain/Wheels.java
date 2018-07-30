package com.google.code.manioc.core.domain;

import com.google.code.manioc.core.ManiocFramework;
import com.google.code.manioc.core.ManiocFramework.Inject;
import com.google.code.manioc.core.ManiocFramework.ManagedBean;

@ManagedBean
public class Wheels
{
    @Inject
    private Tire tire1;

    @Inject
    private Tire tire2;

    @Inject
    private Tire tire3;

    @Inject
    private Tire tire4;

    public Tire getTire1() {
        return tire1;
    }

    public Tire getTire2() {
        return tire2;
    }

    public Tire getTire3() {
        return tire3;
    }

    public Tire getTire4() {
        return tire4;
    }
    
    
    
}
