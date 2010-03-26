/**
 * 
 */
package org.duckapter.adapted;

final class Pair {
	final Class<?> original;
	final Class<?> duck;

	public Pair(Class<?> original, Class<?> duck) {
		this.original = original;
		this.duck = duck;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((duck == null) ? 0 : duck.hashCode());
		result = prime * result
				+ ((original == null) ? 0 : original.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pair other = (Pair) obj;
		if (duck == null) {
			if (other.duck != null)
				return false;
		} else if (!duck.equals(other.duck))
			return false;
		if (original == null) {
			if (other.original != null)
				return false;
		} else if (!original.equals(other.original))
			return false;
		return true;
	}

}