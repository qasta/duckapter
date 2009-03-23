package eu.ebdit.duckapter.property;

public class PropertyTestClass {

	public static String staticStringField;
	
	public int intField;
	
	private static boolean bool;
	
	private String something;
	
	
	public String getProp(){ return something; }
	public void setProp(String s) { something = s; }
	
	public static boolean getBool(){return bool;}
	
	public static void setBool(boolean bool) {PropertyTestClass.bool = bool;}
	
}
