package eu.ebdit.duckapter.statics;

import static org.junit.Assert.*;

import org.junit.Test;

import eu.ebdit.duckapter.Duckapter;


public class MixedTest {

	@Test
	public void testMixed() throws Exception {
		assertFalse(Duckapter.canAdaptInstance(new MyMixed(), IMyMixed.class));
		try {
			Duckapter.adaptInstance(new MyMixed(), IMyMixed.class);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
	
}
