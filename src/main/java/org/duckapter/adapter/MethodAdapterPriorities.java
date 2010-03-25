package org.duckapter.adapter;

public final class MethodAdapterPriorities {

	public static int MIN = Integer.MIN_VALUE;
	public static int OPTIONAL = Integer.MIN_VALUE + 500;
	public static int NONE = OPTIONAL + 500;
	
	public static int FIELD = - 10000 ;
	public static int CONSTRUCTOR = - 5000;
	public static int METHOD = 0;
	
	public static int DEFAULT = Integer.MAX_VALUE - 1000;
	public static int ALL = Integer.MAX_VALUE - 1;
	public static int MAX = Integer.MAX_VALUE;
	
	private MethodAdapterPriorities() {
		// prevents instance creation and subtyping
	}
	
	
	
}
