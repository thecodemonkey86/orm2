package php.entity.method;

import database.column.Column;
import php.core.Param;
import php.core.Types;
import php.core.expression.BoolExpression;
import php.core.expression.NewOperator;
import php.core.expression.PhpStringLiteral;
import php.core.instruction.CaseBlock;
import php.core.instruction.DefaultCaseBlock;
import php.core.instruction.SwitchBlock;
import php.core.instruction.ThrowInstruction;
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
				if(col.isPartOfPk()) {
					caseBlock.addInstr( _this().assignAttr(col.getCamelCaseName()+"Previous",  _this().accessAttr(col.getCamelCaseName())));
					caseBlock.addInstr(caseBlock._this().assignAttr(col.getCamelCaseName(), EntityCls.getTypeMapper().getConvertTypeExpression(pValue, col)));
					caseBlock. addInstr(_this().assignAttr( "primaryKeyModified",BoolExpression.TRUE));
				} else {
					caseBlock.addInstr(caseBlock._this().callAttrSetterMethodInstr(col.getCamelCaseName(), EntityCls.getTypeMapper().getConvertTypeExpression(pValue, col)));
				}
				caseBlock._break();
				
			}
		}
		DefaultCaseBlock defaultCase=new DefaultCaseBlock();
		defaultCase.addInstr(new ThrowInstruction(new NewOperator(Types.Exception,  new PhpStringLiteral("unknown field name: ").concat(pName))));
		switchBlock.setDefaultCase(defaultCase);
		

	}

}
