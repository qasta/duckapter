package org.duckapter.inheritance;

import org.duckapter.Duckapter;
import org.duckapter.annotation.Factory;
import org.duckapter.annotation.Static;
import org.junit.Test;

public class InheritenceTest {

	public static class Inheritance {

		public static void testStatic() {
		};

		public void testInstance() {
		};

	}
	
	public static class Super {
		public void testInstance(){}
	}
	
	public static class Inheritence2 extends Super {
		public static void testStatic(){}
	}
	
	public static interface InheritanceInstance extends InheritanceClass {
		void testInstance();
	}

	public static interface InheritanceClass {

		@Factory
		InheritanceInstance instance();

		@Static
		void testStatic();

	}
	
	@Test public void testInheritance0(){
		Duckapter.adaptInstance(new Inheritance(), InheritanceClass.class);
	}
	
	@Test public void testInheritance1(){
		Duckapter.adaptInstance(new Inheritance(), InheritanceInstance.class);
	}
	
	@Test
	public void testInheritance() throws Exception {
		InheritanceClass iclass = Duckapter.adaptClass(Inheritance.class,
				InheritanceClass.class);
		iclass.testStatic();
		InheritanceInstance iinstance = iclass.instance();
		iinstance.testStatic();
		iinstance.testInstance();

	}
	
	@Test public void testInheritance2(){
		Duckapter.adaptInstance(new Inheritence2(), InheritanceClass.class);
	}
	
	@Test public void testInheritance3(){
		Duckapter.adaptInstance(new Inheritence2(), InheritanceInstance.class);
	}
	
	@Test
	public void testSuper() throws Exception {
		InheritanceClass iclass = Duckapter.adaptClass(Inheritence2.class,
				InheritanceClass.class);
		iclass.testStatic();
		InheritanceInstance iinstance = iclass.instance();
		iinstance.testStatic();
		iinstance.testInstance();

	}

}
