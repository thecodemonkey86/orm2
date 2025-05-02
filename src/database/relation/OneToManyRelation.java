package database.relation;

import java.util.ArrayList;

import database.column.Column;
import util.Pair;

public class OneToManyRelation extends AbstractRelation implements IManyRelation{

	protected ArrayList<Pair<Column,Column>> srcToDestColumns;
	protected boolean composition;
	public OneToManyRelation(String alias) {
		super(alias);
		srcToDestColumns = new ArrayList<>();
		composition = false;
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
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return sourceTable.getName()+"->"+destTable.getName();
	}
	
	public void setComposition(boolean composition) {
		this.composition = composition;
	}
	
	public boolean isComposition() {
		return composition;
	}
}
