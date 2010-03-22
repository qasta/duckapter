package org.duckapter.duck.impl.full;

public class PerfectDuck {

	public static int count = 0;

	private final String name;

	public PerfectDuck() {
		this("Donald");
	}

	public PerfectDuck(String name) {
		this.name = name;
		count++;
	}

	public String getName() {
		return name;
	}

	public void dive() {
		System.out.println(getClass().getName() + " is diving");
	}

	public void doSomething() {
		System.out.println(getClass().getName() + " done something");
	}

	public static boolean canFly() {
		return true;
	}

	@Override
	public String toString() {
		return "Duck called " + getName();
	}
}
