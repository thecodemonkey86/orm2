package sunjava.cls.bean.method;

import pg.PgCppUtil;
import sunjava.Types;
import sunjava.cls.JavaCls;
import sunjava.cls.Method;
import sunjava.cls.expression.Var;
import sunjava.cls.instruction.IfBlock;
import sunjava.lib.ClsArrayList;
import sunjava.lib.ClsSqlParam;
import model.Column;
import model.PrimaryKey;

public class MethodGetUpdateConditionParams extends Method {

	protected PrimaryKey pk;
	
	public MethodGetUpdateConditionParams(PrimaryKey pk) {
		super(Protected, Types.arraylist(Types.SqlParam), "getUpdateConditionParams");
		this.pk = pk;
	}

	@Override
	public void addImplementation() {
		JavaCls parent = (JavaCls) this.parent;
		Var params = _declareInitDefaultConstructor(Types.arraylist(Types.SqlParam), "params");
		IfBlock ifIdModified = _if(parent.getAttrByName("primaryKeyModified"));
		
		for(Column colPk:this.pk.getColumns()) {
			ifIdModified.addIfInstr(params.callMethodInstruction(ClsArrayList.add,Types.SqlParam.callStaticMethod(ClsSqlParam.get,parent.getAttrByName(colPk.getCamelCaseName()+"Previous"))));
			
			if(colPk.hasOneRelation()){
				//colPk.getRelation().getDestTable().getCamelCaseName()
				ifIdModified.addElseInstr(params.callMethodInstruction(ClsArrayList.add,Types.SqlParam.callStaticMethod(ClsSqlParam.get, parent.getAttrByName(PgCppUtil.getOneRelationDestAttrName(colPk.getOneRelation())).callMethod("get"+colPk.getOneRelationMappedColumn().getUc1stCamelCaseName())) )); 
			}else{
				ifIdModified.addElseInstr(params.callMethodInstruction(ClsArrayList.add,Types.SqlParam.callStaticMethod(ClsSqlParam.get, parent.getAttrByName(colPk.getCamelCaseName()))));	
			}
			
			
		}
		
		_return(params);
	}

}
