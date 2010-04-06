package org.duckapter;


public interface Adapted<O,D>{

	O getOriginalInstance();

	AdaptedClass<O,D> getAdaptedClass();
	
	D adaptInstance();
	D adaptClass();

}
