package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.instruction.Instruction;
import cpp.entityquery.EntityQueryType;
import cpp.lib.ClsQVariant;
import util.CodeUtil2;

public class MethodLimitAndOffset extends Method{

	Param pJoinTableAlias, pOn, pQueryParams;
	boolean withConditionParameter;
	EntityQueryType beanQueryType;
	public MethodLimitAndOffset(Cls parentType, EntityQueryType beanQueryType, Param pQueryParams,boolean withConditionParameter) {
		super(Public, parentType.toRef(), "limitAndOffset");
		addParam(Types.Int64, "limit");
		addParam(Types.Int64, "offset");
		if(withConditionParameter)
			addParam(Types.QString.toConstRef(),"condition");
		
		if(pQueryParams!=null)
			this.pQueryParams = addParam(pQueryParams);
				
		this.withConditionParameter = withConditionParameter;
		
		this.beanQueryType = beanQueryType;
	}

	@Override
	public void addImplementation() {
		addInstr(new Instruction() {
			@Override
			public String toString() {
				
				String str=(pQueryParams!=null)? "this->params.append("+ (Types.QVariant.toConstRef().equals(pQueryParams.getType()) ||(Types.QVariantList.toConstRef().equals(pQueryParams.getType())) ? pQueryParams.getReadAccessString() : Types.QVariant.callStaticMethod(ClsQVariant.fromValue, pQueryParams )) +");" + CodeUtil2.NEWLINE  : "";
				
				   str+="	this->limitResults = limit;" + CodeUtil2.NEWLINE + 
						"	this->resultOffset = offset;" + CodeUtil2.NEWLINE ;
				   
				   if(beanQueryType == EntityQueryType.Select) {
						 if(MethodLimitAndOffset.this.withConditionParameter) {
							 str+="	this->limitOffsetCondition = condition;" + CodeUtil2.NEWLINE ;
						// } else {
							// str+="	this->limitOffsetCondition = QLatin1Literal(\""+EntityCls.getDatabase().getBooleanExpressionTrue()+ "\");" + CodeUtil2.NEWLINE ;
						 }
				   }
				 str+="	return *this;";
				
				return str;
			}
		});
	}

}
