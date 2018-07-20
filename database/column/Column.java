package database.column;

import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import database.table.AbstractTable;
import database.table.Table;
import util.CodeUtil2;

public abstract class Column {
	protected AbstractTable parentTable;
	protected String name;
	protected String dbType;
	protected boolean autoIncrement;
	protected boolean nullable;
	protected boolean enableRawValue;
	protected boolean isRelationSourceColumn;
	protected boolean isRelationDestColumn;
	protected int position;
	protected OneRelation oneRelation;
	protected OneToManyRelation oneToManyRelation;
	protected ManyRelation manyToManyRelation;
	protected String defaultValue;
	protected String overrideSelect;
	
	public int getPosition() {
		return position;
	}
	
	public void setPosition(int position) {
		this.position = position;
	}
	
	public Column(AbstractTable parentTable) {
		oneRelation = null;
		this.parentTable = parentTable;
	}
	
	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}
	
	public boolean isNullable() {
		return nullable;
	}
	
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
//	public void setPartOfPk(boolean partOfPk) {
//		this.partOfPk = partOfPk;
//	}
	
	public String getName() {
		return name;
	}
	
	public abstract String getEscapedName() ;
	
	public String getDbType() {
		return dbType;
	}
	
	public boolean isPartOfPk() {
		for(Column pkCol : parentTable.getPrimaryKey()) { 
			if(pkCol.equals(this)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public void setOneRelation(OneRelation relation) {
		if (this.oneRelation == null)
			this.oneRelation = relation;
	}
	
	public boolean hasOneRelation() {
		return oneRelation != null;
	}

	public boolean hasManyRelation() {
		return oneToManyRelation != null;
	}
	
//	public boolean hasOneRelation() {
//		return relation != null && relation.getRelationType() == RelationType.One;
//	}
	
	public String getCamelCaseName() {
		return CodeUtil2.camelCase(name);
	}
	
	public String getUc1stCamelCaseName() {
		return CodeUtil2.uc1stCamelCase(name);
	}
	
	public String getPluralCamelCaseName() {
		return CodeUtil2.plural(CodeUtil2.camelCase(name));
	}
	
	public void setAutoIncrement(boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
	}
	
	public boolean isAutoIncrement() {
		return autoIncrement;
	}
	
//	public Relation getRelation() {
//		return relation;
//	}
	
	public Table getOneRelatedForeignTable() {
		return oneRelation != null ? oneRelation.getDestTable() : null;
	}
	
//	public Type toType() {
//		return BeanCls.getDatabaseMapper().getTypeFromDbDataType(dbType, nullable);
//	}
	
	public OneRelation getOneRelation() {
		return oneRelation;
	}
	
	public OneToManyRelation getOneToManyRelation() {
		return oneToManyRelation;
	}
	
	
	public void setOneToManyRelation(OneToManyRelation manyRelation) {
		this.oneToManyRelation = manyRelation;
	}

	public Column getOneRelationMappedColumn() {
		if (oneRelation != null) {
			for(int i=0;i<oneRelation.getColumnCount();i++) {
				if (oneRelation.getColumns(i).getValue1().equals(this)) {
					return oneRelation.getColumns(i).getValue2();
				}
			}
		}
		return null;
	}
	
	
	public String getDefaultValue() {
		return defaultValue;
	}
	
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public void setManyToManyRelation(ManyRelation manyToManyRelation) {
		this.manyToManyRelation = manyToManyRelation;
	}
	
	public ManyRelation getManyToManyRelation() {
		return manyToManyRelation;
	}

	public boolean hasRelation() {
		return hasManyRelation() || hasOneRelation();
	}

//	public Expression getGenericDefaultValue() {
//		return BeanCls.getDatabaseMapper().getGenericDefaultValueExpression(this);
//	}
	
	public void setEnableRawValue(boolean enableRawValue) {
		this.enableRawValue = enableRawValue;
	}
	
	public boolean isRawValueEnabled() {
		return enableRawValue;
	}
	
	public void setOverrideSelect(String overrideSelect) {
		this.overrideSelect = overrideSelect;
	}
	
	public String getOverrideSelect() {
		return overrideSelect;
	}

	public boolean hasOverrideSelect() {
		return overrideSelect!=null;
	}

	public void setRelationSourceColumn(boolean isRelationSourceColumn) {
		this.isRelationSourceColumn = isRelationSourceColumn;
	}
	
	public boolean isRelationSourceColumn() {
		return isRelationSourceColumn;
	}
	
	public void setRelationDestColumn(boolean isRelationDestColumn) {
		this.isRelationDestColumn = isRelationDestColumn;
	}
	
	public boolean isRelationDestColumn() {
		return isRelationDestColumn;
	}
	
}
