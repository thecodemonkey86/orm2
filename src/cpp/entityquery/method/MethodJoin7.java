package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.MethodTemplate;
import cpp.core.TplSymbol;
import cpp.core.Type;
import cpp.core.instruction.Instruction;
import cpp.core.method.TplMethod;

public class MethodJoin7 extends MethodTemplate{

	public MethodJoin7(Cls parentType) {
		super(Method.Public, parentType.toRef(), "join");
		addParam(Types.QString.toConstRef(),"joinTableAlias");
		addParam(Types.QString.toConstRef(),"on");
		addTplType(new TplSymbol("T"));
		addParam(Types.qset(tplTypes.get(0)).toConstRef(),"params");
	}


	@Override
	public TplMethod getConcreteMethod(Type... types) {
		// TODO Auto-generated method stub
				TplMethod t= new TplMethod(this,types ) {
					
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
				return t;
	}

}
