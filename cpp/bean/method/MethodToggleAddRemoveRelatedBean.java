package cpp.bean.method;

import cpp.Types;
import cpp.bean.ManyAttr;
import cpp.core.Attr;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.Type;
import cpp.core.expression.Expressions;
import cpp.core.instruction.IfBlock;
import cpp.lib.ClsQVector;
import database.relation.AbstractRelation;
import database.relation.ManyRelation;
import database.relation.OneToManyRelation;
import util.StringUtil;

public class MethodToggleAddRemoveRelatedBean extends Method {

	ManyAttr manyAttr;
	
	Param pElement;
	Param pAdd;
	
	public MethodToggleAddRemoveRelatedBean(AbstractRelation r) {
		super(Public, Types.Void, "toggleAddRemoveRelatedBean" +r.getDestTable().getUc1stCamelCaseName()+StringUtil.ucfirst( r.getAlias()));
		if(r instanceof ManyRelation) {
			manyAttr = new ManyAttr((ManyRelation) r);
		} else if(r instanceof OneToManyRelation ) {
			manyAttr = new ManyAttr((OneToManyRelation) r);
		} else {
			throw new IllegalArgumentException("illegal argument");
		}
		Type tElement =Types.getRelationForeignPrimaryKeyType(r);
		pElement = addParam(new Param(tElement.isPrimitiveType() ? tElement : tElement.toConstRef(), "elem"));
		pAdd = addParam(new Param( Types.Bool, "add"));
	}

	@Override
	public void addImplementation() {
		
		
		Attr aRemoved = parent.getAttrByName(manyAttr.getName()+"Removed");
		Attr aAdded = parent.getAttrByName(manyAttr.getName()+"Added");
		IfBlock ifAdd = _if(pAdd);
		ifAdd.thenBlock()._callMethodInstr(aRemoved, ClsQVector.removeOne, pElement);
		
		IfBlock ifNotAddedContains = ifAdd.thenBlock()._if(Expressions.not(aAdded.callMethod(ClsQVector.contains, pElement)));
		ifNotAddedContains.thenBlock()._callMethodInstr(aAdded, ClsQVector.append, pElement);
		
		ifAdd.elseBlock()._callMethodInstr(aAdded, ClsQVector.removeOne, pElement);
		
		IfBlock ifNotRemoveContains = ifAdd.elseBlock()._if(Expressions.not(aRemoved.callMethod(ClsQVector.contains, pElement)));
		ifNotRemoveContains.thenBlock()._callMethodInstr(aRemoved, ClsQVector.append, pElement);
		
	}

}
