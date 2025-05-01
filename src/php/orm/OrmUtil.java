package php.orm;

import database.relation.AbstractRelation;
import database.relation.IManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import php.core.Type;
import php.entity.Entities;
import php.entity.EntityCls;
import util.CodeUtil2;
import util.StringUtil;

public class OrmUtil {
	public static String getOneRelationDestAttrName(OneRelation relation) {
		return relation.hasSubstituteName() 
				?relation.getSubstituteNameSingular() 
				:(  (relation.getColumnCount() == 1 && relation.getColumns(0).getValue1().getName().endsWith("_id")
					? CodeUtil2.camelCase(relation.getColumns(0).getValue1().getName().substring(0, relation.getColumns(0).getValue1().getName().length() - 3))
					: relation.getDestTable().getCamelCaseName())
				);
	}

	public static String getOneToManyRelationDestAttrName(OneToManyRelation relation) {
		if(relation.hasSubstituteName() 
				)
			return relation.getSubstituteNamePlural();
		if (relation.getColumnCount() == 1 && relation.getColumns(0).getValue2().getName().endsWith(relation.getSourceTable() + "_id")) {
			String name = CodeUtil2.plural(CodeUtil2.camelCase(relation.getColumns(0).getValue2().getName().substring(0,
					relation.getColumns(0).getValue2().getName().length() - (relation.getSourceTable().getName() + "_id").length()) + relation.getDestTable()));
			return name;
		}

		return CodeUtil2.plural(relation.getDestTable().getCamelCaseName());
	}

//	public static String getManyRelationDestAttrNameSingular(OneRelation relation) {
//		if (relation.getColumnCount() == 1 && relation.getColumns(0).getValue2().getName().endsWith(relation.getSourceTable() + "_id")) {
//			String name = CodeUtil2.camelCase(relation.getColumns(0).getValue2().getName().substring(0,
//					relation.getColumns(0).getValue2().getName().length() - (relation.getSourceTable().getName() + "_id").length()) + relation.getDestTable());
//			return name;
//		}
//
//		return relation.getDestTable().getCamelCaseName();
//	}
	public static String getOneToManyRelationDestAttrNameSingular(OneToManyRelation relation) {
		if(relation.hasSubstituteName() 
				)
			return relation.getSubstituteNameSingular(); 
		if (relation.getColumnCount() == 1 && relation.getColumns(0).getValue2().getName().endsWith(relation.getSourceTable() + "_id")) {
			String name = CodeUtil2.camelCase(relation.getColumns(0).getValue2().getName().substring(0,
					relation.getColumns(0).getValue2().getName().length() - (relation.getSourceTable().getName() + "_id").length()) + relation.getDestTable());
			return name;
		}

		return relation.getDestTable().getCamelCaseName();
	}

	public static String getOneRelationDestAttrGetter(OneRelation oneRelation) {
		return "get" + StringUtil.ucfirst(getOneRelationDestAttrName(oneRelation));
	}

	public static String getManyRelationDestAttrName(IManyRelation relation) {
		if(relation.hasSubstituteName() 
				)
			return relation.getSubstituteNamePlural(); 
		if (relation.getDestColumnCount() == 1 && relation.getDestMappingColumn(0).getName().endsWith(relation.getSourceTable() + "_id")) {
			String name = CodeUtil2.plural(CodeUtil2.camelCase(relation.getDestMappingColumn(0).getName().substring(0,
					relation.getDestMappingColumn(0).getName().length() - (relation.getSourceTable().getName() + "_id").length()) + relation.getDestTable()));
			System.out.println(name);
			return name;
		}

		return CodeUtil2.plural(relation.getDestTable().getCamelCaseName());
	}

	public static String getManyRelationDestAttrNameSingular(IManyRelation relation) {
		if(relation.hasSubstituteName() 
				)
			return relation.getSubstituteNameSingular(); 
		if (relation.getDestColumnCount() == 1 && relation.getDestMappingColumn(0).getName().endsWith(relation.getSourceTable() + "_id")) {
			String name = CodeUtil2.camelCase(relation.getDestMappingColumn(0).getName().substring(0,
					relation.getDestMappingColumn(0).getName().length() - (relation.getSourceTable().getName() + "_id").length()) + relation.getDestTable());
			return name;
		}

		return relation.getDestTable().getCamelCaseName();
	}
	
	public static String getAddRelatedEntityMethodName(IManyRelation r) {
		return "add" + StringUtil.ucfirst(getManyRelationDestAttrNameSingular(r)); 
	}
	
	public static Type getRelationForeignPrimaryKeyType(AbstractRelation r) {
		Type entityPk = null;
		if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
			entityPk = Entities.get(r.getDestTable().getUc1stCamelCaseName()).getPkType();
			
		} else {
			entityPk = EntityCls.getTypeMapper().columnToType( r.getDestTable().getPrimaryKey().getColumns().get(0));
		}
		return entityPk;
	}
	

}
