package php.bean.method;

import database.column.Column;
import php.bean.EntityCls;
import php.core.Param;
import php.core.Types;
import php.core.expression.PhpStringLiteral;
import php.core.instruction.CaseBlock;
import php.core.instruction.SwitchBlock;
import php.core.method.Method;

public class MethodSetValue extends Method {

	Param pName;
	Param pValue;
	
	public MethodSetValue() {
		super(Public, Types.Void, "setValue");
		pName = addParam(new Param(Types.String, "name"));
		pValue = addParam(new Param(Types.Mixed, "value"));
	}

	@Override
	public void addImplementation() {
		EntityCls bean = (EntityCls) parent;
		SwitchBlock switchBlock = _switch(pName);
		
		for(Column col : bean.getTbl().getAllColumns()) {
			if(!col.hasRelation()) {
				CaseBlock caseBlock = switchBlock._case(new PhpStringLiteral(col.getName()));
				caseBlock.addInstr(caseBlock._this().callAttrSetter(col.getCamelCaseName(), EntityCls.getTypeMapper().getConvertTypeExpression(pValue, col)).asInstruction());
				caseBlock._break();
			}
		}
		
		switchBlock.setStandardDefaultCase();

	}

}
