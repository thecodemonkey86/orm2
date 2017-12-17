package php.orm;

import database.relation.AbstractRelation;
import database.relation.IManyRelation;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import php.bean.BeanCls;
import php.bean.Beans;
import php.core.Type;
import util.CodeUtil2;
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
	
	public static Type getRelationForeignPrimaryKeyType(AbstractRelation r) {
		Type beanPk = null;
		if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
			beanPk = Beans.get(r.getDestTable().getUc1stCamelCaseName()).getPkType();
			
		} else {
			beanPk = BeanCls.getTypeMapper().columnToType( r.getDestTable().getPrimaryKey().getColumns().get(0));
		}
		return beanPk;
	}
	

}
