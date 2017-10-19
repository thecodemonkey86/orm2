package model;

public class ManyRelation extends AbstractRelation implements IManyRelation{
	MappingTable mappingTable;
	M2MColumns sourceMappings;
	M2MColumns destMappings;
	
	public ManyRelation(String alias) {
		super(alias);		
	}
	
	public void setMappingTable(MappingTable mappingTable) {
		this.mappingTable = mappingTable;
		setRelationType(RelationType.ManyToX);
	}
	
	public MappingTable getMappingTable() {
		return mappingTable;
	}
	
	public void setSourceMappings(M2MColumns sourceMappings) {
		this.sourceMappings = sourceMappings;
	}
	
	public void setDestMappings(M2MColumns destMappings) {
		this.destMappings = destMappings;
	}
	
	public M2MColumns getSourceMappings() {
		return sourceMappings;
	}
	
	public M2MColumns getDestMappings() {
		return destMappings;
	}

	public String getMappingAlias() {
		return alias+"mapping";
	}
	
	public String getMappingAlias(String prefix) {
		return prefix+ alias+"mapping";
	}
	
	public int getSourceColumnCount() {
		return sourceMappings.getColumnCount();
	}
	public int getDestColumnCount() {
		return destMappings.getColumnCount();
	}
	
	public Column getSourceEntityColumn(int index) {
		return sourceMappings.getEntityColumn(index);
	}
	public Column getSourceMappingColumn(int index) {
		return sourceMappings.getMappingColumn(index);
	}
	public Column getDestEntityColumn(int index) {
		return destMappings.getEntityColumn(index);
	}
	public Column getDestMappingColumn(int index) {
		return destMappings.getMappingColumn(index);
	}
}
