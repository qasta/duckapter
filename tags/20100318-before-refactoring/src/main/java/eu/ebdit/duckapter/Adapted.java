package eu.ebdit.duckapter;


import java.lang.reflect.Method;
import java.util.Set;

/**
 * This interface gives additional info about adapted object.
 * Every instance returned by {@link Duckapter#adaptClass(Class, Class)}
 * and {@link Duckapter#adaptInstance(Object, Class)} are promised to implement 
 * this interface therefore each returned instance can be simply casted to
 * this interface:
 * 
 * <pre>{@code Adapted adapted = (Adapted) Ducktyper.adaptClass(Some.class, Other.class);}</pre>
 * 
 * @author Vladimir Orany
 *
 */
public interface Adapted {
	/**
	 * Returns set of all unimplemented methods from duck interface.
	 * Only makes sense for duck interface with optional methods.
	 * The method must return empty set otherwise.
	 * @return set of all unimplemented methods from duck interface
	 */
	Set<Method> getUnimplementedMethods();
	/**
	 * Return duck interface class of {@link Adapted} instance.
	 * @return duck interface class of {@link Adapted} instance.
	 */
	Class<?> getDuckedClass();
	/**
	 * Return original class of {@link Adapted} instance.
	 * @return original class of {@link Adapted} instance.
	 */
	Class<?> getOriginalClass();
	/**
	 * Returns original instance which was adapted or <code>null</code> if
	 * this instance of {@link Adapted} is for class.
	 * @return original instance which was adapted or <code>null</code> if
	 * this instance of {@link Adapted} is for class.
	 */
	Object getOriginalInstance();
	/**
	 * Returns <code>true</code> if this {@link Adapted} instance was created for class
	 * @return <code>true</code> if this {@link Adapted} instance was created for class
	 */
	boolean isForClass();
	
	/**
	 * Returns <code>true</code> if this {@link Adapted} instance was created for instance
	 * @return <code>true</code> if this {@link Adapted} instance was created for instance
	 */
	boolean isForInstance();
	
	
	/**
	 * Returns <code>true</code> if this {@link Adapted} has no unimplemented methods
	 * @return <code>true</code> if this {@link Adapted} has no unimplemented methods
	 */
	boolean isFullyImplemented();
}
