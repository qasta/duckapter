package eu.ebdit.duckapter.duck.partial;

import eu.ebdit.duckapter.annotation.Optional;
import eu.ebdit.duckapter.annotation.Static;

public interface IHalfOptional {

	@Static @Optional void optional();
	@Static void mandatory();
}
