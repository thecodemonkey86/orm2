package php.entity.method;

import database.column.Column;
import database.relation.ManyRelation;
import database.relation.OneToManyRelation;
import php.core.Attr;
import php.core.Types;
import php.core.expression.BoolExpression;
import php.core.expression.Expressions;
import php.core.method.Method;
import php.entity.EntityCls;
import php.orm.OrmUtil;

public class MethodClearModified extends Method {

	public MethodClearModified() {
		super(Public, Types.Void, getMethodName());
	}

	@Override
	public void addImplementation() {
		EntityCls entity = (EntityCls) parent;
		for (ManyRelation r : entity.getManyRelations()) {
			Attr attrAdded = entity.getAttrByName(OrmUtil.getManyRelationDestAttrName(r) + "Added");
			addInstr(attrAdded.assign(Expressions.Null));

			Attr attrRemoved = entity.getAttrByName(OrmUtil.getManyRelationDestAttrName(r) + "Removed");
			addInstr(attrRemoved.assign(Expressions.Null));
		}
		for (OneToManyRelation r : entity.getOneToManyRelations()) {
			Attr attrAdded = entity.getAttrByName(OrmUtil.getOneToManyRelationDestAttrName(r) + "Added");
			addInstr(attrAdded.assign(Expressions.Null));
			
			Attr attrRemoved = entity.getAttrByName(OrmUtil.getOneToManyRelationDestAttrName(r) + "Removed");
			addInstr(attrRemoved.assign(Expressions.Null));
		}
		for(Column col:entity.getTbl().getAllColumns()) {

			if (!col.hasOneRelation()

					) {
				
				
				if (!col.isPartOfPk()) {

				  _assign(entity.getAttrByName(	col.getCamelCaseName()+"Modified"), BoolExpression.FALSE);
					
				}
			}
	}
	}

	public static String getMethodName() {
		return "clearModified";
	}
}
