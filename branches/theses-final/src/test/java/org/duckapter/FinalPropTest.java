package org.duckapter;

import static org.junit.Assert.fail;

import org.duckapter.annotation.Property;
import org.duckapter.annotation.Static;
import org.junit.Test;

public class FinalPropTest {

	public class FinalProp {

		public static final String finalProperty = "bla";

	}
	
	public interface FinalSetPropClass {
		@Static
		@Property
		void setFinalProperty(String s);
	}
	
	@Test
	public void testSetFinal() throws Exception {
		try {
			Duck.wrap(FinalProp.class, FinalSetPropClass.class);
			fail();
		} catch (WrappingException e) {
			// ok
		}
	}

}
