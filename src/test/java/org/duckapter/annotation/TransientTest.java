package org.duckapter.annotation;

import static org.duckapter.DuckTestHelper.assertCanAdaptInstance;

import org.duckapter.annotation.Field;
import org.duckapter.annotation.Transient;
import org.junit.Test;

public class TransientTest {

	public static interface WithTransientFieldInterface {
		@Transient
		@Field
		Object getField();
	}

	public static class WithTransientField {
		public transient String field = "";
	}

	public static class WithoutTransientField {
		public String field = "";
	}

	@Test
	public void testTransient() throws Exception {
		assertCanAdaptInstance(WithTransientFieldInterface.class,
				WithTransientField.class, WithoutTransientField.class);
	}

}
