package util.pg;

import cpp.Types;
import cpp.bean.BeanCls;
import cpp.core.Attr;
import cpp.core.ConstRef;
import cpp.core.Method;
import cpp.core.MethodCall;
import cpp.core.Ref;
import cpp.core.SharedPtr;
import cpp.core.expression.Expression;
import cpp.orm.OrmUtil;
import database.column.Column;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;

public class PgCppUtil {
	@Deprecated
	public static Method pgToQVariantConvertMethod(String pgType) {
		switch(pgType) {
		case "integer":
			return Types.QVariant.getMethod("toInt");
		case "bigint":
		case "xid":
		case "oid":
			return Types.QVariant.getMethod("toLongLong");
		case "smallint":
			return Types.QVariant.getMethod("toInt");
		case "character varying":
		case "character":	
		case "text":
		case "name":
			return Types.QVariant.getMethod("toString");
		case "date":
			return Types.QVariant.getMethod("toDate");
		case "timestamp with time zone":
			return Types.QVariant.getMethod("toDateTime");
		case "time with time zone":
			return Types.QVariant.getMethod("toTime");
		case "double precision":
		case "numeric":
			return Types.QVariant.getMethod("toDouble");
		case "bytea":
		case "ARRAY":
			return Types.QVariant.getMethod("toByteArray");
		case "boolean":
			return Types.QVariant.getMethod("toBool");
		default:
			throw new RuntimeException("type " + pgType+" not implemented");
		}
	}
	
	
	public static String getOneRelationDestAttrName(OneRelation relation) {
		return OrmUtil.getOneRelationDestAttrName(relation);
	}
	public static String getOneToManyRelationDestAttrName(OneToManyRelation relation) {
		return OrmUtil.getOneToManyRelationDestAttrName(relation);
	}
	public static String getManyRelationDestAttrName(ManyRelation relation) {
		return OrmUtil.getManyRelationDestAttrName(relation);
	}
	public static String getManyRelationDestAttrNameSingular(OneRelation relation) {
		return OrmUtil.getManyRelationDestAttrNameSingular(relation);
	}
	public static String getManyRelationDestAttrNameSingular(ManyRelation relation) {
		return OrmUtil.getManyRelationDestAttrNameSingular(relation);
	}
	
	public static Expression getPkExpression(Expression e, Column colPk) {
		BeanCls cls= e.getType() instanceof BeanCls 
				? (BeanCls) e.getType() :
				(e.getType() instanceof Ref
						?
								(BeanCls)(((Ref)e.getType()).getBase())
						: (e.getType() instanceof ConstRef 
							
							? (((ConstRef)e.getType()).getBase()) instanceof BeanCls 
									? (BeanCls)(((ConstRef)e.getType()).getBase())
									: (BeanCls)((SharedPtr)(((ConstRef)e.getType()).getBase())).getElementType()
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
	
	public static MethodCall getPkGetterExpression(Expression e, Column colPk) {
		BeanCls cls= e.getType() instanceof BeanCls 
				? (BeanCls) e.getType() :
				(e.getType() instanceof Ref
						?
								(BeanCls)(((Ref)e.getType()).getBase())
						: (e.getType() instanceof ConstRef 
							
							? (((ConstRef)e.getType()).getBase()) instanceof BeanCls 
									? (BeanCls)(((ConstRef)e.getType()).getBase())
									: (BeanCls)((SharedPtr)(((ConstRef)e.getType()).getBase())).getElementType()
							: (BeanCls) ((SharedPtr)e.getType()).getElementType()));
								
		if (colPk.hasOneRelation()) {
			try{
				
			Attr attr = cls.getOneRelationAttr(colPk.getOneRelation());
			return e.callAttrGetter(cls.getAttr(attr)).callMethod("get"+colPk.getOneRelationMappedColumn().getUc1stCamelCaseName());
			} catch(Exception ex) {
				ex.printStackTrace();
				System.out.println(colPk);
				throw ex;
			}
		} else {
			return e.callAttrGetter(cls.getAttrByName(colPk.getCamelCaseName()));
		}
	}

	public static String getOneRelationDestAttrGetter(OneRelation oneRelation) {
		// TODO Auto-generated method stub
		return OrmUtil.getOneRelationDestAttrGetter(oneRelation);
	}
}
