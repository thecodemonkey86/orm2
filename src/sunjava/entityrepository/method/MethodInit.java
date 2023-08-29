package sunjava.entityrepository.method;

import sunjava.core.Method;
import sunjava.core.Type;

public class MethodInit extends Method {

	public MethodInit(String visibility, Type returnType, String name) {
		super(visibility, returnType, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addImplementation() {
		// TODO Auto-generated method stub
		
	}

//	Param pSqlConSupplier;
//	
//	public MethodInit() {
//		super(Public, Types.Void, "init");
//		pSqlConSupplier = addParam(new Param(Types.supplier(Types.Connection), "sqlConSupplier"));
//		setStatic(true);
//	}
//
//	@Override
//	public void addImplementation() {
//		addInstr(new AssignInstruction(
//				((JavaCls) parent).accessStaticAttributeByName("sqlConnection"),new ClsThreadLocal(Types.Connection).callStaticMethod(ClsThreadLocal.withInitial, pSqlConSupplier)));
//		
//	}

}
