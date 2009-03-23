package eu.ebdit.duckapter.duck.itest;

import eu.ebdit.duckapter.annotation.Constructor;
import eu.ebdit.duckapter.annotation.Property;
import eu.ebdit.duckapter.annotation.Static;
import eu.ebdit.duckapter.annotation.Factory;

public interface IDuckClass {
	@Factory IDuck newDuck();
	@Constructor IDuck newDuck(String name);
	@Static boolean canFly();
	@Static @Property int getCount();
}
