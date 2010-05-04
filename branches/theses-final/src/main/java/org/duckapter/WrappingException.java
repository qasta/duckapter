package org.duckapter;

/**
 * {@link WrappingException} is thrown when the adaptation to the specified
 * duck interface failed. This usually happens in
 * {@link Duck#wrap(Class, Class)} or {@link Duck#wrap(Object, Class)} method.
 * 
 * @author Vladimir Orany
 * 
 */
public final class WrappingException extends RuntimeException {

	/**
	 * Generated UID.
	 */
	private static final long serialVersionUID = -1880673867743378698L;

	/**
	 * ObjectWrapper which caused this exception to be thrown.
	 */
	private final ObjectWrapper<?, ?> objectWrapper;

	/**
	 * Creates new instance of this exception with specified objectWrapper as its
	 * cause.
	 * 
	 * @param theAdapted
	 *            objectWrapper which caused this exception to be thrown
	 */
	public WrappingException(final ObjectWrapper<?, ?> theAdapted) {
		this.objectWrapper = theAdapted;
	}

	/**
	 * @return objectWrapper which causes this exception to be thrown
	 */
	public ObjectWrapper<?, ?> getAdapted() {
		return objectWrapper;
	}

	@Override
	public String getMessage() {
		return "Adaptation fail! (Original class: "
				+ objectWrapper.getAdaptedClass().getOriginalClass()
				+ ", duck interface: "
				+ objectWrapper.getAdaptedClass().getDuckInterface()
				+ ")Unimplemented class methods: "
				+ objectWrapper.getAdaptedClass().getUnimplementedForClass()
				+ ", Unimplemented methods for instance: "
				+ objectWrapper.getAdaptedClass().getUnimplementedForInstance();
	}

}
