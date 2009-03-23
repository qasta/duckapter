package eu.ebdit.duckapter.property;

import static org.junit.Assert.*;

import org.junit.Test;

import eu.ebdit.duckapter.Duckapter;


public class PropertyUnitTest {
	
	@Test
	public void testProperty() throws Exception {
		PropertyTestStatic propStatic = Duckapter.adaptClass(PropertyTestClass.class, PropertyTestStatic.class);
		propStatic.setString("Test");
		assertEquals("Test", propStatic.getString());
		PropertyTest fixure = Duckapter.adaptInstance(new PropertyTestClass(), PropertyTest.class);
		fixure.setInt(10);
		assertEquals(10, fixure.getInt());
	}

}
