package eu.ebdit.duckapter.selfeating;

import eu.ebdit.duckapter.annotation.Original;
import eu.ebdit.duckapter.annotation.Factory;

public interface ICompositeClass {
	@Factory IComposite getInstance(String name);
	@Factory IComposite getCopy(@Original IComposite comp);
}
