package sunjava.entity.method;

import database.relation.ManyRelation;
import sunjava.core.Attr;
import sunjava.core.Method;
import sunjava.core.Types;
import sunjava.core.expression.Expressions;
import sunjava.entity.EntityCls;
import sunjava.lib.ClsArrayList;
import sunjava.orm.OrmUtil;
import util.StringUtil;

public class MethodHasAddedManyToMany extends Method{
	protected ManyRelation r;
	
	public MethodHasAddedManyToMany(ManyRelation r) {
		super(Public, Types.Bool, getMethodName(r) );
		this.r = r;
	}

	@Override
	public void addImplementation() {
		EntityCls bean = (EntityCls) parent;
		Attr attrAdded = bean.getAttrByName(OrmUtil.getManyRelationDestAttrName(r)+"Added" );
		_return(Expressions.and(
				attrAdded.isNotNull(),
				_not(attrAdded.callMethod(ClsArrayList.isEmpty))
				));
		
	}

	public static String getMethodName(ManyRelation r) {
		return "hasAdded" + StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrName(r));
	}
}
