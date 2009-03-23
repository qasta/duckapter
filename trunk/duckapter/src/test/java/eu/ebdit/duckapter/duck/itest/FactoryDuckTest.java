package eu.ebdit.duckapter.duck.itest;

import eu.ebdit.duckapter.duck.AbstractFullDuckTest;
import eu.ebdit.duckapter.duck.impl.full.DoSomethingElseDuck;


public class FactoryDuckTest extends AbstractFullDuckTest{

	public FactoryDuckTest() {
		super(DoSomethingElseDuck.class);
	}
	
}
