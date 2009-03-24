package eu.ebdit.duckapter;


import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import eu.ebdit.duckapter.annotation.Alias;
import eu.ebdit.duckapter.annotation.Constructor;
import eu.ebdit.duckapter.annotation.Optional;
import eu.ebdit.duckapter.annotation.Original;
import eu.ebdit.duckapter.annotation.Property;
import eu.ebdit.duckapter.annotation.Static;
import eu.ebdit.duckapter.annotation.Factory;

/**
 * Duckapter brings support for duck typing into the java programming language.<br />
 * Duckapter has two methods {@link #canAdaptInstance(Object, Class)} and
 * {@link #canAdaptClass(Class, Class)} for finding out whether particular class
 * or instance can be "duck typed" or adapted to the given interface. And two
 * methods {@link #adaptClass(Class, Class)} and
 * {@link #adaptInstance(Object, Class)} which performs the adaptation to the
 * interface.<br />
 * Methods {@link #canAdaptInstance(Object, Class)} and
 * {@link #adaptInstance(Object, Class)} can adapt existing objects. Methods
 * {@link #canAdaptClass(Class, Class)} and {@link #adaptInstance(Object, Class)}
 * adapts class object to access static methods and properties but also constructors.<br/>
 * @author vladimir.orany
 */
public class Duckapter {
	
	/**
	 * Tries to adapt existing instance to the interface given as <code>duck</code>.<br/>
	 * <code>duck</code> interface can be any interface. This means that <code>duck</code> interface
	 * can also has methods annotated with {@link Static}, {@link Factory} and {@link Constructor}, but
	 * its usually better to keep this methods separately.</br> 
	 * <code>original</code> must implement
	 * all demanded methods and properties to satisfy {@link #canAdaptInstance(Object, Class)} method.
	 * <br/>
	 * If <code>duck</code> interface has {@link Optional} methods they will throw {@link UnsupportedOperationException}
	 * when called but not implemented for given instance.
	 * <br/>
	 * Both parameters must not be null.
	 * @param <T> interface to be adapted to and returned
	 * @param original instance to be adapted to specified <code>duck</code> interface
	 * @param class of duck interface to be adapted to and returned. Must be an interface.
	 * @return original instance adapted to given <code>duck</code> interface which also implements {@link Adapted} interface
	 * @throws IllegalArgumentException if {@link #canAdaptInstance(Object, Class)} returns false for given parameters
	 */
	public static <T> T adaptInstance(Object original, Class<T> duck){
		assertCanDuck(original, duck);
		return duck.cast(Proxy.newProxyInstance(Duckapter.class.getClassLoader(), new Class[] {duck, Adapted.class}, 
				createInvocationHandler(AdaptedImpl.forInstance(original, duck, hasOptionalMethods(duck) ? getUnimplementedMethods(original.getClass(), duck, false) :  new HashSet<Method>()))));
	}
	
	
	/**
	 * Tries to adapt class to the interface given as <code>duck</code>.
	 * <code>duck</code> interface must have only methods annotated with {@link Static}, {@link Factory} and {@link Constructor}
	 * which are supposed to be access on the class level. <code>original</code> class must have all this method
	 * to satisfy {@link #canAdaptClass(Class, Class)} method.
	 * 
	 * <br/>
	 * If <code>duck</code> interface has {@link Optional} methods they will throw {@link UnsupportedOperationException}
	 * when called but not implemented for given class.
	 * All return types of <code>duckInterface</code> must be same as on <code>original</code> or must be also
	 * able to be adapted.
	 * <br/>
	 * Both parameters must not be null.
	 * @param <T> interface to be adapted to and returned
	 * @param original original class to be adapted to specified <code>duck</code> interface
	 * @param duckInterface	class of duck interface to be adapted and returned. Must be an interface.
	 * @return proxy instance mapping constructors, static methods and properties to the given <code>duck</code> interface and which also implements {@link Adapted} interface
	 * @throws IllegalArgumentException if {@link #canAdaptClass(Class, Class)} returns false for given parameters
	 */
	public static <T> T adaptClass(Class<?> original, Class<T> duckInterface){
		assertClassCanDuck(original, duckInterface);
		return duckInterface.cast(Proxy.newProxyInstance(original.getClassLoader(), new Class[] {duckInterface, Adapted.class}, 
				createInvocationHandler(AdaptedImpl.forClass(original, duckInterface, (hasOptionalMethods(duckInterface) ? getUnimplementedMethods(original, duckInterface, true) : new HashSet<Method>())))));
	}
	
	/**
	 * Determines whether particular class can be adapted to the given interface.
	 * All methods of <code>duckInterface</code> must be annotated with {@link Static}, {@link Factory} or {@link Constructor} or
	 * the interface itself must be annotated with {@link Static}.
	 * <br/>
	 * <code>duckInterface</code> must be an interface.
	 * <br/>
	 * All methods not declared as {@link Optional} must be implemented by <code>original</code> class.
	 * <br/>
	 * All return types of <code>duckInterface</code> must be same as on <code>original</code> or must be also
	 * able to be adapted.
	 * @param original class we want to determine whether is adaptable for given <code>duckInterface</code>
	 * @param duckInterface interface we want to determine whether <code>original</code> class can be adapted to
	 * @return <code>true</code> if <code>duckInterface</code> is an interface and all methods not declared as {@link Optional} are implemented by <code>original</code> class
	 */
	public static boolean canAdaptClass(Class<?> original, Class<?> duckInterface) {
		return canAdapt(original, duckInterface, true).canAdapt;
	}
	
	/**
	 * Determines whether particular instance can be adapted to the given interface.
	 * <br/>
	 * <code>duckInterface</code> must be an interface.
	 * <br/>
	 * All methods not declared as {@link Optional} must be implemented by <code>original</code> instance.
	 * @param original instance we want to determine whether is adaptable for given <code>duckInterface</code>
	 * @param duckInterface interface we want to determine whether <code>original</code> instance can be adapted to
	 * @return <code>true</code> if <code>duckInterface</code> is an interface and all methods not declared as {@link Optional} are implemented by <code>original</code> instance
	 */
	public static boolean canAdaptInstance(Object original, Class<?> duckInterface) {
		return canAdapt(original.getClass(), duckInterface, false).canAdapt;
	}
	
	private static boolean hasOptionalMethods(Class<?> duck){
		if (duck.isAnnotationPresent(Optional.class)) {
			return true;
		}
		for (Method m : duck.getMethods()) {
			if (m.isAnnotationPresent(Optional.class)) {
				return true;
			}
		}
		return false;
	}
	
	private static DuckingInfo canAdapt(Class<?> original, Class<?> duck, boolean onlyStaticAllowed){
		// now we can only duck interface!
		if (!duck.isInterface()) {
			return new DuckingInfo(false, "Duck class in not interaface!");
		}
		// if is instance of, we can duck
		if (!onlyStaticAllowed) {
			if (duck.isAssignableFrom(original)) {
				return new DuckingInfo(true, "Duck class can be assigned!");
			}
		}
		// ok, we have interface, which can be optional
		if (duck.isAnnotationPresent(Optional.class)) {
			return new DuckingInfo(true, "Duck class has optional implementations!");
		}
		return hasDemandedMethods(original, duck, onlyStaticAllowed);
	}
	
	private static DuckingInfo hasDemandedMethods(Class<?> original, Class<?> duck, boolean onlyStaticAllowed){
		for (Method m : duck.getMethods()) {
			return isMethodImplemented(m, original, duck, onlyStaticAllowed, true);
		}
		return new DuckingInfo(true, "All methods implemented");
		
	}

	private static DuckingInfo isMethodImplemented(Method m, Class<?> original,
			Class<?> duck, boolean onlyStaticAllowed, boolean ignoreOptional) {
		if (ignoreOptional && m.isAnnotationPresent(Optional.class)) {
			return new DuckingInfo(true, "Method is optional and will be ignored");
		}
		if (onlyStaticAllowed && !hasStaticAnnotation(m)) {
			return new DuckingInfo(false, "Only static implementations are allowed and method  " + m + " is not declared with " + Static.class.getName() + " anotations");
		}
		if (m.isAnnotationPresent(Constructor.class)) {
			if (findConstructor(m, original, duck) == null) {
				return new DuckingInfo(false, "No constructor found for " + m);
			}
		} else if (m.isAnnotationPresent(Factory.class)) {
			if (findMethod(m, original) == null && findConstructor(m, original, duck) == null) {
				return new DuckingInfo(false, "No method or constructor found for " + m);
			}
		} else {
			if (findMethod(m, original) == null && findField(m, original) == null) {
				return new DuckingInfo(false, "No method or field found for " + m);
			}
		}
		return new DuckingInfo(true, "Method is implemented");
	}
	
	private static Set<Method> getUnimplementedMethods(Class<?> original, Class<?> duck,
			boolean onlyStaticAllowed){
		Set<Method> ret = new HashSet<Method>();
		for (Method method : duck.getMethods()) {
			if (!isMethodImplemented(method, original, duck, onlyStaticAllowed, false).canAdapt) {
				ret.add(method);
			}
		}
		return Collections.unmodifiableSet(ret);
	}

	private static boolean hasStaticAnnotation(Method m) {
		return m.isAnnotationPresent(Static.class) || m.getDeclaringClass().isAnnotationPresent(Static.class) || m.isAnnotationPresent(Factory.class) || m.isAnnotationPresent(Constructor.class);
	}
	
	private static Method findMethod(Method m, Class<?> original) {
		Method met = null;
		// this isn't the best way to do it, but it's simplist possible
		boolean mustBeStatic = m.isAnnotationPresent(Static.class) || m.getDeclaringClass().isAnnotationPresent(Static.class);
		Class<?>[] parameterTypes = handleParameters(m, original);
		List<String> possibleName = new ArrayList<String>();
		possibleName.add(m.getName());
		if (m.isAnnotationPresent(Alias.class)) {
			possibleName.addAll(Arrays.asList(m.getAnnotation(Alias.class).value()));
		}
		if (m.isAnnotationPresent(Property.class)) {
			List<String> possiblePropNames = new ArrayList<String>();
			possiblePropNames.add(getPropertyName(m.getName()));
			if (m.isAnnotationPresent(Alias.class)) {
				for (String value : m.getAnnotation(Alias.class).value()) {
					possiblePropNames.add(getPropertyName(value));
				}
			}
			for (String propName : possiblePropNames) {
				if (isPotentialGetter(m)) {
					if (boolean.class.equals(m.getReturnType()) || Boolean.class.equals(m.getReturnType())) {
						possibleName.add(getGetterOrSetterName("is", propName));
					}
					possibleName.add(getGetterOrSetterName("get", propName));
				}
				if (isPotentialSetter(m, original)) {
					possibleName.add(getGetterOrSetterName("set", propName));
				}
			}
		}
		
		for (String methodName : possibleName) {
			met =  tryFindMethod(original, methodName, mustBeStatic, parameterTypes, m.getReturnType());
			if (met != null) {
				return met;
			}
		}
		return met;
		
	}

	private static String getGetterOrSetterName(String prefix, String propName) {
		return prefix + propName.substring(0,1).toUpperCase().concat(propName.substring(1));
	}

	private static Method tryFindMethod(Class<?> original, String methodName,
			boolean mustBeStatic, Class<?>[] parameterTypes, Class<?> returnType){
		Method met;
		try {
			met = original.getMethod(methodName, parameterTypes);
			if (Modifier.isStatic(met.getModifiers()) == mustBeStatic
					&& (returnType.isAssignableFrom(met.getReturnType()) || original.equals(met.getReturnType()) || canAdapt(met.getReturnType(), returnType, false).canAdapt)
			) {
				return met;
			}
		} catch (SecurityException e) {
			// ok no method - not the best way, we'll try to find something smarter
		} catch (NoSuchMethodException e) {
			// ok no method - not the best way, we'll try to find something smarter
		}
		return null;
	}

	private static Field findField(Method m, Class<?> original){
			if (m.isAnnotationPresent(Property.class)) {
				String propName =  getPropertyName(m.getName()) ;
				List<String> names = new ArrayList<String>();
				names.add(propName);
				if (m.isAnnotationPresent(Alias.class)) {
					names.addAll(Arrays.asList(m.getAnnotation(Alias.class).value()));
				}
				for (String name : names) {
					try {
						Field f = original.getField(name);
						if ((!isFieldSetter(m, f, original) && !isFieldGetter(m, f)) || Modifier.isStatic(f.getModifiers()) != m.isAnnotationPresent(Static.class)) {
							continue;
						}
						return f;
					} catch (SecurityException e) {
						// ok for now, we missed, but find better find to resolve this!
					} catch (NoSuchFieldException e) {
						// ok for now, we missed, but find better find to resolve this!
					}

				}
				

			}
		return null;
	}
	

	private static String getPropertyName(String name) {
		String fromPattern =  tryPattern(name, "(get|is|set)?(\\w+)");
		if (fromPattern != null) {
			return fromPattern.substring(0,1).toLowerCase().concat(fromPattern.substring(1));
		}
		return name;
	}
	private static String tryPattern(String name, String pattern) {
		Pattern getter = Pattern.compile(pattern);
		Matcher getterMatcher = getter.matcher(name);
		if (getterMatcher.matches()) {
			return getterMatcher.group(2);
		}
		return null;
	}

	private static boolean isFieldGetter(Method m, Field f) {
		return isPotentialGetter(m) && f.getType().isAssignableFrom(m.getReturnType());
	}

	private static boolean isPotentialGetter(Method m) {
		return !m.getReturnType().equals(void.class) && m.getTypeParameters().length == 0;
	}

	private static boolean isPotentialSetter(Method m, Class<?> original) {
		return m.getReturnType().equals(void.class) && handleParameters(m, original).length == 1;
	}

	private static boolean isFieldSetter(Method m, Field f, Class<?> original) {
		return isPotentialSetter(m, original)&& !Modifier.isFinal(f.getModifiers()) && f.getType().isAssignableFrom(handleParameters(m, original)[0]);
	}

	private static Class<?>[] handleParameters(Method m, Class<?> original) {
		Class<?>[] params = m.getParameterTypes();
		if (params.length == 0) {
			return params;
		}
		Class<?>[] newParams = Arrays.copyOf(params, params.length);
		Annotation[][] annos =  m.getParameterAnnotations();
		for (int i = 0; i < params.length; i++) {
			for (int j = 0; j < annos[i].length; j++) {
				if (annos[i][j] instanceof Original) {
					newParams[i] = original;
				}
			}
		}
		return newParams;
	}

	private static <T> java.lang.reflect.Constructor<T> findConstructor(Method m, Class<T> original, Class<?> duck) {
		// this isn't the best way to do it, but it's simplist possible
		if (duck.equals(m.getReturnType()) || canAdapt(original, m.getReturnType(), false).canAdapt) {
			try {
				java.lang.reflect.Constructor<T> con = original.getConstructor(handleParameters(m, original));
				return con;
			} catch (SecurityException e) { // lookup failed
			} catch (NoSuchMethodException e) {// lookup failed
			}
		}
		return null;
	}

	private static void assertClassCanDuck(Class<?> original, Class<?> duck){
		DuckingInfo di = canAdapt(original, duck, true);
		if (!di.canAdapt) {
			throw new IllegalArgumentException(di.info);
		}
	}
	
	private static void assertCanDuck(Object o, Class<?> duck){
		DuckingInfo di = canAdapt(o.getClass(), duck, false);
		if (!di.canAdapt) {
			throw new IllegalArgumentException(di.info);
		}
	}
	
	
	private static InvocationHandler createInvocationHandler(Adapted duckTyped){
		return new DuckInvocationHandler(duckTyped);
	}
	
	private static final class DuckInvocationHandler implements
			InvocationHandler {
		private final Adapted duckAdapted;
		
		public DuckInvocationHandler(Adapted duckTyped) {
			this.duckAdapted = duckTyped;
		}

		@Override
		public Object invoke(Object proxy, Method m, Object[] args)
				throws Throwable {
			if (m.getDeclaringClass().equals(Adapted.class)) {
				return m.invoke(duckAdapted, args);
			}
			if (duckAdapted.getDuckedClass().isAssignableFrom(duckAdapted.getOriginalClass())) {
				return m.invoke(duckAdapted.getOriginalInstance(), args);
			}
			if (m.getDeclaringClass().equals(Object.class)) {
				return m.invoke(duckAdapted.getOriginalInstance(), unwrapDuckedArgs(m, args, duckAdapted) );
			}
			if (m.getDeclaringClass().isAssignableFrom(duckAdapted.getDuckedClass())) {
				Class<?> retType = m.getReturnType();
				if (m.isAnnotationPresent(Constructor.class)) {
					java.lang.reflect.Constructor<?> constructor =  findConstructor(m, duckAdapted.getOriginalClass(), duckAdapted.getDuckedClass());
					if (constructor == null) {
						throwUnsupportedOperationExcetpion(m);
					}
					return handleConstructor(m, constructor, args, retType);
				} else if (m.isAnnotationPresent(Factory.class)) {
					Method method = findMethod(m, duckAdapted.getOriginalClass());
					if (method != null) {
						return handleReturnValue(handleInvokeAndArgs(m, method, args), method.getReturnType(), retType);
					}
					java.lang.reflect.Constructor<?> constructor =  findConstructor(m, duckAdapted.getOriginalClass(), duckAdapted.getDuckedClass());
					if (constructor == null) {
						throwUnsupportedOperationExcetpion(m);
					}
					return handleConstructor(m, constructor, args, retType);
					
					
				} else {
					Method method = findMethod(m, duckAdapted.getOriginalClass());
					if (method != null) {
						return handleReturnValue(handleInvokeAndArgs(m, method, args), method.getReturnType(), retType);
					}
					Field field = findField(m, duckAdapted.getOriginalClass());
					if (field == null) {
						throwUnsupportedOperationExcetpion(m);
					}
					if (isFieldGetter(m, field)) {
						return handleReturnValue(field.get(duckAdapted.getOriginalInstance()), field.getType(), retType);
					}
					if (isFieldSetter(m, field, duckAdapted.getOriginalClass())) {
						field.set(duckAdapted.getOriginalInstance(), args[0]);
						return Void.TYPE;
					}
				}
			}
			throw new UnsupportedOperationException("Cannot invoke right method");
		}

		private void throwUnsupportedOperationExcetpion(Method m) {
			throw new UnsupportedOperationException("Method " + m.toString() + " is not supported!");
		}

		private Object handleConstructor(Method m,
				java.lang.reflect.Constructor<?> constructor, Object[] args,
				Class<?> retType) throws InstantiationException,
				IllegalAccessException, InvocationTargetException {
			return handleReturnValue(constructor.newInstance(handleArgs(m.getParameterAnnotations(), args, duckAdapted)), constructor.getDeclaringClass(), retType);
		}

		private Object handleInvokeAndArgs(Method interfacedMethod, Method originalInstanceMethod,
				Object[] args) throws IllegalAccessException,
				InvocationTargetException {
			return handleInvoke(originalInstanceMethod, handleArgs(interfacedMethod.getParameterAnnotations(), args, duckAdapted));
		}

		private Object handleInvoke(Method method, Object[] args)
				throws IllegalAccessException, InvocationTargetException {
			
			return method.invoke(duckAdapted.getOriginalInstance(), args);
		}

		private Object[] unwrapDuckedArgs(Method m, Object[] args,
				Adapted duckAdapted) {
			if (args == null || args.length == 0) {
				return args;
			}
			Object[] newArgs = Arrays.copyOf(args, args.length);
			for (int i = 0; i < args.length; i++) {
				if (args[i] instanceof Adapted) {
					newArgs[i] = ((Adapted)args[i]).getOriginalInstance();
					
				}
			}
			return newArgs;
		}
		
		private Object[] handleArgs(Annotation[][] annos, Object[] args,
				Adapted duckAdapted) {
			if (args == null || args.length == 0) {
				return args;
			}
			Object[] newArgs = Arrays.copyOf(args, args.length);
			for (int i = 0; i < args.length; i++) {
				for (int j = 0; j < annos[i].length; j++) {
					if (annos[i][j] instanceof Original) {
						newArgs[i] = ((Adapted)args[i]).getOriginalInstance();
					}
				}
			}
			return newArgs;
		}

		private Object handleReturnValue(Object retValue, Class<?> retValueType, Class<?> desiredType) {
			if (retValueType.isAssignableFrom(desiredType)) {
				return retValue;
			} else {
				return adaptInstance(retValue, desiredType);
			}
		}
	}
	
	private static class DuckingInfo{
	
		public final boolean canAdapt;
		public final String info;
		public DuckingInfo(boolean canDuck, String info) {
			this.canAdapt = canDuck;
			this.info = info;
		}

		
		
	}

}
