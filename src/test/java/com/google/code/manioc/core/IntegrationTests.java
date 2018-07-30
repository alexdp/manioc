package com.google.code.manioc.core;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.google.code.manioc.core.ManiocFramework.BeanFactory;
import com.google.code.manioc.core.ManiocFramework.BeanInjector;
import com.google.code.manioc.core.ManiocFramework.DefaultApplicationContext;
import com.google.code.manioc.core.ManiocFramework.ManiocException;
import com.google.code.manioc.core.domain.AnotherCar;
import com.google.code.manioc.core.domain.Car;
import com.google.code.manioc.core.domain.Driver;
import com.google.code.manioc.core.domain.IColor;
import com.google.code.manioc.core.domain.IRadioStation;
import com.google.code.manioc.core.domain.Radio;
import com.google.code.manioc.core.domain.RadioStation1;
import com.google.code.manioc.core.domain.RadioStation2;
import com.google.code.manioc.core.domain.RadioStation3;
import com.google.code.manioc.core.domain.Seat;
import com.google.code.manioc.core.domain.Tire;
import com.google.code.manioc.core.domain.Violet;
import com.google.code.manioc.core.domain.Wheels;

/**
 * Not Unit tests but integraion tests
 */
public class IntegrationTests extends TestCase {

    
    public IntegrationTests(String testName) {
        super(testName);

    }

    public static Test suite() {
        return new TestSuite(IntegrationTests.class);
    }
    
    @Override
    protected void setUp() throws Exception {
        BeanFactory factory = BeanFactory.getFactory();
        if (!factory.contains(Seat.class)) {
            factory.register(Seat.class, new Seat());
        }
        if (!factory.contains(IColor.class)) {
            factory.register(IColor.class, new Violet());
        }
    }

    

    public void testInjection() {

        Car aCar = BeanFactory.getFactory().getBean(Car.class);
        assertNotNull(aCar);
        assertNotNull(aCar.getEngine());
        assertNotNull(aCar.getWheels());
    }

    public void testInjectionWithDefaultImplementation() {
        Car aCar = BeanFactory.getFactory().getBean(Car.class);
        Radio radio = aCar.getRadio();
        IRadioStation preselectedRadioStation3 = radio.getPreselectedRadioStation3();
        assertEquals(RadioStation3.class, preselectedRadioStation3.getClass());
    }
    
    public void testInjectionWithTwoImplementations() {
        Car aCar = BeanFactory.getFactory().getBean(Car.class);
        Radio radio = aCar.getRadio();
        IRadioStation preselectedRadioStation1 = radio.getPreselectedRadioStation1();
        IRadioStation preselectedRadioStation2 = radio.getPreselectedRadioStation2();
        assertEquals(RadioStation1.class, preselectedRadioStation1.getClass());
        assertEquals(RadioStation2.class, preselectedRadioStation2.getClass());
    }
    
    public void testInjectionOnExistingBean() {
        Car aCar = BeanFactory.getFactory().getBean(Car.class);
        Driver aDriver = new Driver();
        BeanInjector.getInjector().inject(aDriver);
        assertSame(aCar, aDriver.getCar());
    }
    
    public void testPostConstruct() {
        Car aCar = BeanFactory.getFactory().getBean(Car.class);
        assertTrue(aCar.getEngine().isRunning());
    }
    
    public void testScopeSingleton() {
        Car car1 = BeanFactory.getFactory().getBean(Car.class);
        Car car2 = BeanFactory.getFactory().getBean(Car.class);
        assertSame(car1, car2);
    }
    
    public void testScopePrototype() {
        Car aCar = BeanFactory.getFactory().getBean(Car.class);
        Wheels wheels = aCar.getWheels();
        Tire tire1 = wheels.getTire1();
        Tire tire2 = wheels.getTire2();
        Tire tire3 = wheels.getTire3();
        Tire tire4 = wheels.getTire4();
        assertNotSame(tire1, tire2);
        assertNotSame(tire1, tire3);
        assertNotSame(tire1, tire4);
    }
    
    public void testManualBeanRegistration() {
        assertNotNull(BeanFactory.getFactory().getBean(Seat.class));
    }
    
    public void testChildContextVisibility() {
        // Case 1 : access bean created in the parent context from the child context : should be OK
        Car bean1 = BeanFactory.getFactory().getBean(Car.class);
        Car bean2 = BeanFactory.getFactory(ChildApplicationContext.class).getBean(Car.class);
        assertSame(bean1, bean2);
        // Case 2 : access bean created in the child context from the parent context : should bo KO
        ManiocException beanNotFoundException = null;
        try {
            AnotherCar anotherBean1 = BeanFactory.getFactory().getBean(AnotherCar.class);
        } catch (ManiocException e) {
            beanNotFoundException = e;
        }
        AnotherCar anotherBean2 = BeanFactory.getFactory(ChildApplicationContext.class).getBean(AnotherCar.class);
        assertNotNull(beanNotFoundException);
        assertNotNull(anotherBean2);
    }
    
    public void testBeanRetrieveFromChildAContext() {
        Car bean = BeanFactory.getFactory(ChildApplicationContext.class).getBean(Car.class);
        assertNotNull(bean);
    }
    
    public void testContextBelonging() {
        Car car1 = BeanFactory.getFactory(ChildApplicationContext.class).getBean(Car.class);
        AnotherCar car2 = BeanFactory.getFactory(ChildApplicationContext.class).getBean(AnotherCar.class);
        Class<? extends DefaultApplicationContext> context1 = BeanFactory.getBeanContext(car1);
        Class<? extends DefaultApplicationContext> context2 = BeanFactory.getBeanContext(car2);
        assertEquals(DefaultApplicationContext.class, context1);
        assertEquals(ChildApplicationContext.class, context2);
    }
    

}
