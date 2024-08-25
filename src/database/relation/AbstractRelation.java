package database.relation;

import java.util.HashSet;
import java.util.Set;

import database.table.Table;
import util.StringUtil;

public class AbstractRelation {
	public static enum RelationSqlOptions{disableOnConflictDoNothing};
	protected RelationType relationType;
	protected Table sourceTable;
	protected Table destTable;
	protected String alias;
	protected String substituteNameSingular;
	protected String substituteNamePlural;
	protected String additionalJoin;
	protected String additionalOrderBy;
	protected Set<RelationSqlOptions> sqlOptions;
	
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
	
	public void setSubstituteNamePlural(String substituteNamePlural) {
		this.substituteNamePlural = substituteNamePlural;
	}
	
	public String getSubstituteNamePlural() {
		return substituteNamePlural;
	}
	
	public boolean hasSubstituteName() {
		return substituteNameSingular != null;
	}
	
	public void setSubstituteNameSingular(String substituteNameSingular) {
		this.substituteNameSingular = substituteNameSingular;
	}
	
	public String getSubstituteNameSingular() {
		return substituteNameSingular;
	}
	
	public void setSqlOption(RelationSqlOptions opt) {
		if(this.sqlOptions == null) {
			this.sqlOptions = new HashSet<>();
		}
		this.sqlOptions.add(opt);
	}
	
	public String getAdditionalJoin() {
		return additionalJoin;
	}
	public void setAdditionalJoin(String additionalJoin) {
		this.additionalJoin = additionalJoin;
	}
	
	public String getAdditionalOrderBy() {
		return additionalOrderBy;
	}
	
	public void setAdditionalOrderBy(String additionalOrderBy) {
		this.additionalOrderBy = additionalOrderBy;
	}
	
	public boolean hasAdditionalJoin() {
		return additionalJoin!=null;
	}

	public boolean hasAdditionalOrderBy() {
		return additionalOrderBy!=null;
	}

	public String getIdentifier() {
		return destTable.getCamelCaseName()+StringUtil.ucfirst(alias);
	}
}
