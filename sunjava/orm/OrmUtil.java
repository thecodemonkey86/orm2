package sunjava.orm;

import database.relation.IManyRelation;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import generate.CodeUtil2;
import util.StringUtil;

public class OrmUtil {
	public static String getOneRelationDestAttrName(OneRelation relation) {
		return (relation.getColumnCount() == 1 && relation.getColumns(0).getValue1().getName().endsWith("_id")
				? CodeUtil2.camelCase(relation.getColumns(0).getValue1().getName().substring(0, relation.getColumns(0).getValue1().getName().length() - 3))
				: relation.getDestTable().getCamelCaseName());
	}

	public static String getOneToManyRelationDestAttrName(OneToManyRelation relation) {
		if (CodeUtil2.plural(relation.getDestTable().getCamelCaseName()).equals("trLists")) {
			System.out.println();
		}
		if (relation.getColumnCount() == 1 && relation.getColumns(0).getValue2().getName().endsWith(relation.getSourceTable() + "_id")) {
			String name = CodeUtil2.plural(CodeUtil2.camelCase(relation.getColumns(0).getValue2().getName().substring(0,
					relation.getColumns(0).getValue2().getName().length() - (relation.getSourceTable().getName() + "_id").length()) + relation.getDestTable()));
			System.out.println(name);
			return name;
		}

		return CodeUtil2.plural(relation.getDestTable().getCamelCaseName());
	}

	public static String getManyRelationDestAttrNameSingular(OneRelation relation) {
		if (relation.getColumnCount() == 1 && relation.getColumns(0).getValue2().getName().endsWith(relation.getSourceTable() + "_id")) {
			String name = CodeUtil2.camelCase(relation.getColumns(0).getValue2().getName().substring(0,
					relation.getColumns(0).getValue2().getName().length() - (relation.getSourceTable().getName() + "_id").length()) + relation.getDestTable());
			return name;
		}

		return relation.getDestTable().getCamelCaseName();
	}
	public static String getManyRelationDestAttrNameSingular(OneToManyRelation relation) {
		if (relation.getColumnCount() == 1 && relation.getColumns(0).getValue2().getName().endsWith(relation.getSourceTable() + "_id")) {
			String name = CodeUtil2.camelCase(relation.getColumns(0).getValue2().getName().substring(0,
					relation.getColumns(0).getValue2().getName().length() - (relation.getSourceTable().getName() + "_id").length()) + relation.getDestTable());
			return name;
		}

		return relation.getDestTable().getCamelCaseName();
	}

	public static String getOneRelationDestAttrGetter(OneRelation oneRelation) {
		// TODO Auto-generated method stub
		return "get" + StringUtil.ucfirst(getOneRelationDestAttrName(oneRelation));
	}

	public static String getManyRelationDestAttrName(ManyRelation relation) {
		if (relation.getDestColumnCount() == 1 && relation.getDestMappingColumn(0).getName().endsWith(relation.getSourceTable() + "_id")) {
			String name = CodeUtil2.plural(CodeUtil2.camelCase(relation.getDestMappingColumn(0).getName().substring(0,
					relation.getDestMappingColumn(0).getName().length() - (relation.getSourceTable().getName() + "_id").length()) + relation.getDestTable()));
			System.out.println(name);
			return name;
		}

		return CodeUtil2.plural(relation.getDestTable().getCamelCaseName());
	}

	public static String getManyRelationDestAttrNameSingular(IManyRelation relation) {
		if (relation.getDestColumnCount() == 1 && relation.getDestMappingColumn(0).getName().endsWith(relation.getSourceTable() + "_id")) {
			String name = CodeUtil2.camelCase(relation.getDestMappingColumn(0).getName().substring(0,
					relation.getDestMappingColumn(0).getName().length() - (relation.getSourceTable().getName() + "_id").length()) + relation.getDestTable());
			return name;
		}

		return relation.getDestTable().getCamelCaseName();
	}
	
	public static String getAddRelatedBeanMethodName(IManyRelation r) {
		return "add" + StringUtil.ucfirst(getManyRelationDestAttrNameSingular(r)); 
	}
	
	/*public static void addAssignValueFromResultSetInstructions(Var resultSet, InstructionBlock parentBlock, Expression assignTo, Column col,String alias) {
		
		if (col.isNullable()) {
			Var value = parentBlock._declare(BeanCls.getTypeMapper().columnToType(col),
					"value"
					, BeanCls.getTypeMapper().getResultSetValueGetter(resultSet, col, alias));
			IfBlock ifWasNull = parentBlock._if(resultSet.callMethod(ClsResultSet.METHOD_NAME_WAS_NULL));
				ifWasNull.getIfInstr()
				assignTo.accessAttr(Expressions.Null);
				
				ifWasNull.getElseInstr()
				._return(value);
		} else {
			
		}
		
		
	}*/
	

}
