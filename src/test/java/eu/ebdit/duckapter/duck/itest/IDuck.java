package eu.ebdit.duckapter.duck.itest;

import eu.ebdit.duckapter.annotation.Property;

public interface IDuck {

	@Property String getName();
	void dive();
	
}
