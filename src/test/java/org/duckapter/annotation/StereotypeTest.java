package org.duckapter.annotation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.duckapter.annotation.StereotypeType.AND;
import static org.duckapter.annotation.StereotypeType.OR;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.duckapter.CheckerAnnotation;
import org.duckapter.DuckTestHelper;
import org.duckapter.checker.StereotypeChecker;
import org.junit.Test;

public class StereotypeTest {

	
	@Documented
	@CheckerAnnotation(StereotypeChecker.class)
	@Stereotype(OR)
	@Retention(RUNTIME)
	@Target( { METHOD, ANNOTATION_TYPE })
	@Constructor
	@StaticField
	@StaticMethod
	public static @interface MyFactory {
	}

	public static interface MyFactoryIFace {
		@MyFactory
		Object instance();
	}

	public static class MyFactoryConstructor {
		public MyFactoryConstructor() {
		}
	}

	public static class MyFactoryMethod {
		public static Object instance() {
			return null;
		}
	}

	public static class MyFactoryField {
		public static Object INSTANCE = null;
	}

	public static class MyFactoryFail {
		private MyFactoryFail() {
		}
	}

	@Test
	public void testMyFactoryConstructor() {
		DuckTestHelper.assertCanAdaptInstance(MyFactoryIFace.class,
				MyFactoryConstructor.class, MyFactoryFail.class);
	}
	
	@Test
	public void testMyFactoryField() {
		DuckTestHelper.assertCanAdaptInstance(MyFactoryIFace.class,
				MyFactoryField.class, MyFactoryFail.class);
	}
	
	@Test
	public void testMyFactoryMethod() {
		DuckTestHelper.assertCanAdaptInstance(MyFactoryIFace.class,
				MyFactoryMethod.class, MyFactoryFail.class);
	}
	

	@Documented
	@CheckerAnnotation(StereotypeChecker.class)
	@Stereotype(AND)
	@Retention(RUNTIME)
	@Target( { METHOD, ANNOTATION_TYPE })
	@Package(Visibility.AT_MOST)
	@Static
	@Final
	@Field
	public static @interface AtMostPackageConst {
	}
	
	public static interface AMCPInterface {
		@AtMostPackageConst String constant();
	}
	
	public static class AMCPClassPass {
		static final String CONSTANT = "BLA";
	}
	
	public static class AMCPClassFail {
		public static final String CONSTANT = "BLA";
	}
	
	@Test
	public void testAMCP() {
		DuckTestHelper.assertCanAdaptInstance(AMCPInterface.class,
				AMCPClassPass.class, AMCPClassFail.class);
	}
	

}
