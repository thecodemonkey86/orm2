package cpp.entity.method;

import util.pg.PgCppUtil;
import cpp.CoreTypes;
import cpp.Types;
import cpp.core.Method;
import cpp.core.expression.Var;
import cpp.core.instruction.IfBlock;
import cpp.lib.ClsQVariant;
import database.column.Column;
import database.relation.PrimaryKey;

public class MethodGetUpdateConditionParams extends Method {

	protected PrimaryKey pk;
	
	public MethodGetUpdateConditionParams(PrimaryKey pk) {
		super(Public, CoreTypes.QVariantList, "getUpdateConditionParams");
		this.pk = pk;
	}

	@Override
	public void addImplementation() {
		Var params = _declare(CoreTypes.QVariantList, "params");
		IfBlock ifIdModified = _if(parent.getAttrByName("primaryKeyModified"));
		
		for(Column colPk:this.pk.getColumns()) {
			ifIdModified.addIfInstr(params.callMethodInstruction("append",Types.QVariant.callStaticMethod(ClsQVariant.fromValue, parent.getAttrByName(colPk.getCamelCaseName()+"Previous"))));
			
			if(colPk.hasOneRelation()){
				//colPk.getRelation().getDestTable().getCamelCaseName()
				ifIdModified.addElseInstr(params.callMethodInstruction("append",parent.getAttrByName(PgCppUtil.getOneRelationDestAttrName(colPk.getOneRelation())).callMethod("get"+colPk.getOneRelationMappedColumn().getUc1stCamelCaseName()) )); 
			}else{
				ifIdModified.addElseInstr(params.callMethodInstruction("append",Types.QVariant.callStaticMethod(ClsQVariant.fromValue, parent.getAttrByName(colPk.getCamelCaseName()))));	
			}
			
			
		}
		
		_return(params);
	}

}
