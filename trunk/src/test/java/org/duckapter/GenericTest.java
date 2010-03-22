package org.duckapter;

import org.duckapter.Duckapter;
import org.duckapter.modifier.Static;
import org.junit.Test;

public class GenericTest {

	public static interface GenericClassClass {
		@Static
		<T> T getT(Class<T> clazz);
	}
	
	public static class GenericClass {

		public static <T> T getT(Class<T> clazz) {
			return null;
		}

	}
	
	@Test
	public void testGeneric() throws Exception {
		GenericClassClass gcc = Duckapter.adaptClass(GenericClass.class,
				GenericClassClass.class);
		gcc.getT(Object.class);
	}

}
