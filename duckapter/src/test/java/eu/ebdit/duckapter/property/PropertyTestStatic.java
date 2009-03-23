package eu.ebdit.duckapter.property;

import eu.ebdit.duckapter.annotation.Alias;
import eu.ebdit.duckapter.annotation.Property;
import eu.ebdit.duckapter.annotation.Static;
import eu.ebdit.duckapter.annotation.Factory;

public interface PropertyTestStatic {

	@Factory
	public abstract PropertyTest instantiate();

	@Static @Property @Alias("staticStringField")
	public abstract String getString();

	@Static @Property @Alias("staticStringField")
	public abstract void setString(String s);

}