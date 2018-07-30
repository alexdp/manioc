What is Manioc?
===============

Manioc is a very very (VERY!) light and basic framework made to bring
Inversion of Control to your applications.

When should you use it?
-----------------------

-   you have Java Standard Edition
-   you have basic injection needs so you want a very simple solution
-   your company organisation gives you headaches and it's very
    difficult to add any library to your application
-   the final size of program is important (Manioc"s size is around
    25kb)
-   you want to adapt your IoC framework to your needs quickly

What Manioc does?
-----------------

-   injection on fields (private - no more useless setters!)
-   bean construction with injection of parameters
-   post construction to initialise your beans
-   pre destroy to free bean's resources before JVM shutdown
-   scope handling so your bean can be created as singleton or prototype
    (= new instance created on each BeanFactory.getBean() call)
-   context isolation with hierarchy to make beans created on a parent
    context only visible from child contexts

Cool features list:
-------------------

-   ONE JAVA FILE ONLY!!! : No library to import! Manioc core is
    packaged in only one java file. You just have to copy/paste it into
    your project. [Browse it
    here](https://www.google.com/url?q=http://code.google.com/p/manioc/source/browse/manioc-core/trunk/src/main/java/com/google/code/manioc/core/ManiocFramework.java&sa=D&ust=1532947112481000).
-   configuration by intuitive annotations (similar to the JSR-330)
-   injection on legacy beans just by calling
    BeanInjector.getInjector().inject(this)
-   injection on private fields : no more setters to declare
-   easy code refactoring or code search because Manioc doesn't use any
    hard coded strings like Spring does with its @Qualifier annotation

Download
========

As Manioc is contained in only one file, you can download the latest
version directly from source code :
[ManiocFramework.java](https://www.google.com/url?q=http://manioc.googlecode.com/svn/manioc-core/trunk/src/main/java/com/google/code/manioc/core/ManiocFramework.java&sa=D&ust=1532947112483000)

Installation
============

Light deployment
----------------

Manioc is only one Java source file. Just download it and put it into
your project wherever you want.

AS MANIOC CORE IS DISTRIBUTED IN A JAVA SOURCE FILE, DON'T HESISTATE TO
ADAPT IT TO YOUR NEEDS.PLEASE, CONTRIBUTE TO THIS PROJECT BY SENDING
FEEDBACKS AND BUG FIXES.

Webapp deployment
-----------------

By default, Manioc detroys its beans on JVM shutdown. Depending on your
application, you should want to delegate bean factory initialization and
beans finalization to your application server. To do that, you can
create a class which implements ServletContextListener. Then, declare it
in your web.xml. Here is an example :

The Java class...

public class BeanFactoryServletContextListener implements
ServletContextListener {

@Override\
public void contextInitialized(ServletContextEvent sce) {\
    // Tells Manioc not to finalize beans on JVM shutdown\
    BeanFactory.delegatesBeanFinalization(true);\
    // Example of manual bean registration\
    ManuallyCreatedBeanExample myBean = new
ManuallyCreatedBeanExample();\
    BeanFactory.getFactory().register(ManuallyCreatedBeanExample.class,
myBean);\
}\
\
@Override\
public void contextDestroyed(ServletContextEvent sce) {\
    BeanFactory.finalizeManagedBeans();\
}

}

...and the fragment to add to your web.xml ...

<listener\>\
    <listener-class\>mypackage.BeanFactoryServletContextListener\</listener-class\>\
</listener\>

Overview
========

Supported annotations : \* @ManagedBean \* @Inject \* @Construct \*
@PostConstruct \* @PreDestroy \* @ImplementedBy

The two main classes : \* BeanInjector \* BeanFactory

The two last things to know : \* BeanScope (SINGLETON et PROTOTYPE) \*
DefaultApplicationContext

Usage
=====

First injection sample : inject wheels into a car
-------------------------------------------------

@ManagedBean // \<- tells that this kind of bean is created and
managed by the framework public class BigWheels { ...

}

@ManagedBean public class Car { @Inject // \<- indicates that wheels
need to be injected here private BigWheels wheels; } \`\`\`

OK, that's nice but the real advantage of inversion of control is to use
ijava interfaces.

Second injection sample : inject wheels behind a java interface into a car
--------------------------------------------------------------------------

You have to ways to do this : \* you can declare a default
implementation in your java interface with @ImplementedBy(...) \* or you
can set it directly in the field with @Inject.implementation(...)

You can also combine this two ways. In this case, the
@Inject.implementation(...) will override the @ImplementedBy(...).

Injection with default implementation declared in the java interface :

\`\`\` @ImplementedBy(StandardWheels.class) // \<- defines your default
implementation public interface Wheels { ... }

@ManagedBean // \<- don't forget this on your implementation public
class StandardWheels implements Wheels { ...

}

@ManagedBean public class Car { @Inject // \<- the framework injects of
bean of type StandardWheels.class private Wheels wheels; } \`\`\`

Injection with implementation directly declared on field :

\`\`\` // @ImplementedBy(StandardWheels.class) \<- useless because it is
directly specified on the field public interface Wheels { ... }

@ManagedBean public class BigWheels implements Wheels { ...

}

@ManagedBean public class Car { @Inject(implementation=BigWheels.class)
// \<- the implemention type which is injected private Wheels wheels; }
\`\`\`

Now, get the car
----------------

public static void main(String[] args) { Car myCar =
BeanFactory.getFactory().getBean(Car.class); }

Make injection on an existing bean
----------------------------------

\`\`\` // I'm a driver and I need a car public class Driver { @Inject
private Car car;

...\

} ... Driver aDriver = new Driver(); // \<- Look at the 'new' : this
object is not created by the Manioc
BeanInjector.getInjector().inject(aDriver); \<- This inject a car in its
private field \`\`\`

Make your own bean injectable into other beans
----------------------------------------------

Imagine that you retrieved a bean somewhere and you want to inject it
into other beans. To make that, you have two things to do : 1. annotate
the class of your bean with @ManagedBean(registeredManually=true). This
annotation is mandatory because it's important you be able to keep track
of every kinds of bean manipulated by Manioc. 1. declare your bean to
the BeanFactory

In the following example, we add a seat to our car. The seat is created
before any framework's call.

\`\`\` @ManagedBean(registeredManually=true) // \<- needed to keep track
of which class types are injected public class Seat { ... }

@ManagedBean public class Car { @Inject private Seat aSeat; }

Seat aSeat = new Seat(); // \<- the seat is created by hand BeanFactory
factory = BeanFactory.getFactory(); if (!factory.contains(Seat.class))
// \<- (optional) To avoid 'duplicate beans' exception error if this
code is called again { factory.register(Seat.class, aSeat); // \<- this
seat instance is registered and now injectable into another bean } Car
myCar = BeanFactory.getFactory().getBean(Car.class); // Here, I know
that myCar.getSeat() will return the excepted seat \`\`\`

IMPORTANT NOTE : you have to know that beans created by hand and then
registered in the bean factory are considered AS SINGLETON

Declare bean scope
------------------

The scope is a directive of construction of beans given to the
BeanFactory. Two scopes are supported : \* singleton -\> there's one and
only one instance of class per BeanFactory \* prototype -\> each
injection and each call to BeanFactory.getBean()return a new instance of
the class

If it is not specified, the default scope is SINGLETON

@ManagedBean(scope=BeanScope.SINGLETON) // public class Car { ... }
@ManagedBean(scope=BeanScope.PROTOTYPE) public class Tire { ... }

The different ways to construct a bean
--------------------------------------

The framework supports the following ways to construct a bean : \* if
the class doesn't contain any @Construct annotation, the class is
instanced by invoking the constructor without parameter \* you can add a
@Construct annotation to the constructor you prefer. If it has
parameters, the framework will inject them. \* if your constructors are
private, you can add a @Construct annotation to a factory method like
public static T getInstance()

// Nothing is declared, the framework will use the default constructor
@ManagedBean public class Car { ... }

// If you prefer, you can specify which constructor you want to use
@ManagedBean public class Car { @Construct // \<- Wheels are
automatically injected public Car(Wheels wheels) { ... } }

// You can also use a factory method @ManagedBean public class Car {
@Construct // \<- Wheels are automatically injected public static Car
getInstance(Wheels wheels) { ... } }

Make post construction
----------------------

During the step of construction of a bean, after having injected its
beans into fields, the framework can invoke methods annotated with
@PostConstruct. If a method needs parameters, the framework inject them.

\`\`\` @ManagedBean public class Engine { private boolean isStarted =
false;

@PostConstruct\
public void start() {\
    isStarted = true;\
}\

} \`\`\`

Finalize your beans on JVM shutdown
-----------------------------------

If you need to free ressources before a JVM shutdown, you can add
annotated some methods with @PreDestroy on all classes annotated with
@ManagedBean. The framework will invoke then and inject parameters if
necessary.

\`\`\` @ManagedBean public class Engine { private boolean isStarted =
false;

@PreDestroy\
public void stop() {\
    isStarted = false;\
    System.out.println("Engine stopped on JVM shutdown");\
}\

} \`\`\`

Use Application Contexts hierarchy to define beans visibility
-------------------------------------------------------------

Manioc works with application contexts. Like in the Spring Framework,
each bean is created by a BeanFactory is associated with such a context.
Basically, a bean from a context is not visible from another context,
excepted if they are parent/child. To be more precise, a child context
can get all the beans from its parent context. But a parent cannot
access to the beans of its children. This is useful if you have good
reason to isolate some parts of your application from some others. For
example, hide your "GUI" beans to "domain" beans and keep your "domain"
beans visible from the "GUI". You see?

When you retrieve or inject beans, you can set the application context
to use : \* instead of BeanFactory.getFactory().getBean(...), do
BeanFactory.getFactory(Class\<? extends DefaultApplicationContext\>
context).getBean(...)\* instead of
BeanInjector.getInjector().inject(Object object), do
BeanInjector.getInjector().inject(Object o, Class\<? extends
DefaultApplicationContext\> context)

To define your own application context, you just have to create a class
which inherits from DefaultApplicationContext.class.

public class ChildApplicationContext extends DefaultApplicationContext {
// By default, all beans are attached to DefaultApplicationContext. //
But you can create this tiny class which indicates to the framework to
use it as a custom application context // Did you see that we use the
power of java inheritance to create a context hierarchy? }

When you ask the BeanFactory for a bean, it tries to get it from its
current context. Then, if it doesn't find it and cannot create a new
one, it tries to get it from its parent's BeanFactory. But, why should
it failed to create a new bean? Because you can precise on
@ManagedBean a dedicated application context. So, your bean is no longer
constructible from a parent context or any other context which doesn't
inherits from this dedicated context.

Let's dedicate a class to an application context :

@ManagedBean(applicationContext=ChildApplicationContext.class) public
class AnotherCar { ... } ... AnotherCar anotherCar =
BeanFactory.getFactory(ChildApplicationContext.class).getBean(AnotherCar.class);

In the previous example, it is impossible to do
BeanFactory.getFactory(DefaultApplicationContext.class).getBean(AnotherCar.class)because
beans of type AnotherCar can only be created from
ChildApplicationContext and its child contexts.

Don't forget that beans are always linked to the application context of
their BeanFactory. In the following example, even if Car.class is
associated to DefaultApplicationContext.class, its instances belong to
the BeanFactory's application context. @ManagedBean // \<- equals to
@ManagedBean(applicationContext=DefaultApplicationContext.class) //
because all classes are linked to this application context by default.
// That's why it is not necessary to declare it. public class Car { ...
} ... public void testContextBelonging() { Car car1 =
BeanFactory.getFactory(ChildApplicationContext.class).getBean(Car.class);
Class\<? extends DefaultApplicationContext\> context1 =
BeanFactory.getBeanContext(car1);
assertEquals(DefaultApplicationContext.class, context1); }

