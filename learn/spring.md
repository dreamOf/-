1. # **overview**

2. # **core**

3. ## **The Ioc Container**

##  **1.1 Introduction to the Spring Ioc Container and beans** 

##  **1.2 Container Overview**

##  **1.3 Bean Overview**

##  **1.4 Dependencies(依赖)**

​    A typical enterprise application does not consist of a single object (or bean in the Spring parlance). Even the simplest application has a few objects that work together to present what the end-user sees as a coherent application. This next section explains how you go from defining a number of bean definitions that stand alone to a fully realized application where objects collaborate to achieve a goal.

​    译：一个普通的企业应用不会由一个对象（或spring术语中的'bean'）组成。即使一个最简的应用也有几个一起工作的对象，向最终用户呈现一个连贯的应用。下一节将会解释，怎样定义许多单独的bean去实现一个应用，在应用中，对象相互合作去实现一个目标。

###    **1.4.1 Dependency Injection**

​      译：依赖注入

​      Dependency injection (DI) is a process whereby objects define their dependencies(that is, the other objects with which they work) only through constructor arguments, arguments to a factory method, or properties that are set on the object instance after it is constructed or returned from a factory method. The container then injects those dependencies when it creates the bean. This process is fundamentally the inverse (hence the name, Inversion of Control) of the bean itself controlling the instantiation or location of its dependencies on its own by using direct construction of classes or the Service Locator pattern.

译：依赖注入是一个过程，对象定义他们的依赖仅仅通过构造函数参数、工厂方法的构造函数参数、或者从一个工厂方法中已经结构或者返回之后的对象实例上的属性（即他们使用的其他对象）。容器在创建bean的时候去注入依赖。这个过程是从根本上反转了bean本身实例化控制或者直接用类的构造器或者Service Locator pattern，因此得名控制反转。

code is cleaner with the DI principle, and decoupling is more effective when objects are provided with their dependencies. The object does not look up its dependencies and does not know the location or class of the dependencies. As a result, your classes become easier to test, particularly when the dependencies are on interfaces or abstract base classes,which allow for stub or mock implementations to be used in unit tests.

decouple:v.隔离、分离。

stub：存根。？？

译：使用DI原则，编码更整洁，当对象有其他依赖时，解耦更有效。对象不需要查找自己的依赖、不需要知道依赖的位置或类。因此，你的类更容易测试，尤其当依赖项是接口或者抽象类时，允许stub和mock实现常用于单元测试。

DI exists in two major variants: [Constructor-based dependency injection](https://docs.spring.io/spring/docs/5.2.2.RELEASE/spring-framework-reference/core.html#beans-constructor-injection) and [Setter-based dependency injection](https://docs.spring.io/spring/docs/5.2.2.RELEASE/spring-framework-reference/core.html#beans-setter-injection).

译：DI存在两个主要的变体：基于构造函数的依赖注入和基于set方法的依赖注入。

##### **Constructor-based Dependency Injection**

译：基于构造函数的依赖注入

Constructor-based DI is accomplished by the container invoking a constructor with a number of arguments, each representing a dependency. Calling a static

factory method with specific arguments to construct the bean is nearly equivalent, and this discussion treats arguments to a constructor and to a static factory method similarly. The following example shows a class that can only be dependency-injected with constructor injection:





```
public class SimpleMovieLister {

    // the SimpleMovieLister has a dependency on a MovieFinder
    private MovieFinder movieFinder;

    // a constructor so that the Spring container can inject a MovieFinder
    public SimpleMovieLister(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }

    // business logic that actually uses the injected MovieFinder is omitted...
}
```





译：基于构造函数的依赖注入通过执行拥有许多参数的构造函数完成，每个参数标识一个依赖。调用一个具有明确参数的构造bean的静态工厂方法是差不多的，本讨论中对构造函数和静态工厂方法的参数是类似的。下面的例子展示了一个只能通过构造函数进行依赖注入。

Notice that there is nothing special about this class. It is a POJO that has no dependencies on container specific interfaces, base classes or annotations.

译：注意：这个类没有特殊性。它是一个POJO,不依赖于容器生命的接口、基类和注解。

##### **Constructor Argument Resolution**

Resolution: n.决议; 正式决定;  (问题、分歧等的)解决，消除; 坚定;坚决;有决心

译：构造参数解析

Constructor argument resolution matching occurs by using the argument's type. If no potential ambiguity exists in the constructor arguments of a bean definition, the order in which the constructor arguments are defined in a bean definition is the order in which those arguments are supplied to the appropriate constructor when the bean is being instantiated. Consider the following class:



```
package x.y;

public class ThingOne {

    public ThingOne(ThingTwo thingTwo, ThingThree thingThree) {
        // ...
    }
}
```





potential:adj.潜在的;可能的;n. 可能性;潜在性;潜力;潜质;电位;电势;电压 ;

ambiguity: n.歧义;一语多义;模棱两可的词;含混不清的语句;模棱两可;不明确 ;

appropriate:adj.合适的;恰当的v.盗用;挪用;占用;侵吞;拨(专款等)

译：构造参数解析匹配出现在构造函数中使用的参数类型。如果一个bean定义的构造参数没有潜在的歧义，在bean定义中的每个构造参数中定义的顺序，当bean被实例化时，根据每个参数的顺序提供一个合适的构造器。请考虑以下类：

Assuming that ThingTwo and ThingThree classes are not related by inheritance, no potential ambiguity exists. Thus, the following configuration works fine, and you do not need to specify the constructor argument indexes or types explicitly in the 

<constructor-arg/> element.

Assuming：n.假设、假如

译：假设ThingTwo和ThingThress类没有继承关系，则不存在歧义。因此以下的配置正常工作，并且你不需要在<constructor-arg/>标签中申明构造参数索引或者明确的类的构造器。



```
<beans>
    <bean id="beanOne" class="x.y.ThingOne">
        <constructor-arg ref="beanTwo"/>
        <constructor-arg ref="beanThree"/>
    </bean>

    <bean id="beanTwo" class="x.y.ThingTwo"/>

    <bean id="beanThree" class="x.y.ThingThree"/>
</beans>
```





When another bean is referenced, the type is known, and matching can occur (as was thecase with the preceding example). When a simple type is used, such as

<value>true</value>, Spring cannot determine the type of the value, and so cannot match by type without help. Consider the following class:

译：当引用其他bean时，类是确定的，并且可以匹配出现的类（像前面例子中的情况）。当用到一个简单的类时，比如<value>true</value>，Spring无法确定value的类型，并且不能通过没有帮助的类来匹配。请考虑以下类：



```
package examples;

public class ExampleBean {

    // Number of years to calculate the Ultimate Answer
    private int years;

    // The Answer to Life, the Universe, and Everything
    private String ultimateAnswer;

    public ExampleBean(int years, String ultimateAnswer) {
        this.years = years;
        this.ultimateAnswer = ultimateAnswer;
    }
}
```





Constructor argument type matching

译：构造器参数类型匹配

In the preceding scenario, the container can use type matching with simple types if you explicitly specify the type of the constructor argument by using the type

attribute.as the following example shows:

译：在前面提到的场景中，如果你通过type明确声明构造器参数的类型，容器可以使用类匹配简单类型。



```
<bean id="exampleBean" class="examples.ExampleBean">
    <constructor-arg type="int" value="7500000"/>
    <constructor-arg type="java.lang.String" value="42"/>
</bean>
```





Constructor argument index

译：构造器参数索引

You can use the index attribute to specify explicitly the index of constructor arguments,as the following example shows:

译：你可以使用index属性来明确地声明构造器参数的索引，如下所示：



```
<bean id="exampleBean" class="examples.ExampleBean">
    <constructor-arg index="0" value="7500000"/>
    <constructor-arg index="1" value="42"/>
</bean>
```





In addition to resolving the ambiguity of multiple simple values, specifying an index resolves ambiguity where a constructor has two arguments of the same type.

译：除了解决多个基本类型的值的歧义外，指定一个索引还可以解决一个构造器有两个相同参数的歧义。



The index is 0-based.

译：索引从0开始。

Constructor argument name

译：构造器参数名称

You can also use the constructor parameter name for value disambiguation, as the following example shows:

译：你能使用构造参数名称分辨值，如下所示：



```
<bean id="exampleBean" class="examples.ExampleBean">
    <constructor-arg name="years" value="7500000"/>
    <constructor-arg name="ultimateAnswer" value="42"/>
</bean>
```





Keep in mind that, to make this work out of the box, your code must be compiled with the debug flag enabled so that Spring can look up the parameter name from the constructor.If you cannot or do not want to compile your code with the debug flag, you can use the [@ConstructorProperties](https://download.oracle.com/javase/8/docs/api/java/beans/ConstructorProperties.html) JDK annotation to explicitly name your constructor arguments. The sample class would then have to look as follows:

译：请记住，使这项工作开箱即用，你的代码必须在debug模式下编译，以便于spring可以从构造函数中参数查找名称。如果你不能或者不想用debug模式编译代码，你能使用[@ConstructorProperties](https://download.oracle.com/javase/8/docs/api/java/beans/ConstructorProperties.html)注解显示地命名构造参数。简单的类必须如下所示：



```
package examples;

public class ExampleBean {

    // Fields omitted

    @ConstructorProperties({"years", "ultimateAnswer"})
    public ExampleBean(int years, String ultimateAnswer) {
        this.years = years;
        this.ultimateAnswer = ultimateAnswer;
    }
}
```





##### **Setter-based Dependency Injection**

译：基于set方法的依赖注入

Setter-based DI is accomplished by the container calling setter methods on your

beans after invoking a no-argument constructor or a no-argument static

factory method to instantiate your bean.The following example shows a class that can only be dependency-injected by using pure setter injection. This class is conventional Java. It is a POJO that has no dependencies on container specific interfaces, base classes, or annotations.

译：基于set方法的注入，在执行无参构造函数或者无参静态工厂方法初始化bean之后，通过容器调用bean中的set方法实现.以下示例显示了一个类，仅仅通过使用set注入进行依赖注入。这个类使传统的java.他是一个不依赖于容器指定的接口、基类或者注解的pojo.



```
public class SimpleMovieLister {

    // the SimpleMovieLister has a dependency on the MovieFinder
    private MovieFinder movieFinder;

    // a setter method so that the Spring container can inject a MovieFinder
    public void setMovieFinder(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }

    // business logic that actually uses the injected MovieFinder is omitted...
}
```





The ApplicationContext supports constructor-based and setter-based DI for the beans it manages. It also supports setter-based DI after some dependencies have already been injected through the constructor approach. You configure the dependencies in the form of a  BeanDefinition, which you use in conjunction with 

 PropertyEditor instances to convert properties from one format to another. However, most Spring users do not work with these classes directly (that is, programmatically) but rather with XML bean definitions, annotated components (that is, classes annotated with @Component,@Controller, and so forth), or @Bean methods in Java-based @Configuration classes.These sources are then converted internally into instances of BeanDefinition and used to load an entire Spring IoC container instance.

conjunction：n.连接、结合。

译：ApplicationContext管理的bean支持基于构造函数和set方法的依赖注入。在一些依赖通过构造器原则已经注入之后，也支持基于set的依赖注入。用BeanDefinition的形式配置依赖项，与 PropertyEdito接口一起使用转换属性的类型。然而，大多数spring使用者不会直接用这些类开发（这个使程序化的），但是喜欢使用xml bean定义,注解组件（在类上使用@Component,@Controller等）、或者在基于@Configuration注解的使用@Bean方法。



​                                **Constructor-based or setter-based DI?**译：基于构造器或者基于set方法的依赖注入？Since you can mix constructor-based and setter-based DI, it is a good rule of thumb to use constructors for mandatory dependencies and setter methods or configuration methods for optional dependencies. Note that use of the @Required annotation on a setter method can be used to make the property be a required dependency;however, constructor injection with programmatic validation of arguments is preferable.译：由于你可以混合使用构造器和set方法注入，这是一个好的规则：强制性依赖使用构造器，可选依赖使用set或者配置方法。请注意在一个set方法上使用@Required注解，使得set方法的属性成为必须依赖项，然而，使用程序校验参数的构造器注入更好一些。The Spring team generally advocates constructor injection, as it lets you implement application components as immutable objects and ensures that required dependencies are not null. Furthermore, constructor-injected components are always returned to the client (calling) code in a fully initialized state. As a side note, a large number of constructor arguments is a bad code smell, implying that the class likely has too many responsibilities and should be refactored to better address proper separation of concerns.advocates：v.拥护、倡导、支持 n.拥护者;支持者;提倡者;辩护律师;出庭辩护人 immutable：adj.不可改变的、永恒不变的译：Spring团队倡导构造器注入，因为构造器注入使你实现的应用组件成为不变的对象并且可以确保不需依赖不为空。此外，构造函数注入组件总是用一个完全初始化的状态返回给客户端（调用）。注意另一方面，大量的构造函数参数使得代码坏为味，这也意味着class可能有太多的职责，应该进行重构，使得关注点进行更好的分离。Setter injection should primarily only be used for optional dependencies that can beassigned reasonable default values within the class. Otherwise, not-null checks must beperformed everywhere the code uses the dependency. One benefit of setter injection is that setter methods make objects of that class amenable to reconfiguration or re-injection later. Management through JMX MBeans is therefore a compellinguse case for setter injection.译：set注入应该主要用在可选的依赖项，这些依赖项在类中分配合理的默认值。否则，非空校验必须在使用依赖的代码中到处执行。set方法注入的一个优势是set方法使得类对象在初始化后易于重新配置或注入。通过JMX MBeans管理是一个基于set方法注入的合成案例。Use the DI style that makes the most sense for a particular class. Sometimes, when dealingwith third-party classes for which you do not have the source, the choice is made for you.For example, if a third-party class does not expose any setter methods, then constructorinjection may be the only available form of DI.译：使用DI风格使得一个特定的类更有意义。有时候，当处理没有源代码的第三方类时，会为您做出选择。比如，如果一个第三方类类没有暴露任何set方法，则构造函数注入可能是唯一可用的DI形式。



##### **Dependency Resolution Process#5.2.3**

译：依赖关系解决过程



The container performs bean dependency resolution as follows:

- The ApplicationContext is created and initialized with configuration metadata that describes all the beans. Configuration metadata can be specified by XML, Java code, or annotations.
- For each bean, its dependencies are expressed in the form of properties,          constructor arguments, or arguments to the static-factory method (if you use that    instead of a normal constructor). These dependencies are provided to the bean, when the bean is actually created.

- Each property or constructor argument is an actual definition of the value to                        set, or a reference to another bean in the container.
- Each property or constructor argument that is a value is converted from its     specified format to the actual type of that property or constructor argument. By default, Spring can convert a value supplied in string format to all built-in types, such as int,long，String，boolean, and so forth.

译：容器执行bean依赖解析如下：

-  ApplicationContext使用描述所有Bean的配置元数据创建和初始化，配置元数据考科一通过xml、java code、annotations声明。
- 对于每一个bean,他的依赖关系用属性、构造参数或者静态工厂方法(如果你用静态工厂方法代替正式的构造函数)的参数体现。这些依赖通过bean提供，在bean完全创建的时候。
- 每个属性或者构造参数是一个在容器中定义的值，或者另一个bean的引用。
- 每个属性或者构造参数从明确的格式转换成属性或者构造参数的完全类型。默认，Spring可以把一个用string格式的值转换成所有内置类型，比如int、long、string、boolean等。

The Spring container validates the configuration of each bean as the container is created.However, the bean properties themselves are not set until the bean is actually created.Beans that are singleton-scoped and set to be pre-instantiated (the default) are created when the container is created. Scopes are defined in [Bean Scopes](https://docs.spring.io/spring/docs/5.2.3.RELEASE/spring-framework-reference/core.html#beans-factory-scopes). Otherwise,the bean is created only when it is requested. Creation of a bean potentially causes a graph of beans to be created, as the bean’s dependencies and its dependencies' dependencies (and so on) are created and assigned. Note that resolution mismatches among those dependencies may show up late — that is, on first creation of the affected bean.

译：Spring容器在容器创建的时候校验每个bean的配置。然而，直到bean完全创建之前bean本身的属性不会设置。当容器创建时，单例和预初始化的bean被创建。在bean的scopes中定义Bean的作用范围。此外，当bean被请求时bean才被创建。创建一个bean时，会潜在导致创建一个bean图，因为bean的依赖以及bean的依赖的依赖被创建和分配。请注意，在第一次创建受影响的bean时，这些解析不匹配的依赖会显示。





Circular dependencies译：循环依赖If you use predominantly constructor injection, it is possible to create an unresolvable circular dependency scenario.译：如果你主要使用构造函数注入，可能创建一个无法解决的循环依赖场景。For example: Class A requires an instance of class B through constructor injection, and class B requires an instance of class A through constructor injection. If you configure beans for classes A and B to be injected into each other, the Spring IoC container detects this circular reference at runtime, and throws a BeanCurrentlyInCreationException.译：比如，类A需要一个通过构造函数注入的类B的实例，并且类B需要一个通过构造函数注入的类A的实例。如果你配置类A和类B相互注入，Spring IoC容器在运行时检测这个循环医用，并跑出BeanCurrentlyInCreationException。One possible solution is to edit the source code of some classes to be configured by setters rather than constructors. Alternatively, avoid constructor injection and use setter injection only. In other words, although it is not recommended, you can configure circular dependencies with setter injection.译：一种解决方法是修改相关类的源代码，通过set注入而不是构造函数注入。或者，不使用构造函数注入，并只使用set方法注入。换言而之，尽管不推荐，你可以使用set注入配置循环引用。Unlike the typical case (with no circular dependencies), a circular dependencybetween bean A and bean B forces one of the beans to be injected into the other prior to being fully initialized itself (a classic chicken-and-egg scenario).译：不像传统的案例（没有循环依赖的案例），bean A和bean B之间的依赖强制其中一个bean在完全初始化之前注入到另外一个bean（一个典型的鸡和蛋问题）。





You can generally trust Spring to do the right thing. It detects configuration problems, such as references to non-existent beans and circular dependencies, at container load-time. Spring sets properties and resolves dependencies as late as possible, when the bean is actually created. This means that a Spring container that has loaded correctly can later generate an exception when you request an object if there is a problem creating that object or one of its dependencies — for example, the bean throws an exception as a result of a missing or invalid property. This potentially delayed visibility of some configuration issues is why ApplicationContext implementations by default pre-instantiate singleton beans. At the cost of some upfront time and memory to create these beans before they are actually needed, you discover configuration issues when the ApplicationContext

is created, not later. You can still override this default behavior so that singleton beans initialize lazily, rather than being pre-instantiated.

译：你通常相信Spring会做正确的事情。在容器加载的时候，spring检测配置问题，比如引用不存在的bean和循环依赖.Spring尽可能迟地设置属性和解决依赖，当bean创建的时候。这意味着当你请求一个有问题的对象或者依赖时，已经正确加载的pring容器稍后会生成一个异常，比如，bean抛出一个缺失或者无效属性结果的异常。某些配置问题的延迟可见性是因为ApplicationContext通过默认预实例化实现单例bean.在bean需要之前花费一些前期时间和内存创建这些bean,当ApplicationContext创建时，你发现配置问题，而不是之后。你仍然可以覆盖默认的行为以便于单例bean懒加载而不是预加载。

If no circular dependencies exist, when one or more collaborating beans are being

injected into a dependent bean, each collaborating bean is totally configured prior

to being injected into the dependent bean. This means that, if bean A has a dependency on bean B, the Spring IoC container completely configures bean B prior to invoking the setter method on bean A. In other words, the bean is instantiated (if it is not a pre-instantiated singleton), its dependencies are set, and the relevant lifecycle methods (such as a [configured init method](https://docs.spring.io/spring/docs/5.2.3.RELEASE/spring-framework-reference/core.html#beans-factory-lifecycle-initializingbean) or the [InitializingBean callback method](https://docs.spring.io/spring/docs/5.2.3.RELEASE/spring-framework-reference/core.html#beans-factory-lifecycle-initializingbean))  are invoked.

译：如果没有循环依赖存在，当一个或者多个协作bean被注入到一个依赖bean，每个协作bean先注入依赖bean.这意味着，如果bean A依赖bean B，spring ioc容器完全配置 beanB,在调用Bean A set方法之前。换言而之，bean被实例化（如果没有预实例化单例），他的依赖已经建立，他相关的生命周期方法被执行（比如配置初始化方法或者[InitializingBean ](https://docs.spring.io/spring/docs/5.2.3.RELEASE/spring-framework-reference/core.html#beans-factory-lifecycle-initializingbean) 回调方法被执行）。

##### **Examples of Dependency Injection#5.2.3**

译：依赖注入案例

The following example uses XML-based configuration metadata for setter-based DI. A small part of a Spring XML configuration file specifies some bean definitions as follows:

译：如下所示，使用xml配置元数据作为基于set方法的注入。以下一小部分Spring XML配置文件指定了一些bean.



```
<bean id="exampleBean" class="examples.ExampleBean">
    <!-- setter injection using the nested ref element -->
    <property name="beanOne">
        <ref bean="anotherExampleBean"/>
    </property>

    <!-- setter injection using the neater ref attribute -->
    <property name="beanTwo" ref="yetAnotherBean"/>
    <property name="integerProperty" value="1"/>
</bean>

<bean id="anotherExampleBean" class="examples.AnotherBean"/>
<bean id="yetAnotherBean" class="examples.YetAnotherBean"/>
```





The following example shows the corresponding ExampleBean class:

译：ExampleBean类如下所示：



```
public class ExampleBean {

    private AnotherBean beanOne;

    private YetAnotherBean beanTwo;

    private int i;

    public void setBeanOne(AnotherBean beanOne) {
        this.beanOne = beanOne;
    }

    public void setBeanTwo(YetAnotherBean beanTwo) {
        this.beanTwo = beanTwo;
    }

    public void setIntegerProperty(int i) {
        this.i = i;
    }
}
```





In the preceding example, setters are declared to match against the properties specified in the XML file. The following example uses constructor-based DI:

译：上面的例子，set方法声明在与指定属性匹配的xml文件。接下来的例子使用基于构造函数的依赖注入。



```
<bean id="exampleBean" class="examples.ExampleBean">
    <!-- constructor injection using the nested ref element -->
    <constructor-arg>
        <ref bean="anotherExampleBean"/>
    </constructor-arg>

    <!-- constructor injection using the neater ref attribute -->
    <constructor-arg ref="yetAnotherBean"/>

    <constructor-arg type="int" value="1"/>
</bean>

<bean id="anotherExampleBean" class="examples.AnotherBean"/>
<bean id="yetAnotherBean" class="examples.YetAnotherBean"/>
```





The following example shows the corresponding ExampleBean class:

译：ExampleBean如下所示：



```
public class ExampleBean {

    private AnotherBean beanOne;

    private YetAnotherBean beanTwo;

    private int i;

    public ExampleBean(
        AnotherBean anotherBean, YetAnotherBean yetAnotherBean, int i) {
        this.beanOne = anotherBean;
        this.beanTwo = yetAnotherBean;
        this.i = i;
    }
}
```





The constructor arguments specified in the bean definition are used as arguments to the constructor of the  ExampleBean 。

Now consider a variant of this example, where, instead of using a constructor, Spring is told to call a static factory method to return an instance of the object:

译：在bean定义中的构造参数，作为ExampleBean的构造参数。

​      现在考虑这个例子的变量，在哪里替换使用构造函数，spring考苏调用静态工厂方法返回一个对象实例。



```
<bean id="exampleBean" class="examples.ExampleBean" factory-method="createInstance">
    <constructor-arg ref="anotherExampleBean"/>
    <constructor-arg ref="yetAnotherBean"/>
    <constructor-arg value="1"/>
</bean>

<bean id="anotherExampleBean" class="examples.AnotherBean"/>
<bean id="yetAnotherBean" class="examples.YetAnotherBean"/>
```





The following example shows the corresponding ExampleBean class:

译：ExampleBean如下所示：



```
public class ExampleBean {

    // a private constructor
    private ExampleBean(...) {
        ...
    }

    // a static factory method; the arguments to this method can be
    // considered the dependencies of the bean that is returned,
    // regardless of how those arguments are actually used.
    public static ExampleBean createInstance (
        AnotherBean anotherBean, YetAnotherBean yetAnotherBean, int i) {

        ExampleBean eb = new ExampleBean (...);
        // some other operations...
        return eb;
    }
}
```





Arguments to the static factory method are supplied by <constructor-arg/> elements,exactly the same as if a constructor had actually been used. The type of the class being returned by the factory method does not have to be of the same type as the class that contains the static  factory method (although, in this example, it is). An instance (non-static) factory method can be used in an essentially identical fashion (aside from the use of the factory-bean  attribute instead of the class attribute), so we do not discuss those details here.

译：static工厂方法的参数通过<constructor-arg/>标签提供，与实际使用的构造函数完全相同。工厂方法返回class的类型不必须和包含静态方法的类相同（这个例子是相同的）。一个实例（非static）工厂方法以基本相同的方式使用（除了使用factory-bean  属性而不是class属性），因此在这里不进行详细讨论。

###    **1.4.2 Dependencies and Configuration in Detail**

译：详解依赖和配置

As mentioned in the [previous section](https://docs.spring.io/spring/docs/5.2.3.RELEASE/spring-framework-reference/core.html#beans-factory-collaborators), you can define bean properties and constructor arguments as references to other managed beans (collaborators)

or as values defined inline. Spring’s XML-based configuration metadata supports

sub-element types within its <property/> and <constructor-arg/> elements for this purpose。

译：在上部分提到，定义bean属性和构造函数参数引用其他被管理的bean(协作者)或者内联值。spring基于xml配置元数据支持在<property/> 和 <constructor-arg/>内的子标签类型。

##### **Straight Values (Primitives, Strings, and so on)**

译：直接赋值（Primitives,Strings等）

The value attribute of the <property/> element specifies a property or constructor argument as a human-readable string representation. Spring’s [conversion service](https://docs.spring.io/spring/docs/5.2.3.RELEASE/spring-framework-reference/core.html#core-convert-ConversionService-API) is used to convert these values from a String to the actual type of the property or argument.The following example shows various values being set:

译：<property/>标签的属性value，以可读的String类型表示，指定一个属性或者构造参数。Spring的[conversion servic](https://docs.spring.io/spring/docs/5.2.3.RELEASE/spring-framework-reference/core.html#core-convert-ConversionService-API)[e](https://docs.spring.io/spring/docs/5.2.3.RELEASE/spring-framework-reference/core.html#core-convert-ConversionService-API)用于从String转换成property或者参数的实际类型。以下的例子展示了将被设置的变量值：



```
<bean id="myDataSource" class="org.apache.commons.dbcp.BasicDataSource" destroy-method="close">
    <!-- results in a setDriverClassName(String) call -->
    <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
    <property name="url" value="jdbc:mysql://localhost:3306/mydb"/>
    <property name="username" value="root"/>
    <property name="password" value="masterkaoli"/>
</bean>
```





The following example uses the [p-namespace](https://docs.spring.io/spring/docs/5.2.3.RELEASE/spring-framework-reference/core.html#beans-p-namespace) for even more succinct XML configuration:

succinct：adj:简洁的，整洁的；

译：以下例子使用P命名空间进行更简洁的xml配置；





```
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:p="http://www.springframework.org/schema/p"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
    https://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="myDataSource" class="org.apache.commons.dbcp.BasicDataSource"
        destroy-method="close"
        p:driverClassName="com.mysql.jdbc.Driver"
        p:url="jdbc:mysql://localhost:3306/mydb"
        p:username="root"
        p:password="masterkaoli"/>

</beans>
```





The preceding XML is more succinct. However, typos are discovered at runtime rather than design time, unless you use an IDE (such as [IntelliJ IDEA](https://www.jetbrains.com/idea/) or the [Spring Tool Suite](https://spring.io/tools/sts)) that supports automatic property completion when you create bean definitions. Such IDE assistance is highly recommended.

译：上面的xml更加简洁。在运行时发现错误，而不是编写时。除非你使用IDE(像IntelliJ IDEA或者STS),当你创建bean的时候他们支持自动属性补全，强烈推荐IDE帮助。

You can also configure a java.util.Properties instance, as follows:

译：你能配置一个java.util.Properties实例，如下所示：





```
<bean id="mappings"
    class="org.springframework.context.support.PropertySourcesPlaceholderConfigurer">

    <!-- typed as a java.util.Properties -->
    <property name="properties">
        <value>
            jdbc.driver.className=com.mysql.jdbc.Driver
            jdbc.url=jdbc:mysql://localhost:3306/mydb
        </value>
    </property>
</bean>
```





The Spring container converts the text inside the <value/>

element into a java.util.Properties instance by using the JavaBeans PropertyEditor mechanism. This is a nice shortcut, and is one of a few places where the Spring team do favor the use of the nested <value/> element over the value attribute style。

译：spring容器转换在value标签的文本，通过使用JavaBeans PropertyEditor的机制到java.util.Properties的实例。这是一个很好的快捷方式，并且这也是spring团队喜欢使用<value/>标签胜过value属性方式的之一。

**The idref element**

译：idref标签

The idref element is simply an error-proof way to pass the id (a string value - not a reference) of another bean in the container to a <constructor-arg/> or <property/> element. The following example shows how to use it:

译：idref标签是简单地防错方式，通过在容器中的其他bean的id(一个字符串-非引用)添加到<constructor/>或者<property/>标签中。下面的例子展示了如何使用：



```
<bean id="theTargetBean" class="..."/>

<bean id="theClientBean" class="...">
    <property name="targetName">
        <idref bean="theTargetBean"/>
    </property>
</bean>
```





The preceding bean definition snippet is exactly equivalent (at runtime) to the

following snippet:

译：上面提到的bean定义片段和下面的片段完全相等（在运行时）。



```
<bean id="theTargetBean" class="..." />

<bean id="client" class="...">
    <property name="targetName" value="theTargetBean"/>
</bean>
```





The first form is preferable to the second, because using the idref tag lets the container validate at deployment time that the referenced, named bean actually exists. In the second variation, no validation is performed on the value that is passed to the targetName property of the client bean. Typos are only discovered (with most likely fatal results) when the client bean is actually instantiated. If the client bean is a prototype bean, this typo and the resulting exception may only be discovered long after the container is deployed.

译：第一个例子优于第二个，因为使用idref标签让容器校验在部署时引用、命名bean是否存在。在第二个变体中，当传递给客户端bean的targeName（name属性的值）属性的值没有执行校验。只能在bean实例化的时候发现错误（大部分是致命的结果）。如果客户端bean是ProtoType bean，在容器部署之后的很长一段时间，错误和导致的异常才可能被发现。



The local attribute on the idref element is no longer supported in the 4.0 beansXSD, since it does not provide value over a regular bean reference any more. Change your existing idref local references to idref bean when upgrading to the 4.0 schema.译：在4.0bean XSD中，idref标签的本地属性不再支持，因为他不提供一个常规bean的引用。当升级到4.0 schemea时，改变存在idref本地引用到idref bean.



A common place (at least in versions earlier than Spring 2.0) where the <idref/> element brings value is in the configuration of AOP interceptors in a ProxyFactoryBean bean definition. Using <idref/> elements when you specify the interceptor names prevents you from misspelling an interceptor ID.

译：idref标签用在一个普通的地方（至少在spring2.0之前），ProxyFactoryBean定义的AOP拦截器配置中的值。当你指定了拦截器名称的时候，使用idref标签防止错误拼写interceptor ID.

##### **References to Other Beans (Collaborators)**

译：引用其他的bean(协作者)

The ref element is the final element inside a <constructor-arg/> or <property/> definition element. Here, you set the value of the specified property of a bean to be a reference to another bean (a collaborator) managed by the container. The referenced bean is a dependency of the bean whose property is to be set, and it is initialized on demand as needed before the property is set. (If the collaborator is a singleton bean, it may already be initialized by the container.) All references are ultimately a reference to another object. Scoping and validation depend on whether you specify the ID or name of the other object through the bean, local, or parent attributes.

译：ref是除了<constructor-arg/> 或 <property/>定义元素里面的最后一个元素**。在这里，你设置bean中指定的属性值引用spring容器中管理的其他bean(协作者)。被引用的bean是一个依赖将设置属性的bean,并设置属性之前根据需要初始化。（如果协作者是单例bean，他已经通过容器初始化）。所有的引用最终都是对其他对象的引用。作用域和校验依赖于你是否指定了通过bean、local、parent属性指定了其他对象的id或者name.**

Specifying the target bean through the bean attribute of the <ref/> tag is the most general form and allows creation of a reference to any bean in the same container or parent container, regardless of whether it is in the same XML file. The value of the bean attribute may be the same as the id attribute of the target bean or be the same as one of the values in the name attribute of the target bean. The following example shows how to use a ref element:

译：通过bean的<ref/>标签属性指定目标bean,是最常见的形式，在相同的容器或者父容器中，允许创建任何bean的引用，不管bean是否在形同的xml文件中。bean属性的值可能和目标bean的属性id是相同的或者和目标bean的name属性相同。下面的例子展示了如何去使用ref标签。





```
<ref bean="someBean"/>
```





Specifying the target bean through the parent attribute creates a reference to a bean that is in a parent container of the current container. The value of the parent attribute may be the same as either the id attribute of the target bean or one of the values in the name attribute of the target bean. The target bean must be in a parent container of the current one. You should use this bean reference variant mainly when you have a hierarchy of containers and you want to wrap an existing bean in a parent container with a proxy that has the same name as the parent bean. The following pair of listings shows how to use the parent attribute:

译：指定一个当前容器的父容器的目标bean，通过parent属性创建一个引用。parent属性的值可能和目标bean的id属性值相同或者其中的name属性值相同。目标bean必须在当前bean的父容器中。当你有容器继承树的时候，并且你想通过和父Bean相同名称的代理包装父容器中存在的bean,你应该使用bean引用变量。下面的两个列表展示了如何使用parent属性。



```
<!-- in the parent context -->
<bean id="accountService" class="com.something.SimpleAccountService">
    <!-- insert dependencies as required as here -->
</bean>
```









```
<!-- in the child (descendant) context -->
<bean id="accountService" <!-- bean name is the same as the parent bean -->
    class="org.springframework.aop.framework.ProxyFactoryBean">
    <property name="target">
        <ref parent="accountService"/> <!-- notice how we refer to the parent bean -->
    </property>
    <!-- insert other configuration and dependencies as required here -->
</bean>
```









 The local attribute on the ref element is no longer supported in the 4.0 beans XSD, since it does not provide value over a regular bean reference any more. Change your existing ref local references to ref bean when upgrading to the 4.0 schema. 译：ref标签上的local属性在4.0 bean的XSD中不再支持，因为他不再提供常规的引用。当升级到4.0 schema时，修改存在的ref的 local引用为ref的 bean .



##### **Inner Beans**

译：内部bean

A <bean/> element inside the <property/> or <constructor-arg/> elements defines an inner bean, as the following example shows:

译：在<property/> 或 <constructor-arg/>内部使用<bean/>标签定义一个内部bean.

如下所示：



```
<bean id="outer" class="...">
    <!-- instead of using a reference to a target bean, simply define the target bean inline -->
    <property name="target">
        <bean class="com.example.Person"> <!-- this is the inner bean -->
            <property name="name" value="Fiona Apple"/>
            <property name="age" value="25"/>
        </bean>
    </property>
</bean>
```





An inner bean definition does not require a defined ID or name. If specified, the container does not use such a value as an identifier. The container also ignores the scope flag on creation, because inner beans are always anonymous and are always created with the outer bean. It is not possible to access inner beans independently or to inject them into collaborating beans other than into the enclosing bean.

译：一个内部bean定义不需要定义id或者name属性.如果定义了，容器不使用此类值作为限定符。容器也会忽视创建时的scope标志，因为内部bean总是匿名的并且总是使用outer bean进行创建。不可能访问内部bean,也不可能把内部bean注入到除了封闭bean之外（outer bean）的协作bean。

As a corner case, it is possible to receive destruction callbacks from a custom scope — for example, for a request-scoped inner bean contained within a singleton bean. The creation of the inner bean instance is tied to its containing bean, but destruction callbacks let it participate in the request scope’s lifecycle. This is not a common scenario. Inner beans typically simply share their containing bean’s scope.

译：作为一种特殊情况，从自定义作用于中接收销毁回调，比如，请求作用域在一个单例bean内的内部bean。内部bean实例的创建与包含bean相关联，但是销毁回调参与请求作用域的生命周期。这不是常见情况。内部bean通常只共享其包含的作用域。

##### **Collections**

译：集合

The <list/>, <set/>, <map/>, and <props/> elements set the properties and arguments of the Java Collection types List, Set, Map, and Properties, respectively. The following example shows how to use them:

译：<list/>，<set/>,<map/>和<props/>标签分别设置java集合类型为List、Set、Map和properties的属性和参数。下面的例子演示如何使用。





```
<bean id="moreComplexObject" class="example.ComplexObject">
    <!-- results in a setAdminEmails(java.util.Properties) call -->
    <property name="adminEmails">
        <props>
            <prop key="administrator">administrator@example.org</prop>
            <prop key="support">support@example.org</prop>
            <prop key="development">development@example.org</prop>
        </props>
    </property>
    <!-- results in a setSomeList(java.util.List) call -->
    <property name="someList">
        <list>
            <value>a list element followed by a reference</value>
            <ref bean="myDataSource" />
        </list>
    </property>
    <!-- results in a setSomeMap(java.util.Map) call -->
    <property name="someMap">
        <map>
            <entry key="an entry" value="just some string"/>
            <entry key ="a ref" value-ref="myDataSource"/>
        </map>
    </property>
    <!-- results in a setSomeSet(java.util.Set) call -->
    <property name="someSet">
        <set>
            <value>just some string</value>
            <ref bean="myDataSource" />
        </set>
    </property>
</bean>
```





The value of a map key or value, or a set value, can also be any of the

following elements:

译：映射key或者value值，或者设置值，也可以是如下任一标签。



```
bean | ref | idref | list | set | map | props | value | null
```





###### **Collection Merging**

The Spring container also supports merging collections. An application developer can define a parent <list/>, <map/>, <set/> or <props/> element and have child <list/>, <map/>, <set/> or <props/> elements inherit and override values from the parent collection. That is, the child collection’s values are the result of merging the elements of the parent and child collections, with the child’s collection elements overriding values specified in the parent collection.

译：Spring容器支持合并集合。应用开发者可以定义父<list/>、<map\>、<set/>或者<props/>标签，并且有子<list/>、<map\>、<set/>或者<props/>标签继承并覆盖父集合中的值。那样，子容器的值是父和子容器集合标签合并之后的结果，子容器集合标签覆盖在父集合中定义的值。

This section on merging discusses the parent-child bean mechanism. Readers unfamiliar with parent and child bean definitions may wish to read the

[relevant section](https://docs.spring.io/spring/docs/5.2.3.RELEASE/spring-framework-reference/core.html#beans-child-bean-definitions) before continuing.

译：关于合并的这一节讨论了父子bean机制。不熟悉父子bean定义的读者，在继续之前可能希望阅读相关部分。

The following example demonstrates collection merging:

译：下面的例子证明集合合并。





```
<beans>
    <bean id="parent" abstract="true" class="example.ComplexObject">
        <property name="adminEmails">
            <props>
                <prop key="administrator">administrator@example.com</prop>
                <prop key="support">support@example.com</prop>
            </props>
        </property>
    </bean>
    <bean id="child" parent="parent">
        <property name="adminEmails">
            <!-- the merge is specified on the child collection definition -->
            <props merge="true">
                <prop key="sales">sales@example.com</prop>
                <prop key="support">support@example.co.uk</prop>
            </props>
        </property>
    </bean>
<beans>
```





Notice the use of the merge=true attribute on the <props/> element of the adminEmails property of the child bean definition. When the child bean is resolved and instantiated by the container, the resulting instance has an adminEmails Properties collection that contains the result of merging the child’s adminEmails collection with the parent’s adminEmails collection. The following listing shows the result:

译：请注意子bean定义的adminEmails属性的<props/>标签上使用的merge=true属性。当子bean通过容器解析并实例化时，有adminEmails Properties集合实例的结果为子集合和父集合的合并结果。下面的清单显示了最终结果：



```
administrator=administrator@example.com
sales=sales@example.com
support=support@example.co.uk
```





The child Properties collection’s value set inherits all property elements from the parent <props/>, and the child’s value for the support value overrides the value in the parent collection.

译：子Properties集合继承了所有父<props/>标签的的值，并且子容器的值支持覆盖父集合中的值。

This merging behavior applies similarly to the <list/>, <map/>, and <set/> collection types. In the specific case of the <list/> element, the semantics associated with the List collection type (that is, the notion of an ordered collection of values) is maintained. The parent’s values precede all of the child list’s values. In the case of the Map, Set, and Properties collection types, no ordering exists. Hence, no ordering semantics are in effect for the collection types that underlie the associated Map, Set, and Properties implementation types that the container uses internally.

译：合并行为对<list/>、<map\>、和<set/>集合类型使用相似。在指定的<list/>标签情况中，和list集合类型遵循的语法相关联（值的有序性概念）。父list的值先于子list的值。在Map、Set、和Properties案例中，没有顺序存在。因此，与Map、set、Properties实现相关的在容器内部使用的集合类型，没有有效的顺序语法。

###### **Limitations of Collection Merging**

译：集合合并的限制

You cannot merge different collection types (such as a Map and a List). If you do attempt to do so, an appropriate Exception is thrown. The merge attribute must be specified on the lower, inherited, child definition. Specifying the merge attribute on a parent collection definition is redundant and does not result in the desired merging.

译：你不能合并不同的集合类型（比如Map和List）。如果你尝试这么做，将会抛出一个合适的异常。在低的、继承的、子定义上，merge属性必须指定。在父集合定义中指定merge属性是多余的，不会得到希望的合并结果。

###### **Strongly-typed collection**

译：泛型集合

With the introduction of generic types in Java 5, you can use strongly typed collections. That is, it is possible to declare a Collection type such that it can only contain (for example) String elements. If you use Spring to dependency-inject a strongly-typed Collection into a bean, you can take advantage of Spring’s type-conversion support such that the elements of your strongly-typed Collection instances are converted to the appropriate type prior to being added to the Collection. The following Java class and bean definition show how to do so:

译：在java5中引入了泛型，你能使用泛型集合。那样，可以声明一个仅包含String类型的集合类型。如果你使用Spring依赖注入一个泛型集合到一个bean,你可以使用类型转换支持，在添加到集合之前，他可以使泛型集合实例转换为合适的类型。线面的java类和bean演示如何实现：



```
public class SomeClass {

    private Map<String, Float> accounts;

    public void setAccounts(Map<String, Float> accounts) {
        this.accounts = accounts;
    }
}
```









```
<beans>
    <bean id="something" class="x.y.SomeClass">
        <property name="accounts">
            <map>
                <entry key="one" value="9.99"/>
                <entry key="two" value="2.75"/>
                <entry key="six" value="3.99"/>
            </map>
        </property>
    </bean>
</beans>
```





When the accounts property of the something bean is prepared for injection, the generics information about the element type of the strongly-typed Map<String, Float> is available by reflection. Thus, Spring’s type conversion infrastructure recognizes the various value elements as being of type Float, and the string values (9.99, 2.75, and 3.99) are converted into an actual Float type.

译：当something bean转呗注入accounts属性的时候，泛型集合Map<String, Float>元素类型的泛型信息通过反射得到。因此spring的类型转换基础结构识别出变量标签为Float类型，并且String值（9.99和3.99）也转换成实际的Float类型。

##### **Null and Empty String Values**

译：null和空字符串值

Spring treats empty arguments for properties and the like as empty Strings. The following XML-based configuration metadata snippet sets the email property to the empty String value ("").

译：spring对待属性的空参数视为空字符串。下面基于xml配置的元数据片段设置email属性为空值 ("")。



```
<bean class="ExampleBean">
    <property name="email" value=""/>
</bean>
```





The preceding example is equivalent to the following Java code:

译：前面的例子和下面的java代码相等。



```
exampleBean.setEmail("");
```





The <null/> element handles null values. The following listing shows an example:

译:<null/>标签处理null值。下面的清单显示了一个示例：



```
<bean class="ExampleBean">
    <property name="email">
        <null/>
    </property>
</bean>
```





The preceding configuration is equivalent to the following Java code:

译：前面的配置与相面的java代码相等。



```
exampleBean.setEmail(null);
```





##### **XML Shortcut with the p-namespace**

译：使用p命名空间的xml快捷方式

The p-namespace lets you use the bean element’s attributes (instead of nested <property/> elements) to describe your property values collaborating beans, or both.

译：p命名空间让你使用变迁的属性去描述协作bean的属性值（而不是嵌套<property/>标签），或者两者都是用。

Spring supports extensible configuration formats with namespaces, which are based on an XML Schema definition. The beans configuration format discussed in this chapter is defined in an XML Schema document. However, the p-namespace is not defined in an XSD file and exists only in the core of Spring.

译：Spring支持用命名空间支持可扩展的配置格式，命名空间基于xml模式定义。bean配置格式在XML Schema document章节进行讨论。然而，在xsd文件中 ，p-namespace没有定义，只存在于Sping的核心中。

The following example shows two XML snippets (the first uses

standard XML format and the second uses the p-namespace) that resolve to the same result:

译:下面的例子展示了两个xml片段（第一个使用保准的xml格式，第二个使用p-namespace）,解析出来的结果相同：





```
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:p="http://www.springframework.org/schema/p"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean name="classic" class="com.example.ExampleBean">
        <property name="email" value="someone@somewhere.com"/>
    </bean>

    <bean name="p-namespace" class="com.example.ExampleBean"
        p:email="someone@somewhere.com"/>
</beans>
```





The example shows an attribute in the p-namespace called email in the bean definition. This tells Spring to include a property declaration. As previously mentioned, the p-namespace does not have a schema definition, so you can set the name of the attribute to the property name.

译：这个例子演示了用p-namespace在bean定义中命名email属性。这告诉spring包含一个属性声明。前面提到，p-namespace没有模式定义，因此你可以设置attribute的名字到property的名字。

This next example includes two more bean definitions that both have a reference to another bean:

译：下面的励志包含两个bean定义，双方都有一个其他引用。



```
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:p="http://www.springframework.org/schema/p"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean name="john-classic" class="com.example.Person">
        <property name="name" value="John Doe"/>
        <property name="spouse" ref="jane"/>
    </bean>

    <bean name="john-modern"
        class="com.example.Person"
        p:name="John Doe"
        p:spouse-ref="jane"/>

    <bean name="jane" class="com.example.Person">
        <property name="name" value="Jane Doe"/>
    </bean>
</beans>
```





This example includes not only a property value using the p-namespace but also uses a special format to declare property references. Whereas the first bean definition uses <property name="spouse" ref="jane"/> to create a reference from bean john to bean jane, the second bean definition uses p:spouse-ref="jane" as an attribute to do the exact same thing. In this case, spouse is the property name, whereas the -ref part indicates that this is not a straight value but rather a reference to another bean.

译：这个例子不仅使用p命名空间定义了一个属性值，而且使用特殊的格式定义属性引用。因此第一个bean实例使用<property name="spouse" ref="jane"/>从jane bean中创建引用。第二个bean使用p:spouse-ref="jane" 实现。在这个案例中，spouse是属性名称，尽管-ref部分表示不是一个直接值，而是对其他bean的引用。



the p-namespace is not as flexible as the standard XML format. For example, the format for declaring property references clashes with properties that end in Ref, whereas the standard XML format does not. We recommend that you choose your approach carefully and communicate this to your team members to avoid producing XML documents that use all three approaches at the same time.clashes .n打架、冲突、争论 v.打架、冲突、争论译：p命名空间不如标准的xml格式灵活。比如声明属性引用的格式与以Ref属性结尾的属性冲突。而标准的xml格式不会存在该问题。我们建议你和你的团队成员仔细地洽谈和沟通，以避免同时出现xml文档的三种方法



##### **XML Shortcut with the c-namespace**

Similar to the [XML Shortcut with the p-namespace](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-p-namespace), the c-namespace, introduced in Spring 3.1, allows inlined attributes for configuring the constructor arguments rather then nested constructor-arg elements.

译：与使用p命名空间的xml快捷方式相同，在spirng3.1中引入的c命名空间，允许配置改造器参数的内部属性，而不是嵌套使用constructor-arg标签。

The following example uses the c: namespace to do the same thing as the from

[Constructor-based Dependency Injection](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-constructor-injection):

译：下面的例子使用c命名空间去实现与[Constructor-based Dependency Injection](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-constructor-injection)相同的功能。





```
<beans xmlns="http://www.springframework.org/schema/beans"xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"xmlns:c="http://www.springframework.org/schema/c"xsi:schemaLocation="http://www.springframework.org/schema/beans https://www.springframework.org/schema/beans/spring-beans.xsd">

<bean id="beanTwo" class="x.y.ThingTwo"/>
<bean id="beanThree" class="x.y.ThingThree"/>
<!-- traditional declaration with optional argument names -->
<bean id="beanOne" class="x.y.ThingOne">
<constructor-arg name="thingTwo" ref="beanTwo"/>
<constructor-arg name="thingThree" ref="beanThree"/>
<constructor-arg name="email" value="something@somewhere.com"/>
</bean>
<!-- c-namespace declaration with argument names -->
<bean id="beanOne" class="x.y.ThingOne" c:thingTwo-ref="beanTwo" c:thingThree-ref="beanThree" c:email="something@somewhere.com"/>
</beans>
```





The c: namespace uses the same conventions as the p: one (a trailing -ref for

bean references) for setting the constructor arguments by their names. Similarly,

it needs to be declared in the XML file even though it is not defined in an XSD schema(it exists inside the Spring core).

译：c命名空间像p命名空间一样的约束（尾随-ref对一个bean的引用），通过名字设置构造器参数。同样地，尽管在XSD scheme中没用定义，他在xml文件中需要声明。

For the rare cases where the constructor argument names are not available (usually if the bytecode was compiled without debugging information), you can use fallback to the argument indexes, as follows:

rare:adj.稀少的、罕见的

译：对于构造器参数名字不可用的罕见场合（通常是在编译字节码没有调试信息的情况下），你可以使用参数索引，如下：



```
<!-- c-namespace index declaration -->
<bean id="beanOne" class="x.y.ThingOne" c:_0-ref="beanTwo" c:_1-ref="beanThree"c:_2="something@somewhere.com"/>
```

##### **Compound Property Names**

译：复合属性名称

You can use compound or nested property names when you set bean properties, as long as all components of the path except the final property name are not null. Consider the following bean definition:

译：当设置bean属性的时候，只要路径中的变量不为空（除了最后的属性名），你能使用复合或者嵌套属性名称。考虑如下bean:

```
<bean id="something" class="things.ThingOne">
   <property name="fred.bob.sammy" value="123" />
</bean>
```

 The `something` bean has a `fred` property, which has a `bob` property, which has a `sammy` property, and that final `sammy` property is being set to a value of `123`. In order for this to work, the `fred` property of `something` and the `bob` property of `fred` must not be `null` after the bean is constructed. Otherwise, a `NullPointerException` is thrown.

译：`something` bean有一个`fred`属性，`fred`有一个`bob`属性，`bob`属性有一个`sammy`属性，并且最终的`sammy`属性将设置成123。为了正常工作，在bean被构建后，`fred`的`somgthing`和bob属性必须不为空。否则抛出空指针异常。

###    **1.4.3 Using `depends-on**`

If a bean is a dependency of another bean, that usually means that one bean is set as a property of another. Typically you accomplish this with the [`` element](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-ref-element) in XML-based configuration metadata. However, sometimes dependencies between beans are less direct. An example is when a static initializer in a class needs to be triggered, such as for database driver registration. The `depends-on` attribute can explicitly force one or more beans to be initialized before the bean using this element is initialized. The following example uses the `depends-on` attribute to express a dependency on a single bean:

译：如果一个bean依赖于另一个bean,意味着一个bean作为另一个bean的属性。通常这个配置在基于xml配置中完成。然而，有时候bean之间的依赖不是很直接。例如，当一个在类中的静态的初始值需要触发时，比如数据库驱动注册。`depends-on`属性可以在bean使用这个元素初始化之前，强制一个或多个bean初始化。以下例子使用`depends-on`属性去表示依赖于一个bean.

```xml
<bean id="beanOne" class="ExampleBean" depends-on="manager"/>
<bean id="manager" class="ManagerBean" />
```

To express a dependency on multiple beans, supply a list of bean names as the value of the `depends-on` attribute (commas, whitespace, and semicolons are valid delimiters):

译：为了表示依赖多个bean,支持bean名称列表作为`depends-on`的值（逗号，空格，分号是有效的分隔符）。

```xml
<bean id="beanOne" class="ExampleBean" depends-on="manager,accountDao">
    <property name="manager" ref="manager" />
</bean>

<bean id="manager" class="ManagerBean" />
<bean id="accountDao" class="x.y.jdbc.JdbcAccountDao" />
```

|      | The `depends-on` attribute can specify both an initialization-time dependency and, in the case of [singleton](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-scopes-singleton) beans only, a corresponding destruction-time dependency. Dependent beans that define a `depends-on` relationship with a given bean are destroyed first, prior to the given bean itself being destroyed. Thus, `depends-on` can also control shutdown order. |
| ---- | ------------------------------------------------------------ |
|      | 译：depends-on属性可以同时指定依赖初始化和相应的销毁时间（仅在单例bean场合）。定义在`depends-on`中依附的bean首先被销毁，先于bean本身销毁。因此，`depends-on`也能控制bean的关闭顺序。 |

###    **1.4.4 Lazy-inialized Beans**

By default, `ApplicationContext` implementations eagerly create and configure all [singleton](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-scopes-singleton) beans as part of the initialization process. Generally, this pre-instantiation is desirable, because errors in the configuration or surrounding environment are discovered immediately, as opposed to hours or even days later. When this behavior is not desirable, you can prevent pre-instantiation of a singleton bean by marking the bean definition as being lazy-initialized. A lazy-initialized bean tells the IoC container to create a bean instance when it is first requested, rather than at startup.

<font style="color:blue;">prevent:n.阻碍、防止</font>

译：默认情况下，`applicationContext`实现急切创建和配置所有的单例bean,作为初始化过程中的一部分。通常，这种预实例化是可取的，因为在配置和相关环境中的错误会立刻发现，而不是数小时甚至数天之后发现。当

这个行为不可取时，你可以通过标记该bean为懒加载阻止单例bean的预实例化。懒加载的bean告诉IoC容器当第一次请求时创建bean实例，而不是启动时。

In XML, this behavior is controlled by the `lazy-init` attribute on the `` element, as the following example shows:

在xml中，这个行为通过在标签上的`lazy-init`属性控制，如下所示：

```xml
<bean id="lazy" class="com.something.ExpensiveToCreateBean" lazy-init="true"/>
<bean name="not.lazy" class="com.something.AnotherBean"/>
```

When the preceding configuration is consumed by an `ApplicationContext`, the `lazy` bean is not eagerly pre-instantiated when the `ApplicationContext` starts, whereas the `not.lazy` bean is eagerly pre-instantiated.

<font style="color:blue;">preceding :adj.前面的   </font>

译：当前面的配置被`ApplicationContext`使用时，当`ApplicationContext` 启动 的时候，`lazy`bean不会立刻预实例化。因此，`not.lazy`bean会立刻预实例化。

However, when a lazy-initialized bean is a dependency of a singleton bean that is not lazy-initialized, the `ApplicationContext` creates the lazy-initialized bean at startup, because it must satisfy the singleton’s dependencies. The lazy-initialized bean is injected into a singleton bean elsewhere that is not lazy-initialized.

译：然而，当一个懒加载bean依赖于一个非懒加载的单例bean,`applicationContext`在启东市创建懒加载bean,因为它必须满足单例的依赖项。懒加载bean注入到任何非懒加载的单例bean.

You can also control lazy-initialization at the container level by using the `default-lazy-init` attribute on the `` element, a the following example shows:

译：你也能控制懒加载，在容器级别通过在element上使用 `default-lazy-init` 属性，如下所示：

```xml
<beans default-lazy-init="true">
    <!-- no beans will be pre-instantiated... -->
</beans>
```

###    **1.4.5 Autowiring Collaborators**

译：自动装配

The Spring container can autowire relationships between collaborating beans. You can let Spring resolve collaborators (other beans) automatically for your bean by inspecting the contents of the `ApplicationContext`. Autowiring has the following advantages:

- Autowiring can significantly reduce the need to specify properties or constructor arguments. (Other mechanisms such as a bean template [discussed elsewhere in this chapter](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-child-bean-definitions) are also valuable in this regard.)
- Autowiring can update a configuration as your objects evolve. For example, if you need to add a dependency to a class, that dependency can be satisfied automatically without you needing to modify the configuration. Thus autowiring can be especially useful during development, without negating the option of switching to explicit wiring when the code base becomes more stable.

significantly ：adv.有重大意义地；明显地；显著地；

译：spring容器能自动连接协作bean之间的关系。通过检查`ApplicationContext`的内容，可以让Spring自动解析协作bean（bean本身之外的bean）.自动注入有如下优势：

- 自动注入可以明显地减少需要声明的属性或者构造参数（在这方面，本章节讨论的其他的机制也很有用，比如bean模板）。
- 自动注入可以随着对象的变化更新配置。例如，如果你需要给一个类增加一个依赖，依赖在不需要修改配置文件的情况下自动满足。此外，自动注入在开发期间特别有用，当基础代码非常稳定的时候，不需要对代码进行修改。

When using XML-based configuration metadata (see [Dependency Injection](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-collaborators)), you can specify the autowire mode for a bean definition with the `autowire` attribute of the element. The autowiring functionality has four modes. You specify autowiring per bean and can thus choose which ones to autowire. The following table describes the four autowiring modes:

译：当使用xml配置元数据时，你可以使用`autowire`属性指定注入模型。自动注入功能有四种模型。你可以从这四种注入模型中，指定每个bean的自动注入模型。如下表格描述四种自动注入模型：

| Mode          | Explanation                                                  |
| ------------- | ------------------------------------------------------------ |
| `no`          | (Default) No autowiring. Bean references must be defined by `ref` elements. Changing the default setting is not recommended for larger deployments, because specifying collaborators explicitly gives greater control and clarity. To some extent, it documents the structure of a system. |
| `byName`      | Autowiring by property name. Spring looks for a bean with the same name as the property that needs to be autowired. For example, if a bean definition is set to autowire by name and it contains a `master` property (that is, it has a `setMaster(..)` method), Spring looks for a bean definition named `master` and uses it to set the property. |
| `byType`      | Lets a property be autowired if exactly one bean of the property type exists in the container. If more than one exists, a fatal exception is thrown, which indicates that you may not use `byType` autowiring for that bean. If there are no matching beans, nothing happens (the property is not set). |
| `constructor` | Analogous to `byType` but applies to constructor arguments. If there is not exactly one bean of the constructor argument type in the container, a fatal error is raised. |

| 模型        | 解释                                                         |
| ----------- | ------------------------------------------------------------ |
| no          | 默认为no模型。bean引用必须通过`ref`元素定义。对于大的部署（应用），不推荐修改默认设置，因为指定协作bean控制更明确、清晰。从某种程度上来说，它记录了系统的结构。 |
| byName      | 通过属性名注入。Spring寻找与需要自动注入属性同名的bean.例如，如果一个bean设置为byName,并且这个bean包含一个`master`属性（这意味着该bean有一个setMaster()方法），Spring寻找一个命名为master的bean,并且用它设置属性。 |
| byType      | 如果容器中存在一个属性类型的bean,让这个属性自动注入。如果超过一个，会抛出一个致命异常，表示你不能使用byType自动注入这个bean。如果没有匹配bean,不会发生异常（这个属性也不会设置）。 |
| constructor | 与byType类似，但适用于构造参数。如果在容器中没有具体的构造参数类型的bean,就会引发致命错误。 |

With `byType` or `constructor` autowiring mode, you can wire arrays and typed collections. In such cases, all autowire candidates within the container that match the expected type are provided to satisfy the dependency. You can autowire strongly-typed `Map` instances if the expected key type is `String`. An autowired `Map` instance’s values consist of all bean instances that match the expected type, and the `Map` instance’s keys contain the corresponding bean names.

译：使用`byType`或者`constructor` 注入模型，你能连接数组和类型化的集合。在这个场景下，所有在容器内提供的匹配期望类型的bean都会自动注入以满足依赖。如果预期的key类型是String,可以自动注入强类型的Map实例。自动注入Map实例的值由所有陪陪期望类型的bean实例组成，并且Map实例的key包含相应bean名称。

##### Limitations and Disadvantages of Autowiring

译：自动注入的限制和缺点

Autowiring works best when it is used consistently across a project. If autowiring is not used in general, it might be confusing to developers to use it to wire only one or two bean definitions.

译：当在项目中使用一致的时候，自动注入效果最好。如果通常不使用自动注入，仅仅连接一个或者两个beanshi用使用，可能对开发者造成困惑。

Consider the limitations and disadvantages of autowiring:

- Explicit dependencies in `property` and `constructor-arg` settings always override autowiring. You cannot autowire simple properties such as primitives, `Strings`, and `Classes` (and arrays of such simple properties). This limitation is by-design.
- Autowiring is less exact than explicit wiring. Although, as noted in the earlier table, Spring is careful to avoid guessing in case of ambiguity that might have unexpected results. The relationships between your Spring-managed objects are no longer documented explicitly.
- Wiring information may not be available to tools that may generate documentation from a Spring container.
- Multiple bean definitions within the container may match the type specified by the setter method or constructor argument to be autowired. For arrays, collections, or `Map` instances, this is not necessarily a problem. However, for dependencies that expect a single value, this ambiguity is not arbitrarily resolved. If no unique bean definition is available, an exception is thrown.

译：考虑自动注入的局限性和缺点：

- 在`property`和`constructor-arg`中设置的显示依赖总是忽略自动注入。你不能自动注入简单属性，像基本类型、string和类（像简单属性的数组）。这个局限性是故意设计的。
- 自动注入没有显示注入明确。尽管，在前面表格中记录了，Spring避免了在可能产生意外结果的歧义场合中猜测。spring管理的对象不在明确地记录下来。
- 从spring容器中产生文档的工具，注入信息可能不能用。
- 容器中定义的多个bean可能匹配指定的类，通过set方法或构造参数取匹配。对于数组、集合或者Map实例，这不一定是问题。然而，对于一个值的依赖项，这个歧义性不是任意解决的。如果没有唯一的bean可用，会抛出一个异常。

In the latter scenario, you have several options:

- Abandon autowiring in favor of explicit wiring.
- Avoid autowiring for a bean definition by setting its `autowire-candidate` attributes to `false`, as described in the [next section](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-autowire-candidate).
- Designate a single bean definition as the primary candidate by setting the `primary` attribute of its `` element to `true`.
- Implement the more fine-grained control available with annotation-based configuration, as described in [Annotation-based Container Configuration](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-annotation-config).

Abandon :v.放弃

designate ：v.指定

译：在后一种情况中，你有几个选项：

- 放弃自动注入，支持显示注入。
- 如下一节所述，对一个bean通过设置`autowire-candidate`为false避免自动注入。
- 通过设置primary属性为true,指定一个bean为唯一选项。
- 如在基于注解的容器配置中所述，使用注解实现更细粒度的控制。

##### Excluding a Bean from Autowiring

On a per-bean basis, you can exclude a bean from autowiring. In Spring’s XML format, set the `autowire-candidate` attribute of the  element to `false`. The container makes that specific bean definition unavailable to the autowiring infrastructure (including annotation style configurations such as [@Autowired](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-autowired-annotation)).

译：在每个bean的基础上，你可以从自动注入中排除一个bean.在Spring的xml中，设置`autowire-candidate`的属性为值为false。容器使在自动注入架构下的指定的bean不可用（包含注解风格的配置像@Autowired）

|      | The `autowire-candidate` attribute is designed to only affect type-based autowiring. It does not affect explicit references by name, which get resolved even if the specified bean is not marked as an autowire candidate. As a consequence, autowiring by name nevertheless injects a bean if the name matches. |
| ---- | ------------------------------------------------------------ |
|      | `autowire-candidate`属性设计，仅在byType中有效。尽管指定的bean标记为autowire candidate的属性得到解析，在byName中也不生效。因此，如果名字匹配，byName下仍然注入bean. |

You can also limit autowire candidates based on pattern-matching against bean names. The top-level  element accepts one or more patterns within its `default-autowire-candidates` attribute. For example, to limit autowire candidate status to any bean whose name ends with `Repository`, provide a value of `*Repository`. To provide multiple patterns, define them in a comma-separated list. An explicit value of `true` or `false` for a bean definition’s `autowire-candidate` attribute always takes precedence. For such beans, the pattern matching rules do not apply.

译：你也能基于对bean名称的模式匹配来限制自动注入项。顶级元素在`default-autowire-candidates`属性内接受一个或多个模式。例如，要限制自动注入项状态为任何以`Repository`结尾的bean,提供一个`*Repository`值。去支持更多的匹配模式，在以逗号分隔符的列表中定义。bean的`autowire-candidate`属性的值（true或者false）始终有限。像这些bean,模式匹配规则不会生效。

These techniques are useful for beans that you never want to be injected into other beans by autowiring. It does not mean that an excluded bean cannot itself be configured by using autowiring. Rather, the bean itself is not a candidate for autowiring other beans.

译：对于永远不想通过自动注入注入其他bean的bean，这些技术特别有用。不意味着排除的bean自身不能通过自动注入配置。相反，bean本身不是自动注入其他bean的候选对象。

###    **1.4.6 Method Injection** 

In most application scenarios, most beans in the container are [singletons](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-scopes-singleton). When a singleton bean needs to collaborate with another singleton bean or a non-singleton bean needs to collaborate with another non-singleton bean, you typically handle the dependency by defining one bean as a property of the other. A problem arises when the bean lifecycles are different. Suppose singleton bean A needs to use non-singleton (prototype) bean B, perhaps on each method invocation on A. The container creates the singleton bean A only once, and thus only gets one opportunity to set the properties. The container cannot provide bean A with a new instance of bean B every time one is needed.

译：在大多数应用场景，在容器中的大多数bean是单例的。当一个单例bean需要另一个单例bean协作，或者非单例bean需要另一个非单例bean协作，你通常通过定义一个其他属性的bean处理依赖。单bean的生命周期不同的时候，会引发一个问题。假设单例bean A需要使用非单例bean B,每个方法都可能调用A.容器穿件单例bean仅仅一次，因此仅仅得到一次机会设置属性(bean属性)。容器不能每次需要beanA,都为它提供一个新的bean B。

A solution is to forego some inversion of control. You can [make bean A aware of the container](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-aware) by implementing the `ApplicationContextAware` interface, and by [making a `getBean("B")` call to the container](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-client) ask for (a typically new) bean B instance every time bean A needs it. The following example shows this approach:

译：解决办法是放弃一些控制权的倒置。你能通过实现`ApplicationContextAware`接口使bean A得到容器，每次bean A需要bean B的时候，调用 getBean("B")方法寻找bean B实例。如下实例：

```java
package fiona.apple;

// Spring-API imports
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class CommandManager implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    public Object process(Map commandState) {
        // grab a new instance of the appropriate Command
        Command command = createCommand();
        // set the state on the (hopefully brand new) Command instance
        command.setState(commandState);
        return command.execute();
    }

    protected Command createCommand() {
        // notice the Spring API dependency!
        return this.applicationContext.getBean("command", Command.class);
    }

    public void setApplicationContext(
            ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
```

The preceding is not desirable, because the business code is aware of and coupled to the Spring Framework. Method Injection, a somewhat advanced feature of the Spring IoC container, lets you handle this use case cleanly.

译：前面的不可取，因为业务代码和spring框架耦合了。方法注入，是Spring IoC容器的高级特性，可以让你干净的处理这个问题。

You can read more about the motivation for Method Injection in [this blog entry](https://spring.io/blog/2004/08/06/method-injection/).

译：你可以在[this blog entry](https://spring.io/blog/2004/08/06/method-injection/)中读取更多的关于方法注入动机的内容。

##### Lookup Method Injection

译：查找方法注入

Lookup method injection is the ability of the container to override methods on container-managed beans and return the lookup result for another named bean in the container. The lookup typically involves a prototype bean, as in the scenario described in [the preceding section](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-method-injection). The Spring Framework implements this method injection by using bytecode generation from the CGLIB library to dynamically generate a subclass that overrides the method.

译：查找方法注入是容器覆盖在容器管理bean中的方法并返回在容器中的另一个bean结果的能力。查询荣昌设计一个原型bean,如前一个所述。Spring框架通过使用从DGlib库动态生成子类的覆盖方法字节码实现方法注入。

|      | For this dynamic subclassing to work, the class that the Spring bean container subclasses cannot be `final`, and the method to be overridden cannot be `final`, either.  Unit-testing a class that has an `abstract` method requires you to subclass the class yourself and to supply a stub implementation of the `abstract` method.                                                                                 Concrete methods are also necessary for component scanning, which requires concrete classes to pick up.                                                                                                                                                     A further key limitation is that lookup methods do not work with factory methods and in particular not with `@Bean` methods in configuration classes, since, in that case, the container is not in charge of creating the instance and therefore cannot create a runtime-generated subclass on the fly. |
| ---- | ------------------------------------------------------------ |
|      | 要让动态子类工作，Spring容器的子类不能是final并且覆盖的方法也不能是final.单元测试一个有`abstract`方法的类，需要你自己去实现抽象方法。                                                                                                                   具体方法也需要组件扫描，需要集体类进行提取。       另一个关键限制是，查找方法不能与工厂方法，特别是在配置类中没有@bean方法工作。因为，在这种场合，容器不负责创建实例，因此不能在运行时创建子类。 |

In the case of the `CommandManager` class in the previous code snippet, the Spring container dynamically overrides the implementation of the `createCommand()` method. The `CommandManager` class does not have any Spring dependencies, as the reworked example shows:

译：在前面的代码片段中`CommandManager` 类，spring容器动态覆盖实现的`createCommand`方法.`CommandManager` 类没有任何Spring依赖，重新工作的例子如下:

```java
package fiona.apple;

// no more Spring imports!

public abstract class CommandManager {

    public Object process(Object commandState) {
        // grab a new instance of the appropriate Command interface
        Command command = createCommand();
        // set the state on the (hopefully brand new) Command instance
        command.setState(commandState);
        return command.execute();
    }

    // okay... but where is the implementation of this method?
    protected abstract Command createCommand();
}
```

In the client class that contains the method to be injected (the `CommandManager` in this case), the method to be injected requires a signature of the following form:

译：在包含注入方法的客户端类中（ `CommandManager`类），方法注入需要一个如下表单中的标志：

```xml
<public|protected> [abstract] <return-type> theMethodName(no-arguments);
```

If the method is `abstract`, the dynamically-generated subclass implements the method. Otherwise, the dynamically-generated subclass overrides the concrete method defined in the original class. Consider the following example:

译：如果方法是`abstract`，动态生成的子类实现这个方法。因此，动态生成的子类覆盖在原始类中的具体方法。考虑如下例子：

```xml
<!-- a stateful bean deployed as a prototype (non-singleton) -->
<bean id="myCommand" class="fiona.apple.AsyncCommand" scope="prototype">
    <!-- inject dependencies here as required -->
</bean>

<!-- commandProcessor uses statefulCommandHelper -->
<bean id="commandManager" class="fiona.apple.CommandManager">
    <lookup-method name="createCommand" bean="myCommand"/>
</bean>
```

The bean identified as `commandManager` calls its own `createCommand()` method whenever it needs a new instance of the `myCommand` bean. You must be careful to deploy the `myCommand` bean as a prototype if that is actually what is needed. If it is a [singleton](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-scopes-singleton), the same instance of the `myCommand` bean is returned each time.

译：标识为 `commandManager`bean无论何时需要一个新实例的`myCommand` bean，调用它自身的 `createCommand()` 方法。你必须仔细部署`myCommand` bean，它为原型是需要的。如果他是单例的，每次就会返回相同实例的`myCommand`.

Alternatively, within the annotation-based component model, you can declare a lookup method through the `@Lookup` annotation, as the following example shows:

或者，在基于注解的组件模型内，你可以通过`@Lookup`注解声明lookup方法。如下所示：

```java
public abstract class CommandManager {

    public Object process(Object commandState) {
        Command command = createCommand();
        command.setState(commandState);
        return command.execute();
    }

    @Lookup("myCommand")
    protected abstract Command createCommand();
}
```

Or, more idiomatically, you can rely on the target bean getting resolved against the declared return type of the lookup method:

译：或者更通俗地说，你能依靠lookup方法的返回类型得到解析目标bean。

```java
public abstract class CommandManager {

    public Object process(Object commandState) {
        MyCommand command = createCommand();
        command.setState(commandState);
        return command.execute();
    }

    @Lookup
    protected abstract MyCommand createCommand();
}
```

Note that you should typically declare such annotated lookup methods with a concrete stub implementation, in order for them to be compatible with Spring’s component scanning rules where abstract classes get ignored by default. This limitation does not apply to explicitly registered or explicitly imported bean classes.

译：请注意，你应该在具体的实现方法上定义这个注解，为了兼容Spring的组件扫描规则（抽象方法默认忽略）。这个限制不适用于显示注册或者显示导入的类。

|      | Another way of accessing differently scoped target beans is an `ObjectFactory`/ `Provider` injection point. See [Scoped Beans as Dependencies](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-scopes-other-injection).                                                                          You may also find the `ServiceLocatorFactoryBean` (in the `org.springframework.beans.factory.config` package) to be useful. |
| ---- | ------------------------------------------------------------ |
|      | 译：访问不同scoped的目标类的另外一种方法是`ObjectFactory`/ `Provider`注入点。请看[Scoped Beans as Dependencies](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-scopes-other-injection).你可能发现`ServiceLocatorFactoryBean`（在`org.springframework.beans.factory.config` 包中）特别有用。 |

##### Arbitrary Method Replacement

译：任意方法替换

A less useful form of method injection than lookup method injection is the ability to replace arbitrary methods in a managed bean with another method implementation. You can safely skip the rest of this section until you actually need this functionality.

不如查找方法注入有用的方法注入形式是用另一个方法实现替换在容器管理bean中的任意方法。知道需要这个功能之前，你能安全地跳过剩余的部分。

With XML-based configuration metadata, you can use the `replaced-method` element to replace an existing method implementation with another, for a deployed bean. Consider the following class, which has a method called `computeValue` that we want to override:

译：使用基于xml配置的元数据，你能使用`replaced-method`元素对一个部署的bean,用另一个方法替换存在的方法实现。考虑如下类，它有一个方法叫做`computeValue`，我们期望覆盖该方法。

```java
public class MyValueCalculator {

    public String computeValue(String input) {
        // some real code...
    }

    // some other methods...
}
```

A class that implements the `org.springframework.beans.factory.support.MethodReplacer` interface provides the new method definition, as the following example shows:

译：一个实现了`org.springframework.beans.factory.support.MethodReplacer`接口的类提供了新方法，如下所示:

```java
/**
 * meant to be used to override the existing computeValue(String)
 * implementation in MyValueCalculator
 */
public class ReplacementComputeValue implements MethodReplacer {

    public Object reimplement(Object o, Method m, Object[] args) throws Throwable {
        // get the input value, work with it, and return a computed result
        String input = (String) args[0];
        ...
        return ...;
    }
}
```

The bean definition to deploy the original class and specify the method override would resemble the following example:

译：部署原始类的bean并声明方法覆盖将类似于下面的例子：

```xml
<bean id="myValueCalculator" class="x.y.z.MyValueCalculator">
    <!-- arbitrary method replacement -->
    <replaced-method name="computeValue" replacer="replacementComputeValue">
        <arg-type>String</arg-type>
    </replaced-method>
</bean>

<bean id="replacementComputeValue" class="a.b.c.ReplacementComputeValue"/>
```

 You can use one or more`<arg-type/>` elements within the `<replaced-method>` element to indicate the method signature of the method being overridden. The signature for the arguments is necessary only if the method is overloaded and multiple variants exist within the class. For convenience, the type string for an argument may be a substring of the fully qualified type name. For example, the following all match `java.lang.String`:

译：你能在`<replaced-method>`元素中使用一个或者多个`<arg-type/>`标签去表明被覆盖方法的方法标志。如果在类中方法是重载或者存在多个变量，对于参数的标志是必须的。为了方便起见，一个sting类型的参数可以是全类名的子字符串。如下所示，全部匹配`java.lang.String`：

```java
java.lang.String
String
Str
```

Because the number of arguments is often enough to distinguish between each possible choice, this shortcut can save a lot of typing, by letting you type only the shortest string that matches an argument type.

译： 因为参数的数量通常足以区分每个可能的选择，所以这个快捷方式可以节省大量的输入，只允许您键入与参数类型匹配的最短字符串

### 1.5. Bean Scopes

译：bean的作用域

When you create a bean definition, you create a recipe for creating actual instances of the class defined by that bean definition. The idea that a bean definition is a recipe is important, because it means that, as with a class, you can create many object instances from a single recipe.

译：当你创建一个bean的时候，你通过bean创建一个配方,创建类定义的实际实例.bean是一个配方的注意是重要的，因为他意味着用一个类，你从一个配方中能创建许多对象实例。

You can control not only the various dependencies and configuration values that are to be plugged into an object that is created from a particular bean definition but also control the scope of the objects created from a particular bean definition. This approach is powerful and flexible, because you can choose the scope of the objects you create through configuration instead of having to bake in the scope of an object at the Java class level. Beans can be defined to be deployed in one of a number of scopes. The Spring Framework supports six scopes, four of which are available only if you use a web-aware `ApplicationContext`. You can also create [a custom scope.](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-scopes-custom)

译：你不仅能控制插入到特定bean对象的变量依赖和配置值，而且可以控制特定bean对象的作用域。这种方法强大且灵活，因为你能通过配置选择创建对象的作用域，而不是必须在java类级别固定对象的作用域。bean可以定义为部署在许多作用域中的一个。Spring框架支持六中作用域，其中四种作用域仅用在支持web的`ApplicationContext`.你也能创建自定义作用域。

The following table describes the supported scopes:

译：以下表格描述了支持的作用域：

| Scope                                                        | Description                                                  |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| [singleton](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-scopes-singleton) | (Default) Scopes a single bean definition to a single object instance for each Spring IoC container. |
| [prototype](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-scopes-prototype) | Scopes a single bean definition to any number of object instances. |
| [request](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-scopes-request) | Scopes a single bean definition to the lifecycle of a single HTTP request. That is, each HTTP request has its own instance of a bean created off the back of a single bean definition. Only valid in the context of a web-aware Spring `ApplicationContext`. |
| [session](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-scopes-session) | Scopes a single bean definition to the lifecycle of an HTTP `Session`. Only valid in the context of a web-aware Spring `ApplicationContext`. |
| [application](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-scopes-application) | Scopes a single bean definition to the lifecycle of a `ServletContext`. Only valid in the context of a web-aware Spring `ApplicationContext`. |
| [websocket](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/web.html#websocket-stomp-websocket-scope) | Scopes a single bean definition to the lifecycle of a `WebSocket`. Only valid in the context of a web-aware Spring `ApplicationContext`. |

译：

| 作用域      | 描述                                                         |
| ----------- | ------------------------------------------------------------ |
| singletion  | 默认值。一个bean的作用域对每个Spring IoC容器只产生一个对象实例。 |
| prototype   | 一个bean的作用域产生许多对象实例。                           |
| request     | 一个bean的作用域在一次Http请求的生命周期。每个HTTP请求有自己的实例，这个bean是在请求到来之时创建的。仅在web环境的中有效。 |
| session     | 一个bean的实例限定在一个HTTP`Session`生命周期。仅在web环境中有效。 |
| application | 一个bean的实例限定在一个`ServletContext`生命周期。仅在web环境中有效。 |
| websocket   | 一个bean的作用域限定在webSocket生命周期。仅在web环境有效。   |

|      | As of Spring 3.0, a thread scope is available but is not registered by default. For more information, see the documentation for [`SimpleThreadScope`](https://docs.spring.io/spring-framework/docs/5.2.5.RELEASE/javadoc-api/org/springframework/context/support/SimpleThreadScope.html). For instructions on how to register this or any other custom scope, see [Using a Custom Scope](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-scopes-custom-using). |
| ---- | ------------------------------------------------------------ |
|      | 在spring3.0,一个县城的作用域是可用的但是默认不等级。获取更多信息，请看`SimpleThreadScode`.关于怎样注册这个作用域或其他自定义作用域的说明，请看Using a Sustom Scope. |

#### 1.5.1. The Singleton Scope

Only one shared instance of a singleton bean is managed, and all requests for beans with an ID or IDs that match that bean definition result in that one specific bean instance being returned by the Spring container.

译：仅管理一个bean的共享实例，并且所有bean的请求只用一个ID，匹配到一个bean,通过Spring容器返回一个指定的实例。

To put it another way, when you define a bean definition and it is scoped as a singleton, the Spring IoC container creates exactly one instance of the object defined by that bean definition. This single instance is stored in a cache of such singleton beans, and all subsequent requests and references for that named bean return the cached object. The following image shows how the singleton scope works:

译：换句话说，当你你定义一个bean且作用域为单例的时候，Spring IoC容器确切地创建一个通过bean定义的对象实例。这个实例存储在单例bean缓存中，并且后续所有的请求和引用都返回缓存对象。下面的图片展示单例作用域如何工作：

![](E:\docs\learn\spring\singleton.png)

Spring’s concept of a singleton bean differs from the singleton pattern as defined in the Gang of Four (GoF) patterns book. The GoF singleton hard-codes the scope of an object such that one and only one instance of a particular class is created per ClassLoader. The scope of the Spring singleton is best described as being per-container and per-bean. This means that, if you define one bean for a particular class in a single Spring container, the Spring container creates one and only one instance of the class defined by that bean definition. The singleton scope is the default scope in Spring. To define a bean as a singleton in XML, you can define a bean as shown in the following example:

译：Spring单例bean的概念不同于在Gang of Four (GoF)书中定义的单例模式。GoF单例模式在每个ClassLoader中创建一个实例bean.Spring单例的作用域最好描述为每个容器一个bean.这意味着，如果你在一个Spring容器中定义一个特定类的bean，Spring容器创建一个、仅一个实例。在Spring中单例作用域是默认作用域。在xml中为了定义一个单例bean,你能定义为如下例子：

```xml
<bean id="accountService" class="com.something.DefaultAccountService"/>

<!-- the following is equivalent, though redundant (singleton scope is the default) -->
<bean id="accountService" class="com.something.DefaultAccountService" scope="singleton"/>
```

#### 1.5.2. The Prototype Scope

The non-singleton prototype scope of bean deployment results in the creation of a new bean instance every time a request for that specific bean is made. That is, the bean is injected into another bean or you request it through a `getBean()` method call on the container. As a rule, you should use the prototype scope for all stateful beans and the singleton scope for stateless beans.

译：非单例`prototype` 作用域导致每次请求一个指定bean都创建一个新的bean实例。也就是说，注入到另一个bean或者你通过 `getBean()`方法调用容器得到的bean,都会产生一个新的实例。一般来说，你应该对所用的有状态的bean使用`prototype` 作用域，对无状态bean使用`singleton` 作用域。

The following diagram illustrates the Spring prototype scope:

译：下图说明了Spring的`prototype` 作用域。

![](E:\docs\learn\spring\prototype.png)

(A data access object (DAO) is not typically configured as a prototype, because a typical DAO does not hold any conversational state. It was easier for us to reuse the core of the singleton diagram.)

译：数据访问层（DAO）通常不配置成`prototype`，因为通常DAO没有任何会话状态。我们很容易重用单例图的核心。

The following example defines a bean as a prototype in XML:

译：如下所示在xml中定义了一个`prototype`bean.

```xml
<bean id="accountService" class="com.something.DefaultAccountService" scope="prototype"/>
```

In contrast to the other scopes, Spring does not manage the complete lifecycle of a prototype bean. The container instantiates, configures, and otherwise assembles a prototype object and hands it to the client, with no further record of that prototype instance. Thus, although initialization lifecycle callback methods are called on all objects regardless of scope, in the case of prototypes, configured destruction lifecycle callbacks are not called. The client code must clean up prototype-scoped objects and release expensive resources that the prototype beans hold. To get the Spring container to release resources held by prototype-scoped beans, try using a custom [bean post-processor](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-extension-bpp), which holds a reference to beans that need to be cleaned up.

In contrast to： 与…对比 

译：与其他作用域对比，Spring部管理`prototype`bean的完整生命周期。容器实例化、配置和以其他形式组装`prototype` 对象，并交给用户，并没有记录原型实例。因此，尽管初始化生命周期回调方法在所有对象上被调用，无论其他作用域如何，对于原型，配置销毁生命周期回调不会调用。客户端代码必须清除原型作用域对象和释放原型bean持有的昂贵资源。要得到Spring容器去释放原型bean持有的资源，请使用自定义bean后置处理器，它持有需要清理bean的引用。

In some respects, the Spring container’s role in regard to a prototype-scoped bean is a replacement for the Java `new` operator. All lifecycle management past that point must be handled by the client. (For details on the lifecycle of a bean in the Spring container, see [Lifecycle Callbacks](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-lifecycle).)

译：在某些方面，Spirng容器的原型bean角色是代替Java `new`操作。超过这个点的所有的生命周期管理通过客户端处理。（在spring容器中的bean的生命周期的详情，请看Lifecycle 回调章节）

#### 1.5.3. Singleton Beans with Prototype-bean Dependencies

译:单例bean依赖原型bean.

When you use singleton-scoped beans with dependencies on prototype beans, be aware that dependencies are resolved at instantiation time. Thus, if you dependency-inject a prototype-scoped bean into a singleton-scoped bean, a new prototype bean is instantiated and then dependency-injected into the singleton bean. The prototype instance is the sole instance that is ever supplied to the singleton-scoped bean.

译：当你在原型bean上使用单例作用域依赖的时候，你要知道依赖在实例化时被解析。因此，如果依赖注入一个原型作用域的bean到一个单例bean,会实例化一个新的原型bean注入到单例bean.原型实例是唯一提供给单例作用的bean.

However, suppose you want the singleton-scoped bean to acquire a new instance of the prototype-scoped bean repeatedly at runtime. You cannot dependency-inject a prototype-scoped bean into your singleton bean, because that injection occurs only once, when the Spring container instantiates the singleton bean and resolves and injects its dependencies. If you need a new instance of a prototype bean at runtime more than once, see [Method Injection](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-method-injection)

译：然而，假设你想单例作用域bean在运行时去重复获得一个原型作用域bean的新实例。你不能依赖注入一个原型bean到你的单例bean,因为当Spring容器实例化单例bean并且解析、注入它的依赖的时候，注入只发生一次。如过你在运行时超过依次需要一个原型bean新实例，请看方法注入。

#### 1.5.4. Request, Session, Application, and WebSocket Scopes

译：`request`, `session`, `application`, and `websocket` 作用域。

The `request`, `session`, `application`, and `websocket` scopes are available only if you use a web-aware Spring `ApplicationContext` implementation (such as `XmlWebApplicationContext`). If you use these scopes with regular Spring IoC containers, such as the `ClassPathXmlApplicationContext`, an `IllegalStateException` that complains about an unknown bean scope is thrown.

译：`request`, `session`, `application`, and `websocket` 作用域尽在web环境中有用。如果你在普通的Spring容器中使用这些作用域，像`ClassPathXmlApplicationContext`，会抛出不明确的bean作用域的`IllegalStateException` 异常。

##### Initial Web Configuration

译：初始化web配置

To support the scoping of beans at the `request`, `session`, `application`, and `websocket` levels (web-scoped beans), some minor initial configuration is required before you define your beans. (This initial setup is not required for the standard scopes: `singleton` and `prototype`.)

译：为了支持 `request`, `session`, `application`, and `websocket`级别的bean作用域(web作用域bean)，在定义bean之前需要一些初始化配置。（对`singleton`和`prototype`不需要初始化配置）

How you accomplish this initial setup depends on your particular Servlet environment.

译：如何完成初始化配置取决于特定的Servlet环境。

If you access scoped beans within Spring Web MVC, in effect, within a request that is processed by the Spring `DispatcherServlet`, no special setup is necessary. `DispatcherServlet` already exposes all relevant state.

译：如果在Spring Web MVC内访问作用域bean,实际上，在通过Spring `DispatcherServlet`的请求处理内，弄需要特别的设置。`DispatcherServlet`已经暴露了所有的相关状态。

If you use a Servlet 2.5 web container, with requests processed outside of Spring’s `DispatcherServlet` (for example, when using JSF or Struts), you need to register the `org.springframework.web.context.request.RequestContextListener` `ServletRequestListener`. For Servlet 3.0+, this can be done programmatically by using the `WebApplicationInitializer` interface. Alternatively, or for older containers, add the following declaration to your web application’s `web.xml` file:

译：如果你使用Servlet2.5容器，请求在Spring的DispacherServlet之外处理（比如，当使用JSF或者Struts时），你需要注册`org.springframework.web.context.request.RequestContextListener` `ServletRequestListener`类。对于Servlet3.0之后的版本，你能使用`WebApplicationInitialize`接口完成。或者，对于较旧的容器，增加如下声明到你应用的`web.xml`文件。

```xml
<web-app>
    ...
    <listener>
        <listener-class>
            org.springframework.web.context.request.RequestContextListener
        </listener-class>
    </listener>
    ...
</web-app>
```

Alternatively, if there are issues with your listener setup, consider using Spring’s `RequestContextFilter`. The filter mapping depends on the surrounding web application configuration, so you have to change it as appropriate. The following listing shows the filter part of a web application:

译：或者，如果你的监听设置有问题，考虑使用Spring的`RequestContextFilter`。过滤器映射取决于web应用配置，因此你必须根据需要进行改变。如下清单展示了web应用过滤器的部分：

```xml
<web-app>
    ...
    <filter>
        <filter-name>requestContextFilter</filter-name>
        <filter-class>org.springframework.web.filter.RequestContextFilter</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>requestContextFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
    ...
</web-app>
```

`DispatcherServlet`, `RequestContextListener`, and `RequestContextFilter` all do exactly the same thing, namely bind the HTTP request object to the `Thread` that is servicing that request. This makes beans that are request- and session-scoped available further down the call chain.

译：`DispatcherServlet`, `RequestContextListener`, and `RequestContextFilter` 都是做相同的事情，即绑定当前服务线程的HTTP请求对象。这使得request和sesseion作用域的bean在调用链的更下游使用。

##### Request scope

Consider the following XML configuration for a bean definition:

译：考虑如下bean的xml配置：

```xml
<bean id="loginAction" class="com.something.LoginAction" scope="request"/>
```

The Spring container creates a new instance of the `LoginAction` bean by using the `loginAction` bean definition for each and every HTTP request. That is, the `loginAction` bean is scoped at the HTTP request level. You can change the internal state of the instance that is created as much as you want, because other instances created from the same `loginAction` bean definition do not see these changes in state. They are particular to an individual request. When the request completes processing, the bean that is scoped to the request is discarded.

译：Spring容器对每一次HTTP请求通过`loginAction`创建了一个`LoginAction`新实例.也就是说，`loginAction`bean的作用域在HTTP请求级别。你能随心所欲地改变已创建实例的内部状态，因为从同一个loginAction创建的其他实例看不到这些内部变化。他们是特定于每个请求。当请求完成处理后，request作用域的bean就会丢弃。

When using annotation-driven components or Java configuration, the `@RequestScope` annotation can be used to assign a component to the `request` scope. The following example shows how to do so:

译：当使用注解驱动组件或者java配置的时候，`@RequestScode`注解能标记组件为`request`作用域。如下例子展示如何使用：

```java
@RequestScope
@Component
public class LoginAction {
    // ...
}
```

##### Session Scope

译：session 作用域

Consider the following XML configuration for a bean definition:

译：考虑如下bean的xml配置：

```xml
<bean id="userPreferences" class="com.something.UserPreferences" scope="session"/>
```

The Spring container creates a new instance of the `UserPreferences` bean by using the `userPreferences` bean definition for the lifetime of a single HTTP `Session`. In other words, the `userPreferences` bean is effectively scoped at the HTTP `Session` level. As with request-scoped beans, you can change the internal state of the instance that is created as much as you want, knowing that other HTTP `Session` instances that are also using instances created from the same `userPreferences` bean definition do not see these changes in state, because they are particular to an individual HTTP `Session`. When the HTTP `Session` is eventually discarded, the bean that is scoped to that particular HTTP `Session` is also discarded.

译：Spring容器在单个HTTP Session通过使用`userPreferendes`创建一个新实例`UserPreferences` bean。换言而之，`userPreferences` bean在HTTP sesseion级别的作用域是有效的。与request作用域bean相同，你能随心所欲地改变已创建实例的内部状态，其他的HTTP `Session`从相同`userPreferences` bean创建的实例无法看见其他bean的内部状态。因为他们是特定于每一个HTTP `Session`.当HTTP `Session`完全销毁的时候，在HTTP Session作用域的bean也会销毁。

When using annotation-driven components or Java configuration, you can use the `@SessionScope` annotation to assign a component to the `session` scope.

当使用注解驱动组件或java配置时，你用用`@SessionScope`注解编辑这个组件是`session`作用域。

```java
@SessionScope
@Component
public class UserPreferences {
    // ...
}
```

##### Application Scope

译：应用作用域

Consider the following XML configuration for a bean definition:

译：考虑如下bean的xml配置。

```xml
<bean id="appPreferences" class="com.something.AppPreferences" scope="application"/>
```

The Spring container creates a new instance of the `AppPreferences` bean by using the `appPreferences` bean definition once for the entire web application. That is, the `appPreferences` bean is scoped at the `ServletContext` level and stored as a regular `ServletContext` attribute. This is somewhat similar to a Spring singleton bean but differs in two important ways: It is a singleton per `ServletContext`, not per Spring 'ApplicationContext' (for which there may be several in any given web application), and it is actually exposed and therefore visible as a `ServletContext` attribute.

译:Spring容器为整个web应用通过使用`appPreferences`创建一个AppPreferences新实例。也就是说，`appPreferendes`bean的作用域在`ServletContext`级别并当作一个普通的ServletContext属性存储。和spring的单例bean有些相思，但是有两个重要方式不同：每个ServletContext容器单例，而不是每个Spring ApplicationContext(在任何给定的web应用中可能有几个)并且它是公开的，因此`ServletContext`属性可以看到。

When using annotation-driven components or Java configuration, you can use the `@ApplicationScope` annotation to assign a component to the `application` scope. The following example shows how to do so:

译：当使用注解驱动组件或者java配置时，你可以使用`@ApplicationScope`注解标记一个组件为`application`作用域。如下所示展示如何使用：

```java
@ApplicationScope
@Component
public class AppPreferences {
    // ...
}
```

##### Scoped Beans as Dependencies

译：作用域bean作为依赖

The Spring IoC container manages not only the instantiation of your objects (beans), but also the wiring up of collaborators (or dependencies). If you want to inject (for example) an HTTP request-scoped bean into another bean of a longer-lived scope, you may choose to inject an AOP proxy in place of the scoped bean. That is, you need to inject a proxy object that exposes the same public interface as the scoped object but that can also retrieve the real target object from the relevant scope (such as an HTTP request) and delegate method calls onto the real object.

译：Spring IoC容器不仅管理对象的实例，而且自动注入协作bean(或者说依赖bean).如果你像注入一个HTTP request作用域的bean到另一个生命周期更长作用域的bean，你能在替换的作用域bean上选择注入AOP代理.也就是说，你需要注入一个和作用域对象有相同公共接口并且能从相关作用域中检索目标对象（如HTTP request）的代理对象，并委托方法调用真正的对象。

You may also use`<aop:scoped-proxy/>` between beans that are scoped as `singleton`, with the reference then going through an intermediate proxy that is serializable and therefore able to re-obtain the target singleton bean on deserialization.

译：你可能在单例作用域的bean之间也使用`<aop:scoped-proxy/>`，然后通过可序列化的中间代理，通过反序列化重新获取单例bean的引用。

When declaring `<aop:scoped-proxy/>` against a bean of scope `prototype`, every method call on the shared proxy leads to the creation of a new target instance to which the call is then being forwarded.

译：当针对一个原型作用域的bean定义`<aop:scoped-proxy/>`,共享代理上的每个方法调用创建一个新的目标实例，然后转发到该实例。

Also, scoped proxies are not the only way to access beans from shorter scopes in a lifecycle-safe fashion. You may also declare your injection point (that is, the constructor or setter argument or autowired field) as `ObjectFactory`, allowing for a `getObject()` call to retrieve the current instance on demand every time it is needed — without holding on to the instance or storing it separately.

译：当然，作用域代理不是用生命周期安全的唯一方式访问短作用域bean.你也可以声明注入点为ObjectFactory<MyTargetBean>（也就是说，构造器或者set方法参数或者自动注入属性），在每次需要时允许一个getObject()调用检索当前的实例，不需要持有实例或者单独存储。

As an extended variant, you may declare `ObjectProvider`, which delivers several additional access variants, including `getIfAvailable` and `getIfUnique`.

译：作为一个扩展变量，你可以声明ObjectProvider<MyTargetBean>，它提供了几个额外的访问变量，包含getIfAvailable and getIfUnique。

The JSR-330 variant of this is called `Provider` and is used with a `Provider` declaration and a corresponding `get()` call for every retrieval attempt. See [here](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-standard-annotations) for more details on JSR-330 overall.

译：这个JSR-330变量叫做Provider,并且使用Provider<MyTargetBean>声明，对每次检索用相应的get()方法调用。想得到更多的信息，请参见此处。

The configuration in the following example is only one line, but it is important to understand the “why” as well as the “how” behind it:

译：如下所示的配置仅一行，但是理解它背后的为什么何为何非常重要。

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:aop="http://www.springframework.org/schema/aop"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop
        https://www.springframework.org/schema/aop/spring-aop.xsd">

    <!-- an HTTP Session-scoped bean exposed as a proxy -->
    <bean id="userPreferences" class="com.something.UserPreferences" scope="session">
        <!-- instructs the container to proxy the surrounding bean -->
        <aop:scoped-proxy/> 
    </bean>

    <!-- a singleton-scoped bean injected with a proxy to the above bean -->
    <bean id="userService" class="com.something.SimpleUserService">
        <!-- a reference to the proxied userPreferences bean -->
        <property name="userPreferences" ref="userPreferences"/>
    </bean>
</beans>
```

To create such a proxy, you insert a child  `<aop:scoped-proxy/>`element into a scoped bean definition (see [Choosing the Type of Proxy to Create](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-scopes-other-injection-proxies) and [XML Schema-based configuration](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#xsd-schemas)). Why do definitions of beans scoped at the `request`, `session` and custom-scope levels require the `<aop:scoped-proxy/>`element? Consider the following singleton bean definition and contrast it with what you need to define for the aforementioned scopes (note that the following `userPreferences` bean definition as it stands is incomplete):

译： 为了创建这个代理，你插入一个`<aop:scoped-proxy/>`孩子标签到一个bean作用域。为什么bean的作用域

在request、session何自定义作用域级别需要 `<aop:scoped-proxy/>`标签？考虑如下单例bean并且与上述作用域需要的定义进行对比（注意`userPreferences` bean是不完整的）。

```xml
<bean id="userPreferences" class="com.something.UserPreferences" scope="session"/>

<bean id="userManager" class="com.something.UserManager">
    <property name="userPreferences" ref="userPreferences"/>
</bean>
```

In the preceding example, the singleton bean (`userManager`) is injected with a reference to the HTTP `Session`-scoped bean (`userPreferences`). The salient point here is that the `userManager` bean is a singleton: it is instantiated exactly once per container, and its dependencies (in this case only one, the `userPreferences` bean) are also injected only once. This means that the `userManager` bean operates only on the exact same `userPreferences` object (that is, the one with which it was originally injected.

译：在前面的例子中，单例bean`userManager`注入一个HTTP Session作用域的bean（`userPreferences`）。这的要点是`userManager`是单例作用域：每个容器只实例化一次，它的依赖项同样只注入一次。这意味着`userManager`bean只对完全相同的userPreferendes对象操作（也就是说，这一次是最初的注入）。

This is not the behavior you want when injecting a shorter-lived scoped bean into a longer-lived scoped bean (for example, injecting an HTTP `Session`-scoped collaborating bean as a dependency into singleton bean). Rather, you need a single `userManager` object, and, for the lifetime of an HTTP `Session`, you need a `userPreferences` object that is specific to the HTTP `Session`. Thus, the container creates an object that exposes the exact same public interface as the `UserPreferences` class (ideally an object that is a `UserPreferences` instance), which can fetch the real `UserPreferences` object from the scoping mechanism (HTTP request, `Session`, and so forth). The container injects this proxy object into the `userManager` bean, which is unaware that this `UserPreferences` reference is a proxy. In this example, when a `UserManager` instance invokes a method on the dependency-injected `UserPreferences` object, it is actually invoking a method on the proxy. The proxy then fetches the real `UserPreferences` object from (in this case) the HTTP `Session` and delegates the method invocation onto the retrieved real `UserPreferences` object.

译：当注入一个短周期作用域bean到一个长周期作用域bean的时候，这个结果不是你想要的，比如注入一个HTTP Session作用域的协作bean作为一个依赖项注入到单例bean.相反，你需要一个单例`userManager`对象，并且是Http Session的生命周期，你需要一个`userPreferences` 对象生命为HTTP Session.因此，容器创建一个暴露和`UserPreferendes`类相同公共接口的对象（理想情况下是一个UserPreferendes实例对象）。这个对象从作用域机制中（request、session等）能取得真正的UserPreferences对象。容器注入这个代理对象到`userManager`bean,它不知道UserPreferendes引用是代理的。在这个例子上，当一个`UserManager`实例调用一个在依赖注入UserPreferendes对象上的方法，它完全使用代理调用方法。代理从Http session中取得真实的`UserPreferendes`对象，委托方法调用检索真实的UserPreferendes对象。

Thus, you need the following (correct and complete) configuration when injecting `request-` and `session-scoped` beans into collaborating objects, as the following example shows:

译：因此，当注入`request`和`session`作用域bean的协作对象，你需要如下配置（正确且完整）。如下所示：

```xml
<bean id="userPreferences" class="com.something.UserPreferences" scope="session">
    <aop:scoped-proxy/>
</bean>

<bean id="userManager" class="com.something.UserManager">
    <property name="userPreferences" ref="userPreferences"/>
</bean>
```

###### Choosing the Type of Proxy to Create

译：选择要创建的代理类类型

By default, when the Spring container creates a proxy for a bean that is marked up with the  `<aop:scoped-proxy/>` element, a CGLIB-based class proxy is created.

译:默认情况下，当Spring容器创建代理的时候，如果有`<aop:scoped-proxy/>` 标签，使用DGLIB代理。

|      | CGLIB proxies intercept only public method calls! Do not call non-public methods on such a proxy. They are not delegated to the actual scoped target object. |
| ---- | ------------------------------------------------------------ |
|      | CDLIB代理只拦截公共方法调用!不调用非公共方法。他们不会魏国实际作用域的目标对象。 |

Alternatively, you can configure the Spring container to create standard JDK interface-based proxies for such scoped beans, by specifying `false` for the value of the `proxy-target-class` attribute of the `<aop:scoped-proxy/>`element. Using JDK interface-based proxies means that you do not need additional libraries in your application classpath to affect such proxying. However, it also means that the class of the scoped bean must implement at least one interface and that all collaborators into which the scoped bean is injected must reference the bean through one of its interfaces. The following example shows a proxy based on an interface:

译：或者，你可以配置Spring容器为这个作用域bean创建基于接口的JDK代理。通过指定`<aop:scoped-proxy/>`标签的`proxy-target-class`属性为false.

```xml
<!-- DefaultUserPreferences implements the UserPreferences interface -->
<bean id="userPreferences" class="com.stuff.DefaultUserPreferences" scope="session">
    <aop:scoped-proxy proxy-target-class="false"/>
</bean>

<bean id="userManager" class="com.stuff.UserManager">
    <property name="userPreferences" ref="userPreferences"/>
</bean>
```

For more detailed information about choosing class-based or interface-based proxying, see [Proxying Mechanisms](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#aop-proxying).

关于基于类还是基于接口的代理的更多详细信息，请看代理机制。

#### 1.5.5. Custom Scopes

译：自定义代理

The bean scoping mechanism is extensible. You can define your own scopes or even redefine existing scopes, although the latter is considered bad practice and you cannot override the built-in `singleton` and `prototype` scopes.

译：bean的作用域机制是可扩展的。你可以定义你自己的作用域甚至重新定义已有的作用域，但是后者会有不好的体验，并且你不能覆盖内置的`单例`和`原型`作用域。

##### Creating a Custom Scope

To integrate your custom scopes into the Spring container, you need to implement the `org.springframework.beans.factory.config.Scope` interface, which is described in this section. For an idea of how to implement your own scopes, see the `Scope` implementations that are supplied with the Spring Framework itself and the [`Scope`](https://docs.spring.io/spring-framework/docs/5.2.5.RELEASE/javadoc-api/org/springframework/beans/factory/config/Scope.html) javadoc, which explains the methods you need to implement in more detail.

译：为了把你的自定义作用域整合到Spring容器，你需要实现`org.springframework.beans.factory.config.Scope`接口，在这部分将会进行描述。关于怎样实现自己的作用域的方法，请看Spring框架和`Scope` 文档提供的实现，它详细的解释了你需要实现的方法。

The `Scope` interface has four methods to get objects from the scope, remove them from the scope, and let them be destroyed.

译：`Scope`接口有四个方法从作用域中得到对象，从作用域中删除和让他们销毁。

The session scope implementation, for example, returns the session-scoped bean (if it does not exist, the method returns a new instance of the bean, after having bound it to the session for future reference). The following method returns the object from the underlying scope:

译：例如，`session`作用域实现返回session作用域bean(如果不存在，这个方法返回bean的新实例，在绑定bean到session作用域后，以供将来的引用).如下方法从基础作用域返回对象。

```java
Object get(String name, ObjectFactory<?> objectFactory)
```

The session scope implementation, for example, removes the session-scoped bean from the underlying session. The object should be returned, but you can return null if the object with the specified name is not found. The following method removes the object from the underlying scope:

译：例如，sesseion作用域实现，从基础Session作用域删除sesseion作用域bean.对象应该返回，但是如果指定名字的对象没有发现，你可以返回null。如下方法从基础作用域中删除对象。

```java
Object remove(String name)
```

The following method registers the callbacks the scope should execute when it is destroyed or when the specified object in the scope is destroyed:

译：如下方法注册回调作用域，当作用域销毁或者当在作用域中的指定对象销毁时执行。

```java
void registerDestructionCallback(String name, Runnable destructionCallback)
```

See the [javadoc](https://docs.spring.io/spring-framework/docs/5.2.5.RELEASE/javadoc-api/org/springframework/beans/factory/config/Scope.html#registerDestructionCallback) or a Spring scope implementation for more information on destruction callbacks.

译：销毁回调的更多信息，请看javadoc或者Spring作用域实现

The following method obtains the conversation identifier for the underlying scope:

译：如下方法获得基础作用域的会话标识符。

```java
String getConversationId()
```

This identifier is different for each scope. For a session scoped implementation, this identifier can be the session identifier.

译：每个作用域的标识符不用。对于Session作用域实现，这个标识符可以时session标识符。

##### Using a Custom Scope

After you write and test one or more custom `Scope` implementations, you need to make the Spring container aware of your new scopes. The following method is the central method to register a new `Scope` with the Spring container:

译：在你编写或者测试一个或者多个自定义`Scope`实现之后，你需要使Spring容器知道新作用域.如下方法是Spring容器注册一个新`Scope`的核心方法。

```java
void registerScope(String scopeName, Scope scope);
```

This method is declared on the `ConfigurableBeanFactory` interface, which is available through the `BeanFactory` property on most of the concrete `ApplicationContext` implementations that ship with Spring.

译：在`ConfigurableBeanFactory`接口中声明的方法，该接口可通过在大多数具体ApplicationContext实现上的`beanFactory`属性获得。

The first argument to the `registerScope(..)` method is the unique name associated with a scope. Examples of such names in the Spring container itself are `singleton` and `prototype`. The second argument to the `registerScope(..)` method is an actual instance of the custom `Scope` implementation that you wish to register and use.

译：`registerScope(..)` 方法的第一个参数是关联作用域的唯一名称。例如Spring容器自身的作用域名字singleton和prototype。第二个参数是实际的自定义作用域实现实例（你希望注册和使用的自定义作用域实例）。

Suppose that you write your custom `Scope` implementation, and then register it as shown in the next example.

译：假设你编写自定义作用域实现，且像如下例子注册：

|      | The next example uses `SimpleThreadScope`, which is included with Spring but is not registered by default. The instructions would be the same for your own custom `Scope` implementations. |
| ---- | ------------------------------------------------------------ |
|      | 下一个案例使用`SimpleThreadScope`，默认spring包含它但是没有注册。这些说明和自定义作用域实现是相同的。 |

```java
Scope threadScope = new SimpleThreadScope();
beanFactory.registerScope("thread", threadScope);
```

You can then create bean definitions that adhere to the scoping rules of your custom `Scope`, as follows:

译：然后，你可以创建复核作用域规则的自定义作用域bean,如下所示：

```xml
<bean id="..." class="..." scope="thread">
```

With a custom `Scope` implementation, you are not limited to programmatic registration of the scope. You can also do the `Scope` registration declaratively, by using the `CustomScopeConfigurer` class, as the following example shows:

译：自定义作用域实现，不限定程序注册。你也可以通过使用`CustomScopeConfigurer`类声明式做作用域注册这件事情，如下所示：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:aop="http://www.springframework.org/schema/aop"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop
        https://www.springframework.org/schema/aop/spring-aop.xsd">

    <bean class="org.springframework.beans.factory.config.CustomScopeConfigurer">
        <property name="scopes">
            <map>
                <entry key="thread">
                    <bean class="org.springframework.context.support.SimpleThreadScope"/>
                </entry>
            </map>
        </property>
    </bean>

    <bean id="thing2" class="x.y.Thing2" scope="thread">
        <property name="name" value="Rick"/>
        <aop:scoped-proxy/>
    </bean>

    <bean id="thing1" class="x.y.Thing1">
        <property name="thing2" ref="thing2"/>
    </bean>

</beans>
```

|      | When you place `<aop:scoped-proxy/>` in a `FactoryBean` implementation, it is the factory bean itself that is scoped, not the object returned from `getObject()`. |
| ---- | ------------------------------------------------------------ |
|      | 当你在`FactoryBean`中放置`<aop:scoped-proxy/>`,作用域是factory bean本身，而不是从`getObject()`方法返回的对象。 |

### 1.6. Customizing the Nature of a Bean

译：个性化bean

The Spring Framework provides a number of interfaces you can use to customize the nature of a bean. This section groups them as follows:

译：spring框架提供了许多接口，你可以个性化bean.这部分分组如下：

- [Lifecycle Callbacks](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-lifecycle)
- [`ApplicationContextAware` and `BeanNameAware`](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-aware)
- [Other `Aware` Interfaces](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#aware-list)

#### 1.6.1. Lifecycle Callbacks

To interact with the container’s management of the bean lifecycle, you can implement the Spring `InitializingBean` and `DisposableBean` interfaces. The container calls `afterPropertiesSet()` for the former and `destroy()` for the latter to let the bean perform certain actions upon initialization and destruction of your beans.

译：为了和容器管理的bean生命周期交互，你可以实现Spring的`InitializingBean`和`DisposableBean`接口。容器为前者调用`afterPropertiesSet`方法，为后者调用`destroy`方法，以便在bean初始化和销毁时执行某一些操作。

------

*The JSR-250 `@PostConstruct` and `@PreDestroy` annotations are generally considered best practice for receiving lifecycle callbacks in a modern Spring application. Using these annotations means that your beans are not coupled to Spring-specific interfaces. For details, see [Using `@PostConstruct` and `@PreDestroy`](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-postconstruct-and-predestroy-annotations).*

*If you do not want to use the JSR-250 annotations but you still want to remove coupling, consider `init-method` and `destroy-method` bean definition metadata。*

译:JSR-250的@PostConstruct和@PreDestroy注解通常被认为是在现代Spring应用中接收生命周期回调最好的实践。使用这些注解意味着对特定Spirng接口不是耦合的。详情请看 [Using `@PostConstruct` and `@PreDestroy`](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-postconstruct-and-predestroy-annotations).

 如果你不想用JSR-250注解，但是又想删除耦合，考虑`init-method` 和`destroy-method` bean定义。

------

Internally, the Spring Framework uses `BeanPostProcessor` implementations to process any callback interfaces it can find and call the appropriate methods. If you need custom features or other lifecycle behavior Spring does not by default offer, you can implement a `BeanPostProcessor` yourself. For more information, see [Container Extension Points](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-extension).

译：在内部，spring框架使用`BeanPostProcessor`实现去处理任何回调接口，他能找到调用的方法。如果你需要自定义特性或者其他生命周期行为，Spring默认不提供，你能实现BeanPostProcessor。更多信息，请看 [Container Extension Points](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-extension)。

In addition to the initialization and destruction callbacks, Spring-managed objects may also implement the `Lifecycle` interface so that those objects can participate in the startup and shutdown process, as driven by the container’s own lifecycle.

译：除了初始化和销毁回调，Spring管理对象还可以时间`lifecycle`接口以便这些对象参与启动和关闭过程，由Spring容器自身的生命周期驱动。

The lifecycle callback interfaces are described in this section.

译:这部分描述生命周期回调接口

##### Initialization Callbacks

译：初始化回调

The `org.springframework.beans.factory.InitializingBean` interface lets a bean perform initialization work after the container has set all necessary properties on the bean. The `InitializingBean` interface specifies a single method:

在容器为bean已经设置完必要的属性，`org.springframework.beans.factory.InitializingBean` 接口让一个bean执行初始化工作。`InitializingBean` 接口声明了一个方法：

```java
void afterPropertiesSet() throws Exception;
```

We recommend that you do not use the `InitializingBean` interface, because it unnecessarily couples the code to Spring. Alternatively, we suggest using the [`@PostConstruct`](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-postconstruct-and-predestroy-annotations) annotation or specifying a POJO initialization method. In the case of XML-based configuration metadata, you can use the `init-method` attribute to specify the name of the method that has a void no-argument signature. With Java configuration, you can use the `initMethod` attribute of `@Bean`. See [Receiving Lifecycle Callbacks](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-java-lifecycle-callbacks). Consider the following example:

译：我们建议你不要使用`InitializingBean`接口，因为它会造成与Spring代码有不必要的代码耦合。或者，我们建议使用@PostConstruct注解或者声明一个POJO初始化方法。在基于xml配置的场景，你可以使用`init-method`属性指定一个void 没有参数方法的名字。java 配置，你能使用`@Bean`的`initMethod`。请看收到生命周期回调章节，考虑如下案例：

```xml
<bean id="exampleInitBean" class="examples.ExampleBean" init-method="init"/>
```

```java
public class ExampleBean {

    public void init() {
        // do some initialization work
    }
}
```

The preceding example has almost exactly the same effect as the following example (which consists of two listings):

译：前面的例子和下面的例子有相同的作用（两个清单一致）：

```xml
<bean id="exampleInitBean" class="examples.AnotherExampleBean"/>
```

```java
public class AnotherExampleBean implements InitializingBean {

    @Override
    public void afterPropertiesSet() {
        // do some initialization work
    }
}
```

However, the first of the two preceding examples does not couple the code to Spring.

译：但是，第一个中的两个例子与pring代码没有耦合。

##### Destruction Callbacks

译：销毁回调

Implementing the `org.springframework.beans.factory.DisposableBean` interface lets a bean get a callback when the container that contains it is destroyed. The `DisposableBean` interface specifies a single method:

译：实现`org.springframework.beans.factory.DisposableBean`接口，当容器包含的bean销毁的时候进行回调。`DisposableBean` 接口声明了一个方法：

```java
void destroy() throws Exception;
```

We recommend that you do not use the `DisposableBean` callback interface, because it unnecessarily couples the code to Spring. Alternatively, we suggest using the [`@PreDestroy`](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-postconstruct-and-predestroy-annotations) annotation or specifying a generic method that is supported by bean definitions. With XML-based configuration metadata, you can use the `destroy-method` attribute on the <bean/>. With Java configuration, you can use the `destroyMethod` attribute of `@Bean`. See [Receiving Lifecycle Callbacks](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-java-lifecycle-callbacks). Consider the following definition:

译：我们建议你不要使用`DisposableBean`回调接口，因为它会造成不必要的耦合到spring代码。或者，我们建议使用`@PreDestory`注解或者指定一个bean支持的泛型方法。如果基于xml的配置，你可以在bean标签中使用destroy-method属性。如果使用java配置，你可以使用@Bean的`destroyMethod`属性。具体请看收到声明周期回调章节。考虑如下定义:

```xml
<bean id="exampleInitBean" class="examples.ExampleBean" destroy-method="cleanup"/>
```

```java
public class ExampleBean {

    public void cleanup() {
        // do some destruction work (like releasing pooled connections)
    }
}
```

The preceding definition has almost exactly the same effect as the following definition:

译：前面的定义和下面的定义有相同的作用：

```xml
<bean id="exampleInitBean" class="examples.AnotherExampleBean"/>
```

```java
public class AnotherExampleBean implements DisposableBean {

    @Override
    public void destroy() {
        // do some destruction work (like releasing pooled connections)
    }
}
```

However, the first of the two preceding definitions does not couple the code to Spring.

译：但是，第一个例子中的两个定义与spring代码不耦合。

------

You can assign the `destroy-method` attribute of a <bean> element a special `(inferred)` value, which instructs Spring to automatically detect a public `close` or `shutdown` method on the specific bean class. (Any class that implements `java.lang.AutoCloseable` or `java.io.Closeable` would therefore match.) You can also set this special `(inferred)` value on the `default-destroy-method` attribute of a <bean> element to apply this behavior to an entire set of beans (see [Default Initialization and Destroy Methods](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-lifecycle-default-init-destroy-methods)). Note that this is the default behavior with Java configuration.

译：你可以为bean标签的`destroy-method`属性分配一个特殊值（inferred）,该值指示spirng在特定类上自动监测一个公共的`close`或者`shutdown`方法(任何是实现java.lang.AutoCloseable或者java.io.Closeable都将匹配)。你也可以设置特殊值（inferred）在bean的`default-destroy-method`属性，并且这个行为应用到整个bean集（请看默认初始化或者销毁方法）。请注意这是java配置的默认行为。

------

##### Default Initialization and Destroy Methods

译：默认初始化和销毁方法

When you write initialization and destroy method callbacks that do not use the Spring-specific `InitializingBean` and `DisposableBean` callback interfaces, you typically write methods with names such as `init()`, `initialize()`, `dispose()`, and so on. Ideally, the names of such lifecycle callback methods are standardized across a project so that all developers use the same method names and ensure consistency.

译：当你没有使用Spring的`InitializingBean` 和`DisposableBean` 回调接口，编写`initialization` 和`destroy` 方法回调的时候。你通常编写方法的名字如init(),initialize(),dispose()等。理想情况下，这些声明周期回调方法的名称在整个项目上是标准的，以便于开发者使用相同的名字并确保一致。

You can configure the Spring container to “look” for named initialization and destroy callback method names on every bean. This means that you, as an application developer, can write your application classes and use an initialization callback called `init()`, without having to configure an `init-method="init"` attribute with each bean definition. The Spring IoC container calls that method when the bean is created (and in accordance with the standard lifecycle callback contract [described previously](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-lifecycle)). This feature also enforces a consistent naming convention for initialization and destroy method callbacks.

译：你能在每个bean上配置spring容器去“寻找“初始化和销毁回调方法的名字。这意味着，你作为一个应用开发者，可以编写你的应用类并使用初始化回调init()方法，不必要为每个bean配置`init-method="init"`属性.当bean创建的时候，Spring IoC容器调用初始化方法（根据前面描述的标准的生命周期回调）。这个特性为初始化和销毁方法回调增强了一致性命名规范。

Suppose that your initialization callback methods are named `init()` and your destroy callback methods are named `destroy()`. Your class then resembles the class in the following example:

译：假设你的初始化回调方法被命名为`init()`，销毁回调方法被命名为`destroy()`.你的类类似如下例子：

```java
public class DefaultBlogService implements BlogService {

    private BlogDao blogDao;

    public void setBlogDao(BlogDao blogDao) {
        this.blogDao = blogDao;
    }

    // this is (unsurprisingly) the initialization callback method
    public void init() {
        if (this.blogDao == null) {
            throw new IllegalStateException("The [blogDao] property must be set.");
        }
    }
}
```

You could then use that class in a bean resembling the following:

然后，你可以在bean中使用这个类，类似如下：

```xml
<beans default-init-method="init">

    <bean id="blogService" class="com.something.DefaultBlogService">
        <property name="blogDao" ref="blogDao" />
    </bean>

</beans>
```

The presence of the `default-init-method` attribute on the top-level <beans/> element attribute causes the Spring IoC container to recognize a method called `init` on the bean class as the initialization method callback. When a bean is created and assembled, if the bean class has such a method, it is invoked at the appropriate time.

译：在<beans/>标签属性上出现`default-init-method`导致Spring IoC容器识别一个bean叫做init的初始化回调方法.当一个bean创建和组装时，如果bean有这个方法，就会适时调用。

You can configure destroy method callbacks similarly (in XML, that is) by using the `default-destroy-method` attribute on the top-level <beans/>element.

译：与上面类似,你可以配置销毁方法回调，通过在顶层<beans>标签上使用default-destroy-method属性。

Where existing bean classes already have callback methods that are named at variance with the convention, you can override the default by specifying (in XML, that is) the method name by using the `init-method` and `destroy-method` attributes of the <bean> itself.

译：如果存在的bean classes中已经有了习惯命名的回调方法，你能通过在bean标签上使用`init-method`和`destory-method`属性指定方法名，覆盖默认的。

The Spring container guarantees that a configured initialization callback is called immediately after a bean is supplied with all dependencies. Thus, the initialization callback is called on the raw bean reference, which means that AOP interceptors and so forth are not yet applied to the bean. A target bean is fully created first and then an AOP proxy **(for example)** with its interceptor chain is applied. If the target bean and the proxy are defined separately, your code can even interact with the raw target bean, bypassing the proxy. Hence, it would be inconsistent to apply the interceptors to the `init` method, because doing so would couple the lifecycle of the target bean to its proxy or interceptors and leave strange semantics when your code interacts directly with the raw target bean.

guarantees ：n.保证、担保、保修单；v.保证、保障、担保、确保。

译：Spring 容器担保在bean提供所有依赖项之后立即调用配置的初始化回调方法。因此，初始化回调方法通过原始bean的引用调用，这也意味着AOP拦截器还没用应用。首先，一个目标bean完全创建，然后应用AOP代理的拦截器链。如果目标bean和定义的代理隔离，你的代码可以绕过代理类和原始目标bean交互。因此，给init方法使用拦截器是不一致的，因为这么做将目标bean的生命周期耦合到代理或者拦截器，并且当你的代码直接和原始bean交互时，会产生奇怪的语义。

##### Combining Lifecycle Mechanisms

译：组合生命周期机制

As of Spring 2.5, you have three options for controlling bean lifecycle behavior:

译：从spring2.5开始，你有3种选项控制bean的生命周期行为：

- The [`InitializingBean`](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-lifecycle-initializingbean) and [`DisposableBean`](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-lifecycle-disposablebean) callback interfaces
- Custom `init()` and `destroy()` methods
- The [`@PostConstruct` and `@PreDestroy` annotations](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-postconstruct-and-predestroy-annotations). You can combine these mechanisms to control a given bean.

------

  If multiple lifecycle mechanisms are configured for a bean and each mechanism is configured with a different method name, then each configured method is executed in the order listed after this note. However, if the same method name is configured — for example, `init()` for an initialization method — for more than one of these lifecycle mechanisms, that method is executed once, as explained in the [preceding section](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-lifecycle-default-init-destroy-methods).

译：如果对一个bean配置多个生命周期机制，且每个机制配置不同的方法名字，那么每个配置方法将按照列表种记录的顺序执行。但是，如果配置相同方法名-例如，一个初始化方法init()，用于这些生命周期机制的多个，这个方法执行一次。在前面部分已经解释。

------

Multiple lifecycle mechanisms configured for the same bean, with different initialization methods, are called as follows:

1. Methods annotated with `@PostConstruct`
2. `afterPropertiesSet()` as defined by the `InitializingBean` callback interface
3. A custom configured `init()` method

译：对一个bean采用不同的初始化方法配置多个生命周期机制，按如下调用：

1. `@PostConstruct`注解方法
2. 通过`InitializingBean`接口定义的`afterPropertiesSet()`方法
3. 自定义配置的`init()`方法

Destroy methods are called in the same order:

1. Methods annotated with `@PreDestroy`
2. `destroy()` as defined by the `DisposableBean` callback interface
3. A custom configured `destroy()` method

译：销毁方法调用顺序相同：

1.`@PreDestroy`注解方法

2.通过`DisposableBean`回调接口定义的`destroy()`方法

3.自定义配置的`destroy()`方法

##### Startup and Shutdown Callbacks

The `Lifecycle` interface defines the essential methods for any object that has its own lifecycle requirements (such as starting and stopping some background process):

译：`Lifecycle` 接口为有生命周期需要的任何对象定义了基本方法（比如开始和停止一些后台进程）。

```java
public interface Lifecycle {

    void start();

    void stop();

    boolean isRunning();
}
```

Any Spring-managed object may implement the `Lifecycle` interface. Then, when the `ApplicationContext` itself receives start and stop signals (for example, for a stop/restart scenario at runtime), it cascades those calls to all `Lifecycle` implementations defined within that context. It does this by delegating to a `LifecycleProcessor`, shown in the following listing:

译：任何Spring管理的对象可以是实现`lifecycle`接口。然后，当`ApplicationContext` 收到开始或者停止标志的时候（例如在运行时Stop/restart的场景），它级联到在环境种定义的所用的`Lifecycle`实现。它通过委托`LifecycProcessor`实现，如下所示：

```java
public interface LifecycleProcessor extends Lifecycle {

    void onRefresh();

    void onClose();
}
```

Notice that the `LifecycleProcessor` is itself an extension of the `Lifecycle` interface. It also adds two other methods for reacting to the context being refreshed and closed.

译：请注意`LifecycleProcessor` 继承`Lifecycle`。它对环境的refreshed和closed增加了两个其他方法。

------

Note that the regular `org.springframework.context.Lifecycle` interface is a plain contract for explicit start and stop notifications and does not imply auto-startup at context refresh time. For fine-grained control over auto-startup of a specific bean (including startup phases), consider implementing `org.springframework.context.SmartLifecycle` instead.  Also, please note that stop notifications are not guaranteed to come before destruction. On regular shutdown, all `Lifecycle` beans first receive a stop notification before the general destruction callbacks are being propagated. However, on hot refresh during a context’s lifetime or on aborted refresh attempts, only destroy methods are called.

译：请注意，一般`org.springframework.context.Lifecycle`接口是显示启动和关闭通知的普通约定，并不意味着在上下文刷新时自动启动。对特定bean的自动启动（包含启动阶段）更细粒度的控制，考虑实现`org.springframework.context.SmartLifecycle`接口。另外，请注意停止通知不能保证在销毁之前发出。对于定期的关闭，所用的`Lifecycle`bean在生成传播销毁回调之前，先收到一个停止通知。但是，在热部署的上下文的生命时间或者放弃刷新尝试，仅调用销毁方法

------

The order of startup and shutdown invocations can be important. If a “depends-on” relationship exists between any two objects, the dependent side starts after its dependency, and it stops before its dependency. However, at times, the direct dependencies are unknown. You may only know that objects of a certain type should start prior to objects of another type. In those cases, the `SmartLifecycle` interface defines another option, namely the `getPhase()` method as defined on its super-interface, `Phased`. The following listing shows the definition of the `Phased` interface:

译: 开启和关闭的调用吮吸可能很重要。如果在两个对象之间存在depends-on关系（循环依赖），依赖方在依赖项之后启动，在依赖项之前停止。但是，有时，依赖是未知的。你可能只知道某几个类的对象应该在另一个对象之前开始。在这些情况下，`SmartLifecycle` 接口介意了另一个选项，在父类( `Phased`)接口定义的getPhase()方法,如下所示`Phased`接口：

```java
public interface Phased {

    int getPhase();
}
```

The following listing shows the definition of the `SmartLifecycle` interface:

如下所示`SmartLifecycle` 接口：

```java
public interface SmartLifecycle extends Lifecycle, Phased {

    boolean isAutoStartup();

    void stop(Runnable callback);
}
```

When starting, the objects with the lowest phase start first. When stopping, the reverse order is followed. Therefore, an object that implements `SmartLifecycle` and whose `getPhase()` method returns `Integer.MIN_VALUE` would be among the first to start and the last to stop. At the other end of the spectrum, a phase value of `Integer.MAX_VALUE` would indicate that the object should be started last and stopped first (likely because it depends on other processes to be running). When considering the phase value, it is also important to know that the default phase for any “normal” `Lifecycle` object that does not implement `SmartLifecycle` is `0`. Therefore, any negative phase value indicates that an object should start before those standard components (and stop after them). The reverse is true for any positive phase value.

译：当开始的时候，最低位的对象先启动。停止时，按相反顺序执行。因此，实现SmartLifecycle对象且它的`getPhase()`方法返回`Integer.MIN_VALUE`，将会最先开始但是最后停止。在另一端，`Integer.MAX_VALUE`将表示对象应该后开始先停止（可能因为它依赖其他正在运行的进程）。当考虑阶段值的时候，知道没有实现`SmartLifecycle`接口的一般`Lifecycle`对象的默认值是0非常重要。因此，任何负值表示一个对象应该在这些标准组件之间启动（在他们之后停止）。对于正值顺序相反。

The stop method defined by `SmartLifecycle` accepts a callback. Any implementation must invoke that callback’s `run()` method after that implementation’s shutdown process is complete. That enables asynchronous shutdown where necessary, since the default implementation of the `LifecycleProcessor` interface, `DefaultLifecycleProcessor`, waits up to its timeout value for the group of objects within each phase to invoke that callback. The default per-phase timeout is 30 seconds. You can override the default lifecycle processor instance by defining a bean named `lifecycleProcessor` within the context. If you want only to modify the timeout, defining the following would suffice:

译：通过`SmartLifecycle`定义的停止方法接受一个回调。在实现的关闭过程完成后，任何实现必须调用`run()`方法。必要时能够异步关闭，因为lifecycleProcesor默认实现`DefaultLifecycleProcessor`接口，等待回调在每个阶段内的对象组的超时值。每个阶段的超市时间是30秒。你可以通过定义一个在上下文内的叫做`lifecycleProcessor` 的覆盖默认的生命周期处理器实例。如果你只想修改超时时间，如下定义足以：

```xml
<bean id="lifecycleProcessor" class="org.springframework.context.support.DefaultLifecycleProcessor">
    <!-- timeout value in milliseconds -->
    <property name="timeoutPerShutdownPhase" value="10000"/>
</bean>
```

As mentioned earlier, the `LifecycleProcessor` interface defines callback methods for the refreshing and closing of the context as well. The latter drives the shutdown process as if `stop()` had been called explicitly, but it happens when the context is closing. The 'refresh' callback, on the other hand, enables another feature of `SmartLifecycle` beans. When the context is refreshed (after all objects have been instantiated and initialized), that callback is invoked. At that point, the default lifecycle processor checks the boolean value returned by each `SmartLifecycle` object’s `isAutoStartup()` method. If `true`, that object is started at that point rather than waiting for an explicit invocation of the context’s or its own `start()` method (unlike the context refresh, the context start does not happen automatically for a standard context implementation). The `phase` value and any “depends-on” relationships determine the startup order as described earlier.

译:如前所述，`LifecycleProcessor` 接口为上下文刷新和关闭定义回调方法。如果`stop()`已经显示调用，后面会驱动关闭处理器，但是当上下文正在关闭时发生。 'refresh' 回调，在另一方面，使得另一个`SmartLifecycle` bean的功能执行。当上下文刷新后（在所有对象已经实例化和初始化之后），执行回调。此时，默认的生命周期执行器检查每个`SmartLifecycle`对象的`isAutoStartup()`方法的布尔值。如果为true,此时，对象已经开始了，而不是等待上下文的`start()`方法显示调用（不像上下文刷新，对一个单体上下文实现，上下文启动不会自动发生）。`phase`值和任何“depends-on”关系决定启动的顺序，如前所述。

##### Shutting Down the Spring IoC Container Gracefully in Non-Web Applications

译：在非web应用优雅地关闭Spring IoC容器

------

  This section applies only to non-web applications. Spring’s web-based `ApplicationContext` implementations already have code in place to gracefully shut down the Spring IoC container when the relevant web application is shut down.

译：这部分只适用于非web应用。Spring的基于web的`ApplicationContext`实现，当相关web应用关闭的时候，已经有代码代替优雅的关闭Spring IoC容器。

------

If you use Spring’s IoC container in a non-web application environment (for example, in a rich client desktop environment), register a shutdown hook with the JVM. Doing so ensures a graceful shutdown and calls the relevant destroy methods on your singleton beans so that all resources are released. You must still configure and implement these destroy callbacks correctly.

译：如果你在非web应用环境，使用Spring的IoC容器（例如，在客户端桌面环境），使用jvm注册一个关闭钩子。这样做确保优雅的关闭且对单例bean调用相关的销毁方法，以便于释放所有资源。你仍然必须正确的配置和实现这些销毁方法

To register a shutdown hook, call the `registerShutdownHook()` method that is declared on the `ConfigurableApplicationContext` interface, as the following example shows:

译：注册关闭钩子，调用定义在`ConfigurableApplicationContext` 接口的`registerShutdownHook`方法，如下所示：

```java
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class Boot {

    public static void main(final String[] args) throws Exception {
        ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");

        // add a shutdown hook for the above context...
        ctx.registerShutdownHook();

        // app runs here...

        // main method exits, hook is called prior to the app shutting down...
    }
}
```

#### 1.6.2. `ApplicationContextAware` and `BeanNameAware`

When an `ApplicationContext` creates an object instance that implements the `org.springframework.context.ApplicationContextAware` interface, the instance is provided with a reference to that `ApplicationContext`. The following listing shows the definition of the `ApplicationContextAware` interface:

译：当`ApplicationContext`创建一个实现了`org.springframework.context.ApplicationContextAware`接口的对象实例，将为该实例提供ApplicationContext的引用。如下列表展示了ApplicationContextAware接口的定义：

```java
public interface ApplicationContextAware {

    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
}
```

Thus, beans can programmatically manipulate the `ApplicationContext` that created them, through the `ApplicationContext` interface or by casting the reference to a known subclass of this interface (such as `ConfigurableApplicationContext`, which exposes additional functionality). One use would be the programmatic retrieval of other beans. Sometimes this capability is useful. However, in general, you should avoid it, because it couples the code to Spring and does not follow the Inversion of Control style, where collaborators are provided to beans as properties. Other methods of the `ApplicationContext` provide access to file resources, publishing application events, and accessing a `MessageSource`. These additional features are described in [Additional Capabilities of the `ApplicationContext`](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#context-introduction).

manipulate ：v.操作、操纵

译：因此，bean可以通过`ApplicationContext`接口或者通过强转成它的子类接口ConfigurableApplicationContext，编程操作创建（比如`ApplicationContext`，它暴露了额外的功能)。一个用处是编程检索其他bean.有时，这种能力是有用的。但是一般来说，你应该避免他，因为他将代码耦合到spring并且不遵循控制反转风格，即协作者作为属性提供bean。`ApplicationContext`的另一个方法提供访问文件资源，发布应用时间，访问消息源（`messageSource`）。这些额外的功能在“Application的额外能力”描述。

Autowiring is another alternative to obtain a reference to the `ApplicationContext`. The *traditional* `constructor` and `byType` autowiring modes (as described in [Autowiring Collaborators](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-autowire)) can provide a dependency of type `ApplicationContext` for a constructor argument or a setter method parameter, respectively. For more flexibility, including the ability to autowire fields and multiple parameter methods, use the annotation-based autowiring features. If you do, the `ApplicationContext` is autowired into a field, constructor argument, or method parameter that expects the `ApplicationContext` type if the field, constructor, or method in question carries the `@Autowired` annotation. For more information, see [Using `@Autowired`](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-autowired-annotation).

译：自动注入是另一种获得`ApplicationContext`引用的方法。传统的constructor和byType注入模型可以提供ApplicationContext的依赖类，分别作为构造器参数或者set方法的参数。更灵活的是，包含自动注入属性和多参数方法的能力，使用基于注解功能。如果你使用注解，如果属性，构造器或者方法带有`@Autowired`注解，ApplicationContext类 自动注入到一个属性、构造参数或者方法参数。详细信息，请看使用@Autowired注解

When an `ApplicationContext` creates a class that implements the `org.springframework.beans.factory.BeanNameAware` interface, the class is provided with a reference to the name defined in its associated object definition. The following listing shows the definition of the BeanNameAware interface:

译：当ApplicationContext创建一个实现了`org.springframework.beans.factory.BeanNameAware`接口的类时候，这个类提供了一个在关联对象中定义的名字的引用。如下列表展示了BeanNameAware 接口：

```java
public interface BeanNameAware {

    void setBeanName(String name) throws BeansException;
}
```

The callback is invoked after population of normal bean properties but before an initialization callback such as `InitializingBean`, `afterPropertiesSet`, or a custom init-method.

译：回调在正常bean属性填充之后执行，但是在初始化（比如InitializingBean、afterPropertiesSet或者自定义初始化方法）回调之前。

#### .6.3. Other `Aware` Interfaces

Besides `ApplicationContextAware` and `BeanNameAware` (discussed [earlier](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-aware)), Spring offers a wide range of `Aware` callback interfaces that let beans indicate to the container that they require a certain infrastructure dependency. As a general rule, the name indicates the dependency type. The following table summarizes the most important `Aware` interfaces:

译：除了`ApplicationContextAware`和`beanNameAware`接口外，Spring提供了许多的Aware回调接口，有回调接口的bean表示需要容器的某些基础依赖。一般来说，名字表示依赖的类型。如下表格汇总了大部分重要的`Aware`j接口：

| Name                             | Injected Dependency                                          | Explained in…                                                |
| -------------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| `ApplicationContextAware`        | Declaring `ApplicationContext`.                              | [`ApplicationContextAware` and `BeanNameAware`](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-aware) |
| `ApplicationEventPublisherAware` | Event publisher of the enclosing `ApplicationContext`.       | [Additional Capabilities of the `ApplicationContext`](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#context-introduction) |
| `BeanClassLoaderAware`           | Class loader used to load the bean classes.                  | [Instantiating Beans](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-class) |
| `BeanFactoryAware`               | Declaring `BeanFactory`.                                     | [`ApplicationContextAware` and `BeanNameAware`](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-aware) |
| `BeanNameAware`                  | Name of the declaring bean.                                  | [`ApplicationContextAware` and `BeanNameAware`](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-aware) |
| `BootstrapContextAware`          | Resource adapter `BootstrapContext` the container runs in. Typically available only in JCA-aware `ApplicationContext` instances. | [JCA CCI](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/integration.html#cci) |
| `LoadTimeWeaverAware`            | Defined weaver for processing class definition at load time. | [Load-time Weaving with AspectJ in the Spring Framework](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#aop-aj-ltw) |
| `MessageSourceAware`             | Configured strategy for resolving messages (with support for parametrization and internationalization). | [Additional Capabilities of the `ApplicationContext`](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#context-introduction) |
| `NotificationPublisherAware`     | Spring JMX notification publisher.                           | [Notifications](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/integration.html#jmx-notifications) |
| `ResourceLoaderAware`            | Configured loader for low-level access to resources.         | [Resources](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#resources) |
| `ServletConfigAware`             | Current `ServletConfig` the container runs in. Valid only in a web-aware Spring `ApplicationContext`. | [Spring MVC](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/web.html#mvc) |
| `ServletContextAware`            | Current `ServletContext` the container runs in. Valid only in a web-aware Spring `ApplicationContext`. | [Spring MVC](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/web.html#mvc) |

Note again that using these interfaces ties your code to the Spring API and does not follow the Inversion of Control style. As a result, we recommend them for infrastructure beans that require programmatic access to the container.

译：请再次注意，使用这些接口绑定你的代码到Spring API不遵循控制反转风格。因此，我们建议需要向容器编程访问的基础bean使用他们。

### 1.7. Bean Definition Inheritance

译：bean定义继承

A bean definition can contain a lot of configuration information, including constructor arguments, property values, and container-specific information, such as the initialization method, a static factory method name, and so on. A child bean definition inherits configuration data from a parent definition. The child definition can override some values or add others as needed. Using parent and child bean definitions can save a lot of typing. Effectively, this is a form of templating.

译：一个bean定义包含许多配置信息，包含构造参数、属性、值和容器指定的信息，例如初始化方法、静态工厂方法名等。一个子bean继承父bean的配置数据。子bean能覆盖一些值或者增加需要的其他项。使用父bean和孩子bean可以保存许多类型。实际上，这是形式上的模板。

If you work with an `ApplicationContext` interface programmatically, child bean definitions are represented by the `ChildBeanDefinition` class. Most users do not work with them on this level. Instead, they configure bean definitions declaratively in a class such as the `ClassPathXmlApplicationContext`. When you use XML-based configuration metadata, you can indicate a child bean definition by using the `parent` attribute, specifying the parent bean as the value of this attribute. The following example shows how to do so:

译：如果你编程方式使用`ApplicationContext`接口，子bean定义通过`ChildBeanDefinition`类表示。大多数使用者不在这个级别使用他们。而是，他们在类中显示配置bean(`ClassPathXmlApplicationContext`).当你使用基于xml配置时，你能通过使用`parent`属性指示一个子bean。并将bean指定为这个属性的值。如下所示如何使用：

```xml
<bean id="inheritedTestBean" abstract="true"
        class="org.springframework.beans.TestBean">
    <property name="name" value="parent"/>
    <property name="age" value="1"/>
</bean>

<bean id="inheritsWithDifferentClass"
        class="org.springframework.beans.DerivedTestBean"
        parent="inheritedTestBean" init-method="initialize">  
    <property name="name" value="override"/>
    <!-- the age property value of 1 will be inherited from parent -->
</bean>
```

A child bean definition uses the bean class from the parent definition if none is specified but can also override it. In the latter case, the child bean class must be compatible with the parent (that is, it must accept the parent’s property values).

译：如果未指定，子bean将使用父bean定义的类，但是也可以覆盖它。在后一种情况，子bean必须兼容父bean(也就是说，它必须接受父属性值).

A child bean definition inherits scope, constructor argument values, property values, and method overrides from the parent, with the option to add new values. Any scope, initialization method, destroy method, or `static` factory method settings that you specify override the corresponding parent settings.

译：一个子bean继承父bean的作用域、构造参数值、属性值和方法覆盖，并可选增加新值。任何作用域、初始方法、销毁方法或者静态工厂方法设置覆盖相应的父设置。

The remaining settings are always taken from the child definition: depends on, autowire mode, dependency check, singleton, and lazy init.

译：其余设置都用子定义：depends on, autowire mode, dependency check, singleton, and lazy init.

The preceding example explicitly marks the parent bean definition as abstract by using the `abstract` attribute. If the parent definition does not specify a class, explicitly marking the parent bean definition as `abstract` is required, as the following example shows:

译：前面的例子使用`abstract`属性显示标记了父bean为抽象的。如果父bean没有指定类,则需要指定标记父bean为`abstract`。如下所示：

```xml
<bean id="inheritedTestBeanWithoutClass" abstract="true">
    <property name="name" value="parent"/>
    <property name="age" value="1"/>
</bean>

<bean id="inheritsWithClass" class="org.springframework.beans.DerivedTestBean"
        parent="inheritedTestBeanWithoutClass" init-method="initialize">
    <property name="name" value="override"/>
    <!-- age will inherit the value of 1 from the parent bean definition-->
</bean>
```

The parent bean cannot be instantiated on its own because it is incomplete, and it is also explicitly marked as `abstract`. When a definition is `abstract`, it is usable only as a pure template bean definition that serves as a parent definition for child definitions. Trying to use such an `abstract` parent bean on its own, by referring to it as a ref property of another bean or doing an explicit `getBean()` call with the parent bean ID returns an error. Similarly, the container’s internal `preInstantiateSingletons()` method ignores bean definitions that are defined as abstract.

译：父bean本身不能被实例化因为不完整的，并且它也指定为abstract的.当一个定义是`abstract`时，它只用于模板bean,作为父定义为自定义服务。尝试使用这个`abstract`父bean,通过另一个bean的ref属性关联它，或者用父bean的ID显示使用getBean()方法，将返回错误。类似地，容器的内部`preInstantiateSingletons`方法忽略指定为抽象的bean.

------

`ApplicationContext` pre-instantiates all singletons by default. Therefore, it is important (at least for singleton beans) that if you have a (parent) bean definition which you intend to use only as a template, and this definition specifies a class, you must make sure to set the *abstract* attribute to *true*, otherwise the application context will actually (attempt to) pre-instantiate the `abstract` bean.

译：默认情况下，`ApplicationContext` 预实例化全部单例。因此，它很重要（至少对于单例bean）,如果你有一个bean(父bean)只用于模板，并且指定为class，你必须确保设置了`abstract`属性为true,否则应用上下文将预先实例化`abstract`bean.

------

### 1.8. Container Extension Points

译：容器扩展点

Typically, an application developer does not need to subclass `ApplicationContext` implementation classes. Instead, the Spring IoC container can be extended by plugging in implementations of special integration interfaces. The next few sections describe these integration interfaces.

译：通常，应用开发者不需要子`ApplicationContext`实现类。相反，Spring Ioc容器可以通过插入指定的集成接口扩展。下面几部分描述这些集成接口：

#### 1.8.1. Customizing Beans by Using a `BeanPostProcessor`

译：通过使用`BeanPostProcessor`定制bean

The `BeanPostProcessor` interface defines callback methods that you can implement to provide your own (or override the container’s default) instantiation logic, dependency resolution logic, and so forth. If you want to implement some custom logic after the Spring container finishes instantiating, configuring, and initializing a bean, you can plug in one or more custom `BeanPostProcessor` implementations.

译：`BeanPostProcessor` 接口定义回调方法，你可以实现自己的实例化逻辑、依赖解析逻辑等（或者覆盖容器的默认方法）。在Spring容器完成实例化、配置、初始化一个bean之后，如果想实现一些自定义逻辑,你可以插入一个躲着多个自定义`beanPostProcessor`实现。

You can configure multiple `BeanPostProcessor` instances, and you can control the order in which these `BeanPostProcessor` instances execute by setting the `order` property. You can set this property only if the `BeanPostProcessor` implements the `Ordered` interface. If you write your own `BeanPostProcessor`, you should consider implementing the `Ordered` interface, too. For further details, see the javadoc of the [`BeanPostProcessor`](https://docs.spring.io/spring-framework/docs/5.2.5.RELEASE/javadoc-api/org/springframework/beans/factory/config/BeanPostProcessor.html) and [`Ordered`](https://docs.spring.io/spring-framework/docs/5.2.5.RELEASE/javadoc-api/org/springframework/core/Ordered.html) interfaces. See also the note on [programmatic registration of `BeanPostProcessor` instances](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-programmatically-registering-beanpostprocessors).

译：你可以配置多个`BeanPostProcessor`实例，并且你可以通过这事`order`属性控制`BeanPostProcess`实例的执行顺序。如果`BeanPostProcessor`实现了`Ordered`接口，你才可以设置这个属性。同样，如果你编写自己的`BeanPostProcessor`，你应该考虑实现`Ordered`接口。关于更多详情，请看`beanPostProcessor`和`ordered`接口文档，另看Programmatic registration of BeanPostProcessor instances。

------

`BeanPostProcessor` instances operate on bean (or object) instances. That is, the Spring IoC container instantiates a bean instance and then `BeanPostProcessor` instances do their work.  `BeanPostProcessor` instances are scoped per-container. This is relevant only if you use container hierarchies. If you define a `BeanPostProcessor` in one container, it post-processes only the beans in that container. In other words, beans that are defined in one container are not post-processed by a `BeanPostProcessor` defined in another container, even if both containers are part of the same hierarchy.  To change the actual bean definition (that is, the blueprint that defines the bean), you instead need to use a `BeanFactoryPostProcessor`, as described in [Customizing Configuration Metadata with a `BeanFactoryPostProcessor`](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-extension-factory-postprocessors).

译：`BeanPostprocessor`实例在bean(或者对象)实例上操作。也就是说，Spring IoC容器实例化一个bean实例然后`BeanPostProcessor`实例执行他们的工作。`BeanPostProcessor`实例的作用域是每个容器。如果使用容器继承，这才相关。如果你在一个容器中定义一个`BeanPostProcessor`，它只在这个容器中后置处理bean.换言而之，定义在一个没有后置处理的容器bean,不会被定义在另一个容器的`beanPostProcessor`处理，即使两个容器都是同一个层次的部分。要改变实际的bean,你需要使用`BeanFactoryPostProcessor`，就像在Customizing configuration Meadata with a BeanFactoryPostProcessor.

------

The `org.springframework.beans.factory.config.BeanPostProcessor` interface consists of exactly two callback methods. When such a class is registered as a post-processor with the container, for each bean instance that is created by the container, the post-processor gets a callback from the container both before container initialization methods (such as `InitializingBean.afterPropertiesSet()` or any declared `init` method) are called, and after any bean initialization callbacks. The post-processor can take any action with the bean instance, including ignoring the callback completely. A bean post-processor typically checks for callback interfaces, or it may wrap a bean with a proxy. Some Spring AOP infrastructure classes are implemented as bean post-processors in order to provide proxy-wrapping logic.

译：`org.springframework.beans.factory.config.BeanPostProcessor`接口由两个回调方法组成。当这个类在容器中作为后置处理器注册，通过容器创建的每个bean实例，在容器初始化方法（例如InitializingBean.afterPropertiesSet或者任何声明了`init`方法）被调用之前和在任何初始化方法之后后置处理器就会调用。后置处理器可以使用bean实例做任何操作，包含完全忽略的回调。bean后置处理器通常检查回调接口，或者使用代理包裹bean.一些Spring AOP基础类被实现，作为后置处理为了提供包裹代理逻辑。

An `ApplicationContext` automatically detects any beans that are defined in the configuration metadata that implements the `BeanPostProcessor` interface. The `ApplicationContext` registers these beans as post-processors so that they can be called later, upon bean creation. Bean post-processors can be deployed in the container in the same fashion as any other beans.

译：`ApplicationContext` 自动检测实现了BeanPostProcessor接口的bean。`ApplicationContext`注册这些后置处理器bean以便于在后面bean创建时调用。部署在容器中的Bean后置处理器和其他bean相同。

Note that, when declaring a `BeanPostProcessor` by using an `@Bean` factory method on a configuration class, the return type of the factory method should be the implementation class itself or at least the `org.springframework.beans.factory.config.BeanPostProcessor` interface, clearly indicating the post-processor nature of that bean. Otherwise, the `ApplicationContext` cannot autodetect it by type before fully creating it. Since a `BeanPostProcessor` needs to be instantiated early in order to apply to the initialization of other beans in the context, this early type detection is critical.

译：请记住，当通过配置类使用@Bean工厂方法定义的BeanPostProcessor，工厂方法的返回类型应该为类实现自身或者至少是`org.springframework.beans.factory.config.BeanPostProcessor`接口，清楚地表示后置处理器bean。因此，在完全创建之前，`ApplicationContext`不能通过类型自动监测。由于`BeanPostProcessor`需要提前初始化，为了在上下文中其他bean的初始化，早期的类型检测是重要的：

------

Programmatically registering `BeanPostProcessor` instances 

While the recommended approach for `BeanPostProcessor` registration is through `ApplicationContext` auto-detection (as described earlier), you can register them programmatically against a `ConfigurableBeanFactory` by using the `addBeanPostProcessor` method. This can be useful when you need to evaluate conditional logic before registration or even for copying bean post processors across contexts in a hierarchy. Note, however, that `BeanPostProcessor` instances added programmatically do not respect the `Ordered` interface. Here, it is the order of registration that dictates the order of execution. Note also that `BeanPostProcessor` instances registered programmatically are always processed before those registered through auto-detection, regardless of any explicit ordering.

译：编程方式注册`BeanPostProcessor`实例

我们建议对`BeanPostProcessor`注册通过ApplicationContext自动检测（前面描述的这样），你能靠`ConfigurableBeanFactory`的`addBeanPostProcessor`方法注册他们。当你在注册或则从同一个上下文中复制后置处理器需要条件判断逻辑,它非常有用。请记住，但是编程方式增加的BeanPostProcessor实例没有维护`Ordered`接口。在这儿，等级的顺序表示执行的顺序。同样牢记，无论任何显示的顺序如何，在这些通过自动检测注册之前编程方式注册的BeanPostProcessor总是被执行。

`BeanPostProcessor` instances and AOP auto-proxying

译：BeanPostProcessor实例和自动代理

Classes that implement the `BeanPostProcessor` interface are special and are treated differently by the container. All `BeanPostProcessor` instances and beans that they directly reference are instantiated on startup, as part of the special startup phase of the `ApplicationContext`. Next, all `BeanPostProcessor` instances are registered in a sorted fashion and applied to all further beans in the container. Because AOP auto-proxying is implemented as a `BeanPostProcessor` itself, neither `BeanPostProcessor` instances nor the beans they directly reference are eligible for auto-proxying and, thus, do not have aspects woven into them.

译：实现BeanPostProcessor接口的类是特殊的，容器会区别对待。所有BeanPostProcessor实例和直接引用的bean，在启动时都会被实例化，作为ApplicationContext特殊启动的一部分。接下来，所有的BeanPostProcessor实例按顺序注册并在容器中应用所用bean.由于AOP自动代理作为BeanPostProcessor本身实现，既不是BeanPostProcessor实例也不是自动代理合格的直接引用，因此，没有植入的切面。

For any such bean, you should see an informational log message: `Bean someBean is not eligible for getting processed by all BeanPostProcessor interfaces (for example: not eligible for auto-proxying)`.

译：对于许多这样的bean，你应该看日志信息：`Bean someBean is not eligible for getting processed by all BeanPostProcessor interfaces (for example: not eligible for auto-proxying)`.

If you have beans wired into your `BeanPostProcessor` by using autowiring or `@Resource` (which may fall back to autowiring), Spring might access unexpected beans when searching for type-matching dependency candidates and, therefore, make them ineligible for auto-proxying or other kinds of bean post-processing. For example, if you have a dependency annotated with `@Resource` where the field or setter name does not directly correspond to the declared name of a bean and no name attribute is used, Spring accesses other beans for matching them by type.

译：如果你通过自动注入或者@Resource注解自动注入BeanPostProcessor（可能返回到自动注入），当查找类型匹配依赖项时，Spring可能访问到意外bean,因此，使他们对自动代理或者其他种类的后置处理器不合格。例如，如果你使用@Resouce注解在属性或者setter方法的名字和bean的名称不直接对应，并且没有使用名字属性，Spring通过类型访问其他bean匹配他们。

------

The following examples show how to write, register, and use `BeanPostProcessor` instances in an `ApplicationContext`.

译：如下例子展示如何在ApplicationContext中编写、注册和使用BeanPostProcessor实例

##### Example: Hello World, `BeanPostProcessor`-style

This first example illustrates basic usage. The example shows a custom `BeanPostProcessor` implementation that invokes the `toString()` method of each bean as it is created by the container and prints the resulting string to the system console.

The following listing shows the custom `BeanPostProcessor` implementation class definition:

```java
package scripting;

import org.springframework.beans.factory.config.BeanPostProcessor;

public class InstantiationTracingBeanPostProcessor implements BeanPostProcessor {

    // simply return the instantiated bean as-is
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean; // we could potentially return any object reference here...
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) {
        System.out.println("Bean '" + beanName + "' created : " + bean.toString());
        return bean;
    }
}
```

The following `beans` element uses the `InstantiationTracingBeanPostProcessor`:

译：如下`beans` 标签使用InstantiationTracingBeanPostProcessor：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:lang="http://www.springframework.org/schema/lang"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/lang
        https://www.springframework.org/schema/lang/spring-lang.xsd">

    <lang:groovy id="messenger"
            script-source="classpath:org/springframework/scripting/groovy/Messenger.groovy">
        <lang:property name="message" value="Fiona Apple Is Just So Dreamy."/>
    </lang:groovy>

    <!--
    when the above bean (messenger) is instantiated, this custom
    BeanPostProcessor implementation will output the fact to the system console
    -->
    <bean class="scripting.InstantiationTracingBeanPostProcessor"/>

</beans>
```

Notice how the `InstantiationTracingBeanPostProcessor` is merely defined. It does not even have a name, and, because it is a bean, it can be dependency-injected as you would any other bean. (The preceding configuration also defines a bean that is backed by a Groovy script. The Spring dynamic language support is detailed in the chapter entitled [Dynamic Language Support](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/languages.html#dynamic-language).)

译：请注意`InstantiationTracingBeanPostProcessor`是如何定义的。它甚至没有名字，并且，因为他是bean，他可以依赖注入任何其他bean.(前面的配置通过Groovy脚本也定义了一个bean.Srping动态语言支持详情在章节Dynamic Language Support.)

The following Java application runs the preceding code and configuration:

译：如下java应用运行前面的代码和配置：

```java
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scripting.Messenger;

public final class Boot {

    public static void main(final String[] args) throws Exception {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("scripting/beans.xml");
        Messenger messenger = ctx.getBean("messenger", Messenger.class);
        System.out.println(messenger);
    }

}
```

The output of the preceding application resembles the following:

译：前面应用程序的输出类似如下：

```java
Bean 'messenger' created : org.springframework.scripting.groovy.GroovyMessenger@272961
org.springframework.scripting.groovy.GroovyMessenger@272961
```

##### Example: The `RequiredAnnotationBeanPostProcessor`

Using callback interfaces or annotations in conjunction with a custom `BeanPostProcessor` implementation is a common means of extending the Spring IoC container. An example is Spring’s `RequiredAnnotationBeanPostProcessor` — a `BeanPostProcessor` implementation that ships with the Spring distribution and that ensures that JavaBean properties on beans that are marked with an (arbitrary) annotation are actually (configured to be) dependency-injected with a value.

译：使用回调接口或者注解与自定义BeanPostProcessor实现结合是扩展Spring IoC容器的普遍做法。比如Spring的`RequiredAnnotationBeanPostProcessor`—一个`BeanPostProcessor`实现，它随Spring发布并确保标记任何注解的bean，用一个实际的值依赖注入。

#### 1.8.2. Customizing Configuration Metadata with a `BeanFactoryPostProcessor`

译：使用BeanFactoryPostProcessor自定义配置元数据。

The next extension point that we look at is the `org.springframework.beans.factory.config.BeanFactoryPostProcessor`. The semantics of this interface are similar to those of the `BeanPostProcessor`, with one major difference: `BeanFactoryPostProcessor` operates on the bean configuration metadata. That is, the Spring IoC container lets a `BeanFactoryPostProcessor` read the configuration metadata and potentially change it *before* the container instantiates any beans other than `BeanFactoryPostProcessor` instances.

译：下一个扩展点我们看`org.springframework.beans.factory.config.BeanFactoryPostProcessor`。这个接口的语法和这些BeanPostProcessor类似，有一个主要不同点：BeanFactoryPostProcessor操作bean配置元数据。也就是说，Spring IoC容器让一个BeanFactoryPostProcessor读取配置元数据并在容器实例化任何bean（除了`BeanFactoryPostProcessor`）之前，潜在改变他。

You can configure multiple `BeanFactoryPostProcessor` instances, and you can control the order in which these `BeanFactoryPostProcessor` instances run by setting the `order` property. However, you can only set this property if the `BeanFactoryPostProcessor` implements the `Ordered` interface. If you write your own `BeanFactoryPostProcessor`, you should consider implementing the `Ordered` interface, too. See the javadoc of the [`BeanFactoryPostProcessor`](https://docs.spring.io/spring-framework/docs/5.2.5.RELEASE/javadoc-api/org/springframework/beans/factory/config/BeanFactoryPostProcessor.html) and [`Ordered`](https://docs.spring.io/spring-framework/docs/5.2.5.RELEASE/javadoc-api/org/springframework/core/Ordered.html) interfaces for more details.

译：你可以配置多个BeanFactoryPostProcessor实例，通过设置order属性控制每个BeanFactoryPostProcessor实例的顺序。但是，如果你实现了Ordered接口，你只能设置属性。如果你编写自己的`BeanFactoryPostProcessor`，你应该考虑实现Ordered接口，同样，关于更多信息请看BeanFactoryProsessor和Ordered接口的文档说明。

------

If you want to change the actual bean instances (that is, the objects that are created from the configuration metadata), then you instead need to use a `BeanPostProcessor` (described earlier in [Customizing Beans by Using a `BeanPostProcessor`](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-extension-bpp)). While it is technically possible to work with bean instances within a `BeanFactoryPostProcessor` (for example, by using `BeanFactory.getBean()`), doing so causes premature bean instantiation, violating the standard container lifecycle. This may cause negative side effects, such as bypassing bean post processing.  Also, `BeanFactoryPostProcessor` instances are scoped per-container. This is only relevant if you use container hierarchies. If you define a `BeanFactoryPostProcessor` in one container, it is applied only to the bean definitions in that container. Bean definitions in one container are not post-processed by `BeanFactoryPostProcessor` instances in another container, even if both containers are part of the same hierarchy.

译：如果你想改变实际的bean实例（也就是说，从配置信息中已经配置的对象），你需要使用BeanPostProcessor(在前面通过使用BeanPostProcessor自定义bean章节描述过。).虽然在技术上可以使用在BeanFactoryPostProcessor内的实例bean工作（例如，使用BeanFactory.getBean()），但是这枚做会导致过早实例化bean,违反了标准的容器生命周期。这对引起负面影响，例如绕过bean的后置初期。同样，`BeanFactoryPostProcessor`实例的作用域为每个容器。只有在使用容器继承时，这才相关。如果你在一个容器中定义一个`BeanFactoryPostProcessor`，它只应用于bean定义的容器。在一个容器中的bean定义不会通过另一个容器的`BeanFactoryPostProcessor`实例处理，即使每个容器都是相同的继承的一部分。

------

A bean factory post-processor is automatically executed when it is declared inside an `ApplicationContext`, in order to apply changes to the configuration metadata that define the container. Spring includes a number of predefined bean factory post-processors, such as `PropertyOverrideConfigurer` and `PropertySourcesPlaceholderConfigurer`. You can also use a custom `BeanFactoryPostProcessor` — for example, to register custom property editors.

译：当在`ApplicationContext`里定义一个bean工厂后置处理器时，它是自动执行的，为了改变在容器中定义的配置元数据。Spring包含许多预先定义的bean工厂后置处理器，例如：`PropertyOverrideConfigurer` 和`PropertySourcesPlaceholderConfigurer`。你也可以使用自定义的`BeanFactoryPostProcessor`—例如，注册自定义属性编辑器。

An `ApplicationContext` automatically detects any beans that are deployed into it that implement the `BeanFactoryPostProcessor` interface. It uses these beans as bean factory post-processors, at the appropriate time. You can deploy these post-processor beans as you would any other bean.

译：一个`ApplicationContext`自动监测所有实现`BeanFactoryPostProcessor`接口的部署bean。在适当的时候，他使用这些bean工厂后置处理器bean。你能像任何其他bean一样，部署这些bean

------

As with `BeanPostProcessor`**s** , you typically do not want to configure `BeanFactoryPostProcessor`**s** for lazy initialization. If no other bean references a `Bean(Factory)PostProcessor`, that post-processor will not get instantiated at all. Thus, marking it for lazy initialization will be ignored, and the `Bean(Factory)PostProcessor` will be instantiated eagerly even if you set the `default-lazy-init` attribute to `true` on the declaration of your <beans/> element.

译：在使用BeanPostProcessor时，你通常不想配置`BeanFactoryPostProcessor`为懒加载。如果没有其他bean引用一个`Bean(Factory)PostProcessor`，后置处理器不会立即实例化。因此，使他成为懒加载bean将被忽略，如果你在<beans/>标签中设置`default-lazy-init`为true，，并且`Ban(Factory)PostProcessor`将急切实例化。

------

##### Example: The Class Name Substitution `PropertySourcesPlaceholderConfigurer`

译：例子:类名代替：`PropertySourcesPlaceholderConfigurer`

You can use the `PropertySourcesPlaceholderConfigurer` to externalize property values from a bean definition in a separate file by using the standard Java `Properties` format. Doing so enables the person deploying an application to customize environment-specific properties, such as database URLs and passwords, without the complexity or risk of modifying the main XML definition file or files for the container.

译：你可以使用`PropertySourcesPlaceHoldConfigurer`，通过使用标准化的java`Properties`格式，把一个bean的属性值外部化到一个单独的文件中。这样做，可以使部署应用人员自定义指定环境的属性，比如数据库URLs和passwords,没有修改容器主xml定义文件的风险性和冒险性。

Consider the following XML-based configuration metadata fragment, where a `DataSource` with placeholder values is defined:

译：考虑如下基于xml配置元数据判断，其中定义了占位符的`DataSource`值。

```xml
<bean class="org.springframework.context.support.PropertySourcesPlaceholderConfigurer">
    <property name="locations" value="classpath:com/something/jdbc.properties"/>
</bean>

<bean id="dataSource" destroy-method="close"
        class="org.apache.commons.dbcp.BasicDataSource">
    <property name="driverClassName" value="${jdbc.driverClassName}"/>
    <property name="url" value="${jdbc.url}"/>
    <property name="username" value="${jdbc.username}"/>
    <property name="password" value="${jdbc.password}"/>
</bean>
```

The example shows properties configured from an external `Properties` file. At runtime, a `PropertySourcesPlaceholderConfigurer` is applied to the metadata that replaces some properties of the DataSource. The values to replace are specified as placeholders of the form `${property-name}`, which follows the Ant and log4j and JSP EL style.

译：这个例子展示了从外部的`Properties`文件配置属性。在运行时，一个`PropertySourcePlaceholderConfigurer`应用于替代DataSource的一些属性。

The actual values come from another file in the standard Java `Properties` format:

译：实际的值来自另一个标准java `Properties`格式的文件：

```
jdbc.driverClassName=org.hsqldb.jdbcDriver
jdbc.url=jdbc:hsqldb:hsql://production:9002
jdbc.username=sa
jdbc.password=root
```

Therefore, the `${jdbc.username}` string is replaced at runtime with the value, 'sa', and the same applies for other placeholder values that match keys in the properties file. The `PropertySourcesPlaceholderConfigurer` checks for placeholders in most properties and attributes of a bean definition. Furthermore, you can customize the placeholder prefix and suffix.

译：因此，`${jdbc.username}`字符在运行时用‘sa’替换，对于与属性文件中匹配key的其他的占位值同样适用。`PropertySourcesPlaceholderConfigurer`检查大多数属性和bean定义的属性的占位符。因此，你可以定义占位符的前缀和后缀：

With the `context` namespace introduced in Spring 2.5, you can configure property placeholders with a dedicated configuration element. You can provide one or more locations as a comma-separated list in the `location` attribute, as the following example shows:

dedicated ：adv.专用的。

译：在Spring2.5中介绍过，使用`context`命名空间，你能用专用标签配置属性占位符。你可以在`location`属性中提供逗号隔开的本地化列表，如下所示：

```xml
<context:property-placeholder location="classpath:com/something/jdbc.properties"/>
```

The `PropertySourcesPlaceholderConfigurer` not only looks for properties in the `Properties` file you specify. By default, if it cannot find a property in the specified properties files, it checks against Spring `Environment` properties and regular Java `System` properties.

译：`PropertySourcesPlaceholderConfigurer`不仅在你定义的Properties文件中寻找属性。默认情况下，如果在指定的`properties`文件中没有找到属性，检查依附Spring 的Environment属性和一般的java `System`属性。

------

You can use the `PropertySourcesPlaceholderConfigurer` to substitute class names, which is sometimes useful when you have to pick a particular implementation class at runtime. The following example shows how to do so:

译：你可以使用`PropertySourcesPlaceholderConfigurer`替换类名,有时，当你在运行时必须选择特定实现类时非常有用。如下例子展示如何使用：

```xml
<bean class="org.springframework.beans.factory.config.PropertySourcesPlaceholderConfigurer">
    <property name="locations">
        <value>classpath:com/something/strategy.properties</value>
    </property>
    <property name="properties">
        <value>custom.strategy.class=com.something.DefaultStrategy</value>
    </property>
</bean>

<bean id="serviceStrategy" class="${custom.strategy.class}"/>
```

If the class cannot be resolved at runtime to a valid class, resolution of the bean fails when it is about to be created, which is during the `preInstantiateSingletons()` phase of an `ApplicationContext` for a non-lazy-init bean.

译：在运行时，如果类不能解析为一个有效类，当将要创建时，ApplicationContext在PreInstantiateSingletons阶段的bean，解析失败。

------

##### Example: The `PropertyOverrideConfigurer`

The `PropertyOverrideConfigurer`, another bean factory post-processor, resembles the `PropertySourcesPlaceholderConfigurer`, but unlike the latter, the original definitions can have default values or no values at all for bean properties. If an overriding `Properties` file does not have an entry for a certain bean property, the default context definition is used.

译：`PropertyOverrideConfigurer`，另一个bean工厂后置处理器，类似`PropertySourcesPlaceholderConfigurer`，但是又不像后者，原始的定义的bean属性可以有默认值或者没有值。如果一个覆盖`Properties`文件没有对应的某些bean属性，将使用默认上下文定义：

Note that the bean definition is not aware of being overridden, so it is not immediately obvious from the XML definition file that the override configurer is being used. In case of multiple `PropertyOverrideConfigurer` instances that define different values for the same bean property, the last one wins, due to the overriding mechanism.

译：请记住bean定义不知道被覆盖，因此从xml定义文件中，看不出来正使用覆盖配置。在多个`PropertyOverrideConfigurer`实例的场合，对同一个bean属性定义不同的值，由于覆盖机制，组后一个将会生效。

Properties file configuration lines take the following format:

译：属性文件配置行如下格式：

```
beanName.property=value
```

The following listing shows an example of the format:

译：格式如下所示：

```xml
dataSource.driverClassName=com.mysql.jdbc.Driver
dataSource.url=jdbc:mysql:mydb
```

This example file can be used with a container definition that contains a bean called `dataSource` that has `driver` and `url` properties.

译：这个示例文件可以用于容器包含一个叫做有driver何url的`dataSource`的bean.

Compound property names are also supported, as long as every component of the path except the final property being overridden is already non-null (presumably initialized by the constructors). In the following example, the `sammy` property of the `bob` property of the `fred` property of the `tom` bean is set to the scalar value `123`:

译：同样支持复核属性名，只要路径的每个组件（除了最终覆盖属性）是非空的（可能通过构造器初始化）。如下例子，tom下的fred下的bob下的`sammy`属性，设置值为123.

```
tom.fred.bob.sammy=123
```

------

  Specified override values are always literal values. They are not translated into bean references. This convention also applies when the original value in the XML bean definition specifies a bean reference.

译：指定的覆盖值始终是文本值。他们不会转换为bean引用。当xml bean的原始值指定一个bean引用时，这个约束也适用。

------

With the `context` namespace introduced in Spring 2.5, it is possible to configure property overriding with a dedicated configuration element, as the following example shows:

译：在Spring2.5中介绍的context命名空间，可以用一个显示的配置标签配置覆盖属性，如下所示：

```xml
<context:property-override location="classpath:override.properties"/>
```

#### 1.8.3. Customizing Instantiation Logic with a `FactoryBean`

译：用`FactoryBean`自定义实例化逻辑

You can implement the `org.springframework.beans.factory.FactoryBean` interface for objects that are themselves factories.

译：你可以为本身就是工厂的对象实现`org.springframework.beans.factory.FactoryBean`接口

The `FactoryBean` interface is a point of pluggability into the Spring IoC container’s instantiation logic. If you have complex initialization code that is better expressed in Java as opposed to a (potentially) verbose amount of XML, you can create your own `FactoryBean`, write the complex initialization inside that class, and then plug your custom `FactoryBean` into the container.

译：`FactoryBean`接口是Spring Ioc容器的实例化逻辑插入点。如果你有复杂的实例化编码，最好用java编写，不要适用冗长的xml，你可以创建自己的FactoryBean，写一个复杂的初始化类，并且插入自定义的FactoryBean到容器中：

The `FactoryBean` interface provides three methods:

- `Object getObject()`: Returns an instance of the object this factory creates. The instance can possibly be shared, depending on whether this factory returns singletons or prototypes.
- `boolean isSingleton()`: Returns `true` if this `FactoryBean` returns singletons or `false` otherwise.
- `Class getObjectType()`: Returns the object type returned by the `getObject()` method or `null` if the type is not known in advance.

译：FactoryBean接口提供三个方法：

- `Object getObject()`:返回一个factory创建的对象实例。这个实例可以是共享实例，依赖于工厂返回的单例还是原型。
- `boolean IsSingleton()`:如果这个FactoryBean返回单例为true，否则为false.
- `Class getObjectType()`:返回通过`getObject()`方法返回的对象类型或者如果类型实现不知道类型返回null.

The `FactoryBean` concept and interface is used in a number of places within the Spring Framework. More than 50 implementations of the `FactoryBean` interface ship with Spring itself.

译：在Spring框架内，FactroyBean概念和接口在许多地方使用，Spring本身的FactoryBean接口超过50个实现。

When you need to ask a container for an actual `FactoryBean` instance itself instead of the bean it produces, preface the bean’s `id` with the ampersand symbol (`&`) when calling the `getBean()` method of the `ApplicationContext`. So, for a given `FactoryBean` with an `id` of `myBean`, invoking `getBean("myBean")` on the container returns the product of the `FactoryBean`, whereas invoking `getBean("&myBean")` returns the `FactoryBean` instance itself.

译：当你需要查找容器中实际的FactoryBean实例，而不是他产生的bean,当调用ApplicationContext的getBean()方法的时候，在bean的`id`前面加上与号（`&`）。因此，FactoryBean有一个myBean的id,在容器中调用getBean("myBean")返回FactoryBean产生的bean,因此，调用getBean("&myBean")，返回FactoryBean实例本身。

### 1.9. Annotation-based Container Configuration

译：基于注解的容器配置

------

Are annotations better than XML for configuring Spring?

译：注解比Xml配置更好？

The introduction of annotation-based configuration raised the question of whether this approach is “better” than XML. The short answer is “it depends.” The long answer is that each approach has its pros and cons, and, usually, it is up to the developer to decide which strategy suits them better. Due to the way they are defined, annotations provide a lot of context in their declaration, leading to shorter and more concise configuration. However, XML excels at wiring up components without touching their source code or recompiling them. Some developers prefer having the wiring close to the source while others argue that annotated classes are no longer POJOs and, furthermore, that the configuration becomes decentralized and harder to control.

decentralized ：v.分散

译：基于注解配置的引入提出了，这种方式是否比xml好？简短的回答是"它取决于开发者（it depends.）"。长的回答是，每个方法有各各自的优点和缺点。一般来说，由开发人员决定哪种策略更适合他们。由于他们定义的方式，注解在声明中提供了许多上下文，使得配置简短和简洁。但是，XML的优势是不需要接触源码或者重新编译就可以注入组件。一些开发者更喜欢在源代码附近注入，另一些人认为注解类就不再是POJO了，并且，配置变得分散和难以控制。

accommodate ：v.顾及、容纳。

No matter the choice, Spring can accommodate both styles and even mix them together. It is worth pointing out that through its [JavaConfig](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-java) option, Spring lets annotations be used in a non-invasive way, without touching the target components source code and that, in terms of tooling, all configuration styles are supported by the [Spring Tools for Eclipse](https://spring.io/tools).

你选择没有关系，Spring可以顾及两种风格，甚至两者混合使用。值得指出的是，通过Java配置选项，Spring让注解以非侵入式使用，不用接触目标组件源码，并且在工具方面，通过STS所有配置风格都被支持。

------

An alternative to XML setup is provided by annotation-based configuration, which relies on the bytecode metadata for wiring up components instead of angle-bracket declarations. Instead of using XML to describe a bean wiring, the developer moves the configuration into the component class itself by using annotations on the relevant class, method, or field declaration. As mentioned in [Example: The `RequiredAnnotationBeanPostProcessor`](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-extension-bpp-examples-rabpp), using a `BeanPostProcessor` in conjunction with annotations is a common means of extending the Spring IoC container. For example, Spring 2.0 introduced the possibility of enforcing required properties with the [`@Required`](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-required-annotation) annotation. Spring 2.5 made it possible to follow that same general approach to drive Spring’s dependency injection. Essentially, the `@Autowired` annotation provides the same capabilities as described in [Autowiring Collaborators](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-autowire) but with more fine-grained control and wider applicability. Spring 2.5 also added support for JSR-250 annotations, such as `@PostConstruct` and `@PreDestroy`. Spring 3.0 added support for JSR-330 (Dependency Injection for Java) annotations contained in the `javax.inject` package such as `@Inject` and `@Named`. Details about those annotations can be found in the [relevant section](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-standard-annotations).

译：通过基于注解的配置提供了xml配置之外的另一种选择，它依赖于字节码元数据注解组件，而不是见口号定义。而不是使用XML描述一个bean注入，开发者通过注解到相关类、方法或者属性定义，把配置信息以到了组件类自身上。在[Example: The `RequiredAnnotationBeanPostProcessor`](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-extension-bpp-examples-rabpp)提到的，使用BeanPostProcessor与注解结合是扩展Spring IoC容器的普遍方法。例如，Spring2.0引入的使用@Required注解强制需要属性。Spring2.5遵循相同的方法驱动Spring的依赖注入。本质上，@Autowired注解提供了在AutoWiring Collaborators章节描述的相同能力，但是具有更细粒度的控制和可广泛的应用。Spring2.5也增加了支持JSR-250注解，像@PostConstruct和@PreDestroy。Spring3.0 增加了JSR-330注解，包含在javax.inject包中的@Inject和@Named。关于这些注解的详情在Relevant section部分。

------

Annotation injection is performed before XML injection. Thus, the XML configuration overrides the annotations for properties wired through both approaches.

译：注解注入机制在XML注入之前。因此XML配置覆盖通过这两种方法注入的属性。

------

As always, you can register them as individual bean definitions, but they can also be implicitly registered by including the following tag in an XML-based Spring configuration (notice the inclusion of the `context` namespace):

implicitly ：adv.含蓄的；不明显的；无保留地；绝对地。

与往常一样，你可以为作为单个bean定义注册他们，但是他们可以通过包含如下在基于XML的Spring配置中的标签隐式注册（请注意包含context命名空间）：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        https://www.springframework.org/schema/context/spring-context.xsd">

    <context:annotation-config/>

</beans>
```

(The implicitly registered post-processors include [`AutowiredAnnotationBeanPostProcessor`](https://docs.spring.io/spring-framework/docs/5.2.5.RELEASE/javadoc-api/org/springframework/beans/factory/annotation/AutowiredAnnotationBeanPostProcessor.html), [`CommonAnnotationBeanPostProcessor`](https://docs.spring.io/spring-framework/docs/5.2.5.RELEASE/javadoc-api/org/springframework/context/annotation/CommonAnnotationBeanPostProcessor.html), [`PersistenceAnnotationBeanPostProcessor`](https://docs.spring.io/spring-framework/docs/5.2.5.RELEASE/javadoc-api/org/springframework/orm/jpa/support/PersistenceAnnotationBeanPostProcessor.html), and the aforementioned [`RequiredAnnotationBeanPostProcessor`](https://docs.spring.io/spring-framework/docs/5.2.5.RELEASE/javadoc-api/org/springframework/beans/factory/annotation/RequiredAnnotationBeanPostProcessor.html).)

译：隐式注册后置处理器包含`AutowiredAnnotationBeanPostProcessor`，`CommonAnnotationBeanPostProcessor`,`PersistenceAnnotationBeanPostProcessor`,和前面提到的`RequiredAnnotationBeanPostProcessor`.

------

`<context:annotation-config/>`only looks for annotations on beans in the same application context in which it is defined. This means that, if you put  `<context:annotation-config/>` in a `WebApplicationContext` for a `DispatcherServlet`, it only checks for `@Autowired` beans in your controllers, and not your services. See [The DispatcherServlet](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/web.html#mvc-servlet) for more information.

译：`<context:annotation-config/>`只在定义的同一个应用上下文查找bean上的注解。这就意味着，如果你在`DispatcherServlet`的`WebApplicationContext`上放 `<context:annotation-config/>`，它仅在Controllers查找@`Autowired`bean，并不会查找services层的注解.关于更多信息请看DispatherServlet。

------

#### 1.9.1. @Required

The `@Required` annotation applies to bean property setter methods, as in the following example:

译：@Required注解应用于set方法的bean属性，如下所示：

```java
public class SimpleMovieLister {

    private MovieFinder movieFinder;

    @Required
    public void setMovieFinder(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }

    // ...
}
```

This annotation indicates that the affected bean property must be populated at configuration time, through an explicit property value in a bean definition or through autowiring. The container throws an exception if the affected bean property has not been populated. This allows for eager and explicit failure, avoiding `NullPointerException` instances or the like later on. We still recommend that you put assertions into the bean class itself (for example, into an init method). Doing so enforces those required references and values even when you use the class outside of a container.

译：这个注解表示在配置时必须填充受影响的bean属性（set方法的参数，不能为空），通过在一个bean或者通过自动注入的一个显示属性值。如果受影响的属性值没有被填充，容器就会抛出一个异常。这允许早期的或者明确的异常，避免稍后出现`NullPointerException`实例。我们任然建议，你在bean本身进行断言（例如，在一个初始化方法里面）。这个注解强制执行这些需要的引用和值，即使你使用容器外部类。

------

The `@Required` annotation is formally deprecated as of Spring Framework 5.1, in favor of using constructor injection for required settings (or a custom implementation of `InitializingBean.afterPropertiesSet()` along with bean property setter methods).

译：在Spring框架5.1后，@Required注解正式过时，对必须的设置使用构造器注解（或者自定义实现`InitializingBean.afterPropertiesSet()`，为set方法的bean属性赋值）。

------

#### 1.9.2. Using `@Autowired`

------

  JSR 330’s `@Inject` annotation can be used in place of Spring’s `@Autowired` annotation in the examples included in this section. See [here](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-standard-annotations) for more details.

译：在这部分例子中， JSR 330的@Inject注解可以用于替换Spring的@Autowired注解，详情请看这里。

------

You can apply the `@Autowired` annotation to constructors, as the following example shows:

译：你可以使用@Autowired注解到构造器，如下所示：

```java
public class MovieRecommender {

    private final CustomerPreferenceDao customerPreferenceDao;

    @Autowired
    public MovieRecommender(CustomerPreferenceDao customerPreferenceDao) {
        this.customerPreferenceDao = customerPreferenceDao;
    }

    // ...
}
```

------

  As of Spring Framework 4.3, an `@Autowired` annotation on such a constructor is no longer necessary if the target bean defines only one constructor to begin with. However, if several constructors are available, at least one must be annotated with `@Autowired` in order to instruct the container which one to use.

译：在Spirng框架4.3,如果目标bean只有一个构造器，不在需要构造器上的@Autowired注解。但是如果有多个可用的构造器，为了指导容器使用哪一个，至少在一个构造器上使用@Autowired

------

You can also apply the `@Autowired` annotation to *traditional* setter methods, as the following example shows:

译：你也可以在传统的set方法上使用@Autowired注解，如下所示：

```java
public class SimpleMovieLister {

    private MovieFinder movieFinder;

    @Autowired
    public void setMovieFinder(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }

    // ...
}
```

You can also apply the annotation to methods with arbitrary names and multiple arguments, as the following example shows:

译：你也可以在任何名字、参数的方法上使用注解，如下所示：

```java
public class MovieRecommender {

    private MovieCatalog movieCatalog;

    private CustomerPreferenceDao customerPreferenceDao;

    @Autowired
    public void prepare(MovieCatalog movieCatalog,
            CustomerPreferenceDao customerPreferenceDao) {
        this.movieCatalog = movieCatalog;
        this.customerPreferenceDao = customerPreferenceDao;
    }

    // ...
}
```

You can apply `@Autowired` to fields as well and even mix it with constructors, as the following example shows:

译：你也可以在属性上使用@Autowired注解，甚至在构造器中混合使用，如下所示：

```java
public class MovieRecommender {

    private final CustomerPreferenceDao customerPreferenceDao;

    @Autowired
    private MovieCatalog movieCatalog;

    @Autowired
    public MovieRecommender(CustomerPreferenceDao customerPreferenceDao) {
        this.customerPreferenceDao = customerPreferenceDao;
    }

    // ...
}
```

------

Make sure that your target components (for example, `MovieCatalog` or `CustomerPreferenceDao`) are consistently declared by the type that you use for your `@Autowired`-annotated injection points. Otherwise, injection may fail due to a "no type match found" error at runtime.  

For XML-defined beans or component classes found via classpath scanning, the container usually knows the concrete type up front. However, for `@Bean` factory methods, you need to make sure that the declared return type is sufficiently expressive. For components that implement several interfaces or for components potentially referred to by their implementation type, consider declaring the most specific return type on your factory method (at least as specific as required by the injection points referring to your bean).

为了确保，通过@Autowired注解注入点的类型目标组件声明一致（比如，MovieCatalog或者`CustomerPreferenceDao`）。因此，在运行时，由于“没有匹配类型发现”可能注入失败。

对于通过classpanth扫描的找到的xml定义的bean或者组件类，容器通常预先知道具体的类型。但是对于@Bean工厂方法，你需要确保定义的返回类型足够明确。对于实现多个接口的组件或者通过他们的实现类引用的组件，考虑在工厂方法上声明返回类型（至少指定通过注入点引用的bean为必须的）。

------

You can also instruct Spring to provide all beans of a particular type from the `ApplicationContext` by adding the `@Autowired` annotation to a field or method that expects an array of that type, as the following example shows:

译：你也可以指导Spring提供所有指定类型的bean,通过增加@Autowired注解到一个数组类型的属性或者方法，如下所示：

```java
public class MovieRecommender {

    @Autowired
    private MovieCatalog[] movieCatalogs;

    // ...
}
```

The same applies for typed collections, as the following example shows:

对集合类型相同的使用，如下所示：

```java
public class MovieRecommender {

    private Set<MovieCatalog> movieCatalogs;

    @Autowired
    public void setMovieCatalogs(Set<MovieCatalog> movieCatalogs) {
        this.movieCatalogs = movieCatalogs;
    }

    // ...
}
```

------

  Your target beans can implement the `org.springframework.core.Ordered` interface or use the `@Order` or standard `@Priority` annotation if you want items in the array or list to be sorted in a specific order. Otherwise, their order follows the registration order of the corresponding target bean definitions in the container. 

译：如果你希望数组中或者list中的项用指定的顺序排序，你的目标bean可以实现`org.springframework.core.Ordered`接口或使用@Order或者@Priority注解。

 You can declare the `@Order` annotation at the target class level and on `@Bean` methods, potentially for individual bean definitions (in case of multiple definitions that use the same bean class). `@Order` values may influence priorities at injection points, but be aware that they do not influence singleton startup order, which is an orthogonal concern determined by dependency relationships and `@DependsOn` declarations.  

译：你可以在目标类级别或者在@Bean方法上，为每个bean定义@Order注解(多个定义使用相同bean的场合)，@`order`值可以注入点的优先级，但是要知道它不能影响单例bean的启动顺序，单例bean的启动顺序通过依赖关系和@DependsOn声明的正交关注点决定。

Note that the standard `javax.annotation.Priority` annotation is not available at the `@Bean` level, since it cannot be declared on methods. Its semantics can be modeled through `@Order` values in combination with `@Primary` on a single bean for each type.

译：请记住标准的javax.annotation.Priority注解不能再@Bean级别使用，因此他不能定义在方法上。*它的语义可以通过为每个类的@Order值联合在一个单例bean的@Primary建模。*

------

Even typed `Map` instances can be autowired as long as the expected key type is `String`. The map values contain all beans of the expected type, and the keys contain the corresponding bean names, as the following example shows:

译：即使只要期望的key类型是String,Map实例类型可以自动注入.映射值包含所有期望类型的bean,key包含相应的bean名称，如下所示：

```java
public class MovieRecommender {

    private Map<String, MovieCatalog> movieCatalogs;

    @Autowired
    public void setMovieCatalogs(Map<String, MovieCatalog> movieCatalogs) {
        this.movieCatalogs = movieCatalogs;
    }

    // ...
}
```

By default, autowiring fails when no matching candidate beans are available for a given injection point. In the case of a declared array, collection, or map, at least one matching element is expected.

译：默认情况下，当没有匹配到给定点的候选bean时，自动注入失败。在声明数组、集合、或者Map的场合,期望至少一个匹配元素。

The default behavior is to treat annotated methods and fields as indicating required dependencies. You can change this behavior as demonstrated in the following example, enabling the framework to skip a non-satisfiable injection point through marking it as non-required (i.e., by setting the `required` attribute in `@Autowired` to `false`):

译：默认行为，对注解的方法和属性表示需要依赖。你可以改变这个行为，如下案例进行论证，通过标记非必须，使得框架跳过不满足的注入点（通过设置@Autowired的required属性为false）。

```java
public class SimpleMovieLister {

    private MovieFinder movieFinder;

    @Autowired(required = false)
    public void setMovieFinder(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }

    // ...
}
```

A non-required method will not be called at all if its dependency (or one of its dependencies, in case of multiple arguments) is not available. A non-required field will not get populated at all in such case, leaving its default value in place.

译：如果它的依赖不可用，一个非必须的方法不会调用（或者它的依赖之一不可用，在多个参数场合）。这种场合，一个非必须属性不会填充，默认保留默认值。

Injected constructor and factory method arguments are a special case since the `required` attribute in `@Autowired` has a somewhat different meaning due to Spring’s constructor resolution algorithm that may potentially deal with multiple constructors. Constructor and factory method arguments are effectively required by default but with a few special rules in a single-constructor scenario, such as multi-element injection points (arrays, collections, maps) resolving to empty instances if no matching beans are available. This allows for a common implementation pattern where all dependencies can be declared in a unique multi-argument constructor — for example, declared as a single public constructor without an `@Autowired` annotation.

译：注入构造器和工厂方法参数是一种特殊场合，由于Spring的构造器解析算法可能潜在地处理多个构造器，因此在@Autowired的required属性有不同 的含义。默认情况下，构造器和工厂方法参数是必须的，但是在单个构造器场合有一些特殊规则，比如如果没有匹配到可用的bean,多元素注入点（array,collection,maps）解析空实例。这允许一个普通实现模板，在那里所有的依赖可以声明在一个唯一的多参数构造器—例如，声明一个没有@Autorwired注解的public constructor

------

Only one constructor of any given bean class may declare `@Autowired` with the `required` attribute set to `true`, indicating *the* constructor to autowire when used as a Spring bean. As a consequence, if the `required` attribute is left at its default value `true`, only a single constructor may be annotated with `@Autowired`. If multiple constructors declare the annotation, they will all have to declare `required=false` in order to be considered as candidates for autowiring (analogous to `autowire=constructor` in XML). The constructor with the greatest number of dependencies that can be satisfied by matching beans in the Spring container will be chosen. If none of the candidates can be satisfied, then a primary/default constructor (if present) will be used. If a class only declares a single constructor to begin with, it will always be used, even if not annotated. 

Note that an annotated constructor does not have to be public.  The `required` attribute of `@Autowired` is recommended over the deprecated `@Required` annotation on setter methods. Setting the `required` attribute to `false` indicates that the property is not required for autowiring purposes, and the property is ignored if it cannot be autowired. `@Required`, on the other hand, is stronger in that it enforces the property to be set by any means supported by the container, and if no value is defined, a corresponding exception is raised.

译：任何给定bean类仅一个构造器可以声明@Autowired的required属性设置为true,当使用一个Spring bean时，表明自动注入的构造器。因此，如果required属性保留默认值true,只有一个构造器使用@Autowired注解。如果多个构造器声明这个注解，他们必须声明required=false,为了被视为自动注入的候选项（在xml中类似autowire=constructor）。通过匹配容器中的bean,选择构造器满足数量最多的依赖（使用参数匹配最多的构造函数）。如果没有满足的候选项bean，然后primary/defalut构造器将会使用。如果一个类仅声明一个构造器，它将一直使用，即使没有注解。

请记住，注解的构造器不必须是public的。在set方法上建议使用@Autowired注解的required属性，而不是过时的@Required注解。设置required属性为false表明对自动注入属性不是必须的，如果它不能被注入，这个属性将会忽略。在另一方面，@Required更强大，它强制通过任何容器支持的方法设置属性，如果没有值定义，抛出相应的异常。

------

Alternatively, you can express the non-required nature of a particular dependency through Java 8’s `java.util.Optional`, as the following example shows:

译：或者，你可以通过java8的`java.util.Optional`表示不需要特定依赖。如下所示：

```java
public class SimpleMovieLister {

    @Autowired
    public void setMovieFinder(Optional<MovieFinder> movieFinder) {
        ...
    }
}
```

As of Spring Framework 5.0, you can also use a `@Nullable` annotation (of any kind in any package — for example, `javax.annotation.Nullable` from JSR-305) or just leverage Kotlin builtin null-safety support:

译：从Spring框架5.0开始，你也可以使用@Nullable注解（任何包中的任何类型，例如在JSR-305d的javax.annotation.Nullable）或者Kotlin内置的null-safety支持。

```java
public class SimpleMovieLister {

    @Autowired
    public void setMovieFinder(@Nullable MovieFinder movieFinder) {
        ...
    }
}
```

You can also use `@Autowired` for interfaces that are well-known resolvable dependencies: `BeanFactory`, `ApplicationContext`, `Environment`, `ResourceLoader`, `ApplicationEventPublisher`, and `MessageSource`. These interfaces and their extended interfaces, such as `ConfigurableApplicationContext` or `ResourcePatternResolver`, are automatically resolved, with no special setup necessary. The following example autowires an `ApplicationContext` object:

译：你也可以对众所周知的可解析依赖接口使用@`Autowired`注解：`BeanFactory`，`ApplictionContext`,`Environment`,`ResourceLoader`,`ApplicationEventPulisher`,和`messageSource`.这些接口和他们的扩展接口，比例，ConfigrableApplicationContext或者ResourcePatternResolver,是自动解析，不需要特殊设置。如下例子自动注入`ApplicationContext`对象：

```java
public class MovieRecommender {

    @Autowired
    private ApplicationContext context;

    public MovieRecommender() {
    }

    // ...
}
```

------

The `@Autowired`, `@Inject`, `@Value`, and `@Resource` annotations are handled by Spring `BeanPostProcessor` implementations. This means that you cannot apply these annotations within your own `BeanPostProcessor` or `BeanFactoryPostProcessor` types (if any). These types must be 'wired up' explicitly by using XML or a Spring `@Bean` method.

译：@Autowired,@Inject,@Value和@Resource注解处理通过Spring BeanPostProcessor实现。这意味着你不能使用这些注解在BeanPostProcessor或者BeanFactoryPostProcessor任何类中。这些类型必须通过使用xml或者Spring@Bean方法显示连接起来。

------

#### 1.9.3. Fine-tuning Annotation-based Autowiring with `@Primary`

译：使用@Primary注解微调基于注解的自动注入

Because autowiring by type may lead to multiple candidates, it is often necessary to have more control over the selection process. One way to accomplish this is with Spring’s `@Primary` annotation. `@Primary` indicates that a particular bean should be given preference when multiple beans are candidates to be autowired to a single-valued dependency. If exactly one primary bean exists among the candidates, it becomes the autowired value.

译：由于通过类的自动注入可能导致多个候选项，在选择处理上，经常需要有更多的控制。完成这个功能的一种方式使用Spring的@Primary注解。当对一个单值依赖多个bean时,@Primary表明这个特殊类应该优先考虑。如果刚好在多个候选项中存在一个primary bean，它就成为自动注入的值：

Consider the following configuration that defines `firstMovieCatalog` as the primary `MovieCatalog`:

译:考虑如下定义的配置，`firstMovieCatalog` 作为主`MovieCatalog`:

```java
@Configuration
public class MovieConfiguration {

    @Bean
    @Primary
    public MovieCatalog firstMovieCatalog() { ... }

    @Bean
    public MovieCatalog secondMovieCatalog() { ... }

    // ...
}
```

With the preceding configuration, the following `MovieRecommender` is autowired with the `firstMovieCatalog`:

译：使用先前的配置，如下`MovieRecommender` 类自动注入`firstMovieCatalog`

```java
public class MovieRecommender {

    @Autowired
    private MovieCatalog movieCatalog;

    // ...
}
```

The corresponding bean definitions follow:

译：相应的bean定义如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        https://www.springframework.org/schema/context/spring-context.xsd">

    <context:annotation-config/>

    <bean class="example.SimpleMovieCatalog" primary="true">
        <!-- inject any dependencies required by this bean -->
    </bean>

    <bean class="example.SimpleMovieCatalog">
        <!-- inject any dependencies required by this bean -->
    </bean>

    <bean id="movieRecommender" class="example.MovieRecommender"/>

</beans>
```

#### 1.9.4. Fine-tuning Annotation-based Autowiring with Qualifiers

译：使用Qualifiers微调基于注解的自动注入

`@Primary` is an effective way to use autowiring by type with several instances when one primary candidate can be determined. When you need more control over the selection process, you can use Spring’s `@Qualifier` annotation. You can associate qualifier values with specific arguments, narrowing the set of type matches so that a specific bean is chosen for each argument. In the simplest case, this can be a plain descriptive value, as shown in the following example:

译：通过类型自动注入的多个实例，当需要决定一个主要候选项时，@Primary注解是一个有效的方式。当你在选择执行上,需要更多的控制,你可以使用Spring的@Qualifier注解。你可以用指定的参数关联qualifer值，缩小匹配设置，以便于为每个参数匹配指定接近的bean.在简单的情况下，可以使一个简单的描述值，如下所示：

```java
public class MovieRecommender {

    @Autowired
    @Qualifier("main")
    private MovieCatalog movieCatalog;

    // ...
}
```

You can also specify the `@Qualifier` annotation on individual constructor arguments or method parameters, as shown in the following example:

译：你可以在每个构造参数或者方法参数上指定@Qualifier注解，如下所示：

```java
public class MovieRecommender {

    private MovieCatalog movieCatalog;

    private CustomerPreferenceDao customerPreferenceDao;

    @Autowired
    public void prepare(@Qualifier("main") MovieCatalog movieCatalog,
            CustomerPreferenceDao customerPreferenceDao) {
        this.movieCatalog = movieCatalog;
        this.customerPreferenceDao = customerPreferenceDao;
    }

    // ...
}
```

The following example shows corresponding bean definitions.

译：如下例子显示相应的bean:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        https://www.springframework.org/schema/context/spring-context.xsd">

    <context:annotation-config/>

    <bean class="example.SimpleMovieCatalog">
        <qualifier value="main"/> 

        <!-- inject any dependencies required by this bean -->
    </bean>

    <bean class="example.SimpleMovieCatalog">
        <qualifier value="action"/> 

        <!-- inject any dependencies required by this bean -->
    </bean>

    <bean id="movieRecommender" class="example.MovieRecommender"/>

</beans>
```

The bean with the `main` qualifier value is wired with the constructor argument that is qualified with the same value.

译：有构造器参数`main`限定符值的bean关联到有相同限定符值的构造参数。

  The bean with the `action` qualifier value is wired with the constructor argument that is qualified with the same value

译：有`action`限定符值的连接到有相同值的构造参数。

For a fallback match, the bean name is considered a default qualifier value. Thus, you can define the bean with an `id` of `main` instead of the nested qualifier element, leading to the same matching result. However, although you can use this convention to refer to specific beans by name, `@Autowired` is fundamentally about type-driven injection with optional semantic qualifiers. This means that qualifier values, even with the bean name fallback, always have narrowing semantics within the set of type matches. They do not semantically express a reference to a unique bean `id`. Good qualifier values are `main` or `EMEA` or `persistent`, expressing characteristics of a specific component that are independent from the bean `id`, which may be auto-generated in case of an anonymous bean definition such as the one in the preceding example.

译：对于回退匹配，bean名称被视为默认的限定符值。因此，你可以用`main`的`id`定义bean，而不是嵌套限定符选项定义bean，从而得到相同的结果.但是，尽管你可以使用这个约束按名称引用特定bean,从基本上来说，@Autowired是关于使用自定义语义限定符的类型驱动注入。这意味着，即使使用bean 名称回调，在设置类型匹配中总是具有狭隘的语义。在语义上，他们表示对唯一bean `id`的引用。好的限定符值是`main`或者`EMEA`或者`persistent`，表示指定组件依赖于bean id的独特特性，在匿名bean的场合，可以自动生成，比如在前面例子中的一个：

Qualifiers also apply to typed collections, as discussed earlier — for example, to `Set`. In this case, all matching beans, according to the declared qualifiers, are injected as a collection. This implies that qualifiers do not have to be unique. Rather, they constitute filtering criteria. For example, you can define multiple `MovieCatalog` beans with the same qualifier value “action”, all of which are injected into a `Set` annotated with `@Qualifier("action")`.

译：限定符也使用在集合类，如前面讨论的—比如，Set.在这个场合上，所有匹配的bean，根据声明的限定符，作为一个集合注入。这证明限定符不必须是唯一的。相反，他们构成了过滤标准。例如，你可以使用相同限定符`action`定义多个`MovieCatalog`bean,使用@Qualifier("action")注解的所有的bean都注入到set集合。

------

  Letting qualifier values select against target bean names, within the type-matching candidates, does not require a `@Qualifier` annotation at the injection point. If there is no other resolution indicator (such as a qualifier or a primary marker), for a non-unique dependency situation, Spring matches the injection point name (that is, the field name or parameter name) against the target bean names and choose the same-named candidate, if any.

译：让限定符值针对目标bean名称选择，在匹配类型选项内，在注入点上不需要@Qualifer注解。如果没有其他解析指示器（例如一个qualifier或者primary标记），对一个非唯一依赖情形，Spring匹配注入点名称（也就是说，属性名字或者参数名字）与目标bean匹配并选择相同名称的候选项。

------

That said, if you intend to express annotation-driven injection by name, do not primarily use `@Autowired`, even if it is capable of selecting by bean name among type-matching candidates. Instead, use the JSR-250 `@Resource` annotation, which is semantically defined to identify a specific target component by its unique name, with the declared type being irrelevant for the matching process. `@Autowired` has rather different semantics: After selecting candidate beans by type, the specified `String` qualifier value is considered within those type-selected candidates only (for example, matching an `account` qualifier against beans marked with the same qualifier label).

译：也就是说，如果你倾向使用名称表示基于注解的注入，不要主要使用@Aurowired，即使它能够通过在类型匹配后选中选择。而是，使用JSR-250中的@Resource注解，它在语义上表示为通过唯一名称定义一个指定目标组件，声明的类型与匹配处理过程无关。@Autowired有一些不同的语义：在通过类型选择候选bean之后,指定`String`限定符值只在这些选择类型候选项中考虑（例如，匹配一个`account`限定符与标记有相同限定符标签的bean匹配）。

For beans that are themselves defined as a collection, `Map`, or array type, `@Resource` is a fine solution, referring to the specific collection or array bean by unique name. That said, as of 4.3, collection, you can match `Map`, and array types through Spring’s `@Autowired` type matching algorithm as well, as long as the element type information is preserved in `@Bean` return type signatures or collection inheritance hierarchies. In this case, you can use qualifier values to select among same-typed collections, as outlined in the previous paragraph.

译：对于自身定义为一个集合、Map、或者数组类型的bean,@Resource是一个很好的解决方案，通过唯一名称引用指定集合或者数组bean。也就是说，从4.3之后，集合，你可以通过Spring的@Autowired类型匹配算法匹配Map和数组类型，只要元素类型信息在@Bean返回类型标志或者集合继承中出现。在这个场合中，你可以使用限定符只选择类型相同的集合，如前一段所述。

As of 4.3, `@Autowired` also considers self references for injection (that is, references back to the bean that is currently injected). Note that self injection is a fallback. Regular dependencies on other components always have precedence. In that sense, self references do not participate in regular candidate selection and are therefore in particular never primary. On the contrary, they always end up as lowest precedence. In practice, you should use self references as a last resort only (for example, for calling other methods on the same instance through the bean’s transactional proxy). Consider factoring out the effected methods to a separate delegate bean in such a scenario. Alternatively, you can use `@Resource`, which may obtain a proxy back to the current bean by its unique name.

译：从4.3之后，@Autowired也考虑自注入（也就是说，反过来当前注入的引用）。请注意，自注入是一个回调。对其他组件的常规依赖始终具有优先权。意味着，自引用没有在常规候选项中选拔，因此永远没有唯一项。xian规范，他们总是以最低优先权结束。实践中，你应该使用自引用只作为最后的手段（例如，通过bean的事务代理调用相同实例上的另一个方法）。在这种场景下，考虑分解受影响的方法到一个当都的委托bean.或者，你可以使用@Resource，它通过唯一名字获得当前bean的一个代理。

------

Trying to inject the results from `@Bean` methods on the same configuration class is effectively a self-reference scenario as well. Either lazily resolve such references in the method signature where it is actually needed (as opposed to an autowired field in the configuration class) or declare the affected `@Bean` methods as `static`, decoupling them from the containing configuration class instance and its lifecycle. Otherwise, such beans are only considered in the fallback phase, with matching beans on other configuration classes selected as primary candidates instead (if available).

译：尝试在相同配置类中注入@Bean方法，也是一个自引用场景。要么在实际需要的方法签名上，懒加载这个引用（与配置类中的autowired属性相反）或者定义受影响的bean为`static`的，把包含的配置类实例和生命周期分离。因此，这个bean只在回调阶段考虑，在其他配置类中匹配的bean作为唯一选项（如果可用）。

------

`@Autowired` applies to fields, constructors, and multi-argument methods, allowing for narrowing through qualifier annotations at the parameter level. In contrast, `@Resource` is supported only for fields and bean property setter methods with a single argument. As a consequence, you should stick with qualifiers if your injection target is a constructor or a multi-argument method.

stick with ：坚持做;紧跟;紧随；

译：@Autowired在属性、构造器、多参数方法上使用，允许在参数级别通过限定符变窄。相反，@Resource只支持在属性和单个参数的set方法的bean。因此，如果你的注入目标是一个构造器或者多参数方法，你应该坚持使用限定符。

You can create your own custom qualifier annotations. To do so, define an annotation and provide the `@Qualifier` annotation within your definition, as the following example shows:

译：如果你创建自定义的限定符注解。这么做，定义一个注解并且在定义内提供@Qualifer注解，如下所示：

```java
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface Genre {

    String value();
}
```

Then you can provide the custom qualifier on autowired fields and parameters, as the following example shows:

然后，你可以在自动注入的属性和参数上提供自定义的限定符，如下所示：

```java
public class MovieRecommender {

    @Autowired
    @Genre("Action")
    private MovieCatalog actionCatalog;

    private MovieCatalog comedyCatalog;

    @Autowired
    public void setComedyCatalog(@Genre("Comedy") MovieCatalog comedyCatalog) {
        this.comedyCatalog = comedyCatalog;
    }

    // ...
}
```

Next, you can provide the information for the candidate bean definitions. You can add  `<qualifier/>`tags as sub-elements of the tag and then specify the `type` and `value` to match your custom qualifier annotations. The type is matched against the fully-qualified class name of the annotation. Alternately, as a convenience if no risk of conflicting names exists, you can use the short class name. The following example demonstrates both approaches:

译：接下来，你可以提供候选bean定义信息。你可以增加标签的子标签`<qualifier/>`，然后指定`type`和`value`匹配自定义的限定符注解。`type`匹配注解的完全限定类名。或者，方便起见，如果没有名称冲突存在的风险，你可以使用短类名。如下例子同时论证方法：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        https://www.springframework.org/schema/context/spring-context.xsd">

    <context:annotation-config/>

    <bean class="example.SimpleMovieCatalog">
        <qualifier type="Genre" value="Action"/>
        <!-- inject any dependencies required by this bean -->
    </bean>

    <bean class="example.SimpleMovieCatalog">
        <qualifier type="example.Genre" value="Comedy"/>
        <!-- inject any dependencies required by this bean -->
    </bean>

    <bean id="movieRecommender" class="example.MovieRecommender"/>

</beans>
```

In [Classpath Scanning and Managed Components](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-classpath-scanning), you can see an annotation-based alternative to providing the qualifier metadata in XML. Specifically, see [Providing Qualifier Metadata with Annotations](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-scanning-qualifiers).

译：在类路径扫描和管理组件章节，可以看基于注解的替代方法，而不是在XML中提供限定符元数据。具体说明，请看使用注解提供限定符元数据章节。

In some cases, using an annotation without a value may suffice. This can be useful when the annotation serves a more generic purpose and can be applied across several different types of dependencies. For example, you may provide an offline catalog that can be searched when no Internet connection is available. First, define the simple annotation, as the following example shows:

译：在一些场合中，使用一个没有值的注解可能足够了。当注解服务多个一般目的和跨几个不同依赖类使用，是非常有用的。例如，你可以提供一个offline catalog，当没有网络连接不可用时，可以进行查找。首先定义简单的注解，如下所示：

```java
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface Offline {

}
```

Then add the annotation to the field or property to be autowired, as shown in the following example:

译：然后，增加注解到字段或者自动注入的属性，如下所示：

```java
public class MovieRecommender {

    @Autowired
    @Offline 
    private MovieCatalog offlineCatalog;

    // ...
}
```

Now the bean definition only needs a qualifier `type`, as shown in the following example:

译：现在，bean只需要一个限定符`type`，如下所示：

```xml
<bean class="example.SimpleMovieCatalog">
    <qualifier type="Offline"/> 
    <!-- inject any dependencies required by this bean -->
</bean>
```

You can also define custom qualifier annotations that accept named attributes in addition to or instead of the simple `value` attribute. If multiple attribute values are then specified on a field or parameter to be autowired, a bean definition must match all such attribute values to be considered an autowire candidate. As an example, consider the following annotation definition:

译：你可以定义自定义限定符注解，它接受简单的`value`属性外，还接受命名属性。如果多个属性值在一个字段或者参数自动注入参数上指定，bean定义必须匹配所有这些属性值的自动注入项，考虑如下定义的例子：

```java
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface MovieQualifier {

    String genre();

    Format format();
}
```

In this case `Format` is an enum, defined as follows:

译：这个情况中的Format时一个enum，定义如下：

```java
public enum Format {
    VHS, DVD, BLURAY
}
```

The fields to be autowired are annotated with the custom qualifier and include values for both attributes: `genre` and `format`, as the following example shows:

译：自动注入的字段用自定义限定符注解，同时包含属性值：genre和format,如下所示：

```java
public class MovieRecommender {

    @Autowired
    @MovieQualifier(format=Format.VHS, genre="Action")
    private MovieCatalog actionVhsCatalog;

    @Autowired
    @MovieQualifier(format=Format.VHS, genre="Comedy")
    private MovieCatalog comedyVhsCatalog;

    @Autowired
    @MovieQualifier(format=Format.DVD, genre="Action")
    private MovieCatalog actionDvdCatalog;

    @Autowired
    @MovieQualifier(format=Format.BLURAY, genre="Comedy")
    private MovieCatalog comedyBluRayCatalog;

    // ...
}
```

Finally, the bean definitions should contain matching qualifier values. This example also demonstrates that you can use bean meta attributes instead of the `<qulifier/>`elements. If available, the `<qulifier/>` element and its attributes take precedence, but the autowiring mechanism falls back on the values provided within the `<meta/>` tags if no such qualifier is present, as in the last two bean definitions in the following example:

falls back on :依靠

译：最后，bean应该包含匹配的限定符值。这个例子论证了，你能使用bean的元属性，而不是`<qulifier/>`标签。如果可用，`<qulifier/>`标签和它的属性具有优先权，但是如果没有限定符出现，自动注入机制依靠`<meta/>`标签提供的值，如下最后两个bean定义的例子所示：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        https://www.springframework.org/schema/context/spring-context.xsd">

    <context:annotation-config/>

    <bean class="example.SimpleMovieCatalog">
        <qualifier type="MovieQualifier">
            <attribute key="format" value="VHS"/>
            <attribute key="genre" value="Action"/>
        </qualifier>
        <!-- inject any dependencies required by this bean -->
    </bean>

    <bean class="example.SimpleMovieCatalog">
        <qualifier type="MovieQualifier">
            <attribute key="format" value="VHS"/>
            <attribute key="genre" value="Comedy"/>
        </qualifier>
        <!-- inject any dependencies required by this bean -->
    </bean>

    <bean class="example.SimpleMovieCatalog">
        <meta key="format" value="DVD"/>
        <meta key="genre" value="Action"/>
        <!-- inject any dependencies required by this bean -->
    </bean>

    <bean class="example.SimpleMovieCatalog">
        <meta key="format" value="BLURAY"/>
        <meta key="genre" value="Comedy"/>
        <!-- inject any dependencies required by this bean -->
    </bean>

</beans>
```

#### 1.9.5. Using Generics as Autowiring Qualifiers

译：使用泛型作为自动注入限定符

In addition to the `@Qualifier` annotation, you can use Java generic types as an implicit form of qualification. For example, suppose you have the following configuration:

译：除了@Qualifier注解，你可以使用java泛型类作为限定符的隐式形式.例如，假设你有如下配置：

```java
@Configuration
public class MyConfiguration {

    @Bean
    public StringStore stringStore() {
        return new StringStore();
    }

    @Bean
    public IntegerStore integerStore() {
        return new IntegerStore();
    }
}
```

Assuming that the preceding beans implement a generic interface, (that is, `Store` and `Store`), you can `@Autowire` the `Store` interface and the generic is used as a qualifier, as the following example shows:

译：假设前面的bean实现了泛型接口（即，`Store`和`Store`）,你可以@`Autowired` `Store`接口，并作为限定符使用，如下所示

```java
@Autowired
private Store<String> s1; // <String> qualifier, injects the stringStore bean

@Autowired
private Store<Integer> s2; // <Integer> qualifier, injects the integerStore bean 
```

Generic qualifiers also apply when autowiring lists, `Map` instances and arrays. The following example autowires a generic `List`:

译：当自动注入list、map实例和array，泛型限定符也可以使用。如下所示，自动注入一个泛型List:

```java
@Autowired
private List<Store<Integer>> s;
```

#### 1.9.6. Using `CustomAutowireConfigurer`

[`CustomAutowireConfigurer`](https://docs.spring.io/spring-framework/docs/5.2.5.RELEASE/javadoc-api/org/springframework/beans/factory/annotation/CustomAutowireConfigurer.html) is a `BeanFactoryPostProcessor` that lets you register your own custom qualifier annotation types, even if they are not annotated with Spring’s `@Qualifier` annotation. The following example shows how to use `CustomAutowireConfigurer`:

译：`CustomAutowireConfigurer`是一个`BeanFactoryPostProcessor`，它让你注册自定义限定符注解类，即使他们没有使用Spring的@Qualifier注解。如下例子，展示如何使用`CustomAutowireConfigurer`:

```xml
<bean id="customAutowireConfigurer"
        class="org.springframework.beans.factory.annotation.CustomAutowireConfigurer">
    <property name="customQualifierTypes">
        <set>
            <value>example.CustomQualifier</value>
        </set>
    </property>
</bean>
```

The `AutowireCandidateResolver` determines autowire candidates by:

- The `autowire-candidate` value of each bean definition
- Any `default-autowire-candidates` patterns available on the `<bean/>` element
- The presence of `@Qualifier` annotations and any custom annotations registered with the `CustomAutowireConfigurer`

When multiple beans qualify as autowire candidates, the determination of a “primary” is as follows: If exactly one bean definition among the candidates has a `primary` attribute set to `true`, it is selected.

译：`AutowireCandidateResolver`决定自动注入候选项，通过如下：

- 每个bean定义的`autowire-candidate`值
- 在<bean/>标签上的可用的任何`default-autowire-candidates`模板。
- 出现`@Qualifier`注解和使用`CustomAutowireConfigurer`任何自定义注解

When multiple beans qualify as autowire candidates, the determination of a “primary” is as follows: If exactly one bean definition among the candidates has a `primary` attribute set to `true`, it is selected.

译：当多个bean限定符作为自定义注入候选项，唯一的bean决定如下：如果在多个候选项中有一个bean显示声明`primary`属性为true,它被选择。

#### 1.9.7. Injection with `@Resource`

译：使用@Resource注入

Spring also supports injection by using the JSR-250 `@Resource` annotation (`javax.annotation.Resource`) on fields or bean property setter methods. This is a common pattern in Java EE: for example, in JSF-managed beans and JAX-WS endpoints. Spring supports this pattern for Spring-managed objects as well.

译：通过在字段或者set方法的bean属性上使用JSR-250`@Resource`（javax.annotation.Resource）注解，Spring也支持注入。这是javaEE的常见模式：例如，在JSF-managed bean和JAX-WS点。Spring对Spring管理的对象也支持这个模式。

`@Resource` takes a name attribute. By default, Spring interprets that value as the bean name to be injected. In other words, it follows by-name semantics, as demonstrated in the following example:

译：@Resource有一个名字属性。默认情况下，Spring作为注入bean名称的值。换言而之，它遵循by-name语义，如下例子论证：

```java
public class SimpleMovieLister {

    private MovieFinder movieFinder;

    @Resource(name="myMovieFinder") 
    public void setMovieFinder(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }
}
```

If no name is explicitly specified, the default name is derived from the field name or setter method. In case of a field, it takes the field name. In case of a setter method, it takes the bean property name. The following example is going to have the bean named `movieFinder` injected into its setter method:

译：如果没有名字显示指定，默认名字从字段名字或者set方法名产生。在字段情形，它使用字段名。在set方法情形，它使用bean属性名。如下例子将有一个名为`movieFinder`注入到set方法：

```java
public class SimpleMovieLister {

    private MovieFinder movieFinder;

    @Resource
    public void setMovieFinder(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }
}
```

------

The name provided with the annotation is resolved as a bean name by the `ApplicationContext` of which the `CommonAnnotationBeanPostProcessor` is aware. The names can be resolved through JNDI if you configure Spring’s [`SimpleJndiBeanFactory`](https://docs.spring.io/spring-framework/docs/5.2.5.RELEASE/javadoc-api/org/springframework/jndi/support/SimpleJndiBeanFactory.html) explicitly. However, we recommend that you rely on the default behavior and use Spring’s JNDI lookup capabilities to preserve the level of indirection.

译：注解提供的名称，通过ComonAnntationBeanPostProcessor知道的ApplicationContext作为一个bean名称被解析。如果你显示配置Spring的SimpleJndiBeanFactory，这个名称通过JNDI解析。但是，我们建议你依赖默认行为并且使用Spring的JNDI查找功能保留间接级别。

------

In the exclusive case of `@Resource` usage with no explicit name specified, and similar to `@Autowired`, `@Resource` finds a primary type match instead of a specific named bean and resolves well known resolvable dependencies: the `BeanFactory`, `ApplicationContext`, `ResourceLoader`, `ApplicationEventPublisher`, and `MessageSource` interfaces.

译：在`@Resource`没有使用显示声明名称的情况，和@Autowired类似，@Resource查找唯一类匹配，而不是特定命名bean,并且西知道的依赖项：beanFactory、ApplicationContext、ResourceLoader、ApplicationEventPublisher和MessageSource接口。

Thus, in the following example, the `customerPreferenceDao` field first looks for a bean named "customerPreferenceDao" and then falls back to a primary type match for the type `CustomerPreferenceDao`:

译：因此，如下例子， `customerPreferenceDao`字段寻找叫做”customerPreferenceDao“的bean,并且依靠唯一类型匹配`CustomerPreferenceDao`类。

```java
public class MovieRecommender {

    @Resource
    private CustomerPreferenceDao customerPreferenceDao;

    @Resource
    private ApplicationContext context; 

    public MovieRecommender() {
    }

    // ...
}
```

#### 1.9.8. Using `@Value`

`@Value` is typically used to inject externalized properties:

译：@Value通常用于注入外部属性。

```java
@Component
public class MovieRecommender {

    private final String catalog;

    public MovieRecommender(@Value("${catalog.name}") String catalog) {
        this.catalog = catalog;
    }
}
```

With the following configuration:

译：使用如下配置：

```java
@Configuration
@PropertySource("classpath:application.properties")
public class AppConfig { }
```

And the following `application.properties` file:

译：如下application.properties文件：

```java
catalog.name=MovieCatalog
```

In that case, the `catalog` parameter and field will be equal to the `MovieCatalog` value.

译：在这个情形下,`catalog`参数和属性等于`MovieCatalog` 值。

A default lenient embedded value resolver is provided by Spring. It will try to resolve the property value and if it cannot be resolved, the property name (for example `${catalog.name}`) will be injected as the value. If you want to maintain strict control over nonexistent values, you should declare a `PropertySourcesPlaceholderConfigurer` bean, as the following example shows:

译：Spring提供了默认嵌入式值解析器。它将尝试解析属性值，如果不能解析，属性名将作为值注入（例如`${catalog.name}`）。如果你想对不存在的值得到严格控制，你应该声明一个`PropertySourcePlaceholderConfigurer` bean.如下所示：

```java
@Configuration
public class AppConfig {

     @Bean
     public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
           return new PropertySourcesPlaceholderConfigurer();
     }
}
```

------

When configuring a `PropertySourcesPlaceholderConfigurer` using JavaConfig, the `@Bean` method must be `static`.

译：当使用javaConfig配置PropertySourcesPlaceholderConfigurer时，@Bean方法必须是`static`的

------

Using the above configuration ensures Spring initialization failure if any `${}` placeholder could not be resolved. It is also possible to use methods like `setPlaceholderPrefix`, `setPlaceholderSuffix`, or `setValueSeparator` to customize placeholders.

译：如果任何`@{}`不能解析，使用以上配置确保Spring初始化失败。它也可能使用`setPlaceholderPrefix`、`setPlaceholderSuffix`或者`setValueSeparator` 方法自定义占位符。

------

Spring Boot configures by default a `PropertySourcesPlaceholderConfigurer` bean that will get properties from `application.properties` and `application.yml` files.

译：Spring Boot通过默认的`PropertySourcesPlaceholderConfigurer`bean配置,它从application.properties和application.yml文件得到属性。

------

Built-in converter support provided by Spring allows simple type conversion (to `Integer` or `int` for example) to be automatically handled. Multiple comma-separated values can be automatically converted to String array without extra effort.

译：通过Spring提供的内置转换器支持自动处理简单类型转换（比如，Integer或者int）。多个逗号值可以自动转换为String数组，不需要额外工作。

It is possible to provide a default value as following:

译：它可能提供默认值，如下所示：

```java
@Component
public class MovieRecommender {

    private final String catalog;

    public MovieRecommender(@Value("${catalog.name:defaultCatalog}") String catalog) {
        this.catalog = catalog;
    }
}
```

A Spring `BeanPostProcessor` uses a `ConversionService` behind the scene to handle the process for converting the String value in `@Value` to the target type. If you want to provide conversion support for your own custom type, you can provide your own `ConversionService` bean instance as the following example shows:

译：Spring `beanPostProcessor`使用`ConversionService` 在幕后处理目标类型上@Value的String值。如果你想提供自定义类型的转换支持，你可以提供自己的`ConversionService`bean实例，如下所示：

```java
@Configuration
public class AppConfig {

    @Bean
    public ConversionService conversionService() {
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        conversionService.addConverter(new MyCustomConverter());
        return conversionService;
    }
}
```

When `@Value` contains a [`SpEL` expression](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#expressions) the value will be dynamically computed at runtime as the following example shows:

译：当@Value包含一个 SpEl表达式值，将在运行时动态完成，如下所示：

```java
@Component
public class MovieRecommender {

    private final String catalog;

    public MovieRecommender(@Value("#{systemProperties['user.catalog'] + 'Catalog' }") String catalog) {
        this.catalog = catalog;
    }
}
```

SpEL also enables the use of more complex data structures:

译：SpEL也能使用更多的复杂数据结构：

```java
@Component
public class MovieRecommender {

    private final Map<String, Integer> countOfMoviesPerCatalog;

    public MovieRecommender(
            @Value("#{{'Thriller': 100, 'Comedy': 300}}") Map<String, Integer> countOfMoviesPerCatalog) {
        this.countOfMoviesPerCatalog = countOfMoviesPerCatalog;
    }
}
```

#### 1.9.9. Using `@PostConstruct` and `@PreDestroy`

The `CommonAnnotationBeanPostProcessor` not only recognizes the `@Resource` annotation but also the JSR-250 lifecycle annotations: `javax.annotation.PostConstruct` and `javax.annotation.PreDestroy`. Introduced in Spring 2.5, the support for these annotations offers an alternative to the lifecycle callback mechanism described in [initialization callbacks](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-lifecycle-initializingbean) and [destruction callbacks](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-lifecycle-disposablebean). Provided that the `CommonAnnotationBeanPostProcessor` is registered within the Spring `ApplicationContext`, a method carrying one of these annotations is invoked at the same point in the lifecycle as the corresponding Spring lifecycle interface method or explicitly declared callback method. In the following example, the cache is pre-populated upon initialization and cleared upon destruction:

译：`CommonAnnotationBeanPostProcessor` 不仅识别@Resource注解，而且识别JSR-250生命周期注解：`javax.annotation.postConstruct`和`javax.annotation.PreDestroy`。在Spring2.5引入，这些注解提供了一种代替方法，在初始化回调和销毁回调中描述的生命周期回调机制。`CommonAnnotationBeanPostProcessor` 提供在Spring `ApplicationContext`内部的注册，***在Spring生命周期的相同点调用有这些注解之一的方法*，**。如下例子，在初始化时，缓存被填充，在销毁时，被清除。

```java
public class CachingMovieLister {

    @PostConstruct
    public void populateMovieCache() {
        // populates the movie cache upon initialization...
    }

    @PreDestroy
    public void clearMovieCache() {
        // clears the movie cache upon destruction...
    }
}
```

For details about the effects of combining various lifecycle mechanisms, see [Combining Lifecycle Mechanisms](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-lifecycle-combined-effects).

译：有关各种生命周期机制的组合，请看组合生命周期机制。

------

  Like `@Resource`, the `@PostConstruct` and `@PreDestroy` annotation types were a part of the standard Java libraries from JDK 6 to 8. However, the entire `javax.annotation` package got separated from the core Java modules in JDK 9 and eventually removed in JDK 11. If needed, the `javax.annotation-api` artifact needs to be obtained via Maven Central now, simply to be added to the application’s classpath like any other library.

译：像`@Resource`，`@PostConstruct`和`@PreDestroy`注解类型也是从JDK6到8标准类库中的一部分。但是，整个javax.annotation包与JDK9中的核心模块分离，并在jdk11中删除。如果有必要，javax.annotation-api部件需要通过maven获得，像其他类库一样添加到类路径。

------

### 1.10. Classpath Scanning and Managed Components

译：类路径扫描和组件管理

Most examples in this chapter use XML to specify the configuration metadata that produces each `BeanDefinition` within the Spring container. The previous section ([Annotation-based Container Configuration](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-annotation-config)) demonstrates how to provide a lot of the configuration metadata through source-level annotations. Even in those examples, however, the “base” bean definitions are explicitly defined in the XML file, while the annotations drive only the dependency injection. This section describes an option for implicitly detecting the candidate components by scanning the classpath. Candidate components are classes that match against a filter criteria and have a corresponding bean definition registered with the container. This removes the need to use XML to perform bean registration. Instead, you can use annotations (for example, `@Component`), AspectJ type expressions, or your own custom filter criteria to select which classes have bean definitions registered with the container.

译：在这个章节的大多数例子使用xml声明配置元数据，这些数据产生Spring容器内的每个`BeanDefinition`.前面部分（基于注解的容器配置）论证如何通过源码级别注解提供许多配置元数据.即使在这些例子，基础bean显示定义在xml文件，而注解驱动依赖注入。这部分描述通过扫描类路径隐式监测候选组件。候选组件是匹配过滤条件并且在容器中有相应bean注册的类。这就不需要使用xml执行bean注册。而是，你可以使用注解（例如@Component），AspectJ类型表达式，或者你自定义过滤器选择容器中的注册bean.

------

Starting with Spring 3.0, many features provided by the Spring JavaConfig project are part of the core Spring Framework. This allows you to define beans using Java rather than using the traditional XML files. Take a look at the `@Configuration`, `@Bean`, `@Import`, and `@DependsOn` annotations for examples of how to use these new features.

译：从Spring3.0开始，通过Spring javaConfig项目提供许多功能加入到Spring框架的核心部分。允许你使用java定义bean而不是使用传统的xml文件。查看`@Configuration`、@`Bean`、`@Import`和`@DependsOn`注解例子，看如何使用这些新功能。

------

#### 1.10.1. `@Component` and Further Stereotype Annotations

译：@Component和进一步的原型注解。

The `@Repository` annotation is a marker for any class that fulfills the role or stereotype of a repository (also known as Data Access Object or DAO). Among the uses of this marker is the automatic translation of exceptions, as described in [Exception Translation](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/data-access.html#orm-exception-translation).

译：`@Repository`注解标记在实现存储库的角色或者原型任何类上（也称数据访问对象或者DAO）。这个标记的使用自动转换异常，如异常转换中所述。

Spring provides further stereotype annotations: `@Component`, `@Service`, and `@Controller`. `@Component` is a generic stereotype for any Spring-managed component. `@Repository`, `@Service`, and `@Controller` are specializations of `@Component` for more specific use cases (in the persistence, service, and presentation layers, respectively). Therefore, you can annotate your component classes with `@Component`, but, by annotating them with `@Repository`, `@Service`, or `@Controller` instead, your classes are more properly suited for processing by tools or associating with aspects. For example, these stereotype annotations make ideal targets for pointcuts. `@Repository`, `@Service`, and `@Controller` can also carry additional semantics in future releases of the Spring Framework. Thus, if you are choosing between using `@Component` or `@Service` for your service layer, `@Service` is clearly the better choice. Similarly, as stated earlier, `@Repository` is already supported as a marker for automatic exception translation in your persistence layer.

例：Spring提供进一步的原型注解：`@Component`,`@Service`和`@Controller`;`@Component`是Spring管理组件的一个通用原型注解。`@Respository`,`@Service`和`@Controller`是@Component的更多具体使用场景的专业化（出现在持久层服务层和表现层）。因此，你能使用`@Component`注入组件类,但是，通过使用@Respository，@Service或者@Controller，你的类更适合通过工具类处理或者与切面关联。例如，这些原型注解是切入点的理想目标。@Repository、@Service和@Controller，在Spring框架的未来版本会携带额外的语义。因此，如果你在@Component或者@Service之间选择服务层，@Service显然是更好的选择。类似地，如前面所述，@Respository已经支持作为持久层自动异常转换的标记。

#### 1.10.2. Using Meta-annotations and Composed Annotations

译：使用元注解和组合注解

Many of the annotations provided by Spring can be used as meta-annotations in your own code. A meta-annotation is an annotation that can be applied to another annotation. For example, the `@Service` annotation mentioned [earlier](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-stereotype-annotations) is meta-annotated with `@Component`, as the following example shows:

译：在自己的代码中，通过Spring提供的许多注解可以作为原注解使用。一个元注解可以在另一个注解上使用。例如，@Service注解是使用@Component的元注解，如下所述：

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component 
public @interface Service {

    // ...
}
```

You can also combine meta-annotations to create “composed annotations”. For example, the `@RestController` annotation from Spring MVC is composed of `@Controller` and `@ResponseBody`.

译：你也可以组合元注解创建“组合注解”。例如，Spring MVC中的@`RestController`注解由@Controller和@ResponseBody组成。

In addition, composed annotations can optionally redeclare attributes from meta-annotations to allow customization. This can be particularly useful when you want to only expose a subset of the meta-annotation’s attributes. For example, Spring’s `@SessionScope` annotation hardcodes the scope name to `session` but still allows customization of the `proxyMode`. The following listing shows the definition of the `SessionScope` annotation:

译：另外，组合注解可以有选择地重新定义元注解中允许定制的属性。当你只想暴露元数据注解属性一个子集，这非常有用。例如，Spring的`@SessionScope`注解硬编码作用域名字为session,但是任然允许自定义`proxyMode`.

如下清单显示SessionScope定义的注解：

```java
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Scope(WebApplicationContext.SCOPE_SESSION)
public @interface SessionScope {

    /**
     * Alias for {@link Scope#proxyMode}.
     * <p>Defaults to {@link ScopedProxyMode#TARGET_CLASS}.
     */
    @AliasFor(annotation = Scope.class)
    ScopedProxyMode proxyMode() default ScopedProxyMode.TARGET_CLASS;

}
```

You can then use `@SessionScope` without declaring the `proxyMode` as follows:

译：然后，你也可以使用没有定义ProxyMode的@SessionScope，如下所示。

```java
@Service
@SessionScope
public class SessionScopedService {
    // ...
}
```

You can also override the value for the `proxyMode`, as the following example shows:

译：你可以覆盖proxyMode的值，如下所示：

```java
@Service
@SessionScope(proxyMode = ScopedProxyMode.INTERFACES)
public class SessionScopedUserService implements UserService {
    // ...
}
```

For further details, see the [Spring Annotation Programming Model](https://github.com/spring-projects/spring-framework/wiki/Spring-Annotation-Programming-Model) wiki page.

译：更多详细信息，请看Spring注解编程模型wiki页。

#### 1.10.3. Automatically Detecting Classes and Registering Bean Definitions

译：自动检测类和注册bean

Spring can automatically detect stereotyped classes and register corresponding `BeanDefinition` instances with the `ApplicationContext`. For example, the following two classes are eligible for such autodetection:

译：Spring可以使用`ApplicationContext`自动检测原型类，并且注册相应的`BeanDefinition`实例.例如，如下两个类符合自动检测：

```java
@Service
public class SimpleMovieLister {

    private MovieFinder movieFinder;

    public SimpleMovieLister(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }
}
```

```java
@Repository
public class JpaMovieFinder implements MovieFinder {
    // implementation elided for clarity
}
```

To autodetect these classes and register the corresponding beans, you need to add `@ComponentScan` to your `@Configuration` class, where the `basePackages` attribute is a common parent package for the two classes. (Alternatively, you can specify a comma- or semicolon- or space-separated list that includes the parent package of each class.)

译：自动检测这些类并且注册相应的bean,你需要增加`@ComponentScan`到你的`@Configuration`类，`@ComponentScan`上的basePackages属性是这两个类的父包(或者，你可以指定逗号、分号、空格列表，包含每个类的父包)。

```java
@Configuration
@ComponentScan(basePackages = "org.example")
public class AppConfig  {
    // ...
}
```

------

For brevity, the preceding example could have used the `value` attribute of the annotation (that is, `@ComponentScan("org.example")`).

为了简单起见，前面的例子用到了注解的`value`属性（即@ComponentScan("org.example")）。

------

The following alternative uses XML:

译：如下用xml替换：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        https://www.springframework.org/schema/context/spring-context.xsd">

    <context:component-scan base-package="org.example"/>

</beans>
```

------

  The use of `<context:component-scan/> `implicitly enables the functionality of  `<context:annotation-config/>`. There is usually no need to include the  `<context:annotation-config/>`element when using `<context:component-scan/> `.

译：使用`<context:component-scan/> `隐式使用`<context:annotation-config/>`功能。当使用`<context:component-scan/> `时，通常不需要包含`<context:annotation-config/>`标签。

------

------

The scanning of classpath packages requires the presence of corresponding directory entries in the classpath. When you build JARs with Ant, make sure that you do not activate the files-only switch of the JAR task. Also, classpath directories may not be exposed based on security policies in some environments — for example, standalone apps on JDK 1.7.0_45 and higher (which requires 'Trusted-Library' setup in your manifests — see https://stackoverflow.com/questions/19394570/java-jre-7u45-breaks-classloader-getresources).  

译：扫描类路径包名需要出现相应的类路径目录项。当你使用Ant构建JAR时，请确保你没有激活jar任务的只读（ files-only）开关。同样，在某些环境，基于安全策略类路径文件夹没有暴露—比如，在jdk1.7.0_45和更高上的单应用APP（需要在Manifest中设置’Trusted-Library‘,请看……）。

On JDK 9’s module path (Jigsaw), Spring’s classpath scanning generally works as expected. However, make sure that your component classes are exported in your `module-info` descriptors. If you expect Spring to invoke non-public members of your classes, make sure that they are 'opened' (that is, that they use an `opens` declaration instead of an `exports` declaration in your `module-info` descriptor).

译：在jdk9的模块路劲上 (Jigsaw)，Spring的类路径扫描通常如期工作。但是，请确保你的组件类导出到`module-info`描述符。如果你期望Spring调用非公共的成员变量，请确保他们时“opened”(即，他们在module-info描述符中使用`opens`声明，而不是exports声明)

------

Furthermore, the `AutowiredAnnotationBeanPostProcessor` and `CommonAnnotationBeanPostProcessor` are both implicitly included when you use the component-scan element. That means that the two components are autodetected and wired together — all without any bean configuration metadata provided in XML.

译：此外，当你使用组件扫描元素时，`AutowiredAnnotationBeanPostProcessor` 和`CommonAnnotationBeanPostProcessor` 都是隐式包含。这也以为这，这两个组件时自动检测和注入的-都没有在xml中提供任何bean配置元数据。

You can disable the registration of `AutowiredAnnotationBeanPostProcessor` and `CommonAnnotationBeanPostProcessor` by including the `annotation-config` attribute with a value of `false`.

译：你可以通过包含annotation-config属性为false,可以禁用`AutowiredAnnotationBeanPostProcessor` 和`CommonAnnotationBeanPostProcessor`。

#### 1.10.4. Using Filters to Customize Scanning

译：使用过滤去个性化扫描

By default, classes annotated with `@Component`, `@Repository`, `@Service`, `@Controller`, `@Configuration`, or a custom annotation that itself is annotated with `@Component` are the only detected candidate components. However, you can modify and extend this behavior by applying custom filters. Add them as `includeFilters` or `excludeFilters` attributes of the `@ComponentScan` annotation (or as `<context:include-filter/>` or  `<context:include-filter/>`child elements of the `<context:include-scan/>` element in XML configuration). Each filter element requires the `type` and `expression` attributes. The following table describes the filtering options:

译：默认情况下，使用`@Component`，`@Repository`,`@Service`，`@Controller`，`@Configuration`或者本身使用`@Component`的自定义注解的类时检测到的候选项组件。但是你可以通过使用自定义过滤器修改或者扩展这个行为。增加他们作为@ComponentScan注解的includeFilters或者excludeFilters属性（或者在xml配置中作为 `<context:include-scan/>`的`<context:include-filter/>` 或者`<context:include-filter/>` 孩子标签）。每个过滤标签需要`type`或者`expression`属性，如下表格描述过滤选项：

| Filter Type          | Example Expression           | Description                                                  |
| -------------------- | ---------------------------- | ------------------------------------------------------------ |
| annotation (default) | `org.example.SomeAnnotation` | An annotation to be *present* or *meta-present* at the type level in target components. |
| assignable           | `org.example.SomeClass`      | A class (or interface) that the target components are assignable to (extend or implement). |
| aspectj              | `org.example..*Service+`     | An AspectJ type expression to be matched by the target components. |
| regex                | `org\.example\.Default.*`    | A regex expression to be matched by the target components' class names. |
| custom               |                              |                                                              |

The following example shows the configuration ignoring all `@Repository` annotations and using “stub” repositories instead:

译：如下例子展示忽略@`Repository`注解的配置，并使用“stub”repositories:

```java
@Configuration
@ComponentScan(basePackages = "org.example",
        includeFilters = @Filter(type = FilterType.REGEX, pattern = ".*Stub.*Repository"),
        excludeFilters = @Filter(Repository.class))
public class AppConfig {
    ...
}
```

The following listing shows the equivalent XML:

译：如下列表展示的xml相通：

```xml
<beans>
    <context:component-scan base-package="org.example">
        <context:include-filter type="regex"
                expression=".*Stub.*Repository"/>
        <context:exclude-filter type="annotation"
                expression="org.springframework.stereotype.Repository"/>
    </context:component-scan>
</beans>
```

------

  You can also disable the default filters by setting `useDefaultFilters=false` on the annotation or by providing `use-default-filters="false"` as an attribute of the `<component-scan/>` element. This effectively disables automatic detection of classes annotated or meta-annotated with `@Component`, `@Repository`, `@Service`, `@Controller`, `@RestController`, or `@Configuration`.

译：你也可以通过在注解上设置`useDefaultFilters=false`或者在<component-scan/>标签提供`use-default-filters="false"`，禁用默认过滤。这对使用`@Component`、`@Repository`, `@Service`, `@Controller`, `@RestController`,或者`@Configuration`的类注解或者元注解，也会禁用。

------

#### 1.10.5. Defining Bean Metadata within Components

译：在组件内定义bean元数据

Spring components can also contribute bean definition metadata to the container. You can do this with the same `@Bean` annotation used to define bean metadata within `@Configuration` annotated classes. The following example shows how to do so:

译：Spring组件也可以贡献bean定义元数据给容器。你可以使用在@Configuraion注解类中使用@Bean注解定义bean元数据。如下所示：

```java
@Component
public class FactoryMethodComponent {

    @Bean
    @Qualifier("public")
    public TestBean publicInstance() {
        return new TestBean("publicInstance");
    }

    public void doWork() {
        // Component method implementation omitted
    }
}
```

The preceding class is a Spring component that has application-specific code in its `doWork()` method. However, it also contributes a bean definition that has a factory method referring to the method `publicInstance()`. The `@Bean` annotation identifies the factory method and other bean definition properties, such as a qualifier value through the `@Qualifier` annotation. Other method-level annotations that can be specified are `@Scope`, `@Lazy`, and custom qualifier annotations.

译：前面的类是一个Spring组件，它的doWork()方法有特定应用的代码。但是，它也贡献bean定义，它有一个工厂方法引用`publicInstance()`.@Bean注解识别工厂方法和其他bean定义属性，例如通过@Qualifier注解作为一个限定符值。其他方法级别的注解可以可以使@Scope,@Lazy和自定义限定符注解。

------

  In addition to its role for component initialization, you can also place the `@Lazy` annotation on injection points marked with `@Autowired` or `@Inject`. In this context, it leads to the injection of a lazy-resolution proxy.

译：除了组件初始化角色，你也可以在注入点标记有@Autowired或者@Injectd的地方放@Lazy注解。在这个上下文，它会导致注入懒加载解析代理。

------

Autowired fields and methods are supported, as previously discussed, with additional support for autowiring of `@Bean` methods. The following example shows how to do so:

译：支持自动注入字段和方法，比如前面讨论的，@Bean方法也支持自动注入。

------

```java
@Component
public class FactoryMethodComponent {

    private static int i;

    @Bean
    @Qualifier("public")
    public TestBean publicInstance() {
        return new TestBean("publicInstance");
    }

    // use of a custom qualifier and autowiring of method parameters
    @Bean
    protected TestBean protectedInstance(
            @Qualifier("public") TestBean spouse,
            @Value("#{privateInstance.age}") String country) {
        TestBean tb = new TestBean("protectedInstance", 1);
        tb.setSpouse(spouse);
        tb.setCountry(country);
        return tb;
    }

    @Bean
    private TestBean privateInstance() {
        return new TestBean("privateInstance", i++);
    }

    @Bean
    @RequestScope
    public TestBean requestScopedInstance() {
        return new TestBean("requestScopedInstance", 3);
    }
}
```

The example autowires the `String` method parameter `country` to the value of the `age` property on another bean named `privateInstance`. A Spring Expression Language element defines the value of the property through the notation `#{  }`. For `@Value` annotations, an expression resolver is preconfigured to look for bean names when resolving expression text.

译：自动注入String方法参数`country`，用另一个叫做privateInstance的`age`属性设为值的例子。Spring表达式语言标签通过`#{}`定义属性的值。对于@Value注解，当解析表达式文本的时候，表达式解析器为查找的bean名称预先配置。

As of Spring Framework 4.3, you may also declare a factory method parameter of type `InjectionPoint` (or its more specific subclass: `DependencyDescriptor`) to access the requesting injection point that triggers the creation of the current bean. Note that this applies only to the actual creation of bean instances, not to the injection of existing instances. As a consequence, this feature makes most sense for beans of prototype scope. For other scopes, the factory method only ever sees the injection point that triggered the creation of a new bean instance in the given scope (for example, the dependency that triggered the creation of a lazy singleton bean). You can use the provided injection point metadata with semantic care in such scenarios. The following example shows how to do use `InjectionPoint`:

译：从Spring框架4.3开始，你也可以定义`InjectionPoint`类的工厂方法参数（或者更多声明的子类：`DependencyDescriptor`），访问请求注入点触发创建当前bean.请记住这只应用在实际创建的bean实例,不会注入已经存在的实例。因此，这个功能使用在`Prototype`作用域的bean场景。对于其他作用域，工厂方法只看注入点给定的作用域创建新的实例（例如，触发创建一个懒加载的单例bean依赖）。在这个场景中，你可以使用提供的注入点元语义数据。如下例子展示如何使用`InjectionPoint`:

```java
@Component
public class FactoryMethodComponent {

    @Bean @Scope("prototype")
    public TestBean prototypeInstance(InjectionPoint injectionPoint) {
        return new TestBean("prototypeInstance for " + injectionPoint.getMember());
    }
}
```

The `@Bean` methods in a regular Spring component are processed differently than their counterparts inside a Spring `@Configuration` class. The difference is that `@Component` classes are not enhanced with CGLIB to intercept the invocation of methods and fields. CGLIB proxying is the means by which invoking methods or fields within `@Bean` methods in `@Configuration` classes creates bean metadata references to collaborating objects. Such methods are not invoked with normal Java semantics but rather go through the container in order to provide the usual lifecycle management and proxying of Spring beans, even when referring to other beans through programmatic calls to `@Bean` methods. In contrast, invoking a method or field in a `@Bean` method within a plain `@Component` class has standard Java semantics, with no special CGLIB processing or other constraints applying.

译：常规Spring组件的@Bean方法处理方式与在Spring @Configuration类不同。不同的是@Component类不能使用CGLIB拦截方法和字段的增量。CGLIB代理调用@Configuration类的@Bean方法中的方法和属性创建对协作对象的bean元数据引用。这个方法不是用普通java语义调用的，而是通过容器提供SpringBean的声明周期管理和代理，甚至通过编程方式调用@Bean方法引用其他bean.相反，在普通的@Component类中调用@Bean中的方法或者字段有标准的java语义，不用特殊的CGLIB处理或者其他的约束。

------

You may declare `@Bean` methods as `static`, allowing for them to be called without creating their containing configuration class as an instance. This makes particular sense when defining post-processor beans (for example, of type `BeanFactoryPostProcessor` or `BeanPostProcessor`), since such beans get initialized early in the container lifecycle and should avoid triggering other parts of the configuration at that point.

译：你可以声明@Bean方法为`static`,从而允许不将其包含配置类创建为实例的情况下调用。当定义后置处理器时，这一点特别有意义，因此，这个bean在容器生命周期的早起得到初始化，并且应该避免触发其他的配置部分。

Calls to static `@Bean` methods never get intercepted by the container, not even within `@Configuration` classes (as described earlier in this section), due to technical limitations: CGLIB subclassing can override only non-static methods. As a consequence, a direct call to another `@Bean` method has standard Java semantics, resulting in an independent instance being returned straight from the factory method itself.

译：由于技术限制，对静态@Bean方法的调用永远不会通过容器拦截，甚至在@Configuration类中也不会拦截。

因此，对另一个bean的调用有标准的java语义，导致依赖的实例直接从工厂方法返回。

The Java language visibility of `@Bean` methods does not have an immediate impact on the resulting bean definition in Spring’s container. You can freely declare your factory methods as you see fit in non-`@Configuration` classes and also for static methods anywhere. However, regular `@Bean` methods in `@Configuration` classes need to be overridable — that is, they must not be declared as `private` or `final`.

译：@Bean方法的java语言可见性对Spring容器中产生的bean没有直接影响。你可以在非@Configuration类中自由声明工厂方法，也可以在static方法中声明。但是在@Configuration类中常规的@Bean方法需要可覆盖-即，他们不能以private或者final声明。

`@Bean` methods are also discovered on base classes of a given component or configuration class, as well as on Java 8 default methods declared in interfaces implemented by the component or configuration class. This allows for a lot of flexibility in composing complex configuration arrangements, with even multiple inheritance being possible through Java 8 default methods as of Spring 4.2.

译：@Bean方法也可以在给定的组件或者配置类的基类发现，也可以在通过组件或者配置类的实现接口的java8默认方法声明中发现。这允许在组装复杂配置安排时有更多的灵活性，从Spring4.2开始，通过java8的默认方法实现多重继承。

Finally, a single class may hold multiple `@Bean` methods for the same bean, as an arrangement of multiple factory methods to use depending on available dependencies at runtime. This is the same algorithm as for choosing the “greediest” constructor or factory method in other configuration scenarios: The variant with the largest number of satisfiable dependencies is picked at construction time, analogous to how the container selects between multiple `@Autowired` constructors.

译：最后，一个类对同一个bean持有多个@Bean方法，在运行时，取决于可用依赖安排多个工厂方法使用。这与在其他配置场景中选择最贪婪的构造器或者工厂方法的算法相同：在构建时，选择满足最大数量的变量依赖项，类似于容器如何在多个@Autowired构造器中选择。

------

#### 1.10.6. Naming Autodetected Components

译：名称自动检测组件

When a component is autodetected as part of the scanning process, its bean name is generated by the `BeanNameGenerator` strategy known to that scanner. By default, any Spring stereotype annotation (`@Component`, `@Repository`, `@Service`, and `@Controller`) that contains a name `value` thereby provides that name to the corresponding bean definition.

译：当一个组件作为扫描处理的一部分自动检测时，bean通过BeanNameGenaratoe策略知道的扫描器生成名称。默认情况下，任何包含一个名称`value`的Spring原型注解(`@Component`, `@Repository`, `@Service`, 和`@Controller`)，提供名称给相应的bean定义。

If such an annotation contains no name `value` or for any other detected component (such as those discovered by custom filters), the default bean name generator returns the uncapitalized non-qualified class name. For example, if the following component classes were detected, the names would be `myMovieLister` and `movieFinderImpl`:

译：如果这个注解不包含名称`value`或者任何其他检测到的组件（比如这些通过自定义过滤器发现的组件）。默认bean名称生成器返回没有限定符的类名称。例如，如果检测到如下组件类，名称将是myMovieLister和movieFinderImpl:

```java
@Service("myMovieLister")
public class SimpleMovieLister {
    // ...
}
```

```java
@Repository
public class MovieFinderImpl implements MovieFinder {
    // ...
}
```

If you do not want to rely on the default bean-naming strategy, you can provide a custom bean-naming strategy. First, implement the [`BeanNameGenerator`](https://docs.spring.io/spring-framework/docs/5.2.5.RELEASE/javadoc-api/org/springframework/beans/factory/support/BeanNameGenerator.html) interface, and be sure to include a default no-arg constructor. Then, provide the fully qualified class name when configuring the scanner, as the following example annotation and bean definition show.

译：如果你不想依赖默认的bean命名策略，你可以提供自定义的bean命名策略。首先实现BeanNameGenerator接口，确保包含一个默认无参构造器。然后当配置扫描器时，提供全限定类名称。如下所示注解和bean定义：

```java
@Configuration
@ComponentScan(basePackages = "org.example", nameGenerator = MyNameGenerator.class)
public class AppConfig {
    // ...
}
```

------

If you run into naming conflicts due to multiple autodetected components having the same non-qualified class name (i.e., classes with identical names but residing in different packages), you may need to configure a `BeanNameGenerator` that defaults to the fully qualified class name for the generated bean name. As of Spring Framework 5.2.3, the `FullyQualifiedAnnotationBeanNameGenerator` located in package `org.springframework.context.annotation` can be used for such purposes.

译：如果由于多个自动监测组件有相同的非限定类名（即有完全相同名称但是卫浴不同包的类），而遇到命名冲突，你可能需要配置一个默认的完全限定类名`BeanNameGenerator`生成bean名称。从Spring框架5.2.3开始，位于`org.springframework.context.annotation`包下的`FullyQualifiedAnnotationBeanNameGenerator` 用于此目的。

------

```java
@Configuration
@ComponentScan(basePackages = "org.example", nameGenerator = MyNameGenerator.class)
public class AppConfig {
    // ...
}
```

```xml
<beans>
    <context:component-scan base-package="org.example"
        name-generator="org.example.MyNameGenerator" />
</beans>
```

As a general rule, consider specifying the name with the annotation **whenever** other components may be making explicit references to it. On the other hand, the auto-generated names are adequate **whenever** the container is responsible for wiring.

译：作为一般规则，当其他组件使用明确的引用时，请考虑使用注解声明名称。另一方面，当容器负责注入时，自动生成名称已经足够了。

#### 1.10.7. Providing a Scope for Autodetected Components

译：为自动检测的组件提供作用域

As with Spring-managed components in general, the default and most common scope for autodetected components is `singleton`. However, sometimes you need a different scope that can be specified by the `@Scope` annotation. You can provide the name of the scope within the annotation, as the following example shows:

译：一般来说，作为Spring管理的组件，自动监测组件的默认和普遍作用域为`Singleton`。但是，有时你需要不同的作用域，你可以通过@Scope注解声明。你可以在注解内提供作用域的名字，如下所示：

```java
@Scope("prototype")
@Repository
public class MovieFinderImpl implements MovieFinder {
    // ...
}
```

------

`@Scope` annotations are only introspected on the concrete bean class (for annotated components) or the factory method (for `@Bean` methods). In contrast to XML bean definitions, there is no notion of bean definition inheritance, and inheritance hierarchies at the class level are irrelevant for metadata purposes.

译：`@Scope`注解只在具体类或者工厂方法上有效。与xml bean 定义相比，没有bean继承的概念，并且类级别的继承层级与元数据无关。

------

For details on web-specific scopes such as “request” or “session” in a Spring context, see [Request, Session, Application, and WebSocket Scopes](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-scopes-other). As with the pre-built annotations for those scopes, you may also compose your own scoping annotations by using Spring’s meta-annotation approach: for example, a custom annotation meta-annotated with `@Scope("prototype")`, possibly also declaring a custom scoped-proxy mode.

译：关于在Spirng上下文具体的web作用域，如request或者session,请看request、Session、Application和webSocket作用域章节。与这些作用域的前置内置注解一样，你也可以通过使用Spring的元注解方法组装自己的作用域注解：例如，使用`@Scopde("prototype")`自定义注解元注解，还可以定义自定义代理作用域模型。

------

  To provide a custom strategy for scope resolution rather than relying on the annotation-based approach, you can implement the [`ScopeMetadataResolver`](https://docs.spring.io/spring-framework/docs/5.2.5.RELEASE/javadoc-api/org/springframework/context/annotation/ScopeMetadataResolver.html) interface. Be sure to include a default no-arg constructor. Then you can provide the fully qualified class name when configuring the scanner, as the following example of both an annotation and a bean definition shows:

译：为作用域解析而不是依赖的基于注解的方法提供自定义策略，你可以实现`ScopeMetadataResolver`接口。确保包含默认的无参构造器，然后当配置扫描器时，你可以提供全限定类名，如下两个例子所示：

```java
@Configuration
@ComponentScan(basePackages = "org.example", scopeResolver = MyScopeResolver.class)
public class AppConfig {
    // ...
}
```

```xml
<beans>
    <context:component-scan base-package="org.example" scope-resolver="org.example.MyScopeResolver"/>
</beans>
```

When using certain non-singleton scopes, it may be necessary to generate proxies for the scoped objects. The reasoning is described in [Scoped Beans as Dependencies](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-factory-scopes-other-injection). For this purpose, a scoped-proxy attribute is available on the component-scan element. The three possible values are: `no`, `interfaces`, and `targetClass`. For example, the following configuration results in standard JDK dynamic proxies:

译：当使用某些非单例的作用域时，它需要为作用域对象生成代理。理由在Scoded Beans as Depentencies描述。为了这个目的，在组件扫描标签上的一个代理作用域属性是可用的。这三个可能值是：`no`、`interface`和`targetClass`.例如，如下配置使用标准的JDK动态代理：

```java
@Configuration
@ComponentScan(basePackages = "org.example", scopedProxy = ScopedProxyMode.INTERFACES)
public class AppConfig {
    // ...
}
```

```xml
<beans>
    <context:component-scan base-package="org.example" scoped-proxy="interfaces"/>
</beans>
```

#### 1.10.8. Providing Qualifier Metadata with Annotations

译：为注解提供限定符元数据

The `@Qualifier` annotation is discussed in [Fine-tuning Annotation-based Autowiring with Qualifiers](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-autowired-annotation-qualifiers). The examples in that section demonstrate the use of the `@Qualifier` annotation and custom qualifier annotations to provide fine-grained control when you resolve autowire candidates. Because those examples were based on XML bean definitions, the qualifier metadata was provided on the candidate bean definitions by using the `qualifier` or `meta` child elements of the `bean` element in the XML. When relying upon classpath scanning for auto-detection of components, you can provide the qualifier metadata with type-level annotations on the candidate class. The following three examples demonstrate this technique:

译：在`Fine-tuning Annocatation-based Autowiring with Qualifiers`章节讨论过@Qulifier注解。在这部分的例子论证当解析自动注入候选项时，使用@Qualifier注解和自定义qualifier注解提供更细粒度的控制。因为这些例子基于xml定义，限定符元数据通过在xml中bean标签的`qulifier`或者`meta`子标签，提供候选bean.当依赖类扫描自动检测组件时，你可以在候选项类上使用类级别注解提供限定符元数据。如下三个例子论证这个技术：

```java
@Component
@Qualifier("Action")
public class ActionMovieCatalog implements MovieCatalog {
    // ...
}
```

```java
@Component
@Genre("Action")
public class ActionMovieCatalog implements MovieCatalog {
    // ...
}
```

```java
@Component
@Offline
public class CachingMovieCatalog implements MovieCatalog {
    // ...
}
```

As with most annotation-based alternatives, keep in mind that the annotation metadata is bound to the class definition itself, while the use of XML allows for multiple beans of the same type to provide variations in their qualifier metadata, because that metadata is provided per-instance rather than per-class.

译：与大多数基于注解的备选方案一样，请记住注解元数据绑定到类本身，而xml使用允许相同类型的多个bean在其限定符元数据中提供变量，因为每个实例提供元数据，而不是每个类。

#### 1.10.9. Generating an Index of Candidate Components

译：生成候选项组件索引

While classpath scanning is very fast, it is possible to improve the startup performance of large applications by creating a static list of candidates at compilation time. In this mode, all modules that are target of component scan must use this mechanism.

译：虽然路径扫描非常快时，但是在编译时可以通过创建一个静态清单提高大型应用的启动性能。在这个模型中，作为组件扫描目标的所有模块的必须使用这个机制：

------

  Your existing `@ComponentScan` or **<context:component-scan** directives must stay as is to request the context to scan candidates in certain packages. When the `ApplicationContext` detects such an index, it automatically uses it rather than scanning the classpath.

译：存在的@ComponentScan或者 `<context:component-scan/>`指令必须保持，与请求上下文扫描某些跑中的候选项一样。当`ApplicationContext`监测到这个索引，它自动使用它而不是扫描类：

------

To generate the index, add an additional dependency to each module that contains components that are targets for component scan directives. The following example shows how to do so with Maven:

译：为了生成索引，每个扫描指令的目标模块需要增加额外的依赖，如下例子展示如何使用maven实现：

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context-indexer</artifactId>
        <version>5.2.5.RELEASE</version>
        <optional>true</optional>
    </dependency>
</dependencies>
```

With Gradle 4.5 and earlier, the dependency should be declared in the `compileOnly` configuration, as shown in the following example:

译：使用Gradle4.5和更早的版本，应该在compileOnly配置中声明依赖项，如下所示：

```groovy
dependencies {
    compileOnly "org.springframework:spring-context-indexer:5.2.5.RELEASE"
}
```

With Gradle 4.6 and later, the dependency should be declared in the `annotationProcessor` configuration, as shown in the following example:

译：使用Gradle4.6或者之后的版本，应该在anntationProcessor配置中声明依赖项，如下所示：

```groovy
dependencies {
    annotationProcessor "org.springframework:spring-context-indexer:{spring-version}"
}
```

That process generates a `META-INF/spring.components` file that is included in the jar file.

译：这个过程生成一个在jar文件中的`META-INF/spring.components`文件。

------

  When working with this mode in your IDE, the `spring-context-indexer` must be registered as an annotation processor to make sure the index is up-to-date when candidate components are updated.

译：当在IDE中使用这个模块工作时，`spring-context-indexer` 必须以注解处理器注册，当候选组件更新时，确保索引是最新的。

  The index is enabled automatically when a `META-INF/spring.components` is found on the classpath. If an index is partially available for some libraries (or use cases) but could not be built for the whole application, you can fallback to a regular classpath arrangement (as though no index was present at all) by setting `spring.index.ignore` to `true`, either as a system property or in a `spring.properties` file at the root of the classpath.

译：当在类路径发现`META-INF/spring.components`索引能够自动时，索引能供自动启动。如果对某些库部分可用，但是不能为整个库构建，你可以通过设置`spring.index.ignore`为`true`，退回到一般的类路径安排（就像没有出现索引一样）。

### 1.11. Using JSR 330 Standard Annotations

译：使用JSR330标准化注解。

Starting with Spring 3.0, Spring offers support for JSR-330 standard annotations (Dependency Injection). Those annotations are scanned in the same way as the Spring annotations. To use them, you need to have the relevant jars in your classpath.

译：从Spring3.0开始，Spring为JSR-330标准注释提供支持（依赖注入）。这些注解的扫描方式与Spring注解相同。要想使用他们，需要在类路径中有行管的jar.

------

If you use Maven, the `javax.inject` artifact is available in the standard Maven repository ( https://repo1.maven.org/maven2/javax/inject/javax.inject/1/). You can add the following dependency to your file pom.xml:

译：如果你使用Maven，在标准的Maven仓库中`javax.inject`坐标是可用的。你可以增加如下依赖到Pom.xml文件：

```xml
<dependency>
    <groupId>javax.inject</groupId>
    <artifactId>javax.inject</artifactId>
    <version>1</version>
</dependency>
```

#### 1.11.1. Dependency Injection with `@Inject` and `@Named`

译：使用`@Inject`和`@Named`进行依赖注入

Instead of `@Autowired`, you can use `@javax.inject.Inject` as follows:

译：你可以使用`@javax.inject.Inject`代替`@Autowired`，如下：

```java
import javax.inject.Inject;

public class SimpleMovieLister {

    private MovieFinder movieFinder;

    @Inject
    public void setMovieFinder(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }

    public void listMovies() {
        this.movieFinder.findMovies(...);
        // ...
    }
}
```

As with `@Autowired`, you can use `@Inject` at the field level, method level and constructor-argument level. Furthermore, you may declare your injection point as a `Provider`, allowing for on-demand access to beans of shorter scopes or lazy access to other beans through a `Provider.get()` call. The following example offers a variant of the preceding example:

译：与使用`@Autowired`一样，你可以在字段级别、方法级别和构造参数级别使用`@Inject`。此外，你可以声明注入点为`Provider`，允许按需访问作用域较短的bean或者通过`Provider.get()`调用延迟访问其他bean,如下例子提供前面例子的变体：

```java
import javax.inject.Inject;
import javax.inject.Provider;

public class SimpleMovieLister {

    private Provider<MovieFinder> movieFinder;

    @Inject
    public void setMovieFinder(Provider<MovieFinder> movieFinder) {
        this.movieFinder = movieFinder;
    }

    public void listMovies() {
        this.movieFinder.get().findMovies(...);
        // ...
    }
}
```

If you would like to use a qualified name for the dependency that should be injected, you should use the `@Named` annotation, as the following example shows:

译：如果你喜欢对应该注入的依赖使用限定名，你应该使用@Named注解，如下所示：

```java
import javax.inject.Inject;
import javax.inject.Named;

public class SimpleMovieLister {

    private MovieFinder movieFinder;

    @Inject
    public void setMovieFinder(@Named("main") MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }

    // ...
}
```

As with `@Autowired`, `@Inject` can also be used with `java.util.Optional` or `@Nullable`. This is even more applicable here, since `@Inject` does not have a `required` attribute. The following pair of examples show how to use `@Inject` and `@Nullable`:

译：与使用@Autowired，@Inject一样，你可以使用`java.util.Option`或者`@Nullable`。这在这里更有用，因为@Inject没有`required`属性.如下两个例子显示如何使用@Inject和@Nullable:

```java
public class SimpleMovieLister {

    @Inject
    public void setMovieFinder(Optional<MovieFinder> movieFinder) {
        // ...
    }
}
```

```java
public class SimpleMovieLister {

    @Inject
    public void setMovieFinder(@Nullable MovieFinder movieFinder) {
        // ...
    }
}
```

#### 1.11.2. `@Named` and `@ManagedBean`: Standard Equivalents to the `@Component` Annotation

译：@Named和@ManagedBean：与@Component注解标准相同

Instead of `@Component`, you can use `@javax.inject.Named` or `javax.annotation.ManagedBean`, as the following example shows:

译：你可以使用@javax.injet.Named或者javax.anntation.ManagedBean代替@Component，如下所示：

```java
import javax.inject.Inject;
import javax.inject.Named;

@Named("movieListener")  // @ManagedBean("movieListener") could be used as well
public class SimpleMovieLister {

    private MovieFinder movieFinder;

    @Inject
    public void setMovieFinder(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }

    // ...
}
```

It is very common to use `@Component` without specifying a name for the component. `@Named` can be used in a similar fashion, as the following example shows:

译：不声明组件名称使用@Component是非常常见的。@Name可以以相似的方式使用，如下所示：

```java
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class SimpleMovieLister {

    private MovieFinder movieFinder;

    @Inject
    public void setMovieFinder(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }

    // ...
}
```

When you use `@Named` or `@ManagedBean`, you can use component scanning in the exact same way as when you use Spring annotations, as the following example shows:

译：当你使用@Named或者@ManagedBean,你可以使用与Spring注解相同的方式进行组件扫描，如下所示。

```java
@Configuration
@ComponentScan(basePackages = "org.example")
public class AppConfig  {
    // ...
}
```

------

More ActionsIn contrast to `@Component`, the JSR-330 `@Named` and the JSR-250 `ManagedBean` annotations are not composable. You should use Spring’s stereotype model for building custom component annotations.

译：与@Component比，JSR-330@Named和JSR-250@ManagedBean注解是不能组合的。你应该使用Spring的原型模型为构建自定义组件注解。

------

#### 1.11.3. Limitations of JSR-330 Standard Annotations

译：JSR-330标准注解的局限性

When you work with standard annotations, you should know that some significant features are not available, as the following table shows:

译：当你使用标准注释工作时，你应该知道一些重要功能是不可用的，如下所示：

| Spring              | javax.inject.*        | javax.inject restrictions / comments                         |
| ------------------- | --------------------- | ------------------------------------------------------------ |
| @Autowired          | @Inject               | `@Inject` has no 'required' attribute. Can be used with Java 8’s `Optional` instead. |
| @Component          | @Named / @ManagedBean | JSR-330 does not provide a composable model, only a way to identify named components. |
| @Scope("singleton") | @Singleton            | The JSR-330 default scope is like Spring’s `prototype`. However, in order to keep it consistent with Spring’s general defaults, a JSR-330 bean declared in the Spring container is a `singleton` by default. In order to use a scope other than `singleton`, you should use Spring’s `@Scope` annotation. `javax.inject` also provides a [@Scope](https://download.oracle.com/javaee/6/api/javax/inject/Scope.html) annotation. Nevertheless, this one is only intended to be used for creating your own annotations. |
| @Qualifier          | @Qualifier / @Named   | `javax.inject.Qualifier` is just a meta-annotation for building custom qualifiers. Concrete `String` qualifiers (like Spring’s `@Qualifier` with a value) can be associated through `javax.inject.Named`. |
| @Value              | -                     | no equivalent                                                |
| @Required           | -                     | no equivalent                                                |
| @Lazy               | -                     | no equivalent                                                |
| ObjectFactory       | Provider              | `javax.inject.Provider` is a direct alternative to Spring’s `ObjectFactory`, only with a shorter `get()` method name. It can also be used in combination with Spring’s `@Autowired` or with non-annotated constructors and setter methods. |

### 1.12. Java-based Container Configuration

译：基于java的容器配置

This section covers how to use annotations in your Java code to configure the Spring container. It includes the following topics:

- [Basic Concepts: `@Bean` and `@Configuration`](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-java-basic-concepts)
- [Instantiating the Spring Container by Using `AnnotationConfigApplicationContext`](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-java-instantiating-container)
- [Using the `@Bean` Annotation](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-java-bean-annotation)
- [Using the `@Configuration` annotation](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-java-configuration-annotation)
- [Composing Java-based Configurations](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-java-composing-configuration-classes)
- [Bean Definition Profiles](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-definition-profiles)
- [`PropertySource` Abstraction](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-property-source-abstraction)
- [Using `@PropertySource`](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-using-propertysource)
- [Placeholder Resolution in Statements](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-placeholder-resolution-in-statements)

译：这部分概述如何用java代码配置Spring容器。它包含如下主题：

- 基本概念：@Bean和@Configuration
- 通过使用`AnntationConfigApplicationContext`实例化Spring容器。
- 使用@Bean注解
- 使用@Configuration注解
- 组合基于java的Configurations
- Bean定义Profiles
- PropertySource抽象
- 使用@PropertySource
- 语句中的占位符解析

#### 1.12.1. Basic Concepts: `@Bean` and `@Configuration`

译：基本概念：@Bean和@Configuration

The central artifacts in Spring’s new Java-configuration support are `@Configuration`-annotated classes and `@Bean`-annotated methods.

译：Spring的新java配置中的核心构建是@Configuration注解类和@Bean注解方法。

The `@Bean` annotation is used to indicate that a method instantiates, configures, and initializes a new object to be managed by the Spring IoC container. For those familiar with Spring’s `<bean/>` XML configuration, the `@Bean` annotation plays the same role as the `<bean/>` element. You can use `@Bean`-annotated methods with any Spring `@Component`. However, they are most often used with `@Configuration` beans.

译：@Bean注解常用于表示方法实例，配置和初始化一个通过Spring IoC管理的新对象。对于熟悉Spring的`<bean/>`xml配置的这些人来说，@Bean注解扮演者和`<bean/>`标签先沟通的角色。你可以在任何Spring的@Component类中使用@Bean注解方法。但是，他们经常和@Configuration bean使用。

Annotating a class with `@Configuration` indicates that its primary purpose is as a source of bean definitions. Furthermore, `@Configuration` classes let inter-bean dependencies be defined by calling other `@Bean` methods in the same class. The simplest possible `@Configuration` class reads as follows:

译：使用@Configuration注解一个类表示它的唯一目的是作为bean的源。此外，@Configuration类允许在同一个类调用其他的@Bean方法，来定义bean之间的依赖关系。最简单的@Configuration类如下：

```java
@Configuration
public class AppConfig {

    @Bean
    public MyService myService() {
        return new MyServiceImpl();
    }
}
```

The preceding `AppConfig` class is equivalent to the following Spring `<bean/>` XML:

译:前面的`Appconfig`类与下面的`<bean/>`xml相同：

```xml
<beans>
    <bean id="myService" class="com.acme.services.MyServiceImpl"/>
</beans>
```

------

**Full @Configuration vs “lite” @Bean mode?**

When `@Bean` methods are declared within classes that are not annotated with `@Configuration`, they are referred to as being processed in a “lite” mode. Bean methods declared in a `@Component` or even in a plain old class are considered to be “lite”, with a different primary purpose of the containing class and a `@Bean` method being a sort of bonus there. For example, service components may expose management views to the container through an additional `@Bean` method on each applicable component class. In such scenarios, `@Bean` methods are a general-purpose factory method mechanism.

译：当在一个没有使用@Configuration注解的类中声明了@Bean方法，他们被称为“lite”模式处理。定义在@Component注解或者在一个普通的旧类中声明的bean方法被视为“lite”,包含类和@Bean方法的主要目的不同，这是一种好处。例如，服务组件可以通过每个应用组件类的额外@Bean方法暴露管理视图给容器。在这种场合，@Bean方法是一种通用工厂方法机制。

Unlike full `@Configuration`, lite `@Bean` methods cannot declare inter-bean dependencies. Instead, they operate on their containing component’s internal state and, optionally, on arguments that they may declare. Such a `@Bean` method should therefore not invoke other `@Bean` methods. Each such method is literally only a factory method for a particular bean reference, without any special runtime semantics. The positive side-effect here is that no CGLIB subclassing has to be applied at runtime, so there are no limitations in terms of class design (that is, the containing class may be `final` and so forth).

译：不像`full`@Configuration,lite @Bean方法不能声明交叉bean的依赖关系。相反，他们操作包换组件的内部状态，有选择地他们声明的参数进行操作。每个这样的方法实际上只是特定bean引用的工厂方法，没有任何特殊的运行时语义。这里的积极副作用是在运行时没有使用CGLIB子类，因此在类设计时没有限制（也就是说，包含的类时final等）。

In common scenarios, `@Bean` methods are to be declared within `@Configuration` classes, ensuring that “full” mode is always used and that cross-method references therefore get redirected to the container’s lifecycle management. This prevents the same `@Bean` method from accidentally being invoked through a regular Java call, which helps to reduce subtle bugs that can be hard to track down when operating in “lite” mode.

译：在常见场景中，@Bean方法在@Configuration类中声明，确保始终使用“full”模式，并且绕过方法引用，因此将跨方法引用重定向到容器的声明周期管理。这可以防止常规的java调用意外调用同一个@Bean方法，这有助于减少在“lite”模式下操作时难以跟踪的细微错误。

------

The `@Bean` and `@Configuration` annotations are discussed in depth in the following sections. First, however, we cover the various ways of creating a spring container using by Java-based configuration.

译：@Bean和@Configuration注解在接下来的下来的部分会进行更深层次的讨论。但是，首先我们介绍通过基于java配置创建Spring容器的各种方法：

#### 1.12.2. Instantiating the Spring Container by Using `AnnotationConfigApplicationContext`-（5.2.6）

译：通过使用`AnnotationConfigApplicationContext`实例化spring容器

The following sections document Spring’s `AnnotationConfigApplicationContext`, introduced in Spring 3.0. This versatile `ApplicationContext` implementation is capable of accepting not only `@Configuration` classes as input but also plain `@Component` classes and classes annotated with JSR-330 metadata.

versatile ：adj.多才多艺的；多用途的；多功能的；

译：接下来的各节记录从Spring3.0引入的，Spring的`AnnotationConfigApplicationContext`,这个多功能的`ApplicationContext`实现不仅可以接受`@Configuration`类作为输入，而且可以接受@Component类和使用JSR-330注解元数据的类。

When `@Configuration` classes are provided as input, the `@Configuration` class itself is registered as a bean definition and all declared `@Bean` methods within the class are also registered as bean definitions.

译：当提供`@Configuration`类作为输入，@Configuration类本身作为一个bean注册并且在类中声明的@Bean方法也作为bean注册。

When `@Component` and JSR-330 classes are provided, they are registered as bean definitions, and it is assumed that DI metadata such as `@Autowired` or `@Inject` are used within those classes where necessary.

译：当提供@Component和JSR-330类时，他们作为bean注册，并且在必要时假设使用@Autowired或者@Inject DI元数据。

##### Simple Construction

译：简单构造

In much the same way that Spring XML files are used as input when instantiating a `ClassPathXmlApplicationContext`, you can use `@Configuration` classes as input when instantiating an `AnnotationConfigApplicationContext`. This allows for completely XML-free usage of the Spring container, as the following example shows:

译：与使用xml文件实例化ClassPathXmlApplicatonContext时的方式相同，当实例化AntationConfigApplicationContext时，你可以使用@Configuration类作为输入。这允许完全不适用xml的Spring容器，如下所示：

```java
public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
    MyService myService = ctx.getBean(MyService.class);
    myService.doStuff();
}
```

As mentioned earlier, `AnnotationConfigApplicationContext` is not limited to working only with `@Configuration` classes. Any `@Component` or JSR-330 annotated class may be supplied as input to the constructor, as the following example shows:

译：如前面提到的，AnnotationConfigApplicationContext不局限于使用@Conciguration类。任何@component或者JSR-330注解类也支持作为构造器的输入，如下所示：

```java
public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(MyServiceImpl.class, Dependency1.class, Dependency2.class);
    MyService myService = ctx.getBean(MyService.class);
    myService.doStuff();
}
```

The preceding example assumes that `MyServiceImpl`, `Dependency1`, and `Dependency2` use Spring dependency injection annotations such as `@Autowired`.

译：前面的例子假设`MyserviceImpl`、`Dependency`或`Dependency2`使用依赖注入注解@Autowired.

##### Building the Container Programmatically by Using `register(Class…)`

译：通过使用`register(Class...)`编程方式构建容器

You can instantiate an `AnnotationConfigApplicationContext` by using a no-arg constructor and then configure it by using the `register()` method. This approach is particularly useful when programmatically building an `AnnotationConfigApplicationContext`. The following example shows how to do so:

译：你可以通过使用无参构造器实例化AnnotationConfigApplicationContext，然后通过使用register()方法配置。当以编程方式构建一个`AnnotationConfigApplicationContext`,这种方式特别有用。如下所示如何实现：

```java
public static void main(String[] args) {
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
    ctx.register(AppConfig.class, OtherConfig.class);
    ctx.register(AdditionalConfig.class);
    ctx.refresh();
    MyService myService = ctx.getBean(MyService.class);
    myService.doStuff();
}
```

##### Enabling Component Scanning with `scan(String…)`

以：使用`scan(Stirng)`开启组件扫描

To enable component scanning, you can annotate your `@Configuration` class as follows:

译：为了实现组件扫描，你可以按如下注解@Configuration类

```java
@Configuration
@ComponentScan(basePackages = "com.acme") 
public class AppConfig  {
    ...
}
```

------

Experienced Spring users may be familiar with the XML declaration equivalent from Spring’s `context:` namespace, shown in the following example:

译：有经验的Spring用户非常熟悉，可以从Spring的context命名空间使用XML定义，如下所示：

```xml
<beans>
    <context:component-scan base-package="com.acme"/>
</beans>
```

------

In the preceding example, the `com.acme` package is scanned to look for any `@Component`-annotated classes, and those classes are registered as Spring bean definitions within the container. `AnnotationConfigApplicationContext` exposes the `scan(String…)` method to allow for the same component-scanning functionality, as the following example shows:

译：在前面的例子中，扫描`com.acme`包下的所有@Component注解类和在容器内作为spring bean注册的类。`AnnotationConfigApplicationContext`暴露`scan(String…)`方法允许相同的组件扫描功能，如下例子所示：

```java
public static void main(String[] args) {
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
    ctx.scan("com.acme");
    ctx.refresh();
    MyService myService = ctx.getBean(MyService.class);
}
```

------

Remember that `@Configuration` classes are [meta-annotated](https://docs.spring.io/spring/docs/5.2.6.RELEASE/spring-framework-reference/core.html#beans-meta-annotations) with `@Component`, so they are candidates for component-scanning. In the preceding example, assuming that `AppConfig` is declared within the `com.acme` package (or any package underneath), it is picked up during the call to `scan()`. Upon `refresh()`, all its `@Bean` methods are processed and registered as bean definitions within the container.

译:请记住@Coniguration类使用@Component的元注解，因此他们是扫描组件的候选项。在前面的例子中，假设`AppConfig`在`com.acme`包声明（或者在这个包下面），在调用`scan()`时获取。`refresh()`时，所有的@Bean方法被处理并且作为bean注册到容器。

------

##### Support for Web Applications with `AnnotationConfigWebApplicationContext`

译：使用`AnntationConfigWebApplicationContex`支持web应用

A `WebApplicationContext` variant of `AnnotationConfigApplicationContext` is available with `AnnotationConfigWebApplicationContext`. You can use this implementation when configuring the Spring `ContextLoaderListener` servlet listener, Spring MVC `DispatcherServlet`, and so forth. The following `web.xml` snippet configures a typical Spring MVC web application (note the use of the `contextClass` context-param and init-param):

译：`AnnotationConfigApplicationContext`的`webApplicationContext`可用于`AnnotationConfigWebApplicationContext`。 当配置Spring `ContextLoaderListener`servlet监听器，Spring MVC `DispacherServlet`等时，你可以使用这个实现。如下`web.xml`片段配置通用的Spring MVC 应用（请记住`contextClass` context-param和init-param的使用）

```xml
<web-app>
    <!-- Configure ContextLoaderListener to use AnnotationConfigWebApplicationContext
        instead of the default XmlWebApplicationContext -->
    <context-param>
        <param-name>contextClass</param-name>
        <param-value>
            org.springframework.web.context.support.AnnotationConfigWebApplicationContext
        </param-value>
    </context-param>

    <!-- Configuration locations must consist of one or more comma- or space-delimited
        fully-qualified @Configuration classes. Fully-qualified packages may also be
        specified for component-scanning -->
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>com.acme.AppConfig</param-value>
    </context-param>

    <!-- Bootstrap the root application context as usual using ContextLoaderListener -->
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>

    <!-- Declare a Spring MVC DispatcherServlet as usual -->
    <servlet>
        <servlet-name>dispatcher</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <!-- Configure DispatcherServlet to use AnnotationConfigWebApplicationContext
            instead of the default XmlWebApplicationContext -->
        <init-param>
            <param-name>contextClass</param-name>
            <param-value>
                org.springframework.web.context.support.AnnotationConfigWebApplicationContext
            </param-value>
        </init-param>
        <!-- Again, config locations must consist of one or more comma- or space-delimited
            and fully-qualified @Configuration classes -->
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>com.acme.web.MvcConfig</param-value>
        </init-param>
    </servlet>

    <!-- map all requests for /app/* to the dispatcher servlet -->
    <servlet-mapping>
        <servlet-name>dispatcher</servlet-name>
        <url-pattern>/app/*</url-pattern>
    </servlet-mapping>
</web-app>
```

#### 1.12.3. Using the `@Bean` Annotation

译：使用@Bean注解

`@Bean` is a method-level annotation and a direct analog of the XML `<bean/>` element. The annotation supports some of the attributes offered by `<bean/>`, such as: * [init-method](https://docs.spring.io/spring/docs/5.2.6.RELEASE/spring-framework-reference/core.html#beans-factory-lifecycle-initializingbean) * [destroy-method](https://docs.spring.io/spring/docs/5.2.6.RELEASE/spring-framework-reference/core.html#beans-factory-lifecycle-disposablebean) * [autowiring](https://docs.spring.io/spring/docs/5.2.6.RELEASE/spring-framework-reference/core.html#beans-factory-autowire) * `name`.

译：@Bean是一个方法级别的注解是XML`<bean/>`标签的直接模拟。这个注解支持通过<bean/>提供的许多属性，比如：初始化方法，销毁方法、自动注入的name.

You can use the `@Bean` annotation in a `@Configuration`-annotated or in a `@Component`-annotated class.

译：你可以在@Configuration或者@Component注解类中使用@Bean注解。

##### Declaring a Bean

To declare a bean, you can annotate a method with the `@Bean` annotation. You use this method to register a bean definition within an `ApplicationContext` of the type specified as the method’s return value. By default, the bean name is the same as the method name. The following example shows a `@Bean` method declaration:

译：声明一个bean，你可以使用@Bean注解注解到一个方法上。你使用这个方法注册一个bean到`ApplicationContext`,这个方法返回值为指定类型。默认情况下，bean名称和方法的名称相同。如下所示@Bean方法声明：

```java
@Configuration
public class AppConfig {

    @Bean
    public TransferServiceImpl transferService() {
        return new TransferServiceImpl();
    }
}
```

The preceding configuration is exactly equivalent to the following Spring XML:

译：前面的配置和下面的Spring xml是完全一样的。

```xml
<beans>
    <bean id="transferService" class="com.acme.TransferServiceImpl"/>
</beans>
```

Both declarations make a bean named `transferService` available in the `ApplicationContext`, bound to an object instance of type `TransferServiceImpl`, as the following text image shows:

译：他们的声明都在`ApplicationContext`上声明了一个叫做`transferService`的名称，绑定到类型`TransferServiceImpl`对象实例。如下文本快照显示：

```
transferService -> com.acme.TransferServiceImpl
```

You can also declare your `@Bean` method with an interface (or base class) return type, as the following example shows:

译：你也可以声明@Bean在返回类型为接口的方法（或者基本类型），如下例子所示：

```java
@Configuration
public class AppConfig {

    @Bean
    public TransferService transferService() {
        return new TransferServiceImpl();
    }
}
```

However, this limits the visibility for advance type prediction to the specified interface type (`TransferService`). Then, with the full type (`TransferServiceImpl`) known to the container only once, the affected singleton bean has been instantiated. Non-lazy singleton beans get instantiated according to their declaration order, so you may see different type matching results depending on when another component tries to match by a non-declared type (such as `@Autowired TransferServiceImpl`, which resolves only once the `transferService` bean has been instantiated).

例：但是，这将高级类型的可见性预测为指定接口类型（`TransferService`）.然后，容器使用完全类型（TransferServcieImpl）只知道一次，受影响的单例bean已经实例化。根据他们的声明顺序非懒加载单例bean得到实例化,因此你可以看到不同类型的匹配结果依赖当另一个组件尝试通过非声明类型匹配（例如@Autowired TransferServiceImpl,它只解析一次已经初始化的transferService）。

------

If you consistently refer to your types by a declared service interface, your `@Bean` return types may safely join that design decision. However, for components that implement several interfaces or for components potentially referred to by their implementation type, it is safer to declare the most specific return type possible (at least as specific as required by the injection points that refer to your bean).

译：如果你通过声明的服务接口一致地引用类型，@Bean可以安全地加入设计决策。但是，对于实现多个接口的组件或者通过实现类型潜在引用的组件，最好全的做法是声明是声明可能的返回类型（至少与通过注入点引用的bean的需要一样进行声明）

------

##### Bean Dependencies

译：bean依赖

A `@Bean`-annotated method can have an arbitrary number of parameters that describe the dependencies required to build that bean. For instance, if our `TransferService` requires an `AccountRepository`, we can materialize that dependency with a method parameter, as the following example shows:

译：@Bean注解的方法可以有任意数量参数描述需要构建bean 的依赖。实际上，如果我们的`TransferService` 需要一个`AccountRepository`，可以使用方法参数具体化依赖，如下所示：

```java
@Configuration
public class AppConfig {

    @Bean
    public TransferService transferService(AccountRepository accountRepository) {
        return new TransferServiceImpl(accountRepository);
    }
}
```

The resolution mechanism is pretty much identical to constructor-based dependency injection. See [the relevant section](https://docs.spring.io/spring/docs/5.2.6.RELEASE/spring-framework-reference/core.html#beans-constructor-injection) for more details.

译：这个解析机制与基于构造器依赖注入非常相似。具体请看相关章节。

##### Receiving Lifecycle Callbacks

译：接收声明周期回调

Any classes defined with the `@Bean` annotation support the regular lifecycle callbacks and can use the `@PostConstruct` and `@PreDestroy` annotations from JSR-250. See [JSR-250 annotations](https://docs.spring.io/spring/docs/5.2.6.RELEASE/spring-framework-reference/core.html#beans-postconstruct-and-predestroy-annotations) for further details.

使用@Bean注解的任何类支持常规的生命周期回调，并且可以使用JSR-250的@PostConstruct和@PreDestroy注解。详情请看JSR-250注解。

The regular Spring [lifecycle](https://docs.spring.io/spring/docs/5.2.6.RELEASE/spring-framework-reference/core.html#beans-factory-nature) callbacks are fully supported as well. If a bean implements `InitializingBean`, `DisposableBean`, or `Lifecycle`, their respective methods are called by the container.

译：也完全支持常规的Spring生命周期回调。如果bean实现了InitializingBean，DisposableBean或者lifecycle接口，容器调用他们的每个方法。

The standard set of `*Aware` interfaces (such as [BeanFactoryAware](https://docs.spring.io/spring/docs/5.2.6.RELEASE/spring-framework-reference/core.html#beans-beanfactory), [BeanNameAware](https://docs.spring.io/spring/docs/5.2.6.RELEASE/spring-framework-reference/core.html#beans-factory-aware), [MessageSourceAware](https://docs.spring.io/spring/docs/5.2.6.RELEASE/spring-framework-reference/core.html#context-functionality-messagesource), [ApplicationContextAware](https://docs.spring.io/spring/docs/5.2.6.RELEASE/spring-framework-reference/core.html#beans-factory-aware), and so on) are also fully supported.

译：同样完全支持标准的Aware接口集（比如`BeanFactoryAware`,`BeanNameAware`,`BeanNameAware`,`messageSourceAware`,`ApplicationContextAware`等）

The `@Bean` annotation supports specifying arbitrary initialization and destruction callback methods, much like Spring XML’s `init-method` and `destroy-method` attributes on the `bean` element, as the following example shows:

译：@Bean注解支持生命任何初始化和销毁回调方法，非常类似Spring XML bean标签上的的init-method和destroy-method属性，如下所示：

```java
public class BeanOne {

    public void init() {
        // initialization logic
    }
}

public class BeanTwo {

    public void cleanup() {
        // destruction logic
    }
}

@Configuration
public class AppConfig {

    @Bean(initMethod = "init")
    public BeanOne beanOne() {
        return new BeanOne();
    }

    @Bean(destroyMethod = "cleanup")
    public BeanTwo beanTwo() {
        return new BeanTwo();
    }
}
```

------

  By default, beans defined with Java configuration that have a public `close` or `shutdown` method are automatically enlisted with a destruction callback. If you have a public `close` or `shutdown` method and you do not wish for it to be called when the container shuts down, you can add `@Bean(destroyMethod="")` to your bean definition to disable the default `(inferred)` mode.  

译：默认情况下，使用java配置定义的bean有一个公共的`close`或者`shutdown`方法自动参与方法销毁回调。如果你有公共的`close`或者`shutsdown`方法，当容器关闭的时候，你不希望调用它，你可以添加@Bean(destoryMethod="")到bean，来禁用默认的（inferred）模型。

You may want to do that by default for a resource that you acquire with JNDI, as its lifecycle is managed outside the application. In particular, make sure to always do it for a `DataSource`, as it is known to be problematic on Java EE application servers.  

译：你需要使用JNDI获得资源，可能默认希望这么做，因为它的生命周期在应用之外管理。特别是，确保始终对`DataSource`这么做，因为在JAVA EE应用服务上它是有问题的。

The following example shows how to prevent an automatic destruction callback for a `DataSource`:

译：如下例子展示如何阻止DataSource自动销毁回调。

```java
@Bean(destroyMethod="")
public DataSource dataSource() throws NamingException {
    return (DataSource) jndiTemplate.lookup("MyDS");
}
```

Also, with `@Bean` methods, you typically use programmatic JNDI lookups, either by using Spring’s `JndiTemplate` or `JndiLocatorDelegate` helpers or straight JNDI `InitialContext` usage but not the `JndiObjectFactoryBean` variant (which would force you to declare the return type as the `FactoryBean` type instead of the actual target type, making it harder to use for cross-reference calls in other `@Bean` methods that intend to refer to the provided resource here).

译：另外，对于@Bean方法，你通常使用编程方式JNDI查找，可以使用Spring的JndiTemplate或者JndiLocatorDelegate帮助程序，也可以直接使用JNDI InitialContext，但不能使用JndiObjectFactoryBean变量（这将强制你声明返回类型为`FactoryBean`类型，而不是实际的目标类型，使得使用交叉引用调用更困难——用另一个打算引用提供资源的@Bean方法）。

------

In the case of `BeanOne` from the example above the preceding note, it would be equally valid to call the `init()` method directly during construction, as the following example shows:

译：在前面记录的例子BeanOne情形，在构造起见将直接调用init()方法，如下所示：

```java
@Configuration
public class AppConfig {

    @Bean
    public BeanOne beanOne() {
        BeanOne beanOne = new BeanOne();
        beanOne.init();
        return beanOne;
    }

    // ...
}
```

------

  When you work directly in Java, you can do anything you like with your objects and do not always need to rely on the container lifecycle.

译：当你直接在java中工作，你可以使用对象做任何想做的事，不需要始终依赖容器的声明周期。

------

##### Specifying Bean Scope

译：声明bean作用域

Spring includes the `@Scope` annotation so that you can specify the scope of a bean.

译：Spring包含@Scope注解以便于你可以声明bean的作用域：

###### Using the `@Scope` Annotation

译：使用@Scope注解

You can specify that your beans defined with the `@Bean` annotation should have a specific scope. You can use any of the standard scopes specified in the [Bean Scopes](https://docs.spring.io/spring/docs/5.2.6.RELEASE/spring-framework-reference/core.html#beans-factory-scopes) section.

译：你可以使用@Bean注解声明bean有一个特定的作用域。你可以使用在Bean Scope章节的任何的标准作用域。

The default scope is `singleton`, but you can override this with the `@Scope` annotation, as the following example shows:

译：默认作用域是`singleton`，但是你可以使用@Scope注解覆盖，如下所示：

```java
@Configuration
public class MyConfiguration {

    @Bean
    @Scope("prototype")
    public Encryptor encryptor() {
        // ...
    }
}
```

###### `@Scope` and `scoped-proxy`

Spring offers a convenient way of working with scoped dependencies through [scoped proxies](https://docs.spring.io/spring/docs/5.2.6.RELEASE/spring-framework-reference/core.html#beans-factory-scopes-other-injection). The easiest way to create such a proxy when using the XML configuration is the `<aop:scoped-proxy/>` element. Configuring your beans in Java with a `@Scope` annotation offers equivalent support with the `proxyMode` attribute. The default is no proxy (`ScopedProxyMode.NO`), but you can specify `ScopedProxyMode.TARGET_CLASS` or `ScopedProxyMode.INTERFACES`.

译：Spring提供了一种通过作用域代理的处理作用域依赖的便捷方式。当使用XML配置时，最简单的方式是创建`<aop:scoped-proxy/>`标签。在java中使用@Scope配置bean，使用`proxyMode`属性提供相同的支持。默认不用代理（ScopedProxyMode.NO），但是你可以指定ScopedProxyMode.TARGET_CLASS或者ScopedProxyMode.INTERFACES.

If you port the scoped proxy example from the XML reference documentation (see [scoped proxies](https://docs.spring.io/spring/docs/5.2.6.RELEASE/spring-framework-reference/core.html#beans-factory-scopes-other-injection)) to our `@Bean` using Java, it resembles the following:

译：如果你使用java作为作用域代理的入口从XML参考文档（请看作用域代理）到@Bean.它假设如下：

```java
// an HTTP Session-scoped bean exposed as a proxy
@Bean
@SessionScope
public UserPreferences userPreferences() {
    return new UserPreferences();
}

@Bean
public Service userService() {
    UserService service = new SimpleUserService();
    // a reference to the proxied userPreferences bean
    service.setUserPreferences(userPreferences());
    return service;
}
```

##### Customizing Bean Naming

译：自定义bean名称

By default, configuration classes use a `@Bean` method’s name as the name of the resulting bean. This functionality can be overridden, however, with the `name` attribute, as the following example shows:

译:默认情况下，配置类使用@Bean方法的名字作为结果bean的名字。这个功能可以覆盖，但是，要使用name属性，如下所示：

```java
@Configuration
public class AppConfig {

    @Bean(name = "myThing")
    public Thing thing() {
        return new Thing();
    }
}
```

##### Bean Aliasing

译：bean别名

As discussed in [Naming Beans](https://docs.spring.io/spring/docs/5.2.6.RELEASE/spring-framework-reference/core.html#beans-beanname), it is sometimes desirable to give a single bean multiple names, otherwise known as bean aliasing. The `name` attribute of the `@Bean` annotation accepts a String array for this purpose. The following example shows how to set a number of aliases for a bean:

译：如在Naming Beans中讨论过的，有时候希望给一个bean取多个名字，或者称为bean别名。正因为这个目的，@Bean注解的name属性接受一个字符串数组。如下例子展示如何为bean设置许多别名：

```java
@Configuration
public class AppConfig {

    @Bean({"dataSource", "subsystemA-dataSource", "subsystemB-dataSource"})
    public DataSource dataSource() {
        // instantiate, configure and return DataSource bean...
    }
}
```

##### Bean Description

译：bean描述

Sometimes, it is helpful to provide a more detailed textual description of a bean. This can be particularly useful when beans are exposed (perhaps through JMX) for monitoring purposes.

译：有时候，提供更详细的bean文本描述是很由帮助的。当bean作为监控目的暴露时，这特别有用。

To add a description to a `@Bean`, you can use the [`@Description`](https://docs.spring.io/spring-framework/docs/5.2.6.RELEASE/javadoc-api/org/springframework/context/annotation/Description.html) annotation, as the following example shows:

为了给@Bean增加描述信息，你可以使用@Description注解，如下所示：

```java
@Configuration
public class AppConfig {

    @Bean
    @Description("Provides a basic example of a bean")
    public Thing thing() {
        return new Thing();
    }
}
```

#### 1.12.4. Using the `@Configuration` annotation

译：使用@Configuration注解

`@Configuration` is a class-level annotation indicating that an object is a source of bean definitions. `@Configuration` classes declare beans through public `@Bean` annotated methods. Calls to `@Bean` methods on `@Configuration` classes can also be used to define inter-bean dependencies. See [Basic Concepts: `@Bean` and `@Configuration`](https://docs.spring.io/spring/docs/5.2.6.RELEASE/spring-framework-reference/core.html#beans-java-basic-concepts) for a general introduction.

译：@Configuration时一个类级别注解，表示一个bean对象源。@Configuration类通过@Bean注解方法声明bean。调用在Configuration类中的@Bean方法也可以用于定义交叉bean依赖。请看Basic Concepts:@Bean and @Configuration的通用介绍。

##### Injecting Inter-bean Dependencies

译：注入交叉bean依赖

When beans have dependencies on one another, expressing that dependency is as simple as having one bean method call another, as the following example shows:

译：当bean互相有依赖时,表示这种方法就像一个bean方法调用另一个bean简单，如下所示：

```java
@Configuration
public class AppConfig {

    @Bean
    public BeanOne beanOne() {
        return new BeanOne(beanTwo());
    }

    @Bean
    public BeanTwo beanTwo() {
        return new BeanTwo();
    }
}
```

In the preceding example, `beanOne` receives a reference to `beanTwo` through constructor injection.

译：在前面的例子中，beanOne通过构造器注入接收beanTwo的引用。

------

This method of declaring inter-bean dependencies works only when the `@Bean` method is declared within a `@Configuration` class. You cannot declare inter-bean dependencies by using plain `@Component` classes.

译：当在@Configuration声明bean方法时，这种声明bean之间依赖的方法才能工作。你不能通过普通的@Component类定义bean之间的依赖。

##### Lookup Method Injection

译：查找方法注入：

As noted earlier, [lookup method injection](https://docs.spring.io/spring/docs/5.2.6.RELEASE/spring-framework-reference/core.html#beans-factory-method-injection) is an advanced feature that you should use rarely. It is useful in cases where a singleton-scoped bean has a dependency on a prototype-scoped bean. Using Java for this type of configuration provides a natural means for implementing this pattern. The following example shows how to use lookup method injection:

译：如前面说的，查找方法注入是一个高级特性，你应该很少使用它。在单例作用域依赖于原型作用域bean是很用用的。使用java为这种配置类型提供了一种自然的实现模板。如下所示，如何使用方法查找注入：

```java
public abstract class CommandManager {
    public Object process(Object commandState) {
        // grab a new instance of the appropriate Command interface
        Command command = createCommand();
        // set the state on the (hopefully brand new) Command instance
        command.setState(commandState);
        return command.execute();
    }

    // okay... but where is the implementation of this method?
    protected abstract Command createCommand();
}
```

By using Java configuration, you can create a subclass of `CommandManager` where the abstract `createCommand()` method is overridden in such a way that it looks up a new (prototype) command object. The following example shows how to do so:

译：通过使用java配置，你可以创建一个CommandMange子类，它的抽象方法createCommand()方法，用这个新的（原型）`command`对象覆盖，如下所示，如何使用：

```java
@Bean
@Scope("prototype")
public AsyncCommand asyncCommand() {
    AsyncCommand command = new AsyncCommand();
    // inject dependencies here as required
    return command;
}

@Bean
public CommandManager commandManager() {
    // return new anonymous implementation of CommandManager with createCommand()
    // overridden to return a new prototype Command object
    return new CommandManager() {
        protected Command createCommand() {
            return asyncCommand();
        }
    }
}
```

##### Further Information About How Java-based Configuration Works Internally

译:关于基于java的配置内部如何工作的更多信息

Consider the following example, which shows a `@Bean` annotated method being called twice:

译：考虑如下例子，它展示了@Bean注解方法调用两次：

```java
@Configuration
public class AppConfig {

    @Bean
    public ClientService clientService1() {
        ClientServiceImpl clientService = new ClientServiceImpl();
        clientService.setClientDao(clientDao());
        return clientService;
    }

    @Bean
    public ClientService clientService2() {
        ClientServiceImpl clientService = new ClientServiceImpl();
        clientService.setClientDao(clientDao());
        return clientService;
    }

    @Bean
    public ClientDao clientDao() {
        return new ClientDaoImpl();
    }
}
```

`clientDao()` has been called once in `clientService1()` and once in `clientService2()`. Since this method creates a new instance of `ClientDaoImpl` and returns it, you would normally expect to have two instances (one for each service). That definitely would be problematic: In Spring, instantiated beans have a `singleton` scope by default. This is where the magic comes in: All `@Configuration` classes are subclassed at startup-time with `CGLIB`. In the subclass, the child method checks the container first for any cached (scoped) beans before it calls the parent method and creates a new instance.

译：`clientDao()`在`clicentService1`已经调用一次并且在`clientService2()`又调用了一次。因此这个方法创建了一个新的ClientDaoImpl实例并且返回，你通常希望有两个实例（每个servcie一个）。这肯定是有问题的：在Spring中，默认情况下实例化的bean是singleton作用域的。这就是神奇之处：所有的@Configuration类在启动时使用`CGLIB`子类化。在子类中，在调用父类方法和创建新实例之前，首先，子方法检查容器的所有缓存bean。

------

The behavior could be different according to the scope of your bean. We are talking about singletons here.

译：根据bean的作用域，这个行为是不同的。我们在这谈论singleton：

As of Spring 3.2, it is no longer necessary to add CGLIB to your classpath because CGLIB classes have been repackaged under `org.springframework.cglib` and included directly within the spring-core JAR.

译：从Spring3.2开始，不在需要增加CGLIB到类路径，因为CGLIB类已经在org.springframework.cglib下重新打包并且直接包含在Spring-core jar中。

There are a few restrictions due to the fact that CGLIB dynamically adds features at startup-time. In particular, configuration classes must not be final. However, as of 4.3, any constructors are allowed on configuration classes, including the use of `@Autowired` or a single non-default constructor declaration for default injection.

译：由于CGLIB在启动时动态增加功能，这会有一些限制。特别是，配置类必须不是final.但是，从4.3开始，在配置类中任何构造器允许，包含@Autowired的使用或者单个非默认构造器声明进行默认注入。

If you prefer to avoid any CGLIB-imposed limitations, consider declaring your `@Bean` methods on non-`@Configuration` classes (for example, on plain `@Component` classes instead). Cross-method calls between `@Bean` methods are not then intercepted, so you have to exclusively rely on dependency injection at the constructor or method level there.

译：如果你更喜欢避免任何CGLIB桥架的吸纳会，考虑在非@Configuration类声明@Bean方法（例如，用普通的@Component类替代）。在@Bean方法之间的跨方法调用不会被截取，因此你必须在构造器或者方法级别完成依赖注入。

#### 1.12.5. Composing Java-based Configurations

译：组合基于java的配置

Spring’s Java-based configuration feature lets you compose annotations, which can reduce the complexity of your configuration.

译：Spring的基于java的配置功能，使你组合注解，它能降低配置的复杂度。

##### Using the `@Import` Annotation

Much as the `<import/>` element is used within Spring XML files to aid in modularizing configurations, the `@Import` annotation allows for loading `@Bean` definitions from another configuration class, as the following example shows:

译：如同 `<import/>`标签在Spring xml文件用于帮助模块化配置，`@Import`注解允许加载从另一个配置类中@Bean，如下所示：

```java
@Configuration
public class ConfigA {

    @Bean
    public A a() {
        return new A();
    }
}

@Configuration
@Import(ConfigA.class)
public class ConfigB {

    @Bean
    public B b() {
        return new B();
    }
}
```

Now, rather than needing to specify both `ConfigA.class` and `ConfigB.class` when instantiating the context, only `ConfigB` needs to be supplied explicitly, as the following example shows:

译：现在，当实例化上下文时，不需要同时声明ConfigA.class和ConfigB.class.只有ConfigB需要显示的使用，如下所示：

```java
public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(ConfigB.class);

    // now both beans A and B will be available...
    A a = ctx.getBean(A.class);
    B b = ctx.getBean(B.class);
}
```

This approach simplifies container instantiation, as only one class needs to be dealt with, rather than requiring you to remember a potentially large number of `@Configuration` classes during construction.

译：这个方法，简化了容器实例化，只需要一个类需要处理，而不需要在构造期间，记住隐藏的大量的@Configuration类。

------

As of Spring Framework 4.2, `@Import` also supports references to regular component classes, analogous to the `AnnotationConfigApplicationContext.register` method. This is particularly useful if you want to avoid component scanning, by using a few configuration classes as entry points to explicitly define all your components.

译：从Spring框架4.2开始，@Import也支持引用常规组件类，类似于AnnotationConfigApplicationContext.register方法。如果你想避免组件扫描，这非常有用，通过使用很少的配置类作为入口显示指定所有的组件

------

###### Injecting Dependencies on Imported `@Bean` Definitions

译：对导入的@Bean注入依赖

The preceding example works but is simplistic. In most practical scenarios, beans have dependencies on one another across configuration classes. When using XML, this is not an issue, because no compiler is involved, and you can declare `ref="someBean"` and trust Spring to work it out during container initialization. When using `@Configuration` classes, the Java compiler places constraints on the configuration model, in that references to other beans must be valid Java syntax.

译：前面的例子有效，但是是最简单的。在大多数实际场景，bean在配置类之间相互依赖。当时用XML时，这没有问题，因为不涉及编译器，并且你可以声明`ref="someBean"`,并信任Spring在容器初始化期间解决它。当时用@Configuration类时，java编译器会对配置模型进行约束，对其他bean的引用必须符合java语法。

Fortunately, solving this problem is simple. As [we already discussed](https://docs.spring.io/spring/docs/5.2.6.RELEASE/spring-framework-reference/core.html#beans-java-dependencies), a `@Bean` method can have an arbitrary number of parameters that describe the bean dependencies. Consider the following more real-world scenario with several `@Configuration` classes, each depending on beans declared in the others:

译：幸运地是，解决这个问题很简单。我们已经讨论过，@Bean方法可以有任意数量的参数bean依赖。考虑如下使用多个@Configuration的真实场景：

```java
@Configuration
public class ServiceConfig {

    @Bean
    public TransferService transferService(AccountRepository accountRepository) {
        return new TransferServiceImpl(accountRepository);
    }
}

@Configuration
public class RepositoryConfig {

    @Bean
    public AccountRepository accountRepository(DataSource dataSource) {
        return new JdbcAccountRepository(dataSource);
    }
}

@Configuration
@Import({ServiceConfig.class, RepositoryConfig.class})
public class SystemTestConfig {

    @Bean
    public DataSource dataSource() {
        // return new DataSource
    }
}

public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SystemTestConfig.class);
    // everything wires up across configuration classes...
    TransferService transferService = ctx.getBean(TransferService.class);
    transferService.transfer(100.00, "A123", "C456");
}
```

There is another way to achieve the same result. Remember that `@Configuration` classes are ultimately only another bean in the container: This means that they can take advantage of `@Autowired` and `@Value` injection and other features the same as any other bean.

译：用另一种方法实现相同的结果。请记住@Configuration类最终容器中的另一个Bean:这意味着他们可以使用@Autowired和@Value注入和其他与其他bean相同功能。

------

Make sure that the dependencies you inject that way are of the simplest kind only. `@Configuration` classes are processed quite early during the initialization of the context, and forcing a dependency to be injected this way may lead to unexpected early initialization. Whenever possible, resort to parameter-based injection, as in the preceding example.

译：请确保以这种方式注入的依赖是最简单的。`@Configuration`类在上下文初始化早器执行，并且强制用这种方法强制注入依赖可能导致意外的早器初始化。如果可能，请使用基于参数的注入，如前面的例子。

Also, be particularly careful with `BeanPostProcessor` and `BeanFactoryPostProcessor` definitions through `@Bean`. Those should usually be declared as `static @Bean` methods, not triggering the instantiation of their containing configuration class. Otherwise, `@Autowired` and `@Value` may not work on the configuration class itself, since it is possible to create it as a bean instance earlier than [`AutowiredAnnotationBeanPostProcessor`](https://docs.spring.io/spring-framework/docs/5.2.6.RELEASE/javadoc-api/org/springframework/beans/factory/annotation/AutowiredAnnotationBeanPostProcessor.html).

译：另外，通过@Bean使用BeanPostProcessor和BeanFactoryPostProcessor要特别小心。这些通常声明为static @Bean方法，不能触发他们包含配置类的实例化。因此，@Autowired和@Value不能在配置类本身工作，因为它可能创建早于AutowiredAnnotationBeanPostProcess的bean实例。

------

The following example shows how one bean can be autowired to another bean:

译：如下例子展示一个bean如何自动注入另一个bean:

```java
@Configuration
public class ServiceConfig {

    @Autowired
    private AccountRepository accountRepository;

    @Bean
    public TransferService transferService() {
        return new TransferServiceImpl(accountRepository);
    }
}

@Configuration
public class RepositoryConfig {

    private final DataSource dataSource;

    public RepositoryConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public AccountRepository accountRepository() {
        return new JdbcAccountRepository(dataSource);
    }
}

@Configuration
@Import({ServiceConfig.class, RepositoryConfig.class})
public class SystemTestConfig {

    @Bean
    public DataSource dataSource() {
        // return new DataSource
    }
}

public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SystemTestConfig.class);
    // everything wires up across configuration classes...
    TransferService transferService = ctx.getBean(TransferService.class);
    transferService.transfer(100.00, "A123", "C456");
}
```

------

More ActionsConstructor injection in `@Configuration` classes is only supported as of Spring Framework 4.3. Note also that there is no need to specify `@Autowired` if the target bean defines only one constructor.

译：从Spring框架4.3开始，@Configuration类只只支持更多的ActionsConstructor.请注意如果目标bean只有一个构造器，不需要声明@Autowired.

------

Fully-qualifying imported beans for ease of navigation

译：完全限定导入的bean以便导航

In the preceding scenario, using `@Autowired` works well and provides the desired modularity, but determining exactly where the autowired bean definitions are declared is still somewhat ambiguous. For example, as a developer looking at `ServiceConfig`, how do you know exactly where the `@Autowired AccountRepository` bean is declared? It is not explicit in the code, and this may be just fine. Remember that the [Spring Tools for Eclipse](https://spring.io/tools) provides tooling that can render graphs showing how everything is wired, which may be all you need. Also, your Java IDE can easily find all declarations and uses of the `AccountRepository` type and quickly show you the location of `@Bean` methods that return that type.

译：在前面的场景中，使用`@Autowired`工作的很好并且提供了所需的模块性，但是决定自动注入bean的声明位置任然摸棱两可。比如，作为一个开发者查看`ServiceConfig`，你如何确切地知道`@Autowired` `AccountRepository` bean 声明在何处？它在代码中并不明确，这可能也好。请记住，Spring Tools for Eclipse提供了展示每个自动注入的渲染图，这可能时你需要的全部。领养，你的java IDE可以很容易发现所有的声明和AccountRepository类型的使用并且快速显示 返回类型的@Bean方法的位置。

In cases where this ambiguity is not acceptable and you wish to have direct navigation from within your IDE from one `@Configuration` class to another, consider autowiring the configuration classes themselves. The following example shows how to do so:

译：这儿的摸棱两可是不可接受的，并且你希望在IDE中直接有从一个类另一个@Configuration类的导航，请考虑自动注入配置类本身。如下所示：

```java
@Configuration
public class ServiceConfig {

    @Autowired
    private RepositoryConfig repositoryConfig;

    @Bean
    public TransferService transferService() {
        // navigate 'through' the config class to the @Bean method!
        return new TransferServiceImpl(repositoryConfig.accountRepository());
    }
}
```

In the preceding situation, where `AccountRepository` is defined is completely explicit. However, `ServiceConfig` is now tightly coupled to `RepositoryConfig`. That is the tradeoff. This tight coupling can be somewhat mitigated by using interface-based or abstract class-based `@Configuration` classes. Consider the following example:

在前面的情形中，`AccountRepository` 完全显示地定义。但是，`ServiceConfig` 与`RepositoryConfig`紧密耦合。这就是协调。这种紧密耦合可以通过使用基于接口或者抽象基础类的@Configuration类缓解，考虑如下例子。

```java
@Configuration
public class ServiceConfig {

    @Autowired
    private RepositoryConfig repositoryConfig;

    @Bean
    public TransferService transferService() {
        return new TransferServiceImpl(repositoryConfig.accountRepository());
    }
}

@Configuration
public interface RepositoryConfig {

    @Bean
    AccountRepository accountRepository();
}

@Configuration
public class DefaultRepositoryConfig implements RepositoryConfig {

    @Bean
    public AccountRepository accountRepository() {
        return new JdbcAccountRepository(...);
    }
}

@Configuration
@Import({ServiceConfig.class, DefaultRepositoryConfig.class})  // import the concrete config!
public class SystemTestConfig {

    @Bean
    public DataSource dataSource() {
        // return DataSource
    }

}

public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SystemTestConfig.class);
    TransferService transferService = ctx.getBean(TransferService.class);
    transferService.transfer(100.00, "A123", "C456");
}
```

Now `ServiceConfig` is loosely coupled with respect to the concrete `DefaultRepositoryConfig`, and built-in IDE tooling is still useful: You can easily get a type hierarchy of `RepositoryConfig` implementations. In this way, navigating `@Configuration` classes and their dependencies becomes no different than the usual process of navigating interface-based code.

译：现在，`ServiceConfig`相对于具体的DefaultRepositoryConfig是松耦合，并且内置的IDE工具仍然有用：你可以很容易得到RepositoryConfig实现的继承关系。在这个方法上，导航@Configuration类和他们的依赖与普通的基于接口代码的导航处理没有什么不同。

------

If you want to influence the startup creation order of certain beans, consider declaring some of them as `@Lazy` (for creation on first access instead of on startup) or as `@DependsOn` certain other beans (making sure that specific other beans are created before the current bean, beyond what the latter’s direct dependencies imply).

译：如果你想影响某几个bean的开始创建顺序，请考虑声明这些为@Lazy(在第一次访问时创建，而不是开始)或者@DependsOn某些其他的bean（在当前bean之前，确保声明其他bean已经创建，超过后者直接依赖的含义）.

------

##### Conditionally Include `@Configuration` Classes or `@Bean` Methods

译：有条件地包含@Configuration类或者@Bean方法

It is often useful to conditionally enable or disable a complete `@Configuration` class or even individual `@Bean` methods, based on some arbitrary system state. One common example of this is to use the `@Profile` annotation to activate beans only when a specific profile has been enabled in the Spring `Environment` (see [Bean Definition Profiles](https://docs.spring.io/spring/docs/5.2.6.RELEASE/spring-framework-reference/core.html#beans-definition-profiles) for details).

译：它经常很有用，有条件地启用或者禁用一个全部的@Configuration类或者每一个@Bean方法，基于一些任意系统状态。一个常见的例子是当一个明确的概要文件（profile）在Spring环境中已经使用，使用@Profile注解激活bean（请看bean definition profiles）。

The `@Profile` annotation is actually implemented by using a much more flexible annotation called [`@Conditional`](https://docs.spring.io/spring-framework/docs/5.2.6.RELEASE/javadoc-api/org/springframework/context/annotation/Conditional.html). The `@Conditional` annotation indicates specific `org.springframework.context.annotation.Condition` implementations that should be consulted before a `@Bean` is registered.

译：@Profile注解实际上是使用更令过的@Conditional注解实现的。@Conditional注解表示在@Bean注册之前按，应该参考指定的@org.springframework.context.annotaion.Condition实现

Implementations of the `Condition` interface provide a `matches(…)` method that returns `true` or `false`. For example, the following listing shows the actual `Condition` implementation used for `@Profile`:

译：Condition接口的实现提供了一个`matches()`方法，这个方法返回true或者false.例如，如下清单展示了使用@Profile实际的Condition实现：

```java
@Override
public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    // Read the @Profile annotation attributes
    MultiValueMap<String, Object> attrs = metadata.getAllAnnotationAttributes(Profile.class.getName());
    if (attrs != null) {
        for (Object value : attrs.get("value")) {
            if (context.getEnvironment().acceptsProfiles(((String[]) value))) {
                return true;
            }
        }
        return false;
    }
    return true;
}
```

See the [`@Conditional`](https://docs.spring.io/spring-framework/docs/5.2.6.RELEASE/javadoc-api/org/springframework/context/annotation/Conditional.html) javadoc for more detail.

##### Combining Java and XML Configuration

译：结合java和xml配置

Spring’s `@Configuration` class support does not aim to be a 100% complete replacement for Spring XML. Some facilities, such as Spring XML namespaces, remain an ideal way to configure the container. In cases where XML is convenient or necessary, you have a choice: either instantiate the container in an “XML-centric” way by using, for example, `ClassPathXmlApplicationContext`, or instantiate it in a “Java-centric” way by using `AnnotationConfigApplicationContext` and the `@ImportResource` annotation to import XML as needed.

译：Spring的@Configuration类支持的目标并不是100%替换Spring XML。一些场所，例如Spring xml的命名空间，仍然是配置容器的理想方法。在方便或需要xml的情况下，你可以选择：例如，通过使用ClassPathXmlAppliationContext在一个以xml为中心的方式实例化容器，或者通过使用AnnotationConfigApplicationContext和根据需要@ImportResource注解导入xml.

###### XML-centric Use of `@Configuration` Classes

It may be preferable to bootstrap the Spring container from XML and include `@Configuration` classes in an ad-hoc fashion. For example, in a large existing codebase that uses Spring XML, it is easier to create `@Configuration` classes on an as-needed basis and include them from the existing XML files. Later in this section, we cover the options for using `@Configuration` classes in this kind of “XML-centric” situation.

译：最好从xml引导Spring容器并以特别的方式包含@Configuration类。例如，在大量存在使用Spring xml 的代码库中，更容易创建@Configuration类，并从现有的xml文件中包含他们。在本节的后面，我们将介绍使用@Configuration类在这种xml为中心的场景的选择。

Declaring `@Configuration` classes as plain Spring `<bean/>` elements

译：以普通的Spring `<bean/>` 标签声明@Configuration

Remember that `@Configuration` classes are ultimately bean definitions in the container. In this series examples, we create a `@Configuration` class named `AppConfig` and include it within `system-test-config.xml` as a `<bean/>` definition. Because `<config:anntation-config/> ` is switched on, the container recognizes the `@Configuration` annotation and processes the `@Bean` methods declared in `AppConfig` properly.

译：请记住，@Configuration类最终是容器中的bean.在这几个例子中，我们创建一个叫做`AppConfig`的@Configuration类，包含system-test-config.xml作为bean标签定义。因为`<config:annotation-config/>`切换到on，容器识别@Configuration注解并且处理在`AppConfig`中的@Bean方法

The following example shows an ordinary configuration class in Java:

译：如下例子展示了一个普通的java配置类：

```java
@Configuration
public class AppConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public AccountRepository accountRepository() {
        return new JdbcAccountRepository(dataSource);
    }

    @Bean
    public TransferService transferService() {
        return new TransferService(accountRepository());
    }
}
```

The following example shows part of a sample `system-test-config.xml` file:

译：如下例子展示了system-test-config.xml文件的一部分：

```xml
<beans>
    <!-- enable processing of annotations such as @Autowired and @Configuration -->
    <context:annotation-config/>
    <context:property-placeholder location="classpath:/com/acme/jdbc.properties"/>

    <bean class="com.acme.AppConfig"/>

    <bean class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="url" value="${jdbc.url}"/>
        <property name="username" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>
    </bean>
</beans>
```

The following example shows a possible `jdbc.properties` file:

译：如下例子展示了jdbc.properties文件：

```
jdbc.url=jdbc:hsqldb:hsql://localhost/xdb
jdbc.username=sa
jdbc.password=
```

```java
public static void main(String[] args) {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:/com/acme/system-test-config.xml");
    TransferService transferService = ctx.getBean(TransferService.class);
    // ...
}
```

------

  In `system-test-config.xml` file, the `AppConfig` `<bean/>` does not declare an `id` element. While it would be acceptable to do so, it is unnecessary, given that no other bean ever refers to it, and it is unlikely to be explicitly fetched from the container by name. Similarly, the `DataSource` bean is only ever autowired by type, so an explicit bean `id` is not strictly required.

译：在system-test-config.xml文件，AppConfig `<bean/>`没有声明id标签。尽管这么做是可以接受的，他是不必要的，因为没有其他bean引用它。并且不太可能从容器中通过名称显示得到。类似的，`DataSource`bean只通过类型注入，因此显示的bean不是严格需要的：

 Using `<context:component-scan/>` to pick up `@Configuration` classes

译:使用`<context:component-scan/>`得到@Configuration类

Because `@Configuration` is meta-annotated with `@Component`, `@Configuration`-annotated classes are automatically candidates for component scanning. Using the same scenario as describe in the previous example, we can redefine `system-test-config.xml` to take advantage of component-scanning. Note that, in this case, we need not explicitly declare `<context:component-scan/>`, because ``<context:component-scan/>`` enables the same functionality.

译：由于@Configuration是使用@Component的元注解，@Configuration注解的类自动成为组件扫描的候选项。与前面描述的例子，使用相同的场景，我们可以利用组件扫描重新定义system-test-config.xml。请记住，在这种情形中，我们不需要显示声明`<context:component-config/>`，因为``<context:component-scan/>``启动相同的功能。

The following example shows the modified `system-test-config.xml` file:

译：如下例子展示修改后的system-test-config.xml文件：

```xml
<beans>
    <!-- picks up and registers AppConfig as a bean definition -->
    <context:component-scan base-package="com.acme"/>
    <context:property-placeholder location="classpath:/com/acme/jdbc.properties"/>

    <bean class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="url" value="${jdbc.url}"/>
        <property name="username" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>
    </bean>
</beans>
```

###### `@Configuration` Class-centric Use of XML with `@ImportResource`

译：@Configuration类为中心的使用@ImportResource xml.

In applications where `@Configuration` classes are the primary mechanism for configuring the container, it is still likely necessary to use at least some XML. In these scenarios, you can use `@ImportResource` and define only as much XML as you need. Doing so achieves a “Java-centric” approach to configuring the container and keeps XML to a bare minimum. The following example (which includes a configuration class, an XML file that defines a bean, a properties file, and the `main` class) shows how to use the `@ImportResource` annotation to achieve “Java-centric” configuration that uses XML as needed:

译：在以@Configuration类是配置容器主要机制的应用，使用一些XML任然是必要的。在这些场景中，你可以使用@ImportResource并且定义你需要的xml.这么做实现以java为中心的方法配置容器并且最低程度的使用xml。如下例子（它包含配置类，一个xml文件定义bean,一个属性文件和main类）展示如何使用@ImportResource注解完成以java为中心的配置使用需要的xml：

```java
@Configuration
@ImportResource("classpath:/com/acme/properties-config.xml")
public class AppConfig {

    @Value("${jdbc.url}")
    private String url;

    @Value("${jdbc.username}")
    private String username;

    @Value("${jdbc.password}")
    private String password;

    @Bean
    public DataSource dataSource() {
        return new DriverManagerDataSource(url, username, password);
    }
}
```

```xml
properties-config.xml
<beans>
    <context:property-placeholder location="classpath:/com/acme/jdbc.properties"/>
</beans>
```

```
jdbc.properties
jdbc.url=jdbc:hsqldb:hsql://localhost/xdb
jdbc.username=sa
jdbc.password=
```

```java
public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
    TransferService transferService = ctx.getBean(TransferService.class);
    // ...
}
```

### 1.13. Environment Abstraction

译：环境抽象





















# 三、Testing**



# **四、Data Access**

# **五、Web servlet**

# **六、Web Reactive**

# **七、Integration**

# **八、Languages**