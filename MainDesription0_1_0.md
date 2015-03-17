# Duckapter #
Duckapter brings duck typing into Java programming language. It works like an adapter factory class which adapt classes or instances to particular interfaces. Duckapter allows to map classes or instances by their methods, constructors and/or factory methods and also properties.
# Duck Test #
> If it looks like a duck, swims like a duck and quacks like a duck, then it probably is a duck
[Wikipedia: Duck test](http://en.wikipedia.org/wiki/Duck_test)
# Duck Typing #
> In computer programming, duck typing is a style of dynamic typing in which an object's current set of methods and properties determines the valid semantics, rather than its inheritance from a particular class or implementation of a specific interface.
[Wikipedia: Duck typing](http://en.wikipedia.org/wiki/Duck_typing)
## Duck Typing Warning ##
Duck typing should be used only for special purposes only because it is not type safe in the view of Java Programming Language and it also decreases application's performance. One of this special purposes is dealing with unknown code e.g. student's excercises.
## The Example ##
Let's say that you don't know anything abou Duck class. For example, you have compiled it from some given source code. You want to test whether it has all demanded methods and properties and try to use them.
```
// Duck.class doesn't implement IDuckClass.class
// but it should satisfy methods which this interface describes
if (!Duckapter.canAdaptClass(Duck.class, IDuckClass.class){
   // duck class doesn't have all non-optional methods!
}
// adapt class object
IDuckClass duckClass = Duckaper.adaptClass(Duck.class, IDuckClass.class);
// now we can call static methods of Duck class over IDuckClass interface
int duckCount = duckClass.getDuckCount();
duckClass.doSomethingStatic();
try {
   // this method is marked as optional, so it can throw UnsupportedOperationException
   // if it's not implemented
   duckClass.doSomethingElse();
} catch (UnsupportedOperationException e) {
   // now we know this operation is not supported on duck class
}
// we can also call underlying constuctor using interface method
IDuck newDuck = duckClass.newInstance();
// than we can call instance methods
newDuck.makeSomeSound();
String duckName = newDuck.name();
IDuck otherDuck = duckClass.valueOf("other");
IDuck copyOfOther = duckClass.copy(otherDuck);
```
### Interface For Class Objects ###
To garantee all methods being implemented the method Duckapter.adaptClass(Class original, Class duckInterface) is only aplicable for duck interface which has only declared static methods, factory methods or constructors.
```
public interface IDuckClass {
     // will be matched with method valueOf(String) or constructor
     // with single String parameter
     // because IDuck is declared as return type it will have to satisfy
     // Duckapter.canAdaptInstance(<instanceOfUnderlyingClass>,IDuck.class) method
     @Factory IDuck valueOf(String value);
     // will match default constructor
     @Constructor IDuck newInstance();
     // will match static method getDuckCount() or static field duckCount
     @Static @Property int getDuckCount();
     // will match static method doSomethingStatic()
     @Static void doSomethingStatic();
     // will match static method doSomethingElse() or throws UnsupportedOperationException
     // whether original class does not implement this method
     // this method doesn't need to be implemented by original class
     @Static @Optional void doSomethingElse();
     // will be matched with method copy(<class of original instance> original) which
     // returns instance of same class like original instance
     // use @Original annotation judciously because exception will be thrown whether
     // actual parameter will not match original class (e.g. will be some other duck
     // typed object)
     @Factory IDuck copy(@Original IDuck original);
}
```
### Interface For Instances ###
Inteface given to method Duckapter.adaptInstance(Object original, Class duckInterface) can have any methods or properties they want.
```
public interface IDuck {
     // will be matched with method name() of field called name
     @Property String name();
     // will be matched with method makeSomeSound() or quack() or cry() (in given order)
     @Alias({"quack", "cry"}) void makeSomeSound();
}
```

### The Example Class ###
Following class will satisfy both IDuckClass and IDuck interface when asked to be duck typed.
```
public class Duck {

     public int duckCount = 0;

     public String name = "Donnald";

     public static Duck valueOf(String value){...}
     public static void doSomethingStatic(){...}

     public void quack();
 
}
```