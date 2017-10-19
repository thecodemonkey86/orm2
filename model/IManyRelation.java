package model;

public interface IManyRelation {

	int getDestColumnCount();

	Column getDestMappingColumn(int i);

	Table getDestTable();

	Table getSourceTable();

	String getAlias();


}
