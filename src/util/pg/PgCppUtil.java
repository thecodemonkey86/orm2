package util.pg;

import cpp.CoreTypes;
import cpp.core.Attr;
import cpp.core.ConstRef;
import cpp.core.Method;
import cpp.core.MethodCall;
import cpp.core.Ref;
import cpp.core.SharedPtr;
import cpp.core.expression.Expression;
import cpp.entity.EntityCls;
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
			return CoreTypes.QVariant.getMethod("toInt");
		case "bigint":
		case "xid":
		case "oid":
			return CoreTypes.QVariant.getMethod("toLongLong");
		case "smallint":
			return CoreTypes.QVariant.getTemplateMethod("value",CoreTypes.Short);
		case "character varying":
		case "character":	
		case "text":
		case "name":
			return CoreTypes.QVariant.getMethod("toString");
		case "date":
			return CoreTypes.QVariant.getMethod("toDate");
		case "timestamp with time zone":
		case "timestamp without time zone":
			return CoreTypes.QVariant.getMethod("toDateTime");
		case "time with time zone":
		case "time without time zone":
			return CoreTypes.QVariant.getMethod("toTime");
		case "double precision":
		case "numeric":
		case "real":
			return CoreTypes.QVariant.getMethod("toDouble");
		case "bytea":
		case "ARRAY":
			return CoreTypes.QVariant.getMethod("toByteArray");
		case "boolean":
			return CoreTypes.QVariant.getMethod("toBool");
		default:
			return null;
			//throw new RuntimeException("type " + pgType+" not implemented");
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
//	public static String getManyRelationDestAttrNameSingular(OneRelation relation) {
//		return OrmUtil.getManyRelationDestAttrNameSingular(relation);
//	}
	public static String getManyRelationDestAttrNameSingular(ManyRelation relation) {
		return OrmUtil.getManyRelationDestAttrNameSingular(relation);
	}
	
	public static Expression getPkExpression(Expression e, Column colPk) {
		EntityCls cls= e.getType() instanceof EntityCls 
				? (EntityCls) e.getType() :
				(e.getType() instanceof Ref
						?
								(EntityCls)(((Ref)e.getType()).getBase())
						: (e.getType() instanceof ConstRef 
							
							? (((ConstRef)e.getType()).getBase()) instanceof EntityCls 
									? (EntityCls)(((ConstRef)e.getType()).getBase())
									: (EntityCls)((SharedPtr)(((ConstRef)e.getType()).getBase())).getElementType()
							: (EntityCls) ((SharedPtr)e.getType()).getElementType()));
								
		if (colPk.hasOneRelation()) {
			try{
				
			Attr attr = cls.getOneRelationAttr(colPk.getOneRelation());
			return e.accessAttr(cls.getAttr(attr)).callMethod("get"+colPk.getOneRelationMappedColumn().getUc1stCamelCaseName());
			} catch(Exception ex) {
				ex.printStackTrace();
				throw ex;
			}
		} else {
			return e.accessAttr(cls.getAttrByName(colPk.getCamelCaseName()));
		}
	}
	
	public static MethodCall getPkGetterExpression(Expression e, Column colPk) {
		EntityCls cls= e.getType() instanceof EntityCls 
				? (EntityCls) e.getType() :
				(e.getType() instanceof Ref
						?
								(EntityCls)(((Ref)e.getType()).getBase())
						: (e.getType() instanceof ConstRef 
							
							? (((ConstRef)e.getType()).getBase()) instanceof EntityCls 
									? (EntityCls)(((ConstRef)e.getType()).getBase())
									: (EntityCls)((SharedPtr)(((ConstRef)e.getType()).getBase())).getElementType()
							: (EntityCls) ((SharedPtr)e.getType()).getElementType()));
								
		if (colPk.hasOneRelation()) {
			try{
				
			Attr attr = cls.getOneRelationAttr(colPk.getOneRelation());
			return e.callAttrGetter(cls.getAttr(attr)).callMethod("get"+colPk.getOneRelationMappedColumn().getUc1stCamelCaseName());
			} catch(Exception ex) {
				ex.printStackTrace();
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
