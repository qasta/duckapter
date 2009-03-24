package eu.ebdit.duckapter;


import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;

class AdaptedImpl implements Adapted{

	private final Class<?> originalClass;
	private final Object original;
	private final Class<?> duck;
	private final Set<Method> unimplemented;
	
	
	
	private AdaptedImpl(Object original, Class<?> originalClass, Class<?> duck, Set<Method> unimplemented) {
		this.originalClass = originalClass;
		this.original = original;
		this.duck = duck;
		this.unimplemented = Collections.unmodifiableSet(unimplemented);
		
	}
	
	static Adapted forInstance(Object o, Class<?> duck, Set<Method> unimplemented){
		return new AdaptedImpl(o, o.getClass(), duck, unimplemented);
	}
	
	static Adapted forClass(Class<?> original, Class<?> duck, Set<Method> unimplemented){
		return new AdaptedImpl(null, original, duck, unimplemented);
	}

	@Override
	public Class<?> getDuckedClass() {
		return duck;
	}

	@Override
	public Class<?> getOriginalClass() {
		return originalClass;
	}

	@Override
	public Object getOriginalInstance() {
		return original;
	}

	@Override
	public Set<Method> getUnimplementedMethods() {
		return unimplemented;
	}

	@Override
	public boolean isForClass() {
		return original == null;
	}

	@Override
	public boolean isForInstance() {
		return original != null;
	}
	
	@Override
	public boolean isFullyImplemented() {
		return unimplemented.isEmpty();
	}

}
