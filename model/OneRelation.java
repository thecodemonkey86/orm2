package model;

import java.util.ArrayList;

import codegen.CodeUtil;
import util.Pair;



public class OneRelation extends AbstractRelation{
	
	
	protected ArrayList<Pair<Column,Column>> srcToDestColumns;
	
	public OneRelation(String alias) {
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
	public String toString() {
		return sourceTable + "->" + relationType + CodeUtil.parentheses(destTable);
	}

	public boolean isPartOfPk() {
		for(Pair<Column, Column> c:srcToDestColumns) {
			if (c.getValue1().isPartOfPk()) {
				return true;
			}
		}
		return false;
	}
	
	
}
