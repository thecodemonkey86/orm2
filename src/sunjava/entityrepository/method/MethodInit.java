package sunjava.entityrepository.method;

import sunjava.core.JavaCls;
import sunjava.core.Method;
import sunjava.core.Param;
import sunjava.core.Types;
import sunjava.core.instruction.AssignInstruction;
import sunjava.lib.ClsThreadLocal;

public class MethodInit extends Method {

	Param pSqlConSupplier;
	
	public MethodInit() {
		super(Public, Types.Void, "init");
		pSqlConSupplier = addParam(new Param(Types.supplier(Types.Connection), "sqlConSupplier"));
		setStatic(true);
	}

	@Override
	public void addImplementation() {
		addInstr(new AssignInstruction(
				((JavaCls) parent).accessStaticAttributeByName("sqlConnection"),new ClsThreadLocal(Types.Connection).callStaticMethod(ClsThreadLocal.withInitial, pSqlConSupplier)));
		
	}

}
