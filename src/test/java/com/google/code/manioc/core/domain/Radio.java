package com.google.code.manioc.core.domain;

import com.google.code.manioc.core.ManiocFramework.Inject;
import com.google.code.manioc.core.ManiocFramework.ManagedBean;

@ManagedBean
public class Radio {
    
    @Inject(implementation=RadioStation1.class)
    private IRadioStation preselectedRadioStation1;

    @Inject(implementation=RadioStation2.class)
    private IRadioStation preselectedRadioStation2;
    
    @Inject
    private IRadioStation preselectedRadioStation3;
    
    public IRadioStation getPreselectedRadioStation1() {
        return preselectedRadioStation1;
    }
    
    public IRadioStation getPreselectedRadioStation2() {
        return preselectedRadioStation2;
    }
    
    public IRadioStation getPreselectedRadioStation3() {
        return preselectedRadioStation3;
    }
    
}
