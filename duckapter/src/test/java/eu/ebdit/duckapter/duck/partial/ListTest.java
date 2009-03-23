package eu.ebdit.duckapter.duck.partial;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import eu.ebdit.duckapter.Adapted;
import eu.ebdit.duckapter.Duckapter;



public class ListTest {

	
	@Test
	public void listTest() throws Exception {
		List<Object> list = new ArrayList<Object>();
		@SuppressWarnings("unchecked")
		Collection<Object> c = Duckapter.adaptInstance(list, Collection.class);
		Adapted a = ((Adapted)c);
		assertTrue(a.isForInstance());
		c.isEmpty();
	}
	
}
