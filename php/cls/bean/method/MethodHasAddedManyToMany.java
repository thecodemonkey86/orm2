package php.cls.bean.method;

import model.ManyRelation;
import php.Types;
import php.cls.Attr;
import php.cls.Method;
import php.cls.bean.BeanCls;
import php.cls.expression.Expressions;
import php.cls.expression.IntExpression;
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
