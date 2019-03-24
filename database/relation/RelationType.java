package database.relation;

public enum RelationType {
	OneToMany("Collection"),One("Attribute"),ManyToX("ManyToX");
	
	private String name;
	
	private RelationType(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}