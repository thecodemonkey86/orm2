package cpp.cls.bean.method;

import pg.PgCppUtil;
import model.Column;
import model.PrimaryKey;
import cpp.Types;
import cpp.cls.Method;
import cpp.cls.expression.Var;
import cpp.cls.instruction.IfBlock;

public class MethodGetUpdateConditionParams extends Method {

	protected PrimaryKey pk;
	
	public MethodGetUpdateConditionParams(PrimaryKey pk) {
		super(Public, Types.QVariantList, "getUpdateConditionParams");
		this.pk = pk;
	}

	@Override
	public void addImplementation() {
		Var params = _declare(Types.QVariantList, "params");
		IfBlock ifIdModified = _if(parent.getAttrByName("primaryKeyModified"));
		
		for(Column colPk:this.pk.getColumns()) {
			ifIdModified.addIfInstr(params.callMethodInstruction("append",parent.getAttrByName(colPk.getCamelCaseName()+"Previous")));
			
			if(colPk.hasOneRelation()){
				//colPk.getRelation().getDestTable().getCamelCaseName()
				ifIdModified.addElseInstr(params.callMethodInstruction("append",parent.getAttrByName(PgCppUtil.getOneRelationDestAttrName(colPk.getOneRelation())).callMethod("get"+colPk.getOneRelationMappedColumn().getUc1stCamelCaseName()) )); 
			}else{
				ifIdModified.addElseInstr(params.callMethodInstruction("append",parent.getAttrByName(colPk.getCamelCaseName())));	
			}
			
			
		}
		
		_return(params);
	}

}
