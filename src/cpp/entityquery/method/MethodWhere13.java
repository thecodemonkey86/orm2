package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.MethodTemplate;
import cpp.core.TplSymbol;
import cpp.core.Type;
import cpp.core.instruction.Instruction;
import cpp.core.method.TplMethod;

public class MethodWhere13 extends MethodTemplate{

	
	public MethodWhere13(Cls parentType) {
		super(Method.Public, parentType.toRef(), "where");
		addTplType(new TplSymbol("T"));
		addParam(Types.QString.toConstRef(),"whereCond");
		addParam(Types.qlist(tplTypes.get(0)).toConstRef(),"params");
		
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
						return "for(const auto & p : params)\t\r\n this->params.append(p);\r\n" + 
								"        if (!conditions.empty()) {\r\n" + 
								"            this->conditions.append(SqlUtil3::SqlQuery::AND);\r\n" + 
								"        }\r\n" + 
								"        this->conditions.append(whereCond);\r\n" + 
								"        return *this;";
					}
				});
				
			}
		};
		return t;
	}

}
