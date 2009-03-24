package eu.ebdit.duckapter.inheritance;

import eu.ebdit.duckapter.annotation.Static;
import eu.ebdit.duckapter.annotation.Factory;

public interface InheritanceClass {

	@Factory InheritanceInstance instance();
	@Static void testStatic();
	
}
