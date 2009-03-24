package eu.ebdit.duckapter.property;

import eu.ebdit.duckapter.annotation.Property;
import eu.ebdit.duckapter.annotation.Static;

public interface FinalSetPropClass {
	@Static @Property void setFinalProperty(String s);
}
