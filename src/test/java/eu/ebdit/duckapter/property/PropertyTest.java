package eu.ebdit.duckapter.property;

import eu.ebdit.duckapter.annotation.Alias;
import eu.ebdit.duckapter.annotation.Property;

public interface PropertyTest  {
	@Property() @Alias("intField") int getInt();
	@Property() @Alias("intField") void setInt(int i);
	@Property() @Alias("prop") String getSomeProperty();
	@Property() @Alias("prop") void setSomeProperty(String s);
}
