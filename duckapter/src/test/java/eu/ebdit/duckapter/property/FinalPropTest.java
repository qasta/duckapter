package eu.ebdit.duckapter.property;

import static org.junit.Assert.*;

import org.junit.Test;

import eu.ebdit.duckapter.Duckapter;


public class FinalPropTest {

	@Test
	public void testSetFinal() throws Exception {
		try {
			Duckapter.adaptClass(FinalProp.class, FinalSetPropClass.class);
			fail();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
	
}
