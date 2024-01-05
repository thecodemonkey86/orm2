package php.entity.method;

import database.column.Column;
import php.core.Param;
import php.core.Types;
import php.core.expression.PhpStringLiteral;
import php.core.instruction.CaseBlock;
import php.core.instruction.SwitchBlock;
import php.core.method.Method;
import php.entity.EntityCls;

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
		EntityCls entity = (EntityCls) parent;
		SwitchBlock switchBlock = _switch(pName);
		
		for(Column col : entity.getTbl().getAllColumns()) {
			if(!col.hasRelation()) {
				CaseBlock caseBlock = switchBlock._case(new PhpStringLiteral(col.getName()));
				caseBlock.addInstr(caseBlock._this().assignAttr(col.getCamelCaseName(), EntityCls.getTypeMapper().getConvertTypeExpression(pValue, col)));
				caseBlock._break();
			}
		}
		
		switchBlock.setStandardDefaultCase();

	}

}
