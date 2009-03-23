package eu.ebdit.duckapter.selfeating;

import static org.junit.Assert.*;

import org.junit.Test;

import eu.ebdit.duckapter.Duckapter;

public class CompositeTest {

	
	@Test
	public void composite() throws Exception {
		ICompositeClass composite = Duckapter.adaptClass(Composite.class, ICompositeClass.class);
		IComposite fixure = composite.getInstance("fixure");
		IComposite parent = composite.getInstance("parent");
		fixure.setParent(parent);
		IComposite fixureParent = fixure.getParent();
		assertTrue(parent.equals(fixureParent));
		assertEquals(parent, fixure.getParent());
		IComposite copy = composite.getCopy(fixure);
		assertEquals(parent, fixure.getParent());
		assertNotSame(fixure, copy);
		assertNotSame(parent, fixure.getParent());
	}
	
}
