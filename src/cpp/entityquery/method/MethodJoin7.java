package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.MethodTemplate;
import cpp.core.Param;
import cpp.core.TplSymbol;
import cpp.core.Type;
import cpp.core.instruction.Instruction;
import cpp.core.method.TplMethod;

public class MethodJoin7 extends MethodTemplate{

	Param pJoinTableAlias;
	Param pOn;
	Param pParams;
	
	public MethodJoin7(Cls parentType) {
		super(Method.Public, parentType.toRef(), "join");
		pJoinTableAlias = new Param(Types.QString.toConstRef(),"joinTableAlias");
		pOn = new Param(Types.QString.toConstRef(),"on");
		addTplType(new TplSymbol("T"));
		pParams = new Param(Types.qset(tplTypes.get(0)).toConstRef(),"params");
	}


	@Override
	public TplMethod getConcreteMethod(Type... types) {
		// TODO Auto-generated method stub
				TplMethod t= new TplMethod(this,visibility, returnType, name, types ) {
					
					@Override
					public void addImplementation() {
						addInstr(new Instruction() {
							@Override
							public String toString() {
								return "this->params.append(params);\r\n" + 
										"return join(joinTableAlias,on);";
							}
						});
						
					}
				};
				t.addParam(pJoinTableAlias);
				t.addParam(pOn);
				t.addParam(pParams);
				return t;
	}

}
