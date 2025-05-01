package sunjava.entity.method;

import database.relation.ManyRelation;
import sunjava.core.Attr;
import sunjava.core.JavaCls;
import sunjava.core.Method;
import sunjava.core.Param;
import sunjava.core.Types;
import sunjava.core.expression.NewOperator;
import sunjava.lib.ClsArrayList;
import sunjava.orm.OrmUtil;
import util.StringUtil;

public class MethodAddManyToManyRelatedEntityInternal extends Method {

	protected ManyRelation rel;
	
	public MethodAddManyToManyRelatedEntityInternal(ManyRelation r, Param p) {
		super(Public, Types.Void, 
				"add"+StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrNameSingular(r))+ "Internal");
		addParam(p);
		rel=r;
	}

	@Override
	public void addImplementation() {
		JavaCls parent = (JavaCls) this.parent;
		Attr a=parent.getAttrByName(OrmUtil.getManyRelationDestAttrName(rel));
		_if(a.isNull()).addIfInstr(a.assign(new NewOperator(a.getType())));
		addInstr(a.callMethod(ClsArrayList.add,getParam("entity")).asInstruction());
		
	}
	
	

}
