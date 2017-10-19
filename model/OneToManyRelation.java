package model;

import java.util.ArrayList;

import util.Pair;

public class OneToManyRelation extends AbstractRelation implements IManyRelation{

	protected ArrayList<Pair<Column,Column>> srcToDestColumns;
	
	public OneToManyRelation(String alias) {
		super(alias);
		srcToDestColumns = new ArrayList<>();
	}
	
	
	public boolean add(Pair<Column, Column> e) {
		return srcToDestColumns.add(e);
	}

	
	
	public int getColumnCount() {
		return srcToDestColumns.size();
	}
	
	public Pair<Column,Column> getColumns(int index) {
		return srcToDestColumns.get(index);
	}


	@Override
	public int getDestColumnCount() {
		return getColumnCount();
	}


	@Override
	public Column getDestMappingColumn(int i) {
		return srcToDestColumns.get(i).getValue2();
	}
}
