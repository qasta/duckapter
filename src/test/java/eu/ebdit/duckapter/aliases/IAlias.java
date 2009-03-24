package eu.ebdit.duckapter.aliases;

import eu.ebdit.duckapter.annotation.Alias;
import eu.ebdit.duckapter.annotation.Property;
import eu.ebdit.duckapter.annotation.Static;

@Static
public interface IAlias {
	
	@Alias("getSomethingElse") String getSomething();
	@Alias({"getMoreOptions", "getLotMoreOptions", "getLastOption"}) String getString();
	@Property() @Alias("property") String getProperty();
	
}
