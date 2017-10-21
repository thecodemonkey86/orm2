package util.mysql;

import generate.CodeUtil2;
import cpp.bean.BeanCls;
import cpp.core.Attr;
import cpp.core.ConstRef;
import cpp.core.Method;
import cpp.core.Ref;
import cpp.core.SharedPtr;
import cpp.core.expression.Expression;
import database.column.Column;
import database.relation.OneRelation;

public class MysqlUtil {
	public static Method mysqlPhpConvertMethod(String dbtype) {
		throw new RuntimeException("type" + dbtype+" not implemented");
	}
	
	public static String getOneRelationDestAttrName(OneRelation relation) {
		return (relation.getColumnCount() == 1 && relation.getColumns(0).getValue1().getName().endsWith("_id")
				? CodeUtil2.camelCase(relation.getColumns(0).getValue1().getName().substring(0, relation.getColumns(0).getValue1().getName().length()-3))
				: relation.getDestTable().getCamelCaseName());
	}
	
	public static String getManyRelationDestAttrName(OneRelation relation) {
		if (CodeUtil2.plural(relation.getDestTable().getCamelCaseName()).equals("trLists")) {
			System.out.println();
		}
		if (relation.getColumnCount() == 1 && relation.getColumns(0).getValue2().getName().endsWith(relation.getSourceTable()+"_id")) {
			String name= CodeUtil2.plural(
					CodeUtil2.camelCase(relation.getColumns(0).getValue2().getName().substring(0,relation.getColumns(0).getValue2().getName().length()- (relation.getSourceTable().getName()+"_id") .length()) + relation.getDestTable()));
			System.out.println(name);
			return name;
		}
		
		
		return		 CodeUtil2.plural(relation.getDestTable().getCamelCaseName());
	}
	public static String getManyRelationDestAttrNameSingular(OneRelation relation) {
		if (relation.getColumnCount() == 1 && relation.getColumns(0).getValue2().getName().endsWith(relation.getSourceTable()+"_id")) {
			String name= 
					CodeUtil2.camelCase(relation.getColumns(0).getValue2().getName().substring(0,relation.getColumns(0).getValue2().getName().length()- (relation.getSourceTable().getName()+"_id") .length()) + relation.getDestTable());
			return name;
		}
		
		
		return relation.getDestTable().getCamelCaseName();
	}
	
	public static Expression getPkExpression(Expression e, Column colPk) {
		BeanCls cls= e.getType() instanceof BeanCls 
				? (BeanCls) e.getType() :
				(e.getType() instanceof Ref
						?
								(BeanCls)(((Ref)e.getType()).getBase())
						: (e.getType() instanceof ConstRef 
							
							? (BeanCls)(((ConstRef)e.getType()).getBase())
							: (BeanCls) ((SharedPtr)e.getType()).getElementType()));
								
		if (colPk.hasOneRelation()) {
			try{
				
			Attr attr = cls.getOneRelationAttr(colPk.getOneRelation());
			return e.accessAttr(cls.getAttr(attr)).callMethod("get"+colPk.getOneRelationMappedColumn().getUc1stCamelCaseName());
			} catch(Exception ex) {
				ex.printStackTrace();
				System.out.println(colPk);
				throw ex;
			}
		} else {
			return e.accessAttr(cls.getAttrByName(colPk.getCamelCaseName()));
		}
	}
}
