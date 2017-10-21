package php.bean.method;

import database.relation.ManyRelation;
import php.bean.BeanCls;
import php.core.Attr;
import php.core.Types;
import php.core.expression.Expressions;
import php.core.expression.IntExpression;
import php.core.method.Method;
import php.orm.OrmUtil;
import util.StringUtil;

public class MethodHasAddedManyToMany extends Method{
	protected ManyRelation r;
	
	public MethodHasAddedManyToMany(ManyRelation r) {
		super(Public, Types.Bool, getMethodName(r) );
		this.r = r;
	}

	@Override
	public void addImplementation() {
		BeanCls bean = (BeanCls) parent;
		Attr attrAdded = bean.getAttrByName(OrmUtil.getManyRelationDestAttrName(r)+"Added" );
		_return(Expressions.and(
				attrAdded.isNotNull(),
				attrAdded.count().greaterThan(new IntExpression(0))
				));
		
	}

	public static String getMethodName(ManyRelation r) {
		return "hasAdded" + StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrName(r));
	}
}
