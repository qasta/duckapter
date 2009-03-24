package eu.ebdit.duckapter.selfeating;

import eu.ebdit.duckapter.annotation.Original;


public interface IComposite {

	IComposite getParent();
	void setParent(@Original IComposite parent);
	
}
