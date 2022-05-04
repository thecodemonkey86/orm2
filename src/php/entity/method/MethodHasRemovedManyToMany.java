package php.entity.method;

import database.relation.ManyRelation;
import php.core.Attr;
import php.core.Types;
import php.core.expression.Expressions;
import php.core.expression.IntExpression;
import php.core.method.Method;
import php.entity.EntityCls;
import php.orm.OrmUtil;
import util.StringUtil;

public class MethodHasRemovedManyToMany extends Method{
	protected ManyRelation r;
	
	public MethodHasRemovedManyToMany(ManyRelation r) {
		super(Public, Types.Bool, getMethodName(r) );
		this.r = r;
	}

	@Override
	public void addImplementation() {
		EntityCls bean = (EntityCls) parent;
		Attr attrAdded = bean.getAttrByName(OrmUtil.getManyRelationDestAttrName(r)+"Removed" );
		_return(Expressions.and(
				attrAdded.isNotNull(),
				attrAdded.count().greaterThan(new IntExpression(0))
				));
		
	}

	public static String getMethodName(ManyRelation r) {
		return "hasRemoved" + StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrName(r));
	}
}
