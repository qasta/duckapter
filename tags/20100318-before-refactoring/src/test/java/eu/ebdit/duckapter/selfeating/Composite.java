package eu.ebdit.duckapter.selfeating;

public class Composite {

	private Composite parent;
	private final String name;
	
	public Composite(String name) {this.name = name;}
	
	public Composite(Composite original){
		this.parent = copy(original.parent);
		this.name = original.name;
	}
	
	public void setParent(Composite parent) {
		this.parent = parent;
	}
	
	public Composite getParent() {
		return parent;
	}
	
	private Composite copy(Composite tbc){
		Composite copy = new Composite(tbc.name);
		if (tbc.parent != null) {
			copy.parent = new Composite(tbc.parent.name);
		}
		return tbc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
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
		Composite other = (Composite) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
		return true;
	}
	
	
	
}
