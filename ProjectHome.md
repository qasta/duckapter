# Duckapter #
Duckapter is Java reflection and duck typing library.

Source code is now available on [GitHub](http://code.google.com/p/duckapter/)


The beta release of the version 0.7.0 is out.

The API of the facade class Duck and the behavior of annotations from org.duckapter.annotation package won't change in final release and your application can rely on them since this beta release. Other parts of the library may vary in future due the performance optimalization. In a nutshell, this release can be safely use for duck typing using the built-in annotations but it is not yet ready for creating your own ones. You can join the discussion group, if you want to be notified about upcoming new releases.

## Features ##

### Basic Duck Typing ###

You can use Duckapter for the basic duck testing and duck typing. Just define the interface

```
public interface BirdCalledDuck {

   String quack();

}
```
and you can than test any object if it has the public method quack returning String
```
boolean isDuck = Duck.test(theBird, BirdCalledDuck.class);
```
You can also use the interface to adapt the  object which passes the test and call its methods.
```
if (Duck.test(theBird, BirdCalledDuck.class) {
   BirdCalledDuck theDuck = Duck.type(theBird, BirdCalledDuck.class);
   System.out.println(theDuck.quack());
}
```
Ducking and unducking return values and methods' parameters is also supported.

### Interfacing the Classes ###
Using Duckapter you can create interfaces which will be mapped to the classes' constructors and static methods and fields.
```
public interface HasDefaultConstructor{
   @Constructor Object newInstance();
}
```
```
if(Duck.test(SomeClass.class, HasDefaultConstructor.class){
   Object newOne = Duck.type(SomeClass.class, HasDefaultConstructor.class).newInstance();
}
```
```
public interface HasInstanceCount{
   @Static @Field int getInstanceCount();
}
```
```
if(Duck.test(SomeClass.class, HasInstanceCount.class){
   int count = Duck.type(SomeClass.class, HasInstanceCount.class).getInstanceCount();
}
```
### Precise Reflection ###
You can narrow the tests and adaptation using a large varity of annotation. For example, duckapter provide annotation for each Java modifier. You can also specify if you are looking for a method (default), a field or a constructor. The annotations can be used also on the duck interface to check them against the class of the object to be tested. Any non-checker annotations present on the duck method or duck interface are part of the testing.
```
@Final public interface PreciseReflection {
   @Private @Transient @Field String transientField();
   @Test void testMethod();
}
```
### Enough Flexibility ###
You can also broaden the test to provide more flexibility. Are you missing properties in Java? Following duck test will pass if the x property is accessed by getter or field.
```
public interface XProp {
   @Property String getX();
   @Property void setX(String newX);
}
```
Another popular features are factories. Following duck test would satisfy a constructor, a static method getInstance() or even static field INSTANCE.
```
public interface HasFactory{
   @Factory Object getInstance();
}
```
Also name of the method mustn't be dogmatic. You can use aliases and regular expressions.
```
@Optional @Alias({"other", "oneMore") void method();
@All @Matching("test.*") TestMethod[] testMethods();
```
In previous example you see also the `@All` annotations which gather all elements which passes the test but the result might be also empty. The `@Optional` annotation denote that the duck method needn't to find its counterpart. I such case calling the method will do nothing or return the default value. For more annotation see the documentation. Provided unit test can show you usefull usage hints.

### Easy to extend ###
You can easily create new checker annotation. The `@StereotypeChecker` metaanotation helps you to create new annotation by composing old ones. You can also try to implement your own Checker and/or InvocationAdapter.

## A few complex examples ##
The Library (Utility) class duck test.
```
@Final public interface Library {

   @DoesNotHave @Any @Protected(AT_LEAST) @Constructor @WithAnyParams
   void hasHiddenConstructors();
	
   @DoesNotHave @Any @NonStatic @Field
   void hasNoInstanceFields();
	
   @DoesNotHave @Any @NonStatic @Method
   void hasNoInstanceMethods();
	
}
```

JUnit Test and very simple runner.
```
public interface TestMethod {
	
   void test() throws Exception;
	
}
```
```
public interface JUnit4Test {
	
   @DoesNotHave @NonStatic @BeforeClass 
   void beforeClassMustBeStatic();

   @DoesNotHave @Static @Before
   void setUpCannotBeStatic ();
	
   @DoesNotHave @Static @Test
   void testsCannotBeStatic();
   
   @DoesNotHave @Static @After
   void tearDownCannotBeStatic();
	
   @DoesNotHave @NonStatic @AfterClass 
   void afterClassMustBeStatic();
	
	
	
   @DoesNotHave @Package(AT_MOST) @BeforeClass
   void beforeClassMustBePublic();
	
   @DoesNotHave @Package(AT_MOST) @Before
   void setUpMustBePublic();
	
   @DoesNotHave @Package(AT_MOST) @Test
   void testsMustBePublic();
	
   @DoesNotHave @Package(AT_MOST) @After
   void tearDownMustBePublic();
	
   @DoesNotHave @Package(AT_MOST) @AfterClass
   void afterClassMustBePublic();
	
	
   @Optional @Static @BeforeClass void beforeClass()throws Exception;
   @Optional @Before void setUp()throws Exception;
   @Optional @After void tearDown()throws Exception;
   @Optional @Static @AfterClass void afterClass()throws Exception;
	
   @All @Test TestMethod[] testMethods();

```
```
JUnit4Test tests = Duck.type(testing, JUnit4Test.class);
tests.beforeClass();
for (TestMethod method : tests.testMethods()) {
   try {
      tests.setUp();
      method.test();
      tests.tearDown();
   } catch (Exception e) {
      handleException(e);
   }
}
tests.afterClass();
```