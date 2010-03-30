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

	@Override
	public String getMessage() {
		return "Adaptation fail! (Original class: "
				+ adapted.getAdaptedClass().getOriginalClass()
				+ ", duck interface: "
				+ adapted.getAdaptedClass().getDuckInterface()
				+ ")Unimplemented class methods: "
				+ adapted.getAdaptedClass().getUnimplementedForClass()
				+ ", Unimplemented methods for instance: "
				+ adapted.getAdaptedClass().getUnimplementedForInstance();
	}

}
