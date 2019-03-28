package php.bean.method;

import database.column.Column;
import database.relation.ManyRelation;
import database.relation.OneToManyRelation;
import php.bean.BeanCls;
import php.core.Attr;
import php.core.Types;
import php.core.expression.BoolExpression;
import php.core.expression.Expressions;
import php.core.method.Method;
import php.orm.OrmUtil;

public class MethodClearModified extends Method {

	public MethodClearModified() {
		super(Public, Types.Void, getMethodName());
	}

	@Override
	public void addImplementation() {
		BeanCls bean = (BeanCls) parent;
		for (ManyRelation r : bean.getManyRelations()) {
			Attr attrAdded = bean.getAttrByName(OrmUtil.getManyRelationDestAttrName(r) + "Added");
			addInstr(attrAdded.assign(Expressions.Null));

			Attr attrRemoved = bean.getAttrByName(OrmUtil.getManyRelationDestAttrName(r) + "Removed");
			addInstr(attrRemoved.assign(Expressions.Null));
		}
		for (OneToManyRelation r : bean.getOneToManyRelations()) {
			Attr attrAdded = bean.getAttrByName(OrmUtil.getOneToManyRelationDestAttrName(r) + "Added");
			addInstr(attrAdded.assign(Expressions.Null));
			
			Attr attrRemoved = bean.getAttrByName(OrmUtil.getOneToManyRelationDestAttrName(r) + "Removed");
			addInstr(attrRemoved.assign(Expressions.Null));
		}
		for(Column col:bean.getTbl().getAllColumns()) {

			if (!col.hasOneRelation()

					) {
				
				
				if (!col.isPartOfPk()) {

				  _assign(bean.getAttrByName(	col.getCamelCaseName()+"Modified"), BoolExpression.FALSE);
					
				}
			}
	}
	}

	public static String getMethodName() {
		return "clearModified";
	}
}
