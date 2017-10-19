package model;

public class AbstractRelation {
	protected RelationType relationType;
	protected Table sourceTable;
	protected Table destTable;
	protected String alias;
	
	public AbstractRelation(String alias) {
		this.alias = alias;
	}
	
	public void setRelationType(RelationType relationType) {
		this.relationType = relationType;
	}
	 

	public RelationType getRelationType() {
		return relationType;
	}
	
	public Table getSourceTable() {
		return sourceTable;
	}
	
	public Table getDestTable() {
		return destTable;
	}
	
	public String getAlias() {
		return alias;
	}
	
	public String getAlias(String prefix) {
		return prefix+ alias;
	}
	
	public void setSourceTable(Table srcTable) {
		this.sourceTable = srcTable;
	}
	
	public void setDestTable(Table destTable) {
		this.destTable = destTable;
	}
}
