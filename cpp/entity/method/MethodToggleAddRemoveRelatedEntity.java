package cpp.entity.method;

import cpp.Types;
import cpp.core.Attr;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.Type;
import cpp.core.expression.Expressions;
import cpp.core.instruction.IfBlock;
import cpp.entity.ManyAttr;
import cpp.lib.ClsQVector;
import database.relation.AbstractRelation;
import database.relation.ManyRelation;
import database.relation.OneToManyRelation;
import util.StringUtil;

public class MethodToggleAddRemoveRelatedEntity extends Method {

	ManyAttr manyAttr;
	AbstractRelation r;
	
	public MethodToggleAddRemoveRelatedEntity(AbstractRelation r) {
		super(Public, Types.Void, "toggleAddRemoveRelatedBean" +r.getDestTable().getUc1stCamelCaseName()+StringUtil.ucfirst( r.getAlias()));
		if(r instanceof ManyRelation) {
			manyAttr = new ManyAttr((ManyRelation) r);
		} else if(r instanceof OneToManyRelation ) {
			manyAttr = new ManyAttr((OneToManyRelation) r);
		} else {
			throw new IllegalArgumentException("illegal argument");
		}
		this.r=r;
		
	}

	@Override
	public void addImplementation() {
		// may not work if in contructor
		Type tElement =Types.getRelationForeignPrimaryKeyType(r);
		Param pElement = addParam(new Param(tElement.isPrimitiveType() ? tElement : tElement.toConstRef(), "elem"));
		Param pAdd = addParam(new Param( Types.Bool, "add"));
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
