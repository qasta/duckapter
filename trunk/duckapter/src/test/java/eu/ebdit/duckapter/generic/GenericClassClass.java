package eu.ebdit.duckapter.generic;

import eu.ebdit.duckapter.annotation.Static;

public interface GenericClassClass {
	@Static <T> T getT(Class<T> clazz);
}
