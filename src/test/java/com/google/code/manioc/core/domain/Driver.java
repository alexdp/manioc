package com.google.code.manioc.core.domain;

import com.google.code.manioc.core.ManiocFramework.Inject;

public class Driver {

    @Inject
    private Car car;
    
    public Car getCar() {
        return car;
    }
    
}
