package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.MethodTemplate;
import cpp.core.TplSymbol;
import cpp.core.Type;
import cpp.core.instruction.Instruction;
import cpp.core.method.TplMethod;
import util.CodeUtil2;

public class MethodWhere12 extends MethodTemplate{

	public MethodWhere12(Cls parentType) {
		super(Method.Public, parentType.toRef(), "where");
		addTplType(new TplSymbol("T"));
		addParam(Types.QString.toConstRef(),"whereCond");
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
						return "for(const auto & p : params)\t" + CodeUtil2.NEWLINE + " this->params.append(p);" + CodeUtil2.NEWLINE + 
								"        if (!conditions.empty()) {" + CodeUtil2.NEWLINE + 
								"            this->conditions.append(SqlUtil3::SqlQuery::AND);" + CodeUtil2.NEWLINE + 
								"        }" + CodeUtil2.NEWLINE + 
								"        this->conditions.append(whereCond);" + CodeUtil2.NEWLINE + 
								"        return *this;";
					}
				});
				
			}
		};
		return t;
	}

}
