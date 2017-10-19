package php.cls.bean.method;

import pg.PgCppUtil;
import php.Types;
import php.cls.PhpCls;
import php.cls.bean.BeanCls;
import php.cls.Method;
import php.cls.expression.Var;
import php.cls.instruction.IfBlock;
import php.lib.ClsSqlParam;
import model.Column;
import model.PrimaryKey;

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
			ifIdModified.addIfInstr(params.arrayPush(Types.SqlParam.callStaticMethod(ClsSqlParam.getMethodName(BeanCls.getTypeMapper().columnToType(colPk)),parent.getAttrByName(colPk.getCamelCaseName()+"Previous"))));
			
			if(colPk.hasOneRelation()){
				//colPk.getRelation().getDestTable().getCamelCaseName()
				ifIdModified.addElseInstr(params.arrayPush(Types.SqlParam.callStaticMethod(ClsSqlParam.getMethodName(BeanCls.getTypeMapper().columnToType(colPk)), parent.getAttrByName(PgCppUtil.getOneRelationDestAttrName(colPk.getOneRelation())).callMethod("get"+colPk.getOneRelationMappedColumn().getUc1stCamelCaseName())) )); 
			}else{
				ifIdModified.addElseInstr(params.arrayPush(Types.SqlParam.callStaticMethod(ClsSqlParam.getMethodName(BeanCls.getTypeMapper().columnToType(colPk)), parent.getAttrByName(colPk.getCamelCaseName()))));	
			}
			
			
		}
		
		_return(params);
	}

}
