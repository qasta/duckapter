package eu.ebdit.duckapter.generic;

import org.junit.Test;

import eu.ebdit.duckapter.Duckapter;


public class GenericTest {

	@Test
	public void testGeneric() throws Exception {
		GenericClassClass gcc = Duckapter.adaptClass(GenericClass.class, GenericClassClass.class);
		gcc.getT(Object.class);
	}
	
}
