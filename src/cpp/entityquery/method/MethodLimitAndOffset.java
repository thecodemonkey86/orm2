package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.instruction.Instruction;
import cpp.entity.EntityCls;
import cpp.entityquery.EntityQueryType;
import cpp.lib.ClsQVariant;

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
				
				String str=(pQueryParams!=null)? "this->params.append("+ (Types.QVariant.toConstRef().equals(pQueryParams.getType()) ? pQueryParams.getReadAccessString() : Types.QVariant.callStaticMethod(ClsQVariant.fromValue, pQueryParams )) +");\r\n" : "";
				
				   str+="	this->limitResults = limit;\r\n" + 
						"	this->resultOffset = offset;\r\n";
				   
				   if(beanQueryType == EntityQueryType.Select) {
						 if(MethodLimitAndOffset.this.withConditionParameter) {
							 str+="	this->limitOffsetCondition = condition;\r\n";
						 } else {
							 str+="	this->limitOffsetCondition = QLatin1Literal(\""+EntityCls.getDatabase().getBooleanExpressionTrue()+ "\");\r\n";
						 }
				   }
				 str+="	return *this;";
				
				return str;
			}
		});
	}

}
