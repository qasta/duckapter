package eu.ebdit.duckapter.statics;

import eu.ebdit.duckapter.annotation.Alias;
import eu.ebdit.duckapter.annotation.Property;
import eu.ebdit.duckapter.annotation.Static;

public interface IMyMixed {

	@Static String getString();
	int getInt();
	@Property @Alias("bla") String getBla();
	
}
