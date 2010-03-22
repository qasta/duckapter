package org.duckapter.duck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.duckapter.Duckapter;
import org.duckapter.duck.itest.IDuck;
import org.duckapter.duck.itest.IDuckClass;
import org.junit.Test;

public abstract class AbstractFullDuckTest {

	protected final Class<?> fixure;

	protected AbstractFullDuckTest(Class<?> fixure) {
		this.fixure = fixure;
	}

	@Test
	public void canDuck() throws Exception {
		assertTrue(Duckapter.canAdaptInstance(Duckapter.adaptClass(fixure,
				IDuckClass.class).newDuck(), IDuck.class));
		assertFalse(Duckapter.canAdaptInstance(fixure, IDuckClass.class));
	}

	@Test
	public void canDuckClass() throws Exception {
		assertTrue(Duckapter.canAdaptClass(fixure, IDuckClass.class));
		assertFalse(Duckapter.canAdaptClass(fixure, IDuck.class));
	}

	@Test
	public void newInstance() throws Exception {
		IDuckClass duck = Duckapter.adaptClass(fixure, IDuckClass.class);
		assertEquals("Donald", duck.newDuck().getName());
	}

	@Test
	public void newInstanceString() throws Exception {
		IDuck duck = Duckapter.adaptClass(fixure, IDuckClass.class).newDuck(
				"Other");
		assertEquals("Other", duck.getName());
	}

	@Test
	public void testMethodsWorks() throws Exception {
		IDuck duck = Duckapter.adaptClass(fixure, IDuckClass.class).newDuck(
				"Other");
		assertEquals("Other", duck.getName());
		duck.dive();
		assertNotNull(duck.toString());
	}

	@Test
	public void staticMethodWorks() throws Exception {
		IDuckClass duckClass = Duckapter.adaptClass(fixure, IDuckClass.class);
		duckClass.getCount();
	}

}
