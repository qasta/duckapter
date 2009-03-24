package eu.ebdit.duckapter.duck.impl.full;


public class PropertyDuck {
	
	public static int count = 0;
	public final String name;
	
	public PropertyDuck() { this("Donald");}

	public PropertyDuck(String name) { this.name = name; count ++;}
	
	public void dive(){
		System.out.println(getClass().getName() + " is diving");
	}
	
	public static boolean canFly(){
		return true;
	}
	
	public static int getCount() {
		return count;
	}
	
	@Override
	public String toString() {
		return "Duck called " + name;
	}
}
