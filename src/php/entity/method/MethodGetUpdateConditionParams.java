package php.entity.method;

import database.column.Column;
import database.relation.PrimaryKey;
import php.core.PhpCls;
import php.core.Types;
import php.core.expression.Var;
import php.core.instruction.IfBlock;
import php.core.method.Method;
import php.entity.EntityCls;
import php.lib.ClsSqlParam;
import util.pg.PgCppUtil;

public class MethodGetUpdateConditionParams extends Method {

	protected PrimaryKey pk;
	
	public MethodGetUpdateConditionParams(PrimaryKey pk) {
		super(Protected, Types.array(Types.SqlParam), "getUpdateConditionParams");
		this.pk = pk;
	}

	@Override
	public void addImplementation() {
		PhpCls parent = (PhpCls) this.parent;
		Var params = _declareInitDefaultConstructor(Types.array(Types.SqlParam), "params");
		IfBlock ifIdModified = _if(parent.getAttrByName("primaryKeyModified"));
		
		for(Column colPk:this.pk.getColumns()) {
			ifIdModified.addIfInstr(params.arrayPush(Types.SqlParam.callStaticMethod(ClsSqlParam.getMethodName(EntityCls.getTypeMapper().columnToType(colPk)),parent.getAttrByName(colPk.getCamelCaseName()+"Previous"))));
			
			if(colPk.hasOneRelation()){
				//colPk.getRelation().getDestTable().getCamelCaseName()
				ifIdModified.addElseInstr(params.arrayPush(Types.SqlParam.callStaticMethod(ClsSqlParam.getMethodName(EntityCls.getTypeMapper().columnToType(colPk)), parent.getAttrByName(PgCppUtil.getOneRelationDestAttrName(colPk.getOneRelation())).callMethod("get"+colPk.getOneRelationMappedColumn().getUc1stCamelCaseName())) )); 
			}else{
				ifIdModified.addElseInstr(params.arrayPush(Types.SqlParam.callStaticMethod(ClsSqlParam.getMethodName(EntityCls.getTypeMapper().columnToType(colPk)), parent.getAttrByName(colPk.getCamelCaseName()))));	
			}
			
			
		}
		
		_return(params);
	}

}
