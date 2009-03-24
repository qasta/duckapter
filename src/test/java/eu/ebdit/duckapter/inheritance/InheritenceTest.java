package eu.ebdit.duckapter.inheritance;

import org.junit.Test;

import eu.ebdit.duckapter.Duckapter;


public class InheritenceTest {

	@Test
	public void testInheritance() throws Exception {
		InheritanceClass iclass = Duckapter.adaptClass(Inheritance.class, InheritanceClass.class);
		iclass.testStatic();
		InheritanceInstance iinstance = iclass.instance();
		iinstance.testStatic();
		iinstance.testInstance();
		
	}
	
}
