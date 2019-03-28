package sunjava.bean.method;

import database.relation.ManyRelation;
import sunjava.bean.BeanCls;
import sunjava.core.Attr;
import sunjava.core.Method;
import sunjava.core.Types;
import sunjava.core.expression.Expressions;
import sunjava.lib.ClsArrayList;
import sunjava.orm.OrmUtil;
import util.StringUtil;

public class MethodHasRemovedManyToMany extends Method{
	protected ManyRelation r;
	
	public MethodHasRemovedManyToMany(ManyRelation r) {
		super(Public, Types.Bool, getMethodName(r) );
		this.r = r;
	}

	@Override
	public void addImplementation() {
		BeanCls bean = (BeanCls) parent;
		Attr attrAdded = bean.getAttrByName(OrmUtil.getManyRelationDestAttrName(r)+"Removed" );
		_return(Expressions.and(
				attrAdded.isNotNull(),
				_not(attrAdded.callMethod(ClsArrayList.isEmpty))
				));
		
	}

	public static String getMethodName(ManyRelation r) {
		return "hasRemoved" + StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrName(r));
	}
}
