package eu.ebdit.duckapter.aliases;

import static org.junit.Assert.*;

import org.junit.Test;

import eu.ebdit.duckapter.Duckapter;

public class AliasTest {
	
	@Test
	public void testAlias() throws Exception {
		try {
			Duckapter.adaptClass(Alias.class, IAlias.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue(Duckapter.canAdaptClass(Alias.class, IAlias.class));

	}

}
