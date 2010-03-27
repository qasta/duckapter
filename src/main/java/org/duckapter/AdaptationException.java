package org.duckapter;

public class AdaptationException extends RuntimeException {

	private static final long serialVersionUID = -1880673867743378698L;
	private final Adapted<?, ?> adapted;

	public AdaptationException(Adapted<?, ?> adapted) {
		this.adapted = adapted;
	}
	
	public Adapted<?, ?> getAdapted() {
		return adapted;
	}
	
}
