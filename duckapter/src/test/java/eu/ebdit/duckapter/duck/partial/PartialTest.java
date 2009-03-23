package eu.ebdit.duckapter.duck.partial;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.util.Set;

import org.junit.Test;

import eu.ebdit.duckapter.Adapted;
import eu.ebdit.duckapter.Duckapter;
import eu.ebdit.duckapter.duck.itest.IPartialDuck;


public class PartialTest {

	@Test
	public void testPartial() throws Exception {
		IPartialDuck partial = Duckapter.adaptClass(NoDuck.class, IPartialDuck.class);
		Adapted duckTyped = (Adapted) partial;
		assertEquals(NoDuck.class, duckTyped.getOriginalClass());
		assertEquals(IPartialDuck.class, duckTyped.getDuckedClass());
		assertNull(duckTyped.getOriginalInstance());
		Set<Method> uni = duckTyped.getUnimplementedMethods();
		assertEquals(2, uni.size());
		for (Method method : uni) {
			assertTrue("dive".equals(method.getName()) || "getName".equals(method.getName()));
		}
		assertFalse(duckTyped.isFullyImplemented());
		assertFalse(duckTyped.isForInstance());
		assertTrue(duckTyped.isForClass());
		
		try {
			partial.dive();
			partial.getName();
			fail();
		} catch (UnsupportedOperationException e) {
			// ok
		} catch (Exception e){
			e.printStackTrace();
			fail();
		}
		
		
	}
	
	@Test
	public void testHalfOptional() throws Exception {
		IHalfOptional fully = Duckapter.adaptClass(FullyImplemented.class, IHalfOptional.class);
		fully.optional();
		fully.mandatory();
		assertEquals(0,((Adapted)fully).getUnimplementedMethods().size());
		assertTrue(((Adapted)fully).isFullyImplemented());
		IHalfOptional half = Duckapter.adaptClass(HalfImplemented.class, IHalfOptional.class);
		half.mandatory();
		assertFalse(((Adapted)half).isFullyImplemented());
		assertEquals(1,((Adapted)half).getUnimplementedMethods().size());
		for (Method m : ((Adapted)half).getUnimplementedMethods()) {
			assertEquals("optional", m.getName());
		}
		try {
			half.optional();
			fail();
		} catch (UnsupportedOperationException e) {
			// ok
		}
		
		
	}
	
}
