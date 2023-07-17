package cpp.entity.method;

import cpp.core.Method;
import cpp.core.QString;
import cpp.core.Type;
import cpp.core.instruction.ThrowInstruction;
import cpp.lib.ClsQList;
import cpp.lib.ClsQtException;
import cpp.orm.OrmUtil;
import database.relation.IManyRelation;
import util.StringUtil;

public class MethodGetLastItem extends Method{

	IManyRelation relation;
	
	public MethodGetLastItem(Type relatedBeanCls, IManyRelation relation) {
		super(Public, relatedBeanCls.toConstRef(), "getLast" + StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrNameSingular(relation)));
		this.relation = relation;
	}

	@Override
	public void addImplementation() {
		_if(_this().accessAttr(OrmUtil.getManyRelationDestAttrName(relation)).callMethod(ClsQList.empty)).thenBlock().addInstr
		(new ThrowInstruction(new ClsQtException(),QString.fromStringConstant("no items of "+ OrmUtil.getManyRelationDestAttrName(relation)+ " available")));
		_return(_this().accessAttr(OrmUtil.getManyRelationDestAttrName(relation)).callMethod(ClsQList.last));		
	}

}
